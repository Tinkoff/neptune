package ru.tinkoff.qa.neptune.rabbit.mq.function.get;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rabbitmq.client.GetResponse;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SequentialGetStepSupplier.DefineGetImperativeParameterName
@SequentialGetStepSupplier.DefineCriteriaParameterName("Object criteria")
@MaxDepthOfReporting(0)
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class RabbitMqBasicGetItemFromResponseSupplier<R, M, I extends RabbitMqBasicGetItemFromResponseSupplier<R, M, I>>
        extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<RabbitMqStepContext, R, List<GetResponse>, I> {

    public static final String NO_DESC_ERROR_TEXT = "Description should be defined";

    private RabbitMqBasicGetItemFromResponseSupplier(Function<GetResponse, R> originalFunction) {
        super(l -> l.stream().map(originalFunction).collect(Collectors.toList()));
    }

    private RabbitMqBasicGetItemFromResponseSupplier(GetDeserializedData<M> f1, Function<M, R> f2) {
        super(f1.andThen(ms -> ms.stream().map(f2).collect(toList())));
    }

    @Override
    protected I from(SequentialGetStepSupplier<RabbitMqStepContext, ? extends List<GetResponse>, ?, ?, ?> from) {
        return super.from(from);
    }

    /**
     * Creates a step that gets some value from the iteration that is calculated from the message.
     *
     * @param description is description of value to get
     * @param f           describes how to get desired value
     * @param <R>         is a type of item of iterable
     * @return
     */
    @Description("{description}")
    static <R> RabbitMqBasicGetItemFromResponseSupplier<R, R, ?> itemFromRecords(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            Function<GetResponse, R> f) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new RabbitMqBasicGetItemFromResponseSupplier(f) {
        };
    }

    /**
     * Creates a step that gets some value from the iteration that is calculated from the message.
     *
     * @param description is description of value to get
     * @param cls         is a class of a value to deserialize a message
     * @param conversion  describes how to get desired value
     * @param <R>         is a type of item of iterable
     * @param <M>         is a type of deserialized message
     * @return
     */
    @Description("{description}")
    static <R, M> RabbitMqBasicGetDeserializedItemFromRecordSupplier<R, M> itemFromRecords(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            Class<M> cls,
            Function<M, R> conversion) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new RabbitMqBasicGetDeserializedItemFromRecordSupplier(new GetDeserializedData<>(cls), conversion);
    }

    /**
     * Creates a step that gets some value from the iteration that is calculated from the message.
     *
     * @param description is description of value to get
     * @param typeT       is a reference to type of value to deserialize message
     * @param conversion  describes how to get desired value
     * @param <R>         is a type of item of iterable
     * @param <M>         is a type of deserialized message
     * @return
     */
    @Description("{description}")
    static <R, M> RabbitMqBasicGetDeserializedItemFromRecordSupplier<R, M> itemFromRecords(
            @DescriptionFragment(value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class
            ) String description,
            TypeReference<M> typeT,
            Function<M, R> conversion) {
        checkArgument(isNotBlank(description), NO_DESC_ERROR_TEXT);
        return new RabbitMqBasicGetDeserializedItemFromRecordSupplier(new GetDeserializedData<>(typeT), conversion);
    }


    public static final class RabbitMqBasicGetDeserializedItemFromRecordSupplier<R, M>
            extends RabbitMqBasicGetItemFromResponseSupplier<R, M, RabbitMqBasicGetDeserializedItemFromRecordSupplier<R, M>> {

        final GetDeserializedData<M> getDeserializedData;

        private RabbitMqBasicGetDeserializedItemFromRecordSupplier(GetDeserializedData<M> getDeserializedData, Function<M, R> convert) {
            super(getDeserializedData, convert);
            this.getDeserializedData = getDeserializedData;
        }

        public RabbitMqBasicGetDeserializedItemFromRecordSupplier<R, M> withDataTransformer(DataTransformer transformer) {
            getDeserializedData.setTransformer(transformer);
            return this;
        }
    }
}
