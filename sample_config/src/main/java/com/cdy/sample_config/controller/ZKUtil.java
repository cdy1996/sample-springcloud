package com.cdy.sample_config.controller;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * todo
 * Created by 陈东一
 * 2018/6/30 13:31
 */
public class ZKUtil implements Watcher {
    
    public static final Logger LOG = LoggerFactory.getLogger(ZKUtil.class);
    
    private static final int SESSION_TIMEOUT = 10000;
    
    private ZooKeeper zk = null;
    
    private CountDownLatch connectedSemaphore = new CountDownLatch(1);
    
    /**
     * 连接Zookeeper
     *
     * @param connectString Zookeeper服务地址
     */
    public void connectionZookeeper(String connectString) {
        connectionZookeeper(connectString, SESSION_TIMEOUT);
    }
    
    /**
     * <p>连接Zookeeper</p>
     * <pre>
     *     [关于connectString服务器地址配置]
     *     格式: 192.168.1.1:2181,192.168.1.2:2181,192.168.1.3:2181
     *     这个地址配置有多个ip:port之间逗号分隔,底层操作
     *     ConnectStringParser connectStringParser =  new ConnectStringParser(“192.168.1.1:2181,192.168.1.2:2181,192.168.1.3:2181”);
     *     这个类主要就是解析传入地址列表字符串，将其它保存在一个ArrayList中
     *     ArrayList<InetSocketAddress> serverAddresses = new ArrayList<InetSocketAddress>();
     *     接下去，这个地址列表会被进一步封装成StaticHostProvider对象，并且在运行过程中，一直是这个对象来维护整个地址列表。
     *     ZK客户端将所有Server保存在一个List中，然后随机打乱(这个随机过程是一次性的)，并且形成一个环，具体使用的时候，从0号位开始一个一个使用。
     *     因此，Server地址能够重复配置，这样能够弥补客户端无法设置Server权重的缺陷，但是也会加大风险。
     *
     *     [客户端和服务端会话说明]
     *     ZooKeeper中，客户端和服务端建立连接后，会话随之建立，生成一个全局唯一的会话ID(Session ID)。
     *     服务器和客户端之间维持的是一个长连接，在SESSION_TIMEOUT时间内，服务器会确定客户端是否正常连接(客户端会定时向服务器发送heart_beat，服务器重置下次SESSION_TIMEOUT时间)。
     *     因此，在正常情况下，Session一直有效，并且ZK集群所有机器上都保存这个Session信息。
     *     在出现网络或其它问题情况下（例如客户端所连接的那台ZK机器挂了，或是其它原因的网络闪断）,客户端与当前连接的那台服务器之间连接断了,
     *     这个时候客户端会主动在地址列表（实例化ZK对象的时候传入构造方法的那个参数connectString）中选择新的地址进行连接。
     *
     *     [会话时间]
     *     客户端并不是可以随意设置这个会话超时时间，在ZK服务器端对会话超时时间是有限制的，主要是minSessionTimeout和maxSessionTimeout这两个参数设置的。
     *     如果客户端设置的超时时间不在这个范围，那么会被强制设置为最大或最小时间。 默认的Session超时时间是在2 * tickTime ~ 20 * tickTime
     * </pre>
     *
     * @param connectString  Zookeeper服务地址
     * @param sessionTimeout Zookeeper连接超时时间
     */
    public void connectionZookeeper(String connectString, int sessionTimeout) {
        this.releaseConnection();
        try {
            // ZK客户端允许我们将ZK服务器的所有地址都配置在这里
            zk = new ZooKeeper(connectString, sessionTimeout, this);
            // 使用CountDownLatch.await()的线程（当前线程）阻塞直到所有其它拥有CountDownLatch的线程执行完毕（countDown()结果为0）
            connectedSemaphore.await();
        } catch (InterruptedException e) {
            LOG.error("连接创建失败，发生 InterruptedException , e " + e.getMessage(), e);
        } catch (IOException e) {
            LOG.error("连接创建失败，发生 IOException , e " + e.getMessage(), e);
        }
    }
    
