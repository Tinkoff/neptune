package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions;

import ru.tinkoff.qa.neptune.core.api.hamcrest.MatchObjectName;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Description("Header{header}")
public final class HarRecordHeader extends MatchObjectName {

    @DescriptionFragment("header")
    final String header;

    public HarRecordHeader(String header) {
        this.header = ofNullable(header).map(h -> "[" + h + "]").orElse(EMPTY);;
    }
}
