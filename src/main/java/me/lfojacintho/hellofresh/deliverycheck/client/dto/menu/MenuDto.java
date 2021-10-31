package me.lfojacintho.hellofresh.deliverycheck.client.dto.menu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuDto {

    private String id;

    private String week;

    private List<MealDto> meals;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(final String week) {
        this.week = week;
    }

    public List<MealDto> getMeals() {
        return meals;
    }

    public void setMeals(final List<MealDto> meals) {
        this.meals = meals;
    }

    public List<MealDto> getSelectedMeals() {
        if (meals == null) {
            return Collections.emptyList();
        }

        return meals.stream()
            .filter(meal -> Objects.nonNull(meal.getSelection()))
            .sorted(Comparator.comparingInt(MealDto::getIndex))
            .toList();
    }
}
