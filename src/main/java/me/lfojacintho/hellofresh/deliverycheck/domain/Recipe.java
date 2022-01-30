package me.lfojacintho.hellofresh.deliverycheck.domain;

import java.util.ArrayList;
import java.util.List;

public record Recipe(
    int quantity,
    int index,
    String title,
    List<Ingredient> ingredients)
{
    public List<Ingredient> deliveredIngredients() {
        return getFilteredIngredients(true);
    }

    public List<Ingredient> nonDeliveredIngredients() {
        return getFilteredIngredients(false);
    }

    private List<Ingredient> getFilteredIngredients(final boolean delivered) {
        return ingredients.stream()
            .filter(ingredient -> ingredient.getDelivered() == delivered)
            .toList();
    }

    public static final class RecipeBuilder {

        private int quantity;
        private int index;
        private String title;
        private List<Ingredient> ingredients;

        private RecipeBuilder() {}

        public static RecipeBuilder builder() {
            return new RecipeBuilder();
        }

        public RecipeBuilder withQuantity(final int quantity) {
            this.quantity = quantity;
            return this;
        }

        public RecipeBuilder withIndex(final int index) {
            this.index = index;
            return this;
        }

        public RecipeBuilder withTitle(final String title) {
            this.title = title;
            return this;
        }

        public RecipeBuilder withIngredient(final Ingredient ingredient) {
            if (this.ingredients == null) {
                this.ingredients = new ArrayList<>();
            }

            this.ingredients.add(ingredient);
            return this;
        }

        public Recipe build() {
            return new Recipe(quantity, index, title, ingredients);
        }
    }
}
