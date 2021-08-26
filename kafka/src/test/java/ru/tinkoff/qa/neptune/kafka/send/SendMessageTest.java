package ru.tinkoff.qa.neptune.kafka.send;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.kafka.CustomMapper;
import ru.tinkoff.qa.neptune.kafka.DefaultCallBackSupplier;
import ru.tinkoff.qa.neptune.kafka.DraftDto;
import ru.tinkoff.qa.neptune.kafka.KafkaBaseTest;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.serializedMessage;
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.textMessage;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaCallbackProperty.KAFKA_CALLBACK;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicForSendProperty.DEFAULT_TOPIC_FOR_SEND;

public class SendMessageTest extends KafkaBaseTest {

    DraftDto draftDto = new DraftDto().setName("testName");
    private final Callback customCallBack = (metadata, exception) -> {
    };

    @AfterMethod
    public void clear() {
        DEFAULT_TOPIC_FOR_SEND.accept(null);
        KAFKA_CALLBACK.accept(null);
    }

    @Test(description = "check basic sending of a message with a topic and an object")
    public void checkBaseMessageSending() {
        kafka.send(serializedMessage(draftDto).topic("testTopic"));

        verify(kafka.getProducer(), times(1))
                .send(new ProducerRecord<>("testTopic",
                        null,
                        null,
                        null,
                        "{\"name\":\"testName\"}",
                        null));
    }

    @Test(description = "check string message")
    public void checkStringSending() {
        kafka.send(textMessage("I'm a String!").topic("testTopic"));

        verify(kafka.getProducer(), times(1))
                .send(new ProducerRecord<>("testTopic",
                        null,
                        null,
                        null,
                        "I'm a String!",
                        null));
    }

    @Test(description = "check message sending with no-default DataTransformer")
    public void checkMessageSendingWithNoDefaultDataTransformer() {
        kafka.send(serializedMessage(draftDto)
                .topic("testTopic")
                .dataTransformer(new CustomMapper()));

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
        kafka.send(serializedMessage(draftDto)
                .topic("testTopic")
                .callback(customCallBack));

        verify(kafka.getProducer(), times(1))
                .send(new ProducerRecord<>("testTopic",
                                null,
                                null,
                                null,
                                "{\"name\":\"testName\"}",
                                null),
                        customCallBack);
    }

    @Test(description = "check message sending with parameterForSend")
    public void checkMessageSendingWithParametersForSend() {
        kafka.send(serializedMessage(draftDto)
                .topic("testTopic")
                .partition(1)
                .timestamp(10L));

        verify(kafka.getProducer(), times(1))
                .send(new ProducerRecord<>("testTopic",
                        1,
                        10L,
                        null,
                        "{\"name\":\"testName\"}",
                        null));
    }


    @Test(description = "check message sending with callBack and parameters")
    public void checkMessageSendingWithParametersAndCallBack() {
        kafka.send(serializedMessage(draftDto)
                .topic("testTopic")
                .partition(1)
                .timestamp(10L)
                .callback(customCallBack));

        verify(kafka.getProducer(), times(1))
                .send(new ProducerRecord<>("testTopic",
                                1,
                                10L,
                                null,
                                "{\"name\":\"testName\"}",
                                null),
                        customCallBack);

    }

    @Test(description = "check message sending with parameters and custom DataTransformer")
    public void checkMessageSendingWithParametersAndDataTransformer() {
        kafka.send(serializedMessage(draftDto)
                .topic("testTopic")
                .partition(1)
                .timestamp(10L)
                .dataTransformer(new CustomMapper())
        );

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
        kafka.send(serializedMessage(draftDto)
                .topic("testTopic")
                .partition(1)
                .timestamp(10L)
                .callback(customCallBack)
                .dataTransformer(new CustomMapper()));

        verify(kafka.getProducer(), times(1))
                .send(new ProducerRecord<>("testTopic",
                                1,
                                10L,
                                null,
                                "customSerialize",
                                null),
                        customCallBack);
    }

    @Test(description = "check default topic")
    public void checkDefaultTopic() {
        DEFAULT_TOPIC_FOR_SEND.accept("default_Topic");

        kafka.send(serializedMessage(draftDto)
                .partition(1)
                .timestamp(10L)
                .dataTransformer(new CustomMapper()));

        verify(kafka.getProducer(), times(1))
                .send(new ProducerRecord<>("default_Topic",
                        1,
                        10L,
                        null,
                        "customSerialize",
                        null));
    }

    @Test(description = "check default topic and raw message")
    public void checkSendingRawString() {
        DEFAULT_TOPIC_FOR_SEND.accept("default_Topic");

        kafka.send(textMessage("I'm a String!")
                .partition(1)
                .timestamp(10L));

        verify(kafka.getProducer(), times(1))
                .send(new ProducerRecord<>("default_Topic",
                        1,
                        10L,
                        null,
                        "I'm a String!",
                        null));
    }

    @Test(description = "check basic sending of a message with a topic and an object")
    public void checkSendingWithHeaderAndKey() {
        kafka.send(serializedMessage(draftDto)
                .topic("testTopic")
                .key("Some key")
                .header("Header key", "Value1")
                .header(new RecordHeader("Header key2", "Value2".getBytes())));

        verify(kafka.getProducer(), times(1))
                .send(new ProducerRecord<>("testTopic",
                        null,
                        null,
                        "Some key",
                        "{\"name\":\"testName\"}",
                        List.of(new RecordHeader("Header key", "Value1".getBytes()), new RecordHeader("Header key2", "Value2".getBytes()))));
    }

    @Test(description = "check default topic and raw message")
    public void checkSendingWithDefaultCallBack() {
        KAFKA_CALLBACK.accept(DefaultCallBackSupplier.class);

        kafka.send(textMessage("I'm a String!")
                .topic("testTopic"));

        verify(kafka.getProducer(), times(1))
                .send(new ProducerRecord<>("testTopic",
                        null,
                        null,
                        null,
                        "I'm a String!",
                        null),
                        DefaultCallBackSupplier.CALLBACK);
    }
}
