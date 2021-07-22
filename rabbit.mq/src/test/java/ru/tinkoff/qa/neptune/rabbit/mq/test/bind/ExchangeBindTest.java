package ru.tinkoff.qa.neptune.rabbit.mq.test.bind;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.impl.AMQImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;

import java.io.IOException;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments.arguments;

public class ExchangeBindTest extends BaseRabbitMqTest {
    AMQP.Exchange.BindOk bindOk1;
    AMQP.Exchange.BindOk bindOk2;
    AMQP.Exchange.BindOk bindOk3;

    @BeforeClass(dependsOnMethods = "setUp")
    public void configureMock() throws IOException {
        when(channel.exchangeBind("destination", "source", "routingKey"))
                .thenReturn(bindOk1 = new AMQImpl.Exchange.BindOk());

        when(channel.exchangeBind("destination", "source", "routingKey", arguments().getHashMap()))
                .thenReturn(bindOk2 = new AMQImpl.Exchange.BindOk());

        when(channel.exchangeBind("destination", "source", "routingKey",
                arguments().setArgument("key", "value").getHashMap()))
                .thenReturn(bindOk3 = new AMQImpl.Exchange.BindOk());
    }

    @Test(description = "Check bind exchange to exchange without additional arguments")
    public void bindTest1() {
        assertEquals(bindOk1, rabbitMqStepContext.exchangeBind("destination", "source", "routingKey"));
    }

    @Test(description = "Check bind exchange to exchange with empty additional arguments")
    public void bindTest2() {
        assertEquals(bindOk2, rabbitMqStepContext.exchangeBind("destination", "source", "routingKey", arguments()));
    }

    @Test(description = "Check bind exchange to exchange with additional arguments")
    public void bindTest3() {
        assertEquals(bindOk3, rabbitMqStepContext.exchangeBind("destination", "source", "routingKey", arguments().setArgument("key", "value")));
    }
}
