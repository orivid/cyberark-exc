package com.cyberark.items.services;

import com.cyberark.items.entities.ItemRuleType;
import com.cyberark.items.entities.ItemType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RuleAssociationServiceImpl implements RuleAssociationService, InitializingBean {

    private final Map<ItemType, ItemRuleType> itemRuleTypeByItemType;
    private final ItemRuleType defaultRuleType;
    private final RulesLoader rulesLoader;

    public RuleAssociationServiceImpl(final ItemRuleType defaultRuleType,
                                      final RulesLoader rulesLoader,
                                      final Map<ItemType, ItemRuleType> itemRuleTypeByItemType) {
        this.rulesLoader = rulesLoader;
        this.defaultRuleType = defaultRuleType;
        this.itemRuleTypeByItemType = itemRuleTypeByItemType;
    }

    @Override
    public void afterPropertiesSet() {
        Map<ItemType, ItemRuleType> itemRuleMapping = rulesLoader.getItemRuleMapping();
        itemRuleTypeByItemType.putAll(itemRuleMapping);
    }

    @Override
    public void update(final ItemType itemType, final ItemRuleType itemRuleType) {
        itemRuleTypeByItemType.put(itemType, itemRuleType);
    }

    @Override
    public ItemRuleType getRuleType(ItemType itemType) {
        final ItemRuleType itemRuleType = itemRuleTypeByItemType.get(itemType);

        if (itemRuleType != null) {
            return itemRuleType;
        }

        return defaultRuleType;
    }
}
