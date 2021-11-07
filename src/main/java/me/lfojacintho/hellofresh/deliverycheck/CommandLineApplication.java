package me.lfojacintho.hellofresh.deliverycheck;

import me.lfojacintho.hellofresh.deliverycheck.domain.Delivery;
import me.lfojacintho.hellofresh.deliverycheck.domain.Ingredient;
import me.lfojacintho.hellofresh.deliverycheck.domain.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Profile("!test")
@Component
public class CommandLineApplication implements CommandLineRunner {

    private final DeliveryService deliveryService;

    @Autowired
    public CommandLineApplication(final DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Override
    public void run(final String... args) {
        final Delivery delivery = deliveryService.retrieveDelivery(getCurrentWeek());
        final Map<String, List<Quantity>> nonDeliveredIngredientMap = new LinkedHashMap<>();

        System.out.println("Your delivery contains the following recipes:");
        delivery.recipes().forEach(recipe -> {
            System.out.printf(
                "  %dx (%d) - %s%n",
                recipe.quantity(),
                recipe.index(),
                recipe.title()
            );

            System.out.println();
            System.out.println("The following ingredients should be delivered");
            recipe.deliveredIngredients().forEach(ingredient -> {
                if (ingredient.isQuantityAvailable()) {
                    System.out.printf(
                        "  %s - %.0f %s%n",
                        ingredient.name(),
                        ingredient.quantity().amount(),
                        ingredient.quantity().unit()
                    );
                } else {
                    System.out.printf(
                        "  %s - Unknown quantity%n",
                        ingredient.name()
                    );
                }
            });

            recipe.nonDeliveredIngredients().forEach(ingredient -> {
                if (ingredient.isQuantityAvailable()) {
                    aggregateNonDeliveredIngredients(nonDeliveredIngredientMap, ingredient);
                }
            });

            System.out.println();
            System.out.println();
        });

        System.out.println("You need to have the following ingredients at home:");
        nonDeliveredIngredientMap.forEach((name, quantities) -> quantities.forEach(quantity -> System.out.printf(
            "  %s - %.0f %s%n",
            name,
            quantity.amount(),
            quantity.unit()
        )));
    }

    private String getCurrentWeek() {
        final LocalDate today = LocalDate.now();
        return String.format("%d-W%d", today.getYear(), today.get(ChronoField.ALIGNED_WEEK_OF_YEAR));
    }

    private void aggregateNonDeliveredIngredients(
        final Map<String, List<Quantity>> nonDeliveredIngredientMap,
        final Ingredient ingredient
    ) {
        if (!nonDeliveredIngredientMap.containsKey(ingredient.name())) {
            final List<Quantity> quantities = new ArrayList<>();
            quantities.add(ingredient.quantity());
            nonDeliveredIngredientMap.put(ingredient.name(), quantities);
        } else {
            final List<Quantity> quantities = nonDeliveredIngredientMap.get(ingredient.name());
            final Quantity existingQuantity = findAndRemoveQuantityWithSameUnit(quantities, ingredient.quantity());
            if (existingQuantity != null) {
                quantities.add(existingQuantity.plus(ingredient.quantity()));
            } else {
                quantities.add(ingredient.quantity());
            }
        }
    }

    private Quantity findAndRemoveQuantityWithSameUnit(
        final List<Quantity> quantities,
        final Quantity quantity
    ) {
        for (int i = 0; i < quantities.size(); i++) {
            if (quantities.get(i).isSameUnit(quantity)) {
                return quantities.remove(i);
            }
        }

        return null;
    }
}
