package me.lfojacintho.hellofresh.deliverycheck.domain

data class Recipe(
    val quantity: Int,
    val index: Int,
    val title: String,
    val ingredients: List<Ingredient>
) {
    fun deliveredIngredients() = ingredients.filter { it.delivered }
    fun nonDeliveredIngredients() = ingredients.filter { !it.delivered }
}
