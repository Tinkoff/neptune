package ru.tinkoff.qa.neptune.core.api.localization.steps;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

public class SpecificCriteria {

    @Description("Some criteria")
    public static <T> Criteria<T> someCriteria() {
        return Criteria.condition(t -> true);
    }

    @Description("Some criteria 2")
    public static <T> Criteria<T> someCriteria2() {
        return Criteria.condition(t -> true);
    }

    public static <T> Criteria<T> someCriteria3() {
        return Criteria.condition(t -> true);
    }
}
