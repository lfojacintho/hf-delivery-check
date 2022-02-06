package me.lfojacintho.hellofresh.deliverycheck.domain

data class Quantity(
    val amount: Double,
    val unit: String = "(?)"
) {

    fun isSameUnit(other: Quantity): Boolean {
        return unit == other.unit
    }

    fun plus(other: Quantity): Quantity {
        if (!isSameUnit(other)) {
            throw IllegalArgumentException("Cannot add quantities with different units")
        }

        return Quantity(amount + other.amount, unit)
    }
}
