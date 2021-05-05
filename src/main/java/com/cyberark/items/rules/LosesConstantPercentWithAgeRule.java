package com.cyberark.items.rules;

import com.cyberark.items.entities.Item;
import com.cyberark.items.entities.ItemRuleType;
import org.springframework.stereotype.Component;

@Component
public class LosesConstantPercentWithAgeRule extends AbstractRule {

    @Override
    public ItemRuleType getRuleType() {
        return ItemRuleType.LOSES_CONSTANT_PERCENT_WITH_AGE;
    }

    @Override
    protected Item runRule(final Item item) {
        final int updateDaysToExpire = item.getDaysToExpire() - 1;
        final int updatePrice = getItemPrice(item);

        return new Item(item.getId(), item.getType(), updateDaysToExpire, updatePrice);
    }

    private int getItemPrice(final Item item) {

        int itemPrice = item.getPrice();

        if (itemPrice < 20) {
            return itemPrice;
        }

        float percentage =  (5f / 100) * itemPrice;

        float updatePrice = itemPrice - percentage;

        return Math.round(updatePrice);
    }

}
