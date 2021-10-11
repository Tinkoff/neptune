package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.springframework.test.web.reactive.server.*;

import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

/**
 * This consumer is used to describe the checking.
 *
 * @param <T> is a type of accepted value
 * @see StatusAssertions#value(Consumer)
 * @see HeaderAssertions#value(String, Consumer)
 * @see HeaderAssertions#values(String, Consumer)
 * @see CookieAssertions#value(String, Consumer)
 * @see JsonPathAssertions#value(Consumer)
 * @see JsonPathAssertions#value(Consumer, Class)
 * @see XpathAssertions#nodeCount(Consumer)
 * @see XpathAssertions#number(Consumer)
 * @see XpathAssertions#string(Consumer)
 */
public abstract class DescribedConsumer<T> implements Consumer<T> {

    private final String description;

    protected DescribedConsumer(String description) {
        checkArgument(isNotBlank(description), "Description should not be blank");
        this.description = description;
    }

    @Override
    public final String toString() {
        return translate(description);
    }
}
