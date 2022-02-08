package me.lfojacintho.hellofresh.deliverycheck.domain

data class Recipe(
    val quantity: Int,
    val index: Int,
    val title: String,
    val ingredients: List<Ingredient>
) {

    fun deliveredIngredients(): List<Ingredient> {
        return getFilteredIngredients(true)
    }

    fun nonDeliveredIngredients(): List<Ingredient> {
        return getFilteredIngredients(false)
    }

    private fun getFilteredIngredients(delivered: Boolean) : List<Ingredient> {
        return ingredients.filter { it.delivered == delivered }
    }

}
