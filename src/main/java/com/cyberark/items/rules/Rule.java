package com.cyberark.items.rules;

import com.cyberark.items.entities.Item;
import com.cyberark.items.entities.ItemRuleType;

public interface Rule {
    ItemRuleType getRuleType();

    Item executeRule(Item item);
}
