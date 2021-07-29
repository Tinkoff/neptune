package ru.tinkoff.qa.neptune.rabbit.mq.test.declare;

import com.rabbitmq.client.impl.AMQImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;

import java.io.IOException;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.exchange.ParametersForDeclareExchange.exchangeParams;

public class ExchangeDeclareTest extends BaseRabbitMqTest {
    AMQImpl.Exchange.DeclareOk declareOk1;
    AMQImpl.Exchange.DeclareOk declareOk2;
    AMQImpl.Exchange.DeclareOk declareOk3;
    AMQImpl.Exchange.DeclareOk declareOk4;

    @BeforeClass(dependsOnMethods = "setUp")
    public void configureMock() throws IOException {
        when(channel.exchangeDeclarePassive("passive"))
                .thenReturn(declareOk1 = new AMQImpl.Exchange.DeclareOk());
        when(channel.exchangeDeclare("exchange", "type", false, false, false, null))
                .thenReturn(declareOk2 = new AMQImpl.Exchange.DeclareOk());
        when(channel.exchangeDeclare("exchange", "type", true, true, false, null))
                .thenReturn(declareOk3 = new AMQImpl.Exchange.DeclareOk());
        when(channel.exchangeDeclare("exchange", "type", false, false, false, Map.of("name", "value")))
                .thenReturn(declareOk4 = new AMQImpl.Exchange.DeclareOk());
    }

    @Test(description = "Check passive declare exchange")
    public void declareTest1() {
        assertEquals(declareOk1, rabbitMqStepContext.exchangeDeclare("passive"));
    }

    @Test(description = "Check declare exchange")
    public void declareTest2() {
        assertEquals(declareOk2, rabbitMqStepContext.exchangeDeclare("exchange", "type"));
    }

    @Test(description = "Check declare exchange with params")
    public void declareTest3() {
        assertEquals(declareOk2, rabbitMqStepContext.exchangeDeclare("exchange", "type", exchangeParams()));
    }

    @Test(description = "Check declare exchange with params")
    public void declareTest4() {
        assertEquals(declareOk3, rabbitMqStepContext.exchangeDeclare("exchange", "type", exchangeParams().durable().autoDelete()));
    }

    @Test(description = "Check declare exchange with params and additional arguments")
    public void declareTest5() {
        assertEquals(declareOk4, rabbitMqStepContext.exchangeDeclare("exchange", "type", exchangeParams().argument("name", "value")));
    }
}
