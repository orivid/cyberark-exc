package com.cyberark.test.integration.items;

import com.cyberark.items.entities.Item;
import com.cyberark.items.entities.ItemRuleType;
import com.cyberark.items.entities.ItemType;
import com.cyberark.items.services.ItemService;
import com.cyberark.items.services.RuleAssociationService;
import com.cyberark.test.TestApp;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest(classes = {TestApp.class}, properties = {"local.server.port=8080"}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ItemRestIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ItemService itemService;

    @Autowired
    private AtomicLong idCounter;

    @Autowired
    private Map<ItemType, ItemRuleType> itemRuleTypeByItemType;

    @Autowired
    private RuleAssociationService ruleAssociationService;

    private Item expectedItem;

    @BeforeAll
    public void init() {
        expectedItem = new Item(1L, ItemType.T_SHIRT, 10, 20);
    }

    @BeforeEach
    public void before() throws Exception {
        itemService.clearItems();
        itemRuleTypeByItemType.clear();
        idCounter.set(0);
        ((InitializingBean) itemService).afterPropertiesSet();
        ((InitializingBean) ruleAssociationService).afterPropertiesSet();
    }

    @Test
    public void testGetAllItems() {

        List<Item> items = webTestClient.get()
                .uri("/items")
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(new ParameterizedTypeReference<List<Item>>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(items).isNotNull();
        assertThat(items.size()).isGreaterThan(0);

        assertThat(items).usingFieldByFieldElementComparator().contains(expectedItem);
    }

    @Test
    public void testGetItem() {

        Item item = invokeGetItem(expectedItem.getId());

        assertThat(item).isNotNull();
        assertThat(item).isEqualToComparingFieldByField(expectedItem);
    }

    @Test
    public void testGetItemNotFound() {

        webTestClient.get()
                .uri("/items/-1")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody();
    }

    private HttpEntity<String> getCreateRequest(Item itemToCreate) {

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONObject personJsonObject = new JSONObject();
            personJsonObject.put("type", itemToCreate.getType());
            personJsonObject.put("daysToExpire", itemToCreate.getDaysToExpire());
            personJsonObject.put("price", itemToCreate.getPrice());

            return new HttpEntity<>(personJsonObject.toString(), headers);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateItem() {
        Item itemToCreate = new Item(ItemType.LAPTOP, 1, 2);

        HttpEntity<String> request = getCreateRequest(itemToCreate);

        Item createdReturnedItem = invokeCreateItem(request);

        assertThat(createdReturnedItem).isNotNull();
        assertThat(createdReturnedItem).isEqualToIgnoringGivenFields(itemToCreate, "id");

        Item itemGetResponse = invokeGetItem(createdReturnedItem.getId());

        assertThat(itemGetResponse).isEqualToComparingFieldByField(createdReturnedItem);
    }

    @Test
    public void testSetItemRule() {
        Item itemToCreate = new Item(ItemType.LAPTOP, 1, 2);

        HttpEntity<String> request = getCreateRequest(itemToCreate);

        Item createReturnedItem = invokeCreateItem(request);

        assertThat(createReturnedItem).isNotNull();
        assertThat(createReturnedItem).isEqualToIgnoringGivenFields(itemToCreate, "id");

        invokeChangeItemRule(createReturnedItem.getType(), ItemRuleType.GAINS_VALUE_WITH_AGE);
    }

    @Test
    public void testDailyUpdate() {

        invokeChangeItemRule(ItemType.T_SHIRT, ItemRuleType.GAINS_VALUE_WITH_AGE);

        invokeDailyUpdate();

        Item item = invokeGetItem(expectedItem.getId());

        Item expectedUpdatedItem = new Item(expectedItem.getId(), expectedItem.getType(), expectedItem.getDaysToExpire(), expectedItem.getPrice() + 1);

        assertThat(item).isEqualToComparingFieldByField(expectedUpdatedItem);
    }

    @Test
    public void testSetItemRuleNotAffectingOtherItems() {

        invokeChangeItemRule(ItemType.BANANA, ItemRuleType.GAINS_VALUE_WITH_AGE);

        invokeDailyUpdate();

        Item bananaResponseItem = invokeGetItem(7);

        Item expectedBananaItem = new Item(7, ItemType.BANANA, 3, 3);

        assertThat(bananaResponseItem).isEqualToComparingFieldByField(expectedBananaItem);

        Item beerResponseItem = invokeGetItem(3);

        Item expectedBeerItem = new Item(3, ItemType.BEER, 4, 6);

        assertThat(beerResponseItem).isEqualToComparingFieldByField(expectedBeerItem);
    }

    @Test
    public void testSetItemRuleAndItemLosingTwiceAsMuchValue() {

        invokeChangeItemRule(ItemType.BASKETBALL, ItemRuleType.LOSES_CONSTANT_VALUE_WITH_AGE);

        invokeDailyUpdate();

        Item basketBall = invokeGetItem(5);

        Item expectedBasketballItem = new Item(5, ItemType.BASKETBALL, -1, 48);

        assertThat(basketBall).isEqualToComparingFieldByField(expectedBasketballItem);
    }

    @Test
    public void testItemAffectedByRulesChange() {

        invokeChangeItemRule(ItemType.LAPTOP, ItemRuleType.LOSES_CONSTANT_PERCENT_WITH_AGE);

        invokeDailyUpdate();
        invokeDailyUpdate();

        Item laptopUpdatedItem = invokeGetItem(6);

        Item expectedUpdatedLaptop = new Item(6, ItemType.LAPTOP, 363, 451);

        assertThat(laptopUpdatedItem).isEqualToComparingFieldByField(expectedUpdatedLaptop);

        invokeChangeItemRule(ItemType.LAPTOP, ItemRuleType.GAINS_VALUE_WITH_AGE);

        invokeDailyUpdate();

        Item laptopItem = invokeGetItem(6);

        Item expectedItem = new Item(6, ItemType.LAPTOP, 363, 452);
        assertThat(laptopItem).isEqualToComparingFieldByField(expectedItem);

    }

    @Test
    public void testNewItemAffectedByRulesChange() {

        Item newBanana = new Item(ItemType.BANANA, 2, 40);
        HttpEntity<String> createRequest = getCreateRequest(newBanana);
        Item createdBananaResponse = invokeCreateItem(createRequest);

        Item expectedNewBanana = new Item(8, newBanana.getType(), newBanana.getDaysToExpire(), newBanana.getPrice());

        assertThat(createdBananaResponse).isEqualToComparingFieldByField(expectedNewBanana);

        invokeChangeItemRule(ItemType.BANANA, ItemRuleType.LOSES_CONSTANT_PERCENT_WITH_AGE);

        invokeDailyUpdate();
        invokeDailyUpdate();

        Item updateByRuleBanana = invokeGetItem(8);

        Item expectedItemUpdatedByRule = new Item(8, ItemType.BANANA, 0, 34);

        assertThat(updateByRuleBanana).isEqualToComparingFieldByField(expectedItemUpdatedByRule);
    }

    @Test
    public void testInvalidItemId() {
        webTestClient.get()
                .uri("/items/null")
                .exchange()
                .expectStatus().isBadRequest();
    }

    private void invokeChangeItemRule(ItemType itemType, ItemRuleType itemRuleType) {
        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/items/rules")
                        .queryParam("itemType", itemType)
                        .queryParam("itemRuleType", itemRuleType).build())
                .exchange()
                .expectStatus().isOk();
    }

    private Item invokeGetItem(long itemId) {
        return webTestClient.get()
                .uri("/items/" + itemId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class)
                .returnResult()
                .getResponseBody();
    }

    private void invokeDailyUpdate() {
        webTestClient.put()
                .uri("/items/dailyUpdate")
                .exchange()
                .expectStatus().isAccepted();
    }

    private Item invokeCreateItem(HttpEntity<String> request) {
        return webTestClient.post()
                .uri("/items")
                .contentType(Objects.requireNonNull(request.getHeaders().getContentType()))
                .bodyValue(Objects.requireNonNull(request.getBody()))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Item.class)
                .returnResult()
                .getResponseBody();
    }
}