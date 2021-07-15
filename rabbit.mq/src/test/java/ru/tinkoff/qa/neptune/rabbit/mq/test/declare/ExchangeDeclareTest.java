package ru.tinkoff.qa.neptune.rabbit.mq.test.declare;

import com.rabbitmq.client.impl.AMQImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;

import java.io.IOException;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.exchange.ParametersForDeclareExchange.exchangeParams;

public class ExchangeDeclareTest extends BaseRabbitMqTest {
    AMQImpl.Exchange.DeclareOk declareOk1;
    AMQImpl.Exchange.DeclareOk declareOk2;
    AMQImpl.Exchange.DeclareOk declareOk3;

    @BeforeClass(dependsOnMethods = "setUp")
    public void configureMock() throws IOException {
        when(channel.exchangeDeclarePassive("passive"))
                .thenReturn(declareOk1 = new AMQImpl.Exchange.DeclareOk());
        when(channel.exchangeDeclare("exchange", "type", false, false, false, null))
                .thenReturn(declareOk2 = new AMQImpl.Exchange.DeclareOk());
        when(channel.exchangeDeclare("exchange", "type", true, true, false, null))
                .thenReturn(declareOk3 = new AMQImpl.Exchange.DeclareOk());
    }

    @Test
    public void declareTest1() {
        assertEquals(declareOk1, rabbitMqStepContext.exchangeDeclare("passive"));
    }

    @Test
    public void declareTest2() {
        assertEquals(declareOk2, rabbitMqStepContext.exchangeDeclare("exchange", "type"));
    }

    @Test
    public void declareTest3() {
        assertEquals(declareOk2, rabbitMqStepContext.exchangeDeclare("exchange", "type", exchangeParams()));
    }

    @Test
    public void declareTest4() {
        assertEquals(declareOk3, rabbitMqStepContext.exchangeDeclare("exchange", "type", exchangeParams().durable().autoDelete()));
    }
}
