package com.cyberark.items.rules;

import com.cyberark.items.entities.Item;
import com.cyberark.items.entities.ItemRuleType;
import org.springframework.stereotype.Component;

@Component
public class GainValuesWithAgeRule extends AbstractRule {

    @Override
    public ItemRuleType getRuleType() {
        return ItemRuleType.GAINS_VALUE_WITH_AGE;
    }

    @Override
    protected Item runRule(final Item item) {
        int price = item.getPrice() + 1;
        return new Item(item.getId(), item.getType(), item.getDaysToExpire(), price);
    }
}
