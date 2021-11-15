package ru.tinkoff.qa.neptune.spring.web.testclient;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Gets some {@link Iterable} which is taken from / calculated by body of a response.
 *
 * @param <T> is a type of iterable to get
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted iterable")
public final class GetListFromResponse<R, T extends Iterable<R>> extends SequentialGetStepSupplier.GetListStepSupplier<WebTestClientContext, T, R, GetListFromResponse<R, T>> {

    private GetListFromResponse(Function<WebTestClientContext, T> f) {
        super(f);
    }

    @Description("{description}")
    static <R, T, S extends Iterable<T>> GetListFromResponse<T, S> list(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            FromBodyGet<S, R> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetListFromResponse<>(f);
    }
}
