package me.lfojacintho.hellofresh.deliverycheck.client

import me.lfojacintho.hellofresh.deliverycheck.client.dto.menu.MenuDto
import me.lfojacintho.hellofresh.deliverycheck.client.dto.recipe.RecipeDto
import me.lfojacintho.hellofresh.deliverycheck.config.HelloFreshProductConfiguration
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient

private const val MENU_ENDPOINT = "gw/my-deliveries/menu"
private const val RECIPE_ENDPOINT = "gw/recipes/recipes/{recipeId}"

@Component
class HelloFreshClient(
    private val productConfiguration: HelloFreshProductConfiguration,
    private val webClient: WebClient
) {

    fun fetchMenu(week: String) = webClient.get()
        .uri { it.path(MENU_ENDPOINT).queryParams(menuParameters(week)).build() }
        .retrieve()
        .bodyToMono(MenuDto::class.java)
        .block()!!

    fun fetchRecipe(recipeId: String) = webClient.get()
        .uri { it.path(RECIPE_ENDPOINT).queryParams(recipeParameters()).build(recipeId) }
        .retrieve()
        .bodyToMono(RecipeDto::class.java)
        .block()!!

    private fun menuParameters(week: String): MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
        .addIfPresent("delivery-option", productConfiguration.deliveryOption)
        .addIfPresent("locale", productConfiguration.locale)
        .addIfPresent("postcode", productConfiguration.postcode)
        .addIfPresent("preference", productConfiguration.preference)
        .addIfPresent("product-sku", productConfiguration.sku)
        .addIfPresent("servings", productConfiguration.servings?.toString())
        .addIfPresent("subscription", productConfiguration.subscription)
        .addIfPresent("week", week)

    private fun recipeParameters(): MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
        .addIfPresent("locale", productConfiguration.locale)
        .addIfPresent("country", productConfiguration.country)

}

fun MultiValueMap<String, String>.addIfPresent(key: String, value: String?) : MultiValueMap<String, String> {
    if (value != null) {
        this.add(key, value)
    }

    return this
}
