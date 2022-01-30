package me.lfojacintho.hellofresh.deliverycheck.domain

class Ingredient private constructor(
    val name: String,
    val quantity: Quantity?,
    val delivered: Boolean
) {
    fun isQuantityAvailable() : Boolean {
        return quantity != null
    }

    data class Builder(
        var name: String? = null,
        var quantity: Quantity? = null,
        var delivered: Boolean = false
    ) {
        fun withName(name: String) = apply { this.name = name }
        fun withQuantity(quantity: Quantity) = apply { this.quantity = quantity }
        fun withDelivered(delivered: Boolean) = apply { this.delivered = delivered }
        fun build() = Ingredient(name!!, quantity, delivered)
    }
}
