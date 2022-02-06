package me.lfojacintho.hellofresh.deliverycheck.client.dto.recipe

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class IngredientAmountDto(
    @JsonProperty("id") val id: String,
    @JsonProperty("amount") val amount: Double,
    @JsonProperty("unit") val unit: String?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class IngredientDto(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("shipped") val shipped: Boolean
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class RecipeDto(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("ingredients") val ingredients: List<IngredientDto>,
    @JsonProperty("yields") val yields: List<YieldDto>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class YieldDto(
    @JsonProperty("yields") val yields: Int,
    @JsonProperty("ingredients") val ingredients: List<IngredientAmountDto>
)
