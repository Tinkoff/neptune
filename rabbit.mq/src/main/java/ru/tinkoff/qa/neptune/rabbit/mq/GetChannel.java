package ru.tinkoff.qa.neptune.rabbit.mq;

import com.rabbitmq.client.Channel;

import java.util.function.Function;

public final class GetChannel implements Function<RabbitMqStepContext, Channel> {

    private GetChannel() {
        super();
    }

    public static GetChannel getChannel() {
        return new GetChannel();
    }

    @Override
    public Channel apply(RabbitMqStepContext rabbitMqStepContext) {
        return rabbitMqStepContext.getChannel();
    }
}
