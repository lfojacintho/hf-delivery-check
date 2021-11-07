package me.lfojacintho.hellofresh.deliverycheck.domain;

public record Ingredient(
    String name,
    Double quantity,
    String unit,
    boolean delivered
) {

    public boolean isQuantityAvailable() {
        return quantity != null && unit != null;
    }

    public static final class IngredientBuilder {

        private boolean delivered;
        private String name;
        private double quantity;
        private String unit;

        private IngredientBuilder() {}

        public static IngredientBuilder builder() {
            return new IngredientBuilder();
        }

        public IngredientBuilder withName(final String name) {
            this.name = name;
            return this;
        }

        public IngredientBuilder withQuantity(final double quantity) {
            this.quantity = quantity;
            return this;
        }

        public IngredientBuilder withUnit(final String unit) {
            this.unit = unit;
            return this;
        }

        public IngredientBuilder withDelivered(final boolean delivered) {
            this.delivered = delivered;
            return this;
        }

        public Ingredient build() {return new Ingredient(name, quantity, unit, delivered);}
    }
}
