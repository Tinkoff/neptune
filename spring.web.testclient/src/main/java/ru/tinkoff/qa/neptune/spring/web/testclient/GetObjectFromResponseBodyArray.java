package ru.tinkoff.qa.neptune.spring.web.testclient;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Gets some object from array which is taken from / calculated by body of a response.
 *
 * @param <T> is a type of object to get
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria")
public final class GetObjectFromResponseBodyArray<T> extends SequentialGetStepSupplier
        .GetObjectFromArrayStepSupplier<WebTestClientContext, T, GetObjectFromResponseBodyArray<T>> {

    private GetObjectFromResponseBodyArray(Function<WebTestClientContext, T[]> f) {
        super(f);
    }

    @Description("{description}")
    static <T, R> GetObjectFromResponseBodyArray<T> objectFromArray(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            FromBodyGet<T[], R> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromResponseBodyArray<>(f);
    }
}
