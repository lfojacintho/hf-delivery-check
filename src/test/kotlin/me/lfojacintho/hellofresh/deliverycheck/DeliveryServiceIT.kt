package me.lfojacintho.hellofresh.deliverycheck

import me.lfojacintho.hellofresh.deliverycheck.domain.Recipe
import me.lfojacintho.hellofresh.deliverycheck.util.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class DeliveryServiceIT(
    @Autowired private val deliveryService: DeliveryService
) : AbstractIntegrationTest() {

    @Test
    fun retrieveDeliveryShouldBringDeliveryForTheWeek() {
        val week = "2021-W45"
        val delivery = deliveryService.retrieveDelivery(week)

        assertThat(delivery.recipes)
            .hasSize(4)
            .extracting(
                Recipe::quantity,
                Recipe::index,
                Recipe::title
            ).containsExactly(
                tuple(1, 4, "Honig-Senf-Hähnchen mit Kürbispüree"),
                tuple(1, 28, "Schnelle Maxi-Tortelli Ricotta-Spinat"),
                tuple(2, 34, "Knuspriges Kräuterschnitzel"),
                tuple(1, 303, "Hoisin-Hackfleisch-Miniburger")
            )

        val ingredientWithoutQuantity = delivery
            .recipes[0]
            .deliveredIngredients()
            .find { it.name == "Feldsalat" }
        assertThat(ingredientWithoutQuantity)
            .isNotNull
            .extracting { it!!.quantity.isKnown }
            .isEqualTo(false)
    }
}
