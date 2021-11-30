package me.lfojacintho.hellofresh.deliverycheck;

import me.lfojacintho.hellofresh.deliverycheck.client.HelloFreshClient;
import me.lfojacintho.hellofresh.deliverycheck.client.dto.menu.MenuDto;
import me.lfojacintho.hellofresh.deliverycheck.config.HelloFreshProductConfiguration;
import me.lfojacintho.hellofresh.deliverycheck.domain.Delivery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeliveryServiceTest {

    private static final String ARBITRARY_WEEK = "2021-W40";

    private HelloFreshClient client;

    private DeliveryService deliveryService;

    @BeforeEach
    void setUp() {
        client = mock(HelloFreshClient.class);
        final HelloFreshProductConfiguration productConfiguration = new HelloFreshProductConfiguration(
            "country",
            "locale",
            "sku",
            "preference",
            2,
            "subscription",
            "postcode",
            "deliveryOption"
        );
        deliveryService = new DeliveryService(client, productConfiguration);
    }

    @Test
    void retrieveDeliveryShouldUseTheWeekPassedAsArgument() {
        // arrange
        when(client.fetchMenu(ARBITRARY_WEEK)).thenReturn(new MenuDto());

        // act
        deliveryService.retrieveDelivery(ARBITRARY_WEEK);

        // assert
        verify(client).fetchMenu(ARBITRARY_WEEK);
    }

    @Test
    void retrieveDeliveryShouldReturnEmptyDeliveryWhenNoMealIsFound() {
        // arrange
        when(client.fetchMenu(ARBITRARY_WEEK)).thenReturn(new MenuDto());

        // act
        final Delivery delivery = deliveryService.retrieveDelivery(ARBITRARY_WEEK);

        // assert
        assertThat(delivery)
            .isNotNull()
            .extracting(Delivery::recipes)
            .isNull();
        verify(client, never()).fetchRecipe(anyString());
    }
}
