package me.lfojacintho.hellofresh.deliverycheck.client;

import me.lfojacintho.hellofresh.deliverycheck.client.dto.menu.MenuDto;
import me.lfojacintho.hellofresh.deliverycheck.client.dto.recipe.RecipeDto;
import me.lfojacintho.hellofresh.deliverycheck.config.HelloFreshProductConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class HelloFreshClient {

    private static final String MENU_ENDPOINT = "gw/my-deliveries/menu";
    private static final String RECIPE_ENDPOINT = "gw/recipes/recipes/{recipeId}";

    private final HelloFreshProductConfiguration productConfiguration;
    private final WebClient webClient;

    @Autowired
    public HelloFreshClient(
        final HelloFreshProductConfiguration productConfiguration,
        final WebClient webClient
    ) {
        this.productConfiguration = productConfiguration;
        this.webClient = webClient;
    }

    public MenuDto fetchMenu(final String week) {
        final WebClient.RequestHeadersUriSpec<?> menuSpec = webClient.get();
        menuSpec.uri(uriBuilder -> uriBuilder.path(MENU_ENDPOINT).queryParams(getMenuParameters(week)).build());
        return menuSpec.retrieve().bodyToMono(MenuDto.class).block();
    }

    public RecipeDto fetchRecipe(final String recipeId) {
        final WebClient.RequestHeadersUriSpec<?> recipeSpec = webClient.get();
        recipeSpec.uri(uriBuilder -> uriBuilder.path(RECIPE_ENDPOINT)
            .queryParams(getRecipeParameters())
            .build(recipeId));
        return recipeSpec.retrieve().bodyToMono(RecipeDto.class).block();
    }

    private MultiValueMap<String, String> getMenuParameters(final String week) {
        final MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        addParameterIfPresent(parameters, "delivery-option", productConfiguration.getDeliveryOption());
        addParameterIfPresent(parameters, "locale", productConfiguration.getLocale());
        addParameterIfPresent(parameters, "postcode", productConfiguration.getPostcode());
        addParameterIfPresent(parameters, "preference", productConfiguration.getPreference());
        addParameterIfPresent(parameters, "product-sku", productConfiguration.getSku());
        addParameterIfPresent(parameters, "servings", String.valueOf(productConfiguration.getServings()));
        addParameterIfPresent(parameters, "subscription", productConfiguration.getSubscription());
        addParameterIfPresent(parameters, "week", week);

        return parameters;
    }

    private MultiValueMap<String, String> getRecipeParameters() {
        final LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        addParameterIfPresent(parameters, "country", productConfiguration.getCountry());
        addParameterIfPresent(parameters, "locale", productConfiguration.getLocale());
        return parameters;
    }

    private void addParameterIfPresent(
        final MultiValueMap<String, String> parameterMap,
        final String parameterKey,
        final String parameterValue
    ) {
        if (parameterValue != null) {
            parameterMap.add(parameterKey, parameterValue);
        }
    }
}
