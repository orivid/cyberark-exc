package com.cyberark.items.repository;

import com.cyberark.items.entities.Item;
import com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> itemByItemId;
    private final AtomicLong atomicLong;

    public InMemoryItemRepositoryImpl(final AtomicLong itemIdCounter) {
        itemByItemId = new ConcurrentHashMap<>();
        this.atomicLong = itemIdCounter;
    }

    @Override
    public List<Item> findItems() {
        return ImmutableList.copyOf(itemByItemId.values());
    }

    @Override
    public Item findItem(final long id) {
        return itemByItemId.get(id);
    }

    @Override
    public Item saveItem(final Item item) {
        final long itemId = atomicLong.incrementAndGet();
        Item newItem = new Item(itemId, item.getType(), item.getDaysToExpire(), item.getPrice());
        itemByItemId.put(itemId, newItem);
        return newItem;
    }

    @Override
    public void updateItem(Item item) {
        itemByItemId.put(item.getId(), item);
    }

    @Override
    public void deleteItems() {
        itemByItemId.clear();
    }
}
