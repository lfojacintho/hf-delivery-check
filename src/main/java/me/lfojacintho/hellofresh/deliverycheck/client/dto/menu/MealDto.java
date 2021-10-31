package me.lfojacintho.hellofresh.deliverycheck.client.dto.menu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MealDto {

    private int index;

    private SelectionDto selection;

    private RecipeInfoDto recipe;

    public int getIndex() {
        return index;
    }

    public void setIndex(final int index) {
        this.index = index;
    }

    public SelectionDto getSelection() {
        return selection;
    }

    public void setSelection(final SelectionDto selection) {
        this.selection = selection;
    }

    public RecipeInfoDto getRecipe() {
        return recipe;
    }

    public void setRecipe(final RecipeInfoDto recipe) {
        this.recipe = recipe;
    }
}
