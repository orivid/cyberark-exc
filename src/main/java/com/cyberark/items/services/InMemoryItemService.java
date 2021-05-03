package com.cyberark.items.services;

import com.cyberark.items.entities.Item;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Service
public class InMemoryItemService implements ItemService {

    @Override
    public void clearItems() {
        throw new NotImplementedException();
    }

    @Override
    public List<Item> getItems() {
        throw new NotImplementedException();
    }

    @Override
    public Item getItem(long id) {
        throw new NotImplementedException();

    }

    @Override
    public Item addItem(Item item) {
        throw new NotImplementedException();

    }

    @Override
    public void dailyUpdateItems() {
        throw new NotImplementedException();
    }
}