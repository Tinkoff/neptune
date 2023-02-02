package ru.tinkoff.qa.neptune.rabbit.mq.function.get;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rabbitmq.client.GetResponse;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetItemFromResponseSupplier.itemFromRecords;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetListFromResponseSupplier.listFromRecords;
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_QUEUE_NAME;

@SequentialGetStepSupplier.DefineGetImperativeParameterName("Get from RabbitMQ:")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting")
@SequentialGetStepSupplier.DefineCriteriaParameterName("GetResponse criteria")
@Description("Rabbit responses")
public class GetResponseSupplier extends SequentialGetStepSupplier
        .GetListStepSupplier<RabbitMqStepContext, List<GetResponse>, GetResponse, GetResponseSupplier> {

    final GetFromQueue function;

    protected GetResponseSupplier(GetFromQueue originalFunction) {
        super(originalFunction);
        this.function = originalFunction;

    }

    public static GetResponseSupplier responses(String queue) {
        return new GetResponseSupplier(new GetFromQueue(queue));
    }

    public static GetResponseSupplier responses() {
        return responses(DEFAULT_QUEUE_NAME.get());
    }

    public <R> RabbitMqBasicGetListFromResponseSupplier<R, R, ?> thenGetList(String description, Function<GetResponse, R> getItemFunction) {
        return listFromRecords(description, getItemFunction).from(this);
    }

    public <R, M> RabbitMqBasicGetListFromResponseSupplier.RabbitMqBasicGetDeserializedFromSupplier<R, M> thenGetList(String description, Class<M> cls, Function<M, R> conversion) {
        return listFromRecords(description, cls, conversion).from(this);
    }

    public <R, M> RabbitMqBasicGetListFromResponseSupplier.RabbitMqBasicGetDeserializedFromSupplier<R, M> thenGetList(String description, TypeReference<M> typeT, Function<M, R> conversion) {
        return listFromRecords(description, typeT, conversion).from(this);
    }

    public <R> RabbitMqBasicGetListFromResponseSupplier.RabbitMqBasicGetDeserializedFromSupplier<R, ?> thenGetList(String description, Class<R> cls) {
        return thenGetList(description, cls, t -> t);
    }

    public <R> RabbitMqBasicGetListFromResponseSupplier.RabbitMqBasicGetDeserializedFromSupplier<R, ?> thenGetList(String description, TypeReference<R> typeT) {
        return thenGetList(description, typeT, t -> t);
    }

    public <R> RabbitMqBasicGetItemFromResponseSupplier<R, R, ?> thenGetItem(String description, Function<GetResponse, R> getItemFunction) {
        return itemFromRecords(description, getItemFunction).from(this);
    }

    public <R, M> RabbitMqBasicGetItemFromResponseSupplier.RabbitMqBasicGetDeserializedItemFromRecordSupplier<R, M> thenGetItem(String description, Class<M> cls, Function<M, R> conversion) {
        return itemFromRecords(description, cls, conversion).from(this);
    }

    public <R, M> RabbitMqBasicGetItemFromResponseSupplier.RabbitMqBasicGetDeserializedItemFromRecordSupplier<R, M> thenGetItem(String description, TypeReference<M> typeT, Function<M, R> conversion) {
        return itemFromRecords(description, typeT, conversion).from(this);
    }

    public <R> RabbitMqBasicGetItemFromResponseSupplier.RabbitMqBasicGetDeserializedItemFromRecordSupplier<R, ?> thenGetItem(String description, Class<R> cls) {
        return thenGetItem(description, cls, t -> t);
    }

    public <R> RabbitMqBasicGetItemFromResponseSupplier.RabbitMqBasicGetDeserializedItemFromRecordSupplier<R, ?> thenGetItem(String description, TypeReference<R> typeT) {
        return thenGetItem(description, typeT, t -> t);
    }

    public GetResponseSupplier autoAck() {
        this.function.setAutoAck();
        return this;
    }

    @Override
    public GetResponseSupplier timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }
}
