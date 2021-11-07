package me.lfojacintho.hellofresh.deliverycheck.domain;

public record Quantity (
    double amount,
    String unit
) {

    public static Quantity of(double amount, String unit) {
        return new Quantity(amount, unit);
    }

    public boolean isSameUnit(final Quantity other) {
        return this.unit.equals(other.unit);
    }

    public Quantity plus(final Quantity other) {
        if (!isSameUnit(other)) {
            throw new IllegalArgumentException("Cannot add quantities with different units");
        }

        return new Quantity(amount + other.amount, unit);
    }
}