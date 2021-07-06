package ru.tinkoff.qa.neptune.http.api.hamcrest.response;

import ru.tinkoff.qa.neptune.core.api.hamcrest.MatchObjectName;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Description("http response header{header}")
final class Header extends MatchObjectName {

    @DescriptionFragment("header")
    final String header;

    Header(String header) {
        this.header = ofNullable(header).map(h -> "[" + h + "]").orElse(EMPTY);
    }
}
