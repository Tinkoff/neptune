package ru.tinkoff.qa.neptune.spring.web.testclient;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Gets some object from body of a response.
 *
 * @param <T> is a type of object to get
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria")
public final class GetObjectFromResponseBody<T> extends SequentialGetStepSupplier
        .GetObjectStepSupplier<WebTestClientContext, T, GetObjectFromResponseBody<T>> {

    private GetObjectFromResponseBody(Function<WebTestClientContext, T> f) {
        super(f);
    }

    @Description("Response body")
    static <T> GetObjectFromResponseBody<T> responseBody(FromBodyGet<T, T> f) {
        return new GetObjectFromResponseBody<>(f);
    }

    @Description("{description}")
    static <T, R> GetObjectFromResponseBody<T> objectFromBody(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            FromBodyGet<T, R> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromResponseBody<>(f);
    }
}
