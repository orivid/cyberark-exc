package com.cyberark.items.configuration;

import com.cyberark.items.entities.ItemRuleType;
import com.cyberark.items.entities.ItemType;
import com.cyberark.items.rules.Rule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class ItemConfiguration {

    @Bean
    public ItemRuleType getDefaultRuleType() {
        return ItemRuleType.LOSES_CONSTANT_VALUE_WITH_AGE;
    }

    @Bean
    public Map<ItemType, ItemRuleType> itemRuleTypeByItemType(){
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<ItemRuleType, Rule> ruleByItemRuleType(){
        return new ConcurrentHashMap<>();
    }

    @Bean
    public AtomicLong itemIdCounter(){
        return new AtomicLong();
    }
}
