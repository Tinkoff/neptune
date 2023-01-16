# Критерии

Варианты использования критериев подробно описаны [здесь](../../../quick_start/steps/criteria/index.md)

Ниже описан рекомендуемый способ описания библиотеки критериев

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

public final class MyCriteriaLib {

    private MyCriteriaLib() {
        super();
    }
    
    @Description("Название/описания критерия. Параметры: {someParam1}, {someParam2}")
    public static <T> Criteria<T> someCriteria(
        @DescriptionFragment("someParam1") Object param1,
        @DescriptionFragment("someParam2") Object param2) {
        return condition(t -> /* предикат*/);
    }

    @Description("Название/описания критерия. Параметры: {someParam1}, {someParam2}")
    public static <T> Criteria<T> oneMoreCriteria(
        @DescriptionFragment("someParam1") Object param1,
        @DescriptionFragment("someParam2") Object param2) {
        return condition(t -> /* предикат*/);
    }
}

```

Документация про аннотации [@Description и @DescriptionFragment](../annotations/@Description_@DescriptionFragment.rst)

