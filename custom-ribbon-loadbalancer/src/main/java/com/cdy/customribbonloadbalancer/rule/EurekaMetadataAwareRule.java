package com.cdy.customribbonloadbalancer.rule;


import com.cdy.customribbonloadbalancer.predicate.EurekaMetadataAwarePredicate;

public class EurekaMetadataAwareRule extends DiscoveryEnabledRule {

    public EurekaMetadataAwareRule() {
        super(new EurekaMetadataAwarePredicate());
    }
    
    
}