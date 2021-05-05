package com.cyberark.items.services;

import com.cyberark.items.entities.Item;
import com.cyberark.items.entities.ItemType;
import com.cyberark.test.UnitTestApp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {UnitTestApp.class}, initializers = ConfigFileApplicationContextInitializer.class)
class RuleServiceImplTest {

    @Autowired
    private RuleServiceImpl ruleService;

    @Test
    void when_executeRuleWithTShirt_expect_fixedValueRule() {

        Item tShirtItem = new Item(1, ItemType.T_SHIRT, 1, 1);
        Item updatedItem = ruleService.executeRule(tShirtItem);

        Item expectedItem = new Item(1, ItemType.T_SHIRT, 1, 1);

        assertThat(updatedItem).isEqualToComparingFieldByField(expectedItem);
    }

    @Test
    void when_executeRuleWithBasketball_expect_fixedValueRule() {

        Item basketballItem = new Item(1, ItemType.BASKETBALL, 10, 10);
        Item updatedItem = ruleService.executeRule(basketballItem);

        Item expectedItem = new Item(1, ItemType.BASKETBALL, 10, 10);

        assertThat(updatedItem).isEqualToComparingFieldByField(expectedItem);
    }

    @Test
    void when_executeRuleWithScotch_expect_gainsValueWithAgeRule() {
        Item scotchItem = new Item(1, ItemType.SCOTCH_BOTTLE, 10, 10);
        Item updatedItem = ruleService.executeRule(scotchItem);

        Item expectedItem = new Item(1, ItemType.SCOTCH_BOTTLE, 10, 11);

        assertThat(updatedItem).isEqualToComparingFieldByField(expectedItem);
    }

    @Test
    void when_executeRuleWithLaptopWithPriceOf98_expect_losesConstantPercentWithAgeRule() {
        Item laptopItem = new Item(1, ItemType.LAPTOP, 10, 98);
        Item updatedItem = ruleService.executeRule(laptopItem);


        Item expectedItem = new Item(1, ItemType.LAPTOP, 9, 93);
        assertThat(updatedItem).isEqualToComparingFieldByField(expectedItem);
    }

    @Test
    void when_executeRuleWithLaptopWithPriceOf15_expect_losesConstantPercentWithAgeRuleWithoutChangingPrice() {
        Item laptopItem = new Item(1, ItemType.LAPTOP, 10, 15);
        Item updatedItem = ruleService.executeRule(laptopItem);

        Item expectedItem = new Item(1, ItemType.LAPTOP, 9, 15);
        assertThat(updatedItem).isEqualToComparingFieldByField(expectedItem);
    }

    @Test
    void when_executeRuleWithBanana_expect_losesConstantValueWithAgeRule() {

        Item bananaItem = new Item(1, ItemType.BANANA, 2, 2);
        Item updatedItem = ruleService.executeRule(bananaItem);

        Item expectedItem = new Item(1, ItemType.BANANA, 1, 1);
        assertThat(updatedItem).isEqualToComparingFieldByField(expectedItem);
    }

    @Test
    void when_executeRuleWithBeer_expect_losesConstantValueWithAgeRule() {

        Item beerItem = new Item(1, ItemType.BEER, 5, 1);
        Item updatedItem = ruleService.executeRule(beerItem);

        Item expectedItem = new Item(1, ItemType.BEER, 4, 0);
        assertThat(updatedItem).isEqualToComparingFieldByField(expectedItem);
    }

    @Test
    void when_executeRuleWithDaysToExpireIsZero_expect_losingTwiceTheValue() {
        Item beerItem = new Item(1, ItemType.BEER, 0, 4);
        Item updatedItem = ruleService.executeRule(beerItem);

        Item expectedItem = new Item(1, ItemType.BEER, -1, 2);
        assertThat(updatedItem).isEqualToComparingFieldByField(expectedItem);
    }

    @Test
    void when_executeRuleWithDaysToExpireIsNegative_expect_losingTwiceTheValue() {
        Item beerItem = new Item(1, ItemType.BEER, -2, 10);
        Item updatedItem = ruleService.executeRule(beerItem);

        Item expectedItem = new Item(1, ItemType.BEER, -3, 8);
        assertThat(updatedItem).isEqualToComparingFieldByField(expectedItem);
    }

    @Test
    void when_executeWithLosingRuleWithLowPrice_expect_priceIsNotNegative() {
        Item beerItem = new Item(1, ItemType.BEER, 0, 1);
        Item updatedItem = ruleService.executeRule(beerItem);

        Item expectedItem = new Item(1, ItemType.BEER, -1, 0);
        assertThat(updatedItem).isEqualToComparingFieldByField(expectedItem);
    }

    @Test
    void when_executeWithLosingRuleWithPriceAtZero_expect_priceIsNotNegativeAndDaysToExpireChanges() {
        Item beerItem = new Item(1, ItemType.BEER, -4, 0);
        Item updatedItem = ruleService.executeRule(beerItem);

        Item expectedItem = new Item(1, ItemType.BEER, -5, 0);
        assertThat(updatedItem).isEqualToComparingFieldByField(expectedItem);
    }

    @Test
    void when_executeWithPriceOneThousand_expect_priceRemainsOneThousand() {
        Item scotchItem = new Item(1, ItemType.SCOTCH_BOTTLE, 5, 1000);
        Item updatedItem = ruleService.executeRule(scotchItem);

        Item expectedItem = new Item(1, ItemType.SCOTCH_BOTTLE, 5, 1000);
        assertThat(updatedItem).isEqualToComparingFieldByField(expectedItem);
    }

    @Test
    void when_executeWithGainsValueWithAgeAndZeroDaysToExpire_expect_priceIncreases() {

        Item scotchItem = new Item(1, ItemType.SCOTCH_BOTTLE, 0, 45);
        Item updatedItem = ruleService.executeRule(scotchItem);

        Item expectedItem = new Item(1, ItemType.SCOTCH_BOTTLE, 0, 46);
        assertThat(updatedItem).isEqualToComparingFieldByField(expectedItem);

    }

}