    /**
     * <p>创建zNode节点, String create(path<节点路径>, data[]<节点内容>, List(ACL访问控制列表), CreateMode<zNode创建类型>) </p><br/>
     * <pre>
     *     节点创建类型(CreateMode)
     *     1、PERSISTENT:持久化节点
     *     2、PERSISTENT_SEQUENTIAL:顺序自动编号持久化节点，这种节点会根据当前已存在的节点数自动加 1
     *     3、EPHEMERAL:临时节点客户端,session超时这类节点就会被自动删除
     *     4、EPHEMERAL_SEQUENTIAL:临时自动编号节点
     * </pre>
     *
     * @param path zNode节点路径
     * @param data zNode数据内容
     * @return 创建成功返回true, 反之返回false.
     */
    public boolean createPath(String path, String data) {
        try {
            String zkPath = this.zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            LOG.info("节点创建成功, Path: " + zkPath + ", content: " + data);
            return true;
        } catch (KeeperException e) {
            LOG.error("节点创建失败, 发生KeeperException! path: " + path + ", data:" + data
                    + ", errMsg:" + e.getMessage(), e);
        } catch (InterruptedException e) {
            LOG.error("节点创建失败, 发生 InterruptedException! path: " + path + ", data:" + data
                    + ", errMsg:" + e.getMessage(), e);
        }
        return false;
    }
    
    /**
     * <p>删除一个zMode节点, void delete(path<节点路径>, stat<数据版本号>)</p><br/>
     * <pre>
     *     说明
     *     1、版本号不一致,无法进行数据删除操作.
     *     2、如果版本号与znode的版本号不一致,将无法删除,是一种乐观加锁机制;如果将版本号设置为-1,不会去检测版本,直接删除.
     * </pre>
     *
     * @param path zNode节点路径
     * @return 删除成功返回true, 反之返回false.
     */
    public boolean deletePath(String path) {
        try {
            this.zk.delete(path, -1);
            LOG.info("节点删除成功, Path: " + path);
            return true;
        } catch (KeeperException e) {
            LOG.error("节点删除失败, 发生KeeperException! path: " + path
                    + ", errMsg:" + e.getMessage(), e);
        } catch (InterruptedException e) {
            LOG.error("节点删除失败, 发生 InterruptedException! path: " + path
                    + ", errMsg:" + e.getMessage(), e);
        }
        return false;
    }
    
    /**
     * <p>更新指定节点数据内容, Stat setData(path<节点路径>, data[]<节点内容>, stat<数据版本号>)</p>
     * <pre>
     *     设置某个znode上的数据时如果为-1，跳过版本检查
     * </pre>
     *
     * @param path zNode节点路径
     * @param data zNode数据内容
     * @return 更新成功返回true, 返回返回false
     */
    public boolean writeData(String path, String data) {
        try {
            Stat stat = this.zk.setData(path, data.getBytes(), -1);
            LOG.info("更新数据成功, path：" + path + ", stat: " + stat);
            return true;
        } catch (KeeperException e) {
            LOG.error("更新数据失败, 发生KeeperException! path: " + path + ", data:" + data
                    + ", errMsg:" + e.getMessage(), e);
        } catch (InterruptedException e) {
            LOG.error("更新数据失败, 发生InterruptedException! path: " + path + ", data:" + data
                    + ", errMsg:" + e.getMessage(), e);
        }
        return false;
    }
    
    /**
     * <p>读取指定节点数据内容,byte[] getData(path<节点路径>, watcher<监视器>, stat<数据版本号>)</p>
     *
     * @param path zNode节点路径
     * @return 节点存储的值, 有值返回, 无值返回null
     */
    public String readData(String path) {
        String data = null;
        try {
            data = new String(this.zk.getData(path, false, null));
            LOG.info("读取数据成功, path:" + path + ", content:" + data);
        } catch (KeeperException e) {
            LOG.error("读取数据失败,发生KeeperException! path: " + path
                    + ", errMsg:" + e.getMessage(), e);
        } catch (InterruptedException e) {
            LOG.error("读取数据失败,发生InterruptedException! path: " + path
                    + ", errMsg:" + e.getMessage(), e);
        }
        return data;
    }
    
