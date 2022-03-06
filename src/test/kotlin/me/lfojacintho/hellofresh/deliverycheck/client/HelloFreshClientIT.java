package me.lfojacintho.hellofresh.deliverycheck.client;

import me.lfojacintho.hellofresh.deliverycheck.client.dto.menu.MenuDto;
import me.lfojacintho.hellofresh.deliverycheck.client.dto.recipe.RecipeDto;
import me.lfojacintho.hellofresh.deliverycheck.util.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class HelloFreshClientIT extends AbstractIntegrationTest {

    private final HelloFreshClient helloFreshClient;

    @Autowired
    public HelloFreshClientIT(final HelloFreshClient helloFreshClient) {
        this.helloFreshClient = helloFreshClient;
    }

    @Test
    void fetchMenuShouldCallTheEndpointWithCorrectParameters() {
        // stub for the endpoint with expected parameters are set at WireMockTestExtension
        // parameters are set at application.properties from test resources

        final String week = "2021-W45";
        final MenuDto menuDto = helloFreshClient.fetchMenu(week);
        assertThat(menuDto).isNotNull()
            .extracting(
                MenuDto::getId,
                MenuDto::getWeek
            )
            .containsExactly(
                "61508b64665c2655834786b3",
                week
            );

        assertThat(menuDto.getMeals())
            .isNotNull()
            .hasSize(7);

        assertThat(menuDto.selectedMeals())
            .isNotNull()
            .hasSize(3);
    }

    @Test
    void fetchRecipeShouldCallTheEndpointWithCorrectParameters() {
        // stub for the endpoint with expected parameters are set at WireMockTestExtension
        // parameters are set at application.properties from test resources

        final String recipeId = "61508a0dcc9f2a786e62b22f";
        final RecipeDto recipeDto = helloFreshClient.fetchRecipe(recipeId);
        assertThat(recipeDto).isNotNull()
            .extracting(
                RecipeDto::getId,
                RecipeDto::getName
            )
            .containsExactly(
                recipeId,
                "Honig-Senf-Hähnchen mit Kürbispüree"
            );

        assertThat(recipeDto.getIngredients())
            .isNotNull()
            .hasSize(14);

        assertThat(recipeDto.getYields())
            .isNotNull()
            .hasSize(3);
    }

}
