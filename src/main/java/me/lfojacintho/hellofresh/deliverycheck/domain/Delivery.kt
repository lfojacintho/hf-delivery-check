package me.lfojacintho.hellofresh.deliverycheck.domain

class Delivery private constructor(
    val recipes: List<Recipe>
) {
    data class Builder(
        val recipes: MutableList<Recipe> = mutableListOf()
    ) {
        fun withRecipe(recipe: Recipe) = apply { recipes.add(recipe) }

        fun build() = Delivery(recipes)
    }
}
