package me.lfojacintho.hellofresh.deliverycheck;

import me.lfojacintho.hellofresh.deliverycheck.domain.Delivery;
import me.lfojacintho.hellofresh.deliverycheck.domain.Ingredient;
import me.lfojacintho.hellofresh.deliverycheck.domain.Recipe;
import me.lfojacintho.hellofresh.deliverycheck.util.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class DeliveryServiceIT extends AbstractIntegrationTest {

    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryServiceIT(final DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Test
    void retrieveDeliveryShouldBringDeliveryForTheWeek() {
        final String week = "2021-W45";
        final Delivery delivery = deliveryService.retrieveDelivery(week);
        assertThat(delivery)
            .isNotNull();

        assertThat(delivery.getRecipes())
            .isNotNull()
            .hasSize(3)
            .extracting(
                Recipe::quantity,
                Recipe::index,
                Recipe::title
            )
            .containsExactly(
                tuple(1, 4, "Honig-Senf-Hähnchen mit Kürbispüree"),
                tuple(1, 28, "Schnelle Maxi-Tortelli Ricotta-Spinat"),
                tuple(2, 34, "Knuspriges Kräuterschnitzel")
            );

        final Ingredient ingredientWithoutQuantity = delivery
            .getRecipes()
            .get(0)
            .deliveredIngredients()
            .stream()
            .filter(ingredient -> ingredient.getName().equals("Feldsalat"))
            .findFirst()
            .get();
        assertThat(ingredientWithoutQuantity.isQuantityAvailable()).isFalse();
    }
}
