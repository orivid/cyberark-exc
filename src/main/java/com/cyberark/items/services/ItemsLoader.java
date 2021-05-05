package com.cyberark.items.services;

import com.cyberark.items.entities.Item;

import java.util.List;

public interface ItemsLoader {
    List<Item> getItems();
}
