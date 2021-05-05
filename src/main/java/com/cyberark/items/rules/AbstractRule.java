package com.cyberark.items.rules;

import com.cyberark.items.entities.Item;

public abstract class AbstractRule implements Rule {

    @Override
    public Item executeRule(final Item item) {

        final Item updatedItem = runRule(item);
        final boolean validPrice = isItemPriceValid(updatedItem);

        if (!validPrice) {


            return new Item(item.getId(), item.getType(), updatedItem.getDaysToExpire(), item.getPrice());
        }

        return processItem(item, updatedItem);
    }

    private boolean isItemPriceValid(final Item updatedItem) {
        return updatedItem.getPrice() >= 0 && updatedItem.getPrice() <= 1000;
    }

    private Item processItem(final Item originalItem, final Item updatedItem) {
        if (updatedItem.getDaysToExpire() > 0) {
            return updatedItem;
        }

        int lostValue = (originalItem.getPrice() - updatedItem.getPrice()) * 2;

        if (lostValue < 0) {
            return updatedItem;
        }

        int finalPrice = originalItem.getPrice() - lostValue;

        int updatePrice = Math.max(finalPrice, 0);

        return new Item(updatedItem.getId(), updatedItem.getType(), updatedItem.getDaysToExpire(), updatePrice);
    }

    protected abstract Item runRule(final Item item);
}
