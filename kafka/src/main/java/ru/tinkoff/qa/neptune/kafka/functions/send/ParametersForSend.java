package ru.tinkoff.qa.neptune.kafka.functions.send;

import org.apache.kafka.common.header.Header;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;

public class ParametersForSend implements StepParameterPojo {
    @StepParameter("partition")
    private Integer partition;
    @StepParameter("timestamp")
    private Long timestamp;
    @StepParameter("key")
    private Object key;
    @StepParameter("headers")
    private Iterable<Header> headers;

    public static ParametersForSend parameters() {
        return new ParametersForSend();
    }

    Integer getPartition() {
        return partition;
    }

    public ParametersForSend partition(Integer partition) {
        this.partition = partition;
        return this;
    }

    Long getTimestamp() {
        return timestamp;
    }

    public ParametersForSend timestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    Object getKey() {
        return key;
    }

    public ParametersForSend key(Object key) {
        this.key = key;
        return this;
    }

    Iterable<Header> getHeaders() {
        return headers;
    }

    public ParametersForSend headers(Iterable<Header> headers) {
        this.headers = headers;
        return this;
    }
}
