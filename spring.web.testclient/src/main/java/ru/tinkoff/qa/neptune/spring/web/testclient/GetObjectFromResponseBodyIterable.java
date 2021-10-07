package ru.tinkoff.qa.neptune.spring.web.testclient;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Gets some object from {@link Iterable} which is taken from / calculated by body of a response.
 *
 * @param <T> is a type of object to get
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria")
public final class GetObjectFromResponseBodyIterable<T> extends SequentialGetStepSupplier
        .GetObjectFromIterableStepSupplier<WebTestClientContext, T, GetObjectFromResponseBodyIterable<T>> {

    private <S extends Iterable<T>> GetObjectFromResponseBodyIterable(Function<WebTestClientContext, S> f) {
        super(f);
    }

    @Description("{description}")
    static <T, R, S extends Iterable<T>> GetObjectFromResponseBodyIterable<T> objectFromIterable(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            FromBodyGet<S, R> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromResponseBodyIterable<>(f);
    }
}
