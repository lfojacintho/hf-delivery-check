package me.lfojacintho.hellofresh.deliverycheck.domain

class Ingredient(
    val name: String,
    val quantity: Quantity = UNKNOWN_QUANTITY,
    val delivered: Boolean
) {
}
