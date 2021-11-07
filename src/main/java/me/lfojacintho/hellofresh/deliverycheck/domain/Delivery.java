package me.lfojacintho.hellofresh.deliverycheck.domain;

import java.util.ArrayList;
import java.util.List;

public record Delivery(List<Recipe> recipes) {

    public static final class DeliveryBuilder {

        private List<Recipe> recipes;

        private DeliveryBuilder() {}

        public static DeliveryBuilder builder() {
            return new DeliveryBuilder();
        }

        public DeliveryBuilder withRecipe(final Recipe recipe) {
            if (this.recipes == null) {
                this.recipes = new ArrayList<>();
            }

            recipes.add(recipe);
            return this;
        }

        public Delivery build() {return new Delivery(recipes);}
    }
}
