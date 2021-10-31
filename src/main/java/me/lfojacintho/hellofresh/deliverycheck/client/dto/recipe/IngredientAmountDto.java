package me.lfojacintho.hellofresh.deliverycheck.client.dto.recipe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientAmountDto {

    private String id;

    private double amount;

    private String unit;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(final String unit) {
        this.unit = unit;
    }
}
