package me.lfojacintho.hellofresh.deliverycheck

import me.lfojacintho.hellofresh.deliverycheck.client.HelloFreshClient
import me.lfojacintho.hellofresh.deliverycheck.client.dto.menu.MenuAddOnDto
import me.lfojacintho.hellofresh.deliverycheck.client.dto.menu.MenuDto
import me.lfojacintho.hellofresh.deliverycheck.config.HelloFreshProductConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

const val ARBITRARY_WEEK = "2021-W40"

class DeliveryServiceTest {
    private lateinit var client: HelloFreshClient
    private lateinit var deliveryService: DeliveryService

    @BeforeEach
    fun setUp() {
        val productConfig = HelloFreshProductConfiguration(
            "country",
            "locale",
            "sku",
            "preference",
            2,
            "subscription",
            "postcode",
            "deliveryOption"
        )

        client = mock(HelloFreshClient::class.java)
        deliveryService = DeliveryService(client, productConfig)
    }

    @Test
    fun retrieveDeliveryShouldUseTheWeekPassedAsArgument() {
        // arrange
        `when`(client.fetchMenu(ARBITRARY_WEEK))
            .thenReturn(MenuDto("id", ARBITRARY_WEEK, emptyList(), emptyAddOn()))

        // act
        deliveryService.retrieveDelivery(ARBITRARY_WEEK)

        // assert
        verify(client).fetchMenu(ARBITRARY_WEEK)
    }

    @Test
    fun retrieveDeliveryShouldReturnEmptyDeliveryWhenNoMealIsFound() {
        // arrange
        `when`(client.fetchMenu(ARBITRARY_WEEK))
            .thenReturn(MenuDto("id", ARBITRARY_WEEK, emptyList(), emptyAddOn()))

        // act
        val delivery = deliveryService.retrieveDelivery(ARBITRARY_WEEK)

        // assert
        assertThat(delivery.recipes)
            .isEmpty()
        verify(client, never()).fetchRecipe(anyString())
    }

    private fun emptyAddOn() = MenuAddOnDto(emptyList())
}
