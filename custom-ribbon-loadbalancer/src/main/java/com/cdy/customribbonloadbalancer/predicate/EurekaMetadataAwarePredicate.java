package com.cdy.customribbonloadbalancer.predicate;

import com.cdy.customribbonloadbalancer.api.RibbonFilterContext;
import com.cdy.customribbonloadbalancer.robin.EurekaWeightRoundRobin;
import com.cdy.customribbonloadbalancer.robin.WeightRoundRobin;
import com.cdy.customribbonloadbalancer.support.RibbonFilterContextHolder;
import com.google.common.base.Optional;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.cdy.customribbonloadbalancer.support.DefaultRibbonFilterContext.VERSION;

public class EurekaMetadataAwarePredicate extends DiscoveryEnabledPredicate {
    
    WeightRoundRobin weightRoundRobin = new EurekaWeightRoundRobin();
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean apply(Server server) {
 
        final RibbonFilterContext context = RibbonFilterContextHolder.getCurrentContext();
        final Set<Map.Entry<String, String>> attributes = Collections.unmodifiableSet(context.getAttributes().entrySet());
        final Map<String, String> metadata = ((DiscoveryEnabledServer)server).getInstanceInfo().getMetadata();
        return metadata.entrySet().containsAll(attributes);
    }
    
    @Override
    public Optional<Server> chooseRoundRobinAfterFiltering(List<Server> servers, Object loadBalancerKey) {
        List<Server> eligible = getEligibleServers(servers, loadBalancerKey);
        if (eligible.size() == 0) {
            return Optional.absent();
        }
        return Optional.of(weightRoundRobin.choose(eligible));
    }
}