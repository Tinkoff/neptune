package ru.tinkoff.qa.neptune.spring.mock.mvc;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.mock.web.MockHttpServletResponse;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.DeserializeBody.deserializeBodyAndGet;

/**
 * Gets some {@link Iterable} which is taken from / calculated by body of a response.
 *
 * @param <T> is a type of iterable to get
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Criteria of an item of resulted iterable")
public final class GetIterableFromResponse<R, T extends Iterable<R>> extends SequentialGetStepSupplier.GetListChainedStepSupplier<MockMvcContext, T, MockHttpServletResponse, R, GetIterableFromResponse<R, T>> {

    private <M> GetIterableFromResponse(DataTransformer transformer,
                                        Class<M> deserializeTo,
                                        Function<M, T> howToGet) {
        super(deserializeBodyAndGet(transformer, deserializeTo, howToGet));
    }

    private <M> GetIterableFromResponse(DataTransformer transformer,
                                        TypeReference<M> deserializeTo,
                                        Function<M, T> howToGet) {
        super(deserializeBodyAndGet(transformer, deserializeTo, howToGet));
    }

    @Description("{description}")
    static <T, R, S extends Iterable<T>> GetIterableFromResponse<T, S> iterable(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            DataTransformer transformer,
            Class<R> deserializeTo,
            Function<R, S> howToGet) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetIterableFromResponse<>(transformer, deserializeTo, howToGet);
    }

    @Description("{description}")
    static <T, R, S extends Iterable<T>> GetIterableFromResponse<T, S> iterable(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            DataTransformer transformer,
            TypeReference<R> deserializeTo,
            Function<R, S> howToGet) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetIterableFromResponse<>(transformer, deserializeTo, howToGet);
    }

    GetIterableFromResponse<R, T> from(GetMockMvcResponseResultSupplier from) {
        return super.from(from);
    }
}
