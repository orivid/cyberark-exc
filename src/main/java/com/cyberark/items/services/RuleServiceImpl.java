package com.cyberark.items.services;

import com.cyberark.items.entities.Item;
import com.cyberark.items.entities.ItemRuleType;
import com.cyberark.items.rules.Rule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RuleServiceImpl implements RuleService {

    private final Map<ItemRuleType, Rule> ruleByItemRuleType;
    private final List<Rule> rules;
    private final RuleAssociationService ruleAssociationService;

    public RuleServiceImpl(final List<Rule> rules,
                           final RuleAssociationService ruleAssociationService,
                           final Map<ItemRuleType, Rule> ruleByItemRuleType) {
        this.ruleAssociationService = ruleAssociationService;
        this.rules = rules;
        this.ruleByItemRuleType = ruleByItemRuleType;
    }

    @PostConstruct
    public void init() {
        rules.forEach(rule -> ruleByItemRuleType.put(rule.getRuleType(), rule));
    }

    @Override
    public Item executeRule(final Item item) {

        final ItemRuleType itemRuleType = ruleAssociationService.getRuleType(item.getType());

        final Rule rule = ruleByItemRuleType.get(itemRuleType);
        return rule.executeRule(item);
    }
}
