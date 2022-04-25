package ru.tinkoff.qa.neptune.http.api.response.dictionary;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("Response body data has item '{description}': {criteria}")
public final class CalculatedResponseDateHasItem {

    @DescriptionFragment(value = "description")
    final String description;

    @DescriptionFragment(value = "criteria")
    final String criteria;


    private CalculatedResponseDateHasItem(String description, String criteria) {
        this.description = description;
        this.criteria = criteria;
    }

    public static <R> CalculatedResponseDateHasItem hasResultItem(String description, String criteria) {
        return new CalculatedResponseDateHasItem(description, criteria);
    }

    @Override
    public String toString() {
        return translate(this);
    }
}
