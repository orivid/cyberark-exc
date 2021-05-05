package com.cyberark.items.services;

import com.cyberark.items.entities.ItemRuleType;
import com.cyberark.items.entities.ItemType;

import java.util.Map;

public interface RulesLoader {

    Map<ItemType, ItemRuleType> getItemRuleMapping();
}
