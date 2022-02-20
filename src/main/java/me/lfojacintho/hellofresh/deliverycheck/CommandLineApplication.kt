package me.lfojacintho.hellofresh.deliverycheck

import me.lfojacintho.hellofresh.deliverycheck.domain.Ingredient
import me.lfojacintho.hellofresh.deliverycheck.domain.Quantity
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.temporal.ChronoField

@Profile("!test")
@Component
class CommandLineApplication(private val deliveryService: DeliveryService) : CommandLineRunner {

    override fun run(vararg args: String?) {
        val delivery = deliveryService.retrieveDelivery(currentWeek())
        val nonDeliveredIngredientMap = mutableMapOf<String, MutableList<Quantity>>()

        println("Your delivery contains the following recipes:")
        delivery.recipes.forEach { recipe ->
            println(" ${recipe.quantity}x (${recipe.index}) - ${recipe.title}")
            println()
            println("The following ingredients should be delivered")
            recipe.deliveredIngredients().forEach { ingredient ->
                if (ingredient.quantity.isKnown) {
                    println("  ${ingredient.name} - ${"%.0f".format(ingredient.quantity.amount)} ${ingredient.quantity.unit}")
                } else {
                    println("  ${ingredient.name} - Unknown quantity")
                }
            }

            recipe.nonDeliveredIngredients().forEach { ingredient ->
                if (ingredient.quantity.isKnown) {
                    aggregateNonDeliveredIngredients(nonDeliveredIngredientMap, ingredient)
                }
            }

            println()
            println()
        }

        println("You need to have the following ingredients at home:")
        nonDeliveredIngredientMap.forEach { (name, quantities) ->
            quantities.forEach { quantity ->
                println("  $name - ${"%.0f".format(quantity.amount)} ${quantity.unit}")
            }
        }
    }

    private fun currentWeek() : String {
        val today = LocalDate.now()
        return "%d-W%02d".format(today.year, today.get(ChronoField.ALIGNED_WEEK_OF_YEAR))
    }

    private fun aggregateNonDeliveredIngredients(
        nonDeliveredIngredientMap: MutableMap<String, MutableList<Quantity>>,
        ingredient: Ingredient
    ) {
        if (!nonDeliveredIngredientMap.containsKey(ingredient.name)) {
            val quantities = mutableListOf<Quantity>()
            quantities.add(ingredient.quantity)
            nonDeliveredIngredientMap[ingredient.name] = quantities
        } else {
            val quantities = nonDeliveredIngredientMap[ingredient.name]!!
            val existingQuantity = findAndRemoveQuantityWithSameUnit(quantities, ingredient.quantity)
            quantities.add(existingQuantity?.plus(ingredient.quantity) ?: ingredient.quantity)
        }

    }

    private fun findAndRemoveQuantityWithSameUnit(quantities: MutableList<Quantity>, quantity: Quantity) : Quantity? {
        for (item in quantities) {
            if (item.isSameUnit(quantity)) {
                quantities.remove(item)
                return item
            }
        }

        return null
    }
}
