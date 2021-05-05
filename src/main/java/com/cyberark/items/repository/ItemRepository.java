package com.cyberark.items.repository;

import com.cyberark.items.entities.Item;

import java.util.List;

public interface ItemRepository {
    List<Item> findItems();
    Item findItem(long id);
    Item saveItem(Item item);
    void updateItem(Item item);
    void deleteItems();
}
