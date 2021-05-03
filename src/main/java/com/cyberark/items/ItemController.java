package com.cyberark.items;

import com.cyberark.items.entities.Item;
import com.cyberark.items.entities.ItemRuleType;
import com.cyberark.items.entities.ItemType;
import com.cyberark.items.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping(path = "/api/items")
    public ResponseEntity<List<Item>> findAllItems() {

        throw new NotImplementedException();
    }

    @GetMapping(path = "/api/items/{id}")
    public ResponseEntity<Item> getItem(@PathVariable(name = "id") Long itemId) {

        throw new NotImplementedException();
    }

    @PostMapping(path = "/api/items")
    @ResponseBody
    public ResponseEntity<Item> createItem(@RequestBody Item item) {

        throw new NotImplementedException();
    }

    @PutMapping(path = "/api/items/dailyUpdate")
    @ResponseBody
    public ResponseEntity<Void> dailyUpdate() {
        itemService.dailyUpdateItems();
        throw new NotImplementedException();
    }

    @PutMapping(path = "/api/items/rules")
    @ResponseBody
    public ResponseEntity<Void> setItemRule(@RequestParam ItemType itemType, @RequestParam ItemRuleType itemRuleType) {

       throw new NotImplementedException();
    }
}