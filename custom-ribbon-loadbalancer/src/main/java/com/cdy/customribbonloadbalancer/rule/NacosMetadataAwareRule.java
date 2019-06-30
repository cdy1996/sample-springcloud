package com.cdy.customribbonloadbalancer.rule;


import com.cdy.customribbonloadbalancer.predicate.NacosMetadataAwarePredicate;

public class NacosMetadataAwareRule extends DiscoveryEnabledRule {

    public NacosMetadataAwareRule() {
        super(new NacosMetadataAwarePredicate());
    }
}