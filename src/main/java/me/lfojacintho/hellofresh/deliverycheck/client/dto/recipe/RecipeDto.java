package me.lfojacintho.hellofresh.deliverycheck.client.dto.recipe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeDto {

    private String id;

    private String name;

    private List<IngredientDto> ingredients;

    private List<YieldDto> yields;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<IngredientDto> getIngredients() {
        return ingredients;
    }

    public void setIngredients(final List<IngredientDto> ingredients) {
        this.ingredients = ingredients;
    }

    public List<YieldDto> getYields() {
        return yields;
    }

    public void setYields(final List<YieldDto> yields) {
        this.yields = yields;
    }
}
