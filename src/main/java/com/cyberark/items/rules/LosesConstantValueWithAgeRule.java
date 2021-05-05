package com.cyberark.items.rules;

import com.cyberark.items.entities.Item;
import com.cyberark.items.entities.ItemRuleType;
import org.springframework.stereotype.Component;

@Component
public class LosesConstantValueWithAgeRule extends AbstractRule {

    @Override
    public ItemRuleType getRuleType() {
        return ItemRuleType.LOSES_CONSTANT_VALUE_WITH_AGE;
    }

    @Override
    protected Item runRule(final Item item) {
        int updatePrice = item.getPrice() - 1;
        int updatedDaysToExpire = item.getDaysToExpire() - 1;

        return new Item(item.getId(), item.getType(), updatedDaysToExpire, updatePrice);
    }
}
