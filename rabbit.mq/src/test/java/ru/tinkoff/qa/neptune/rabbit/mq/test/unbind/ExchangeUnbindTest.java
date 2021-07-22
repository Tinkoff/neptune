package ru.tinkoff.qa.neptune.rabbit.mq.test.unbind;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.impl.AMQImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;

import java.io.IOException;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments.arguments;

public class ExchangeUnbindTest extends BaseRabbitMqTest {
    AMQP.Exchange.UnbindOk unbindOk1;
    AMQP.Exchange.UnbindOk unbindOk2;
    AMQP.Exchange.UnbindOk unbindOk3;

    @BeforeClass(dependsOnMethods = "setUp")
    public void configureMock() throws IOException {
        when(channel.exchangeUnbind("destination", "source", "routingKey"))
                .thenReturn(unbindOk1 = new AMQImpl.Exchange.UnbindOk());

        when(channel.exchangeUnbind("destination", "source", "routingKey", arguments().getHashMap()))
                .thenReturn(unbindOk2 = new AMQImpl.Exchange.UnbindOk());

        when(channel.exchangeUnbind("destination", "source", "routingKey", arguments().setArgument("key", "value").getHashMap()))
                .thenReturn(unbindOk3 = new AMQImpl.Exchange.UnbindOk());
    }

    @Test(description = "Check unbind exchange from exchange")
    public void unbindTest1() {
        assertEquals(unbindOk1, rabbitMqStepContext.exchangeUnbind("destination", "source", "routingKey"));
    }

    @Test(description = "Check unbind exchange from exchange with empty arguments")
    public void unbindTest2() {
        assertEquals(unbindOk2, rabbitMqStepContext.exchangeUnbind("destination", "source", "routingKey", arguments()));
    }

    @Test(description = "Check unbind exchange from exchange with arguments")
    public void unbindTest3() {
        assertEquals(unbindOk3, rabbitMqStepContext.exchangeUnbind("destination", "source", "routingKey", arguments().setArgument("key", "value")));
    }
}
