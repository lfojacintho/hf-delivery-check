package me.lfojacintho.hellofresh.deliverycheck.domain;

public class QuantityInfo implements Comparable<QuantityInfo> {

    private final int recipeIndex;
    private final int amount;
    private final String unit;

    public QuantityInfo(final int recipeIndex, final int amount, final String unit) {
        this.recipeIndex = recipeIndex;
        this.amount = amount;
        this.unit = unit;
    }

    public int getRecipeIndex() {
        return recipeIndex;
    }

    public int getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return String.format("%d (%d %s)", recipeIndex, amount, unit);
    }

    @Override
    public int compareTo(final QuantityInfo o) {
        if (recipeIndex < o.getRecipeIndex()) {
            return -1;
        } else if (recipeIndex > o.getRecipeIndex()) {
            return 1;
        }

        return 0;
    }
}
