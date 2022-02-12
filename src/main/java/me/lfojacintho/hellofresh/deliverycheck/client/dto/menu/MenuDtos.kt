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
    @JsonProperty("meals") val meals: List<MealDto>
) {
    fun selectedMeals() = meals.filter { it.selection != null }.sortedBy { it.index }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class RecipeInfoDto(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SelectionDto(
    @JsonProperty("quantity") val quantity: Int
)
