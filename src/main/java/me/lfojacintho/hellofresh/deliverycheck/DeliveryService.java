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

    public Delivery retrieveDelivery(final String week) {
        final List<MealDto> mealDtos = fetchSelectedMeals(week);
        final Delivery.Builder deliveryBuilder = new Delivery.Builder();

        mealDtos.forEach(mealDto -> {
            final Recipe recipe = retrieveRecipe(mealDto);
            deliveryBuilder.withRecipe(recipe);
        });

        return deliveryBuilder.build();
    }

    private Recipe retrieveRecipe(final MealDto mealDto) {
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
                .orElse(null);

        ingredients.forEach(ingredientDto -> {
            final Ingredient ingredient = retrieveIngredient(ingredientDto, yieldDto);
            recipeBuilder.withIngredient(ingredient);
        });

        return recipeBuilder.build();
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

        final Ingredient.IngredientBuilder ingredientBuilder = Ingredient.IngredientBuilder.builder()
            .withName(ingredientDto.getName())
            .withDelivered(ingredientDto.getShipped());

        if (maybeYieldIngredient.isPresent()) {
            final IngredientAmountDto yieldIngredient = maybeYieldIngredient.get();
            ingredientBuilder.withQuantity(Quantity.of(
                yieldIngredient.getAmount(),
                yieldIngredient.getUnit()
            ));
        }
        return ingredientBuilder.build();
    }

    private List<MealDto> fetchSelectedMeals(final String week) {
        return client.fetchMenu(week).getSelectedMeals();
    }


}
