package com.cyberark.items;

import com.cyberark.items.entities.Item;
import com.cyberark.items.entities.ItemRuleType;
import com.cyberark.items.entities.ItemType;
import com.cyberark.items.services.ItemService;
import com.cyberark.items.services.RuleAssociationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/items")
@Validated
public class ItemController {

    private final ItemService itemService;
    private final RuleAssociationService ruleAssociationService;

    @Autowired
    public ItemController(final ItemService itemService, final RuleAssociationService ruleAssociationService) {
        this.itemService = itemService;
        this.ruleAssociationService = ruleAssociationService;
    }

    @GetMapping
    public List<Item> findAllItems() {
        return itemService.getItems();
    }

    @GetMapping(path = "/{id}")
    public Item getItem(@Valid @NotNull @PathVariable(name = "id") long itemId) {
        return itemService.getItem(itemId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Item createItem(@Valid @NotNull @RequestBody Item item) {
        return itemService.addItem(item);
    }

    @PutMapping(path = "/dailyUpdate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void dailyUpdate() {
        itemService.dailyUpdateItems();
    }

    @PutMapping(path = "/rules")
    public void setItemRule(@Valid @NotNull @RequestParam ItemType itemType,@Valid @NotNull @RequestParam ItemRuleType itemRuleType) {
        ruleAssociationService.update(itemType, itemRuleType);
    }
}