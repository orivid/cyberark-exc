package com.cyberark.items.services;

import com.cyberark.items.entities.ItemRuleType;
import com.cyberark.items.entities.ItemType;

public interface RuleAssociationService {

    void update(ItemType itemType, ItemRuleType itemRuleType);

    ItemRuleType getRuleType(ItemType itemType);
}
