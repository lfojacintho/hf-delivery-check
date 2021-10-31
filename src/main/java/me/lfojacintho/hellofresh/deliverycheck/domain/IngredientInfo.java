package me.lfojacintho.hellofresh.deliverycheck.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IngredientInfo {

    private final String apiId;
    private final String name;
    private final List<QuantityInfo> quantityInfos;

    public IngredientInfo(final String apiId, final String name) {
        this.apiId = apiId;
        this.name = name;
        quantityInfos = new ArrayList<>();
    }

    public void addQuantityInfo(final QuantityInfo quantityInfo) {
        quantityInfos.add(quantityInfo);
    }

    @Override
    public String toString() {
        final String quantityByRecipe = quantityInfos.stream()
            .sorted()
            .map(QuantityInfo::toString)
            .collect(Collectors.joining(", "));

        return String.format(
            "%s (contained in %s)%n",
            name,
            quantityByRecipe
        );
    }
}
