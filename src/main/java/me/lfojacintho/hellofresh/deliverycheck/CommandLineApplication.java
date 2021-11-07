package me.lfojacintho.hellofresh.deliverycheck;

import me.lfojacintho.hellofresh.deliverycheck.domain.Delivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineApplication implements CommandLineRunner {

    private final DeliveryService deliveryService;

    @Autowired
    public CommandLineApplication(final DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Override
    public void run(final String... args) {
        final Delivery delivery = deliveryService.retrieveDelivery();

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
                        ingredient.quantity(),
                        ingredient.unit()
                    );
                } else {
                    System.out.printf(
                        "  %s - Unknown quantity%n",
                        ingredient.name()
                    );
                }
            });

            System.out.println();
            System.out.println();
        });
    }
}
