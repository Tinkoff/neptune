package ru.tinkoff.qa.neptune.kafka.send;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.kafka.CustomMapper;
import ru.tinkoff.qa.neptune.kafka.DraftDto;
import ru.tinkoff.qa.neptune.kafka.KafkaBaseTest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.kafka.functions.send.ParametersForSend.parameters;

public class SendMessageTest extends KafkaBaseTest {

    @Test(description = "check basic sending of a message with a topic and an object")
    public void checkBaseMessageSending() {
        kafka.sendMessage("testTopic", new DraftDto().setName("testName"));

        verify(kafka.getProducer(), times(1))
                .send(new ProducerRecord<>("testTopic",
                        null,
                        null,
                        null,
                        "{\"name\":\"testName\"}",
                        null));
    }

    @Test(description = "check message sending with no-default DataTransformer")
    public void checkMessageSendingWithNoDefaultDataTransformer() {
        kafka.sendMessage("testTopic", new DraftDto().setName("testName"), new CustomMapper());

        verify(kafka.getProducer(), times(1))
                .send(new ProducerRecord<>("testTopic",
                        null,
                        null,
                        null,
                        "customSerialize",
                        null));
    }

    @Test(description = "check message sending with callBack")
    public void checkMessageSendingWithCallBack() {
        kafka.sendMessage("testTopic", new DraftDto().setName("testName"), callBack);

        verify(kafka.getProducer(), times(1))
                .send(new ProducerRecord<>("testTopic",
                                null,
                                null,
                                null,
                                "{\"name\":\"testName\"}",
                                null),
                        callBack);
    }

    @Test(description = "check message sending with parameterForSend")
    public void checkMessageSendingWithParametersForSend() {
        kafka.sendMessage("testTopic", new DraftDto().setName("testName"), parameters().partition(1).timestamp(10L));

        verify(kafka.getProducer(), times(1))
                .send(new ProducerRecord<>("testTopic",
                        1,
                        10L,
                        null,
                        "{\"name\":\"testName\"}",
                        null));
    }

    @Test(description = "check message sending with callBack and DataTransformer")
    public void checkMessageSendingWithCallBackAndDataTransformer() {
        kafka.sendMessage("testTopic", new DraftDto().setName("testName"), callBack, new CustomMapper());

        verify(kafka.getProducer(), times(1))
                .send(new ProducerRecord<>("testTopic",
                                null,
                                null,
                                null,
                                "customSerialize",
                                null),
                        callBack);
    }

    @Test(description = "check message sending with callBack and parameters")
    public void checkMessageSendingWithParametersAndCallBack() {
        kafka.sendMessage("testTopic", new DraftDto().setName("testName"), parameters().partition(1).timestamp(10L), callBack);

        verify(kafka.getProducer(), times(1))
                .send(new ProducerRecord<>("testTopic",
                                1,
                                10L,
                                null,
                                "{\"name\":\"testName\"}",
                                null),
                        callBack);

    }

    @Test(description = "check message sending with parameters and custom DataTransformer")
    public void checkMessageSendingWithParametersAndDataTransformer() {
        kafka.sendMessage("testTopic", new DraftDto().setName("testName"), parameters().partition(1).timestamp(10L), new CustomMapper());

        verify(kafka.getProducer(), times(1))
                .send(new ProducerRecord<>("testTopic",
                        1,
                        10L,
                        null,
                        "customSerialize",
                        null));
    }

    @Test(description = "check message sending with parameters, custom DataTransformer and CallBack")
    public void checkMessageSendingWithParametersAndDataTransformerAndCallback() {
        kafka.sendMessage("testTopic", new DraftDto().setName("testName"), parameters().partition(1).timestamp(10L), callBack, new CustomMapper());

        verify(kafka.getProducer(), times(1))
                .send(new ProducerRecord<>("testTopic",
                                1,
                                10L,
                                null,
                                "customSerialize",
                                null),
                        callBack);
    }
}
