package com.cyberark.items.rules;

import com.cyberark.items.entities.Item;
import com.cyberark.items.entities.ItemRuleType;
import org.springframework.stereotype.Component;

@Component
public class FixedValueRule extends AbstractRule {

    @Override
    public ItemRuleType getRuleType() {
        return ItemRuleType.FIXED_VALUE;
    }

    @Override
    protected Item runRule(final Item item) {
        return item;
    }
}
