package com.cyberark.items.services;

import com.cyberark.items.entities.Item;
import com.cyberark.items.exception.ItemNotFoundException;
import com.cyberark.items.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.slf4j.helpers.MessageFormatter.format;

@Slf4j
@Service
public class InMemoryItemService implements ItemService, InitializingBean {

    private final ItemRepository itemRepository;
    private final ItemsLoader itemsLoader;
    private final RuleService ruleService;

    @Autowired
    public InMemoryItemService(final ItemRepository itemRepository,
                               final ItemsLoader itemsLoader,
                               final RuleService ruleService) {
        this.itemRepository = itemRepository;
        this.itemsLoader = itemsLoader;
        this.ruleService = ruleService;
    }

    @Override
    public void afterPropertiesSet() {
        final List<Item> items = itemsLoader.getItems();
        items.forEach(itemRepository::saveItem);
    }

    @Override
    public void clearItems() {
        itemRepository.deleteItems();
    }

    @Override
    public List<Item> getItems() {
        return itemRepository.findItems();
    }

    @Override
    public Item getItem(final long id) {
        final Item item = itemRepository.findItem(id);

        if (item == null) {
            throw new ItemNotFoundException(format("Item with id:{} was not found", id).getMessage());
        }

        return item;
    }

    @Override
    public Item addItem(final Item item) {
        final Item savedItem = itemRepository.saveItem(item);
        log.info("Created new item. {}", savedItem);
        return savedItem;
    }

    @Override
    @Async
    public void dailyUpdateItems() {
        log.info("Starting daily update process for items");

        final List<Item> items = itemRepository.findItems();

        items.forEach((item -> {
            Item updateItem = ruleService.executeRule(item);
            log.debug("Update Item. {} -> {}", item, updateItem);
            itemRepository.updateItem(updateItem);
        }));

        log.info("Finished daily update");

    }
}