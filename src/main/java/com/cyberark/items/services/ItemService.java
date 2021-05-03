package com.cyberark.items.services;

import com.cyberark.items.entities.Item;

import java.util.List;

public interface ItemService {
    void clearItems();
    List<Item> getItems();
    Item getItem(long id);
    Item addItem(Item item);
    void dailyUpdateItems();
}
