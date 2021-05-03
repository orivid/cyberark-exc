package com.cyberark.test.integration.items;

import com.cyberark.items.entities.Item;
import com.cyberark.items.entities.ItemType;
import com.cyberark.test.TestApp;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestApp.class}, initializers = ConfigFileApplicationContextInitializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemRestIntegrationTest {

    @Autowired
    private RestTemplate restTemplate;
    private Item expectedItem;

    @BeforeAll
    public void init() {
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:8080/api"));
        restTemplate.setErrorHandler(new IgnoreExceptionErrorHandler());

        expectedItem = new Item(1L, ItemType.T_SHIRT, 10, 20);
    }

    @Test
    public void testGetAllItems() {

        ResponseEntity<List<Item>> itemsResponse =
                restTemplate.exchange("/items", GET, null,
                        new ParameterizedTypeReference<List<Item>>() {
                        });

        assertThat(itemsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Item> items = itemsResponse.getBody();

        assertThat(items).isNotNull();
        assertThat(items.size()).isGreaterThan(0);

        assertThat(items).usingFieldByFieldElementComparator().contains(expectedItem);
    }

    @Test
    public void testGetItem() {
        ResponseEntity<Item> itemResponse =
                restTemplate.getForEntity("/items/" + expectedItem.getId(), Item.class);

        assertThat(itemResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        Item item = itemResponse.getBody();

        assertThat(item).isNotNull();
        assertThat(item).isEqualToComparingFieldByField(expectedItem);
    }

    @Test
    public void testGetItemNotFound() {
        ResponseEntity<Item> returnItem = restTemplate.getForEntity("/items/-1", Item.class);

        assertThat(returnItem).isNotNull();
        assertThat(returnItem.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private HttpEntity<String> getCreateRequest(Item itemToCreate) throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject personJsonObject = new JSONObject();
        personJsonObject.put("type", itemToCreate.getType());
        personJsonObject.put("daysToExpire", itemToCreate.getDaysToExpire());
        personJsonObject.put("price", itemToCreate.getPrice());

        return new HttpEntity<>(personJsonObject.toString(), headers);
    }

    @Test
    public void testCreateItem() throws JSONException {
        Item itemToCreate = new Item(ItemType.LAPTOP, 1, 2);

        HttpEntity<String> request = getCreateRequest(itemToCreate);

        ResponseEntity<Item> itemCreateResponse = restTemplate.postForEntity("/items",
                request,
                Item.class);

        assertThat(itemCreateResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Item createReturnedItem = itemCreateResponse.getBody();

        assertThat(createReturnedItem).isNotNull();
        assertThat(createReturnedItem).isEqualToIgnoringGivenFields(itemToCreate, "id");

        ResponseEntity<Item> itemGetResponse =
                restTemplate.getForEntity("/items/" + createReturnedItem.getId(), Item.class);

        assertThat(itemGetResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        Item getReturnedItem = itemCreateResponse.getBody();
        assertThat(getReturnedItem).isEqualToComparingFieldByField(createReturnedItem);
    }

    @Test
    public void testSetItemRule() throws JSONException {
        Item itemToCreate = new Item(ItemType.LAPTOP, 1, 2);

        HttpEntity<String> request = getCreateRequest(itemToCreate);

        ResponseEntity<Item> itemCreateResponse = restTemplate.postForEntity("/items",
                request,
                Item.class);

        assertThat(itemCreateResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Item createReturnedItem = itemCreateResponse.getBody();

        assertThat(createReturnedItem).isNotNull();
        assertThat(createReturnedItem).isEqualToIgnoringGivenFields(itemToCreate, "id");


        ResponseEntity<Void> responseEntity = restTemplate.exchange("/items/rules?itemType=" + createReturnedItem.getType() + "&itemRuleType=DEFAULT_RULE", HttpMethod.PUT, null, Void.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private class IgnoreExceptionErrorHandler implements ResponseErrorHandler {
        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return response.getStatusCode() != HttpStatus.OK ||
                    response.getStatusCode() != HttpStatus.ACCEPTED;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            // Do nothing in order to make sure no exception is thrown
        }
    }
}