    /**
     * <p>获取某个节点下的所有子节点,List getChildren(path<节点路径>, watcher<监视器>)该方法有多个重载</p>
     *
     * @param path zNode节点路径
     * @return 子节点路径集合 说明,这里返回的值为节点名
     * <pre>
     *     eg.
     *     /node
     *     /node/child1
     *     /node/child2
     *     getChild( "node" )户的集合中的值为["child1","child2"]
     * </pre>
     * @throws KeeperException
     * @throws InterruptedException
     */
    public List<String> getChild(String path) {
        try {
            List<String> list = this.zk.getChildren(path, false);
            if (list.isEmpty()) {
                LOG.info("中没有节点" + path);
            }
            return list;
        } catch (KeeperException e) {
            LOG.error("读取子节点数据失败,发生KeeperException! path: " + path
                    + ", errMsg:" + e.getMessage(), e);
        } catch (InterruptedException e) {
            LOG.error("读取子节点数据失败,发生InterruptedException! path: " + path
                    + ", errMsg:" + e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * <p>判断某个zNode节点是否存在, Stat exists(path<节点路径>, watch<并设置是否监控这个目录节点，这里的 watcher 是在创建 ZooKeeper 实例时指定的 watcher>)</p>
     *
     * @param path zNode节点路径
     * @return 存在返回true, 反之返回false
     */
    public boolean isExists(String path) {
        try {
            Stat stat = this.zk.exists(path, false);
            return null != stat;
        } catch (KeeperException e) {
            LOG.error("读取数据失败,发生KeeperException! path: " + path
                    + ", errMsg:" + e.getMessage(), e);
        } catch (InterruptedException e) {
            LOG.error("读取数据失败,发生InterruptedException! path: " + path
                    + ", errMsg:" + e.getMessage(), e);
        }
        return false;
    }
    
    /**
     * Watcher Server,处理收到的变更
     *
     * @param watchedEvent
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        LOG.info("收到事件通知：" + watchedEvent.getState());
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            connectedSemaphore.countDown();
        }
    }
    
    /**
     * 关闭ZK连接
     */
    public void releaseConnection() {
        if (null != zk) {
            try {
                this.zk.close();
            } catch (InterruptedException e) {
                LOG.error("release connection error ," + e.getMessage(), e);
            }
        }
    }
    
    public static void main(String[] args) {
        
        // 定义父子类节点路径
        String rootPath = "/nodeRoot";
        String child1Path = rootPath + "/nodeChildren1";
        String child2Path = rootPath + "/nodeChildren2";
        
        ZKUtil zkWatchAPI = new ZKUtil();
        
        // 连接zk服务器
        zkWatchAPI.connectionZookeeper("127.0.0.1:2181");
        
        // 创建节点数据
        if (zkWatchAPI.createPath(rootPath, "<父>节点数据")) {
            System.out.println("节点[" + rootPath + "]数据内容[" + zkWatchAPI.readData(rootPath) + "]");
        }
        // 创建子节点, 读取 + 删除
        if (zkWatchAPI.createPath(child1Path, "<父-子(1)>节点数据")) {
            System.out.println("节点[" + child1Path + "]数据内容[" + zkWatchAPI.readData(child1Path) + "]");
            zkWatchAPI.deletePath(child1Path);
            System.out.println("节点[" + child1Path + "]删除值后[" + zkWatchAPI.readData(child1Path) + "]");
        }
        
        // 创建子节点, 读取 + 修改
        if (zkWatchAPI.createPath(child2Path, "<父-子(2)>节点数据")) {
            System.out.println("节点[" + child2Path + "]数据内容[" + zkWatchAPI.readData(child2Path) + "]");
            zkWatchAPI.writeData(child2Path, "<父-子(2)>节点数据,更新后的数据");
            System.out.println("节点[" + child2Path + "]数据内容更新后[" + zkWatchAPI.readData(child2Path) + "]");
        }
        
        // 获取子节点
        List<String> childPaths = zkWatchAPI.getChild(rootPath);
        if (null != childPaths) {
            System.out.println("节点[" + rootPath + "]下的子节点数[" + childPaths.size() + "]");
            for (String childPath : childPaths) {
                System.out.println(" |--节点名[" + childPath + "]");
            }
        }
        
        // 判断节点是否存在
        System.out.println("检测节点[" + rootPath + "]是否存在:" + zkWatchAPI.isExists(rootPath));
        System.out.println("检测节点[" + child1Path + "]是否存在:" + zkWatchAPI.isExists(child1Path));
        System.out.println("检测节点[" + child2Path + "]是否存在:" + zkWatchAPI.isExists(child2Path));
        
        
        zkWatchAPI.releaseConnection();
    }
}
