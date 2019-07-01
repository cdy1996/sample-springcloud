package com.cdy.customribbonloadbalancer.robin;


import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EurekaWeightRoundRobin extends WeightRoundRobin{
	
	public com.netflix.loadbalancer.Server choose(List<? extends com.netflix.loadbalancer.Server> servers){
		return super.getServer(init(servers));
	}
	
	
	//EurekaNotificationServerListUpdater
	public synchronized List<WeightRoundRobin.Server> init(List<? extends com.netflix.loadbalancer.Server> servers){
		
		List<Server> serverList = servers.stream().map(e -> {
			Map<String, String> metadata = ((DiscoveryEnabledServer)e).getInstanceInfo().getMetadata();
			String weight = metadata.get("weight");
			System.out.println(e.getPort()+":"+((DiscoveryEnabledServer) e).getInstanceInfo().getMetadata().get("weight"));
			return new Server(e, StringUtils.isBlank(weight) ? 1 : Integer.valueOf(weight));
		}).collect(Collectors.toList());
		
		boolean update = super.servers.containsAll(serverList);
		
		if (!update) {
			maxWeight = greatestWeight(serverList);
			gcdWeight = greatestCommonDivisor(serverList);
			serverCount = serverList.size();
			super.servers = serverList;
		}
		return serverList;
	}

 
}
