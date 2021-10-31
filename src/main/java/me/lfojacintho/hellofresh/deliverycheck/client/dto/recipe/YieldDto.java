package me.lfojacintho.hellofresh.deliverycheck.client.dto.recipe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YieldDto {

    private int yields;

    private List<IngredientAmountDto> ingredients;

    public int getYields() {
        return yields;
    }

    public void setYields(final int yields) {
        this.yields = yields;
    }

    public List<IngredientAmountDto> getIngredients() {
        return ingredients;
    }

    public void setIngredients(final List<IngredientAmountDto> ingredients) {
        this.ingredients = ingredients;
    }
}
