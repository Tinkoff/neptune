package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions;

import ru.tinkoff.qa.neptune.core.api.hamcrest.MatchObjectName;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Description("Request query parameter{param}")
public final class HarRecordQueryParam extends MatchObjectName {

    @DescriptionFragment("param")
    final String param;

    public HarRecordQueryParam(String param) {
        this.param = ofNullable(param).map(h -> "[" + h + "]").orElse(EMPTY);
    }
}
