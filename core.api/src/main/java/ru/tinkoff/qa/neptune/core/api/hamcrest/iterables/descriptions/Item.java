package ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions;

import ru.tinkoff.qa.neptune.core.api.hamcrest.MatchObjectName;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Description("item{indexStr}")
public final class Item extends MatchObjectName {

    @DescriptionFragment("indexStr")
    final String indexStr;

    public Item(Integer index) {
        this.indexStr = ofNullable(index).map(i -> " [" + i + "]").orElse(EMPTY);
    }

    public Item() {
        this(null);
    }
}
