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
 * Gets some object from {@link Iterable} which is taken from / calculated by body of a response.
 *
 * @param <T> is a type of object to get
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria")
public final class GetObjectFromResponseBodyIterable<T> extends SequentialGetStepSupplier
        .GetObjectFromIterableChainedStepSupplier<MockMvcContext, T, MockHttpServletResponse, GetObjectFromResponseBodyIterable<T>> {

    private <R, S extends Iterable<T>> GetObjectFromResponseBodyIterable(DataTransformer transformer,
                                                                         Class<R> deserializeTo,
                                                                         Function<R, S> howToGet) {
        super(deserializeBodyAndGet(transformer, deserializeTo, howToGet));
    }

    private <R, S extends Iterable<T>> GetObjectFromResponseBodyIterable(DataTransformer transformer,
                                                                         TypeReference<R> deserializeTo,
                                                                         Function<R, S> howToGet) {
        super(deserializeBodyAndGet(transformer, deserializeTo, howToGet));
    }

    @Description("{description}")
    static <T, R, S extends Iterable<T>> GetObjectFromResponseBodyIterable<T> objectFromIterable(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            DataTransformer transformer,
            Class<R> deserializeTo,
            Function<R, S> howToGet) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromResponseBodyIterable<>(transformer, deserializeTo, howToGet);
    }

    @Description("{description}")
    static <T, R, S extends Iterable<T>> GetObjectFromResponseBodyIterable<T> objectFromIterable(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            DataTransformer transformer,
            TypeReference<R> deserializeTo,
            Function<R, S> howToGet) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromResponseBodyIterable<>(transformer, deserializeTo, howToGet);
    }

    GetObjectFromResponseBodyIterable<T> from(GetMockMvcResponseResultSupplier from) {
        return super.from(from);
    }
}
