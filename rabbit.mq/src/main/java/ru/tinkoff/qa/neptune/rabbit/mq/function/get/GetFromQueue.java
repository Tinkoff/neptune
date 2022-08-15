package ru.tinkoff.qa.neptune.rabbit.mq.function.get;

import com.rabbitmq.client.GetResponse;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;
import ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("unchecked")
final class GetFromQueue implements Function<RabbitMqStepContext, List<GetResponse>>, StepParameterPojo {

    @StepParameter("queue")
    private final String queue;

    @StepParameter("autoAck")
    private boolean autoAck;

    private List<GetResponse> responses = new ArrayList<>();

    public GetFromQueue(String queue) {
        checkArgument(isNotBlank(queue), "Queue should be defined");
        this.queue = queue;
    }

    @Override
    public List<GetResponse> apply(RabbitMqStepContext context) {
        try {
            var input = context.getChannel();
            responses.add(input.basicGet(queue, autoAck));
            return responses;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    void setAutoAck() {
        this.autoAck = true;
    }

    @Override
    public <V> MergeProperty<V> andThen(Function<? super List<GetResponse>, ? extends V> after) {
        Objects.requireNonNull(after);
        return new MergeProperty<>(this, (Function<List<GetResponse>, V>) after);
    }

    static class MergeProperty<T> implements Function<RabbitMqStepContext, T>, StepParameterPojo {
        private final GetFromQueue before;
        private final Function<List<GetResponse>, T> after;

        public MergeProperty(GetFromQueue before, Function<List<GetResponse>, T> after) {
            this.before = before;
            this.after = after;
        }

        @Override
        public T apply(RabbitMqStepContext context) {
            return after.apply(before.apply(context));
        }

        @Override
        public Map<String, String> getParameters() {
            if (before != null && after instanceof StepParameterPojo) {
                var parameters = before.getParameters();
                parameters.putAll(((StepParameterPojo) after).getParameters());
                return parameters;
            }
            return StepParameterPojo.super.getParameters();
        }

        public GetFromQueue getBefore() {
            return before;
        }

        public Function<List<GetResponse>, T> getAfter() {
            return after;
        }
    }

    public List<String> getMessages() {
        return responses.stream().map(response -> new String(response.getBody())).collect(toList());
    }
}
