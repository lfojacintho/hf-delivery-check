package me.lfojacintho.hellofresh.deliverycheck

import me.lfojacintho.hellofresh.deliverycheck.client.HelloFreshClient
import me.lfojacintho.hellofresh.deliverycheck.client.dto.menu.MealDto
import me.lfojacintho.hellofresh.deliverycheck.client.dto.recipe.IngredientDto
import me.lfojacintho.hellofresh.deliverycheck.client.dto.recipe.YieldDto
import me.lfojacintho.hellofresh.deliverycheck.config.HelloFreshProductConfiguration
import me.lfojacintho.hellofresh.deliverycheck.domain.*
import org.springframework.stereotype.Service

@Service
class DeliveryService(
    private val client: HelloFreshClient,
    private val productConfiguration: HelloFreshProductConfiguration
) {

    fun retrieveDelivery(week: String): Delivery {
        val selectedMeals = client
            .fetchMenu(week)
            .selectedMeals()
            .map { retrieveRecipe(it) }

        return Delivery(selectedMeals)
    }

    private fun retrieveRecipe(mealDto: MealDto): Recipe {
        val recipeDto = client.fetchRecipe(mealDto.recipe.id)
        val ingredients = recipeDto.ingredients;
        val yieldDto = recipeDto.yields.firstOrNull { it.yields == productConfiguration.servings }
        val recipeIngredients = ingredients.map { retrieveIngredient(it, yieldDto) }

        return Recipe(
            mealDto.selection!!.quantity(),
            mealDto.index,
            mealDto.recipe.name,
            recipeIngredients
        )
    }

    private fun retrieveIngredient(ingredientDto: IngredientDto, yieldDto: YieldDto?): Ingredient {
        val yieldIngredient = yieldDto?.ingredients?.firstOrNull { it.id == ingredientDto.id }
        val quantity = if (yieldIngredient != null) Quantity(
            yieldIngredient.amount,
            yieldIngredient.unit ?: "(?)"
        ) else UNKNOWN_QUANTITY

        return Ingredient(ingredientDto.name, quantity, ingredientDto.shipped)
    }
}
