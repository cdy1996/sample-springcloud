package com.cdy.customribbonloadbalancer.robin;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public abstract class WeightRoundRobin{
	
	/**上次选择的服务器*/
	protected int currentIndex = -1;
	/**当前调度的权值*/
	protected int currentWeight = 0;
	/**最大权重*/
	protected int maxWeight;
	/**权重的最大公约数*/
	protected int gcdWeight;
	/**服务器数*/
	protected int serverCount;
	protected List<Server> servers = new ArrayList<Server>();
	
	public int greaterCommonDivisor(int a, int b){
		BigInteger aBig = new BigInteger(String.valueOf(a));
		BigInteger bBig = new BigInteger(String.valueOf(b));
		return aBig.gcd(bBig).intValue();
	}
	
	public int greatestCommonDivisor(List<Server> servers){
		int divisor = 0;
		for(int index = 0, len = servers.size(); index < len - 1; index++){
			if(index ==0){
				divisor = greaterCommonDivisor(
							servers.get(index).getWeight(), servers.get(index + 1).getWeight());
			}else{
				divisor = greaterCommonDivisor(divisor, servers.get(index).getWeight());
			}
		}
		return divisor;
	}
	
	public int greatestWeight(List<Server> servers){
		int weight = 0;
		for(Server server : servers){
			if(weight < server.getWeight()){
				weight = server.getWeight();
			}
		}
		return weight;
	}
	
	/**
    *  算法流程： 
    *  假设有一组服务器 S = {S0, S1, …, Sn-1}
    *  有相应的权重，变量currentIndex表示上次选择的服务器
    *  权值currentWeight初始化为0，currentIndex初始化为-1 ，当第一次的时候返回 权值取最大的那个服务器，
    *  通过权重的不断递减 寻找 适合的服务器返回，直到轮询结束，权值返回为0 
    */
	public com.netflix.loadbalancer.Server getServer(List<Server> servers){
		while(true){
			currentIndex = (currentIndex + 1) % serverCount;
			if(currentIndex == 0){
				currentWeight = currentWeight - gcdWeight;
				if(currentWeight <= 0){
					currentWeight = maxWeight;
					if(currentWeight == 0){
						return null;
					}
				}
			}
			if(servers.get(currentIndex).getWeight() >= currentWeight){
				com.netflix.loadbalancer.Server server = servers.get(currentIndex).getServer();
				System.out.println("选择的服务器端口为:"+server.getPort());
				return server;
			}
		}
	}
	
	
	
	public static void main(String[] args){
//		servers.add(new Server("192.168.1.101", 1));
//		servers.add(new Server("192.168.1.102", 2));
//		servers.add(new Server("192.168.1.103", 3));
//		servers.add(new Server("192.168.1.104", 4));
//		servers.add(new Server("192.168.1.105", 5));

//		WeightRoundRobin weightRoundRobin = new WeightRoundRobin();
//		weightRoundRobin.init();
//
//		for(int i = 0; i < 15; i++){
//			Server server = weightRoundRobin.getServer();
//			System.out.println("server " + server.getIp() + " weight=" + server.getWeight());
//		}
	}
	
	public abstract com.netflix.loadbalancer.Server choose(List<? extends com.netflix.loadbalancer.Server> servers);
	
	
	static class Server {
		
		com.netflix.loadbalancer.Server server;
		Integer weight;
		
		public Server(com.netflix.loadbalancer.Server server, Integer weight) {
			this.server = server;
			this.weight = weight;
		}
		
		public com.netflix.loadbalancer.Server getServer() {
			return server;
		}
		
		public void setServer(com.netflix.loadbalancer.Server server) {
			this.server = server;
		}
		
		public Integer getWeight() {
			return weight;
		}
		
		public void setWeight(Integer weight) {
			this.weight = weight;
		}
	}

 
}
