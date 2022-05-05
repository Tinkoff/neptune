package ru.tinkoff.qa.neptune.http.api.response.dictionary;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

public abstract class AdditionalCriteriaDescription {

    @DescriptionFragment(value = "description")
    final String description;

    @DescriptionFragment(value = "criteria")
    final String criteria;

    private AdditionalCriteriaDescription(String description, String criteria) {
        this.description = description;
        this.criteria = criteria;
    }

    public static AdditionalCriteriaDescription hasResultItem(String description, String criteria) {
        return new CalculatedResponseDateHasItem(description, criteria);
    }

    public static AdditionalCriteriaDescription hasResultItem(String description) {
        return hasResultItem(description, new HasAtLeastOneItem().toString());
    }

    public static AdditionalCriteriaDescription isPossibleToGetExpectedValue(String description, String criteria) {
        return new IsPossibleToCalculateByResponseData(description, criteria);
    }

    @Override
    public String toString() {
        return translate(this);
    }

    @Description("'{description}' calculated by response body data has/have item: {criteria}")
    static final class CalculatedResponseDateHasItem extends AdditionalCriteriaDescription {

        private CalculatedResponseDateHasItem(String description, String criteria) {
            super(description, criteria);
        }
    }

    @Description("'{description}' calculated by response body data: {criteria}")
    static final class IsPossibleToCalculateByResponseData extends AdditionalCriteriaDescription {

        private IsPossibleToCalculateByResponseData(String description, String criteria) {
            super(description, criteria);
        }
    }
}
