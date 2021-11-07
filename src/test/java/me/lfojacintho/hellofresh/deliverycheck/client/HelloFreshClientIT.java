package me.lfojacintho.hellofresh.deliverycheck.client;

import me.lfojacintho.hellofresh.deliverycheck.client.dto.menu.MenuDto;
import me.lfojacintho.hellofresh.deliverycheck.client.dto.recipe.RecipeDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static me.lfojacintho.hellofresh.deliverycheck.client.WireMockTestExtension.getWireMockServer;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(WireMockTestExtension.class)
class HelloFreshClientIT {

    private final HelloFreshClient helloFreshClient;

    @Autowired
    public HelloFreshClientIT(final HelloFreshClient helloFreshClient) {
        this.helloFreshClient = helloFreshClient;
    }

    @DynamicPropertySource
    static void applicationPropertiesOverride(final DynamicPropertyRegistry registry) {
        registry.add("wiremock-port", getWireMockServer()::port);
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

        assertThat(menuDto.getSelectedMeals())
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
