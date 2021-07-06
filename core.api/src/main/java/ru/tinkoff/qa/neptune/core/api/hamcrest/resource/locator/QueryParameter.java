package ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator;

import ru.tinkoff.qa.neptune.core.api.hamcrest.MatchObjectName;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Description("URI/URL query parameter{param}")
class QueryParameter extends MatchObjectName {

    @DescriptionFragment("param")
    final String param;

    QueryParameter(String param) {
        this.param = ofNullable(param).map(h -> "[" + h + "]").orElse(EMPTY);
    }
}
