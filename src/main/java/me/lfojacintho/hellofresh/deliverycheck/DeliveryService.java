package me.lfojacintho.hellofresh.deliverycheck;

import me.lfojacintho.hellofresh.deliverycheck.client.HelloFreshClient;
import me.lfojacintho.hellofresh.deliverycheck.client.dto.menu.MealDto;
import me.lfojacintho.hellofresh.deliverycheck.client.dto.recipe.IngredientAmountDto;
import me.lfojacintho.hellofresh.deliverycheck.client.dto.recipe.IngredientDto;
import me.lfojacintho.hellofresh.deliverycheck.client.dto.recipe.RecipeDto;
import me.lfojacintho.hellofresh.deliverycheck.client.dto.recipe.YieldDto;
import me.lfojacintho.hellofresh.deliverycheck.config.HelloFreshProductConfiguration;
import me.lfojacintho.hellofresh.deliverycheck.domain.Delivery;
import me.lfojacintho.hellofresh.deliverycheck.domain.Ingredient;
import me.lfojacintho.hellofresh.deliverycheck.domain.Recipe;
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

    public Delivery retrieveDelivery() {
        final List<MealDto> mealDtos = fetchSelectedMeals();
        final Delivery.DeliveryBuilder deliveryBuilder = Delivery.DeliveryBuilder.builder();

        mealDtos.forEach(mealDto -> {
            final Recipe.RecipeBuilder recipeBuilder = Recipe.RecipeBuilder.builder()
                .withQuantity(mealDto.getSelection().getQuantity())
                .withIndex(mealDto.getIndex())
                .withTitle(mealDto.getRecipe().getName());


            final RecipeDto recipeDto = client.fetchRecipe(mealDto.getRecipe().getId());
            final List<IngredientDto> ingredients = recipeDto.getIngredients();
            final YieldDto yieldDto =
                recipeDto.getYields()
                    .stream()
                    .filter(y -> y.getYields() == productConfiguration.servings())
                    .findFirst()
                    .get();

            ingredients.forEach(ingredientDto -> {
                final Optional<IngredientAmountDto> maybeYieldIngredient = yieldDto.getIngredients().stream()
                    .filter(ingredientAmountDto -> ingredientDto.getId().equals(ingredientAmountDto.getId()))
                    .findFirst();
                final Ingredient.IngredientBuilder ingredientBuilder = Ingredient.IngredientBuilder.builder()
                    .withName(ingredientDto.getName())
                    .withDelivered(ingredientDto.isShipped());

                if (maybeYieldIngredient.isPresent()) {
                    final IngredientAmountDto yieldIngredient = maybeYieldIngredient.get();
                    ingredientBuilder.withQuantity(yieldIngredient.getAmount())
                        .withUnit(yieldIngredient.getUnit());
                }

                recipeBuilder.withIngredient(ingredientBuilder.build());
            });

            deliveryBuilder.withRecipe(recipeBuilder.build());
        });

        return deliveryBuilder.build();

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
