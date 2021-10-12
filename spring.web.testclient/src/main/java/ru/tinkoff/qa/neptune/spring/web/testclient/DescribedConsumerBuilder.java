package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.*;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.isNull;
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
public final class DescribedConsumerBuilder<T> implements Supplier<Consumer<T>> {

    private final String description;
    private Consumer<T> consumer;

    private DescribedConsumerBuilder(String description) {
        checkArgument(isNotBlank(description), "Description should not be blank");
        this.description = description;
    }

    public static DescribedConsumerBuilder<Object> describeConsumer(String description) {
        return new DescribedConsumerBuilder<>(description);
    }

    public <R> DescribedConsumerBuilder<R> of(Class<R> rClass) {
        return (DescribedConsumerBuilder<R>) this;
    }

    public <R> DescribedConsumerBuilder<R> of(ParameterizedTypeReference<R> typeReference) {
        return (DescribedConsumerBuilder<R>) this;
    }

    public DescribedConsumerBuilder<T> consume(Consumer<T> consumer) {
        checkNotNull(consumer);
        this.consumer = consumer;
        return this;
    }


    @Override
    public Consumer<T> get() {
        if (isNull(consumer)) {
            return new Consumer<T>() {
                @Override
                public void accept(T t) {

                }

                @Override
                public String toString() {
                    return translate(description);
                }
            };
        }

        return new Consumer<T>() {
            @Override
            public void accept(T t) {
                consumer.accept(t);
            }

            @Override
            public String toString() {
                return translate(description);
            }
        };
    }
}
