package me.lfojacintho.hellofresh.deliverycheck.client

import me.lfojacintho.hellofresh.deliverycheck.client.dto.menu.MenuDto
import me.lfojacintho.hellofresh.deliverycheck.client.dto.recipe.RecipeDto
import me.lfojacintho.hellofresh.deliverycheck.util.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class HelloFreshClientIT(
    @Autowired private val helloFreshClient: HelloFreshClient
) : AbstractIntegrationTest() {

    @Test
    fun fetchMenuShouldCallTheEndpointWithCorrectParameters() {
        // stub for the endpoint with expected parameters are set at WireMockTestExtension
        // parameters are set at application.properties from test resources

        val week = "2021-W45"
        val menuDto = helloFreshClient.fetchMenu(week)
        assertThat(menuDto)
            .extracting(
                MenuDto::id,
                MenuDto::week,
            ).containsExactly(
                "61508b64665c2655834786b3",
                week
            )

        assertThat(menuDto.meals).hasSize(7)
        assertThat(menuDto.selectedMeals()).hasSize(4)
    }

    @Test
    fun fetchRecipeShouldCallTheEndpointWithCorrectParameters() {
        // stub for the endpoint with expected parameters are set at WireMockTestExtension
        // parameters are set at application.properties from test resources

        val recipeId = "61508a0dcc9f2a786e62b22f"
        val recipeDto = helloFreshClient.fetchRecipe(recipeId)
        assertThat(recipeDto)
            .extracting(
                RecipeDto::id,
                RecipeDto::name
            ).containsExactly(
                recipeId,
                "Honig-Senf-Hähnchen mit Kürbispüree"
            )

        assertThat(recipeDto.ingredients).hasSize(14)
        assertThat(recipeDto.yields).hasSize(3)
    }

}

