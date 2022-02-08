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
import me.lfojacintho.hellofresh.deliverycheck.domain.Quantity;
import me.lfojacintho.hellofresh.deliverycheck.domain.Recipe;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {

    // TODO remove when migrate this to Kotlin
    private static final Quantity UNKNOWN_QUANTITY = new Quantity(0, "(?)", false);

    private final HelloFreshClient client;
    private final HelloFreshProductConfiguration productConfiguration;

    public DeliveryService(
        final HelloFreshClient client,
        final HelloFreshProductConfiguration productConfiguration
    ) {
        this.client = client;
        this.productConfiguration = productConfiguration;
    }

    public Delivery retrieveDelivery(final String week) {
        final List<MealDto> mealDtos = fetchSelectedMeals(week);
        final List<Recipe> recipes = new ArrayList<>();

        mealDtos.forEach(mealDto -> recipes.add(retrieveRecipe(mealDto)));

        return new Delivery(recipes);
    }

    private Recipe retrieveRecipe(final MealDto mealDto) {
        final RecipeDto recipeDto = client.fetchRecipe(mealDto.getRecipe().getId());
        final List<IngredientDto> ingredients = recipeDto.getIngredients();
        final YieldDto yieldDto =
            recipeDto.getYields()
                .stream()
                .filter(y -> y.getYields() == productConfiguration.servings())
                .findFirst()
                .orElse(null);

        final List<Ingredient> recipeIngredients = new ArrayList<>();
        ingredients.forEach(ingredientDto -> {
            final Ingredient ingredient = retrieveIngredient(ingredientDto, yieldDto);
            recipeIngredients.add(ingredient);
        });

        return new Recipe(
            mealDto.getSelection().getQuantity(),
            mealDto.getIndex(),
            mealDto.getRecipe().getName(),
            recipeIngredients
        );
    }

    private Ingredient retrieveIngredient(
        final IngredientDto ingredientDto,
        final YieldDto yieldDto
    ) {
        final Optional<IngredientAmountDto> maybeYieldIngredient = Optional.ofNullable(yieldDto)
            .map(YieldDto::getIngredients)
            .stream()
            .flatMap(List::stream)
            .filter(ingredientAmountDto -> ingredientDto.getId().equals(ingredientAmountDto.getId()))
            .findFirst();

        final Quantity quantity;
        if (maybeYieldIngredient.isPresent()) {
            final IngredientAmountDto yieldIngredient = maybeYieldIngredient.get();
            quantity = new Quantity(
                yieldIngredient.getAmount(),
                Optional.ofNullable(yieldIngredient.getUnit()).orElse("(?)"),
                true
            );
        } else {
            quantity = UNKNOWN_QUANTITY;
        }

        return new Ingredient(ingredientDto.getName(), quantity, ingredientDto.getShipped());
    }

    private List<MealDto> fetchSelectedMeals(final String week) {
        return client.fetchMenu(week).getSelectedMeals();
    }


}
