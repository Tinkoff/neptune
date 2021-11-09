package ru.tinkoff.qa.neptune.spring.web.testclient;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Gets array which is taken from / calculated by body of a response.
 *
 * @param <T> is a type of item of resulted array
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted array")
public final class GetArrayFromResponse<T> extends SequentialGetStepSupplier
        .GetArrayStepSupplier<WebTestClientContext, T, GetArrayFromResponse<T>> {

    private GetArrayFromResponse(Function<WebTestClientContext, T[]> f) {
        super(f);
    }

    @Description("{description}")
    static <T, R> GetArrayFromResponse<T> array(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            FromBodyGet<T[], R> f) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetArrayFromResponse<>(f);
    }
}
