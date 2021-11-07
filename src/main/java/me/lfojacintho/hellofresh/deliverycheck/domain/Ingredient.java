package me.lfojacintho.hellofresh.deliverycheck.domain;

public record Ingredient(
    String name,
    Quantity quantity,
    boolean delivered
) {

    public boolean isQuantityAvailable() {
        return quantity != null;
    }

    public static final class IngredientBuilder {

        private String name;
        private Quantity quantity;
        private boolean delivered;

        private IngredientBuilder() {}

        public static IngredientBuilder builder() {
            return new IngredientBuilder();
        }

        public IngredientBuilder withName(final String name) {
            this.name = name;
            return this;
        }

        public IngredientBuilder withQuantity(final Quantity quantity) {
            this.quantity = quantity;
            return this;
        }

        public IngredientBuilder withDelivered(final boolean delivered) {
            this.delivered = delivered;
            return this;
        }

        public Ingredient build() {return new Ingredient(name, quantity, delivered);}
    }
}
