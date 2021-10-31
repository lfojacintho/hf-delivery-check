package me.lfojacintho.hellofresh.deliverycheck;

import me.lfojacintho.hellofresh.deliverycheck.client.HelloFreshClient;
import me.lfojacintho.hellofresh.deliverycheck.client.dto.menu.MealDto;
import me.lfojacintho.hellofresh.deliverycheck.client.dto.recipe.IngredientAmountDto;
import me.lfojacintho.hellofresh.deliverycheck.client.dto.recipe.IngredientDto;
import me.lfojacintho.hellofresh.deliverycheck.client.dto.recipe.RecipeDto;
import me.lfojacintho.hellofresh.deliverycheck.client.dto.recipe.YieldDto;
import me.lfojacintho.hellofresh.deliverycheck.config.HelloFreshProductConfiguration;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {

    private final HelloFreshClient client;
    private final HelloFreshProductConfiguration productConfiguration;

    public DeliveryService(
        final HelloFreshClient client,
        final HelloFreshProductConfiguration productConfiguration
    ) {
        this.client = client;
        this.productConfiguration = productConfiguration;
    }

    public void generateDeliveryList() {
        final List<MealDto> mealDtos = fetchSelectedMeals();

        System.out.println("Your delivery contains the following recipes:");
        mealDtos.forEach(mealDto -> {
            System.out.printf(
                "  %dx (%d) - %s%n",
                mealDto.getSelection().getQuantity(),
                mealDto.getIndex(),
                mealDto.getRecipe().getName()
            );

            System.out.println();
            System.out.println("The following ingredients should be delivered");
            final RecipeDto recipeDto = client.fetchRecipe(mealDto.getRecipe().getId());
            final List<IngredientDto> ingredients = recipeDto.getShippedIngredients();
            final YieldDto yieldDto =
                recipeDto.getYields().stream().filter(y -> y.getYields() == productConfiguration.servings()).findFirst().get();

            ingredients.forEach(ingredientDto -> {
                final Optional<IngredientAmountDto> maybeYieldIngredient = yieldDto.getIngredients().stream()
                    .filter(ingredientAmountDto -> ingredientDto.getId().equals(ingredientAmountDto.getId()))
                    .findFirst();

                if (maybeYieldIngredient.isEmpty()) {
                    System.out.printf(
                        "  %s - Unknown quantity%n",
                        ingredientDto.getName()
                    );
                } else {
                    final IngredientAmountDto yieldIngredient = maybeYieldIngredient.get();

                    System.out.printf(
                        "  %s - %.0f %s%n",
                        ingredientDto.getName(),
                        yieldIngredient.getAmount(),
                        yieldIngredient.getUnit()
                    );
                }

            });

            System.out.println();
            System.out.println();
        });

//        System.out.println();
//        System.out.println("The following ingredients should be delivered");
//
//        final Map<String, IngredientInfo> ingredientsMap = new LinkedHashMap<>();
//        mealDtos.forEach(mealDto -> {
//            final RecipeDto recipeDto = fetchIngredients(mealDto.getRecipe().getId());
//            final List<IngredientDto> ingredients = recipeDto.getShippedIngredients();
//            final YieldDto yieldDto =
//                recipeDto.getYields().stream().filter(y -> y.getYields() == 2).findFirst().get();
//
//            ingredients.forEach(ingredientDto -> {
////                final IngredientInfo ingredientInfo = ingredientsMap.computeIfAbsent(
////                    ingredientDto.getId(),
////                    v -> new IngredientInfo(ingredientDto.getId(), ingredientDto.getName())
////                );
//                final Optional<IngredientAmountDto> maybeYieldIngredient = yieldDto.getIngredients().stream()
//                    .filter(ingredientAmountDto -> ingredientDto.getId().equals(ingredientAmountDto.getId()))
//                    .findFirst();
//
//                if (maybeYieldIngredient.isEmpty()) {
//                    System.err.println(">>> Quantity not found for " + ingredientDto.getName());
//                } else {
////                    final IngredientAmountDto yieldIngredient = maybeYieldIngredient.get();
////                    ingredientInfo.addQuantityInfo(new QuantityInfo(
////                        mealDto.getIndex(),
////                        yieldIngredient.getAmount(),
////                        yieldIngredient.getUnit()
////                    ));
//                }
//            });
//        });
//
//        ingredientsMap.values().forEach(value -> System.out.printf(
//            "  %s",
//            value
//        ));
    }

    private List<MealDto> fetchSelectedMeals() {
        return client.fetchMenu(getCurrentWeek()).getSelectedMeals();
    }

    private String getCurrentWeek() {
        final LocalDate today = LocalDate.now();
        return String.format("%d-W%d", today.getYear(), today.get(ChronoField.ALIGNED_WEEK_OF_YEAR));
    }
}
