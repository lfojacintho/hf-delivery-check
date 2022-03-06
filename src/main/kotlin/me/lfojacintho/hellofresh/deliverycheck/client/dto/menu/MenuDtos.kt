package me.lfojacintho.hellofresh.deliverycheck.client.dto.menu

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MealDto(
    @JsonProperty("index") val index: Int,
    @JsonProperty("selection") val selection: SelectionDto?,
    @JsonProperty("recipe") val recipe: RecipeInfoDto
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MenuDto(
    @JsonProperty("id") val id: String,
    @JsonProperty("week") val week: String,
    @JsonProperty("meals") val meals: List<MealDto>,
    @JsonProperty("addOns") val addOn: MenuAddOnDto
) {
    fun selectedMeals() = (meals + addOn.groups.map { it.addOns }.flatten())
            .filter { it.selection != null }
            .sortedBy { it.index }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class RecipeInfoDto(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SelectionDto(
    @JsonProperty("quantity") private val quantity: Int?,
    @JsonProperty("oneOffQuantity") private val oneOffQuantity: Int?
) {
    fun quantity() = quantity ?: oneOffQuantity ?: 0
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class MenuAddOnDto(
    @JsonProperty("groups") val groups: List<AddOnGroupDto>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class AddOnGroupDto(
    @JsonProperty("groupType") val type: String,
    @JsonProperty("sku") val sku: String,
    @JsonProperty("addOns") val addOns: List<MealDto>
)
