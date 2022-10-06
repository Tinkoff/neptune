package ru.tinkoff.qa.neptune.rabbit.mq.function.get;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rabbitmq.client.GetResponse;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class RabbitMqBasicGetListFromResponseSupplier<R, M, S extends RabbitMqBasicGetListFromResponseSupplier<R, M, S>>
        extends SequentialGetStepSupplier.GetListChainedStepSupplier<RabbitMqStepContext,
        List<R>,
        List<GetResponse>,
        R,
        S> {

    public static final String NO_DESC_ERROR_TEXT = "Description should be defined";

    private RabbitMqBasicGetListFromResponseSupplier(Function<GetResponse, R> originalFunction) {
        super(l -> l.stream().map(originalFunction).collect(toList()));
    }

    private RabbitMqBasicGetListFromResponseSupplier(Function<List<GetResponse>, List<M>> f1, Function<M, R> conversion) {
        super(f1.andThen(ms -> ms.stream().map(conversion).collect(Collectors.toList())));
    }

    @Override
    protected S from(SequentialGetStepSupplier<RabbitMqStepContext, ? extends List<GetResponse>, ?, ?, ?> from) {
        return super.from(from);
    }

    /**
     * Creates a step that returns an iteration of values calculated from read messages.
     *
     * @param description is description of value to get
     * @param f           describes how to get desired value
     * @param <R>         is a type of item of iterable
     * @return RabbitMqBasicGetListFromResponseSupplier
     */
    @Description("{description}")
    static <R> RabbitMqBasicGetListFromResponseSupplier<R, R, ?> listFromRecords(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            Function<GetResponse, R> f) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new RabbitMqBasicGetListFromResponseSupplier(f) {
        };
    }

    /**
     * Creates a step that returns iterable of values which are calculated by data of read messages.
     *
     * @param description is description of value to get
     * @param cls         is a class of a value to deserialize a message
     * @param conversion  describes how to get desired value
     * @param <R>         is a type of item of iterable
     * @param <M>         is a type of deserialized message
     * @return RabbitMqBasicGetDeserializedFromSupplier
     */
    @Description("{description}")
    static <R, M> RabbitMqBasicGetDeserializedFromSupplier<R, M> listFromRecords(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            Class<M> cls,
            Function<M, R> conversion) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new RabbitMqBasicGetDeserializedFromSupplier<>(new GetDeserializedData<>(cls), conversion);
    }

    /**
     * Creates a step that returns iterable of values which are calculated by data of read messages.
     *
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param conversion  describes how to get desired value
     * @param <R>         is a type of item of iterable
     * @param <M>         is a type of deserialized message
     * @return RabbitMqBasicGetDeserializedFromSupplier
     */
    @Description("{description}")
    static <R, M> RabbitMqBasicGetDeserializedFromSupplier<R, M> listFromRecords(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            TypeReference<M> typeT,
            Function<M, R> conversion) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new RabbitMqBasicGetDeserializedFromSupplier<>(new GetDeserializedData<>(typeT), conversion);
    }

    public static final class RabbitMqBasicGetDeserializedFromSupplier<R, M>
            extends RabbitMqBasicGetListFromResponseSupplier<R, M, RabbitMqBasicGetDeserializedFromSupplier<R, M>> {

        final GetDeserializedData<M> getDeserializedData;

        private RabbitMqBasicGetDeserializedFromSupplier(GetDeserializedData<M> getDeserializedData, Function<M, R> conversion) {
            super(getDeserializedData, conversion);
            this.getDeserializedData = getDeserializedData;
        }

        public RabbitMqBasicGetDeserializedFromSupplier<R, M> withDataTransformer(DataTransformer transformer) {
            getDeserializedData.setTransformer(transformer);
            return this;
        }
    }
}
