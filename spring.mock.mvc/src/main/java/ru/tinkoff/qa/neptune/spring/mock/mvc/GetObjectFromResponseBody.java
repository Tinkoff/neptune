package ru.tinkoff.qa.neptune.spring.mock.mvc;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.mock.web.MockHttpServletResponse;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.io.UnsupportedEncodingException;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.DeserializeBody.deserializeBodyAndGet;

/**
 * Gets some object from body of a response.
 *
 * @param <T> is a type of object to get
 */
@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria")
public final class GetObjectFromResponseBody<T> extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<MockMvcContext, T, MockHttpServletResponse, GetObjectFromResponseBody<T>> {

    private GetObjectFromResponseBody(Function<MockHttpServletResponse, T> f) {
        super(f);
    }

    private <R> GetObjectFromResponseBody(DataTransformer transformer,
                                          Class<R> deserializeTo,
                                          Function<R, T> howToGet) {
        this(deserializeBodyAndGet(transformer, deserializeTo, howToGet));
    }

    private <R> GetObjectFromResponseBody(DataTransformer transformer,
                                          TypeReference<R> deserializeTo,
                                          Function<R, T> howToGet) {
        this(deserializeBodyAndGet(transformer, deserializeTo, howToGet));
    }

    @Description("String content of response body")
    static GetObjectFromResponseBody<String> contentAsString() {
        return new GetObjectFromResponseBody<>(mockHttpServletResponse -> {
            try {
                return mockHttpServletResponse.getContentAsString();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Description("Response body")
    static <T> GetObjectFromResponseBody<T> responseBody(DataTransformer transformer,
                                                         Class<T> deserializeTo) {
        return new GetObjectFromResponseBody<>(transformer, deserializeTo, t -> t);
    }

    @Description("Response body")
    static <T> GetObjectFromResponseBody<T> responseBody(DataTransformer transformer,
                                                         TypeReference<T> deserializeTo) {
        return new GetObjectFromResponseBody<>(transformer, deserializeTo, t -> t);
    }

    @Description("{description}")
    static <T, R> GetObjectFromResponseBody<T> objectFromBody(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            DataTransformer transformer,
            Class<R> deserializeTo,
            Function<R, T> howToGet) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromResponseBody<>(transformer, deserializeTo, howToGet);
    }

    @Description("{description}")
    static <T, R> GetObjectFromResponseBody<T> objectFromBody(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            DataTransformer transformer,
            TypeReference<R> deserializeTo,
            Function<R, T> howToGet) {
        checkArgument(isNotBlank(description), "description of resulted value is not defined");
        return new GetObjectFromResponseBody<>(transformer, deserializeTo, howToGet);
    }

    GetObjectFromResponseBody<T> from(GetMockMvcResponseResultSupplier from) {
        return super.from(from);
    }

}
