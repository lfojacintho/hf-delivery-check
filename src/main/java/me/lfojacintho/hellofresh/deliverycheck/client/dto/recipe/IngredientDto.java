package me.lfojacintho.hellofresh.deliverycheck.client.dto.recipe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientDto {

    private String id;

    private String name;

    private boolean shipped;

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

    public boolean isShipped() {
        return shipped;
    }

    public void setShipped(final boolean shipped) {
        this.shipped = shipped;
    }
}
