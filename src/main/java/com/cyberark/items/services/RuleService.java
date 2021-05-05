package com.cyberark.items.services;

import com.cyberark.items.entities.Item;

public interface RuleService {
    Item executeRule(Item item);
}
