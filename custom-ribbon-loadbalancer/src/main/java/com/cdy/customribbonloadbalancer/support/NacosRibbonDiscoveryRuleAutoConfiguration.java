package com.cdy.customribbonloadbalancer.support;

import com.cdy.customribbonloadbalancer.rule.NacosMetadataAwareRule;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AutoConfigureBefore(RibbonClientConfiguration.class)
@ConditionalOnClass(NacosDiscoveryClient.class)
@ConditionalOnProperty(value = "ribbon.filter.metadata.enabled", matchIfMissing = true)
public class NacosRibbonDiscoveryRuleAutoConfiguration {
    public NacosRibbonDiscoveryRuleAutoConfiguration() {
        System.out.println("NacosRibbonDiscoveryRuleAutoConfiguration");
    }
    
    @Bean
    @ConditionalOnMissingBean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public NacosMetadataAwareRule metadataAwareRuleNacos() {
        NacosMetadataAwareRule metadataAwareRule = new NacosMetadataAwareRule();
        System.out.println("metadataAwareRuleNacos");
        return metadataAwareRule;
    }
    

}