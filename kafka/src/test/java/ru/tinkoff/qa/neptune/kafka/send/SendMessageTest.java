package ru.tinkoff.qa.neptune.kafka.send;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.Serializer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.kafka.DefaultCallBackSupplier;
import ru.tinkoff.qa.neptune.kafka.DraftDto;
import ru.tinkoff.qa.neptune.kafka.KafkaBasePreparations;
import ru.tinkoff.qa.neptune.kafka.SomeSerializer;

import java.util.List;

import static java.util.Objects.nonNull;
import static org.apache.kafka.clients.producer.ProducerConfig.MAX_REQUEST_SIZE_CONFIG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.kafka.DefaultCallBackSupplier.CALLBACK;
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.producerRecord;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaCallbackProperty.KAFKA_CALLBACK;
import static ru.tinkoff.qa.neptune.kafka.properties.KafkaDefaultTopicForSendProperty.DEFAULT_TOPIC_FOR_SEND;

public class SendMessageTest extends KafkaBasePreparations {

    DraftDto draftDto = new DraftDto().setName("testName");
    private final Callback customCallBack = (metadata, exception) -> {
    };

    @DataProvider
    Object[][] sendStringWithKyeData() {
        return new Object[][]{
            {draftDto, new SomeSerializer(), producerWithDeserializedKey},
            {"Some String", null, producerRaw}
        };
    }

    @DataProvider
    Object[][] sendObjectWithKyeData() {
        return new Object[][]{
            {draftDto, new SomeSerializer(), producerWithDeserializedKeyAndValue},
            {"Some String", null, producerWithDeserializedValue}
        };
    }

    @AfterMethod
    public void clear() {
        DEFAULT_TOPIC_FOR_SEND.accept(null);
        KAFKA_CALLBACK.accept(null);
    }

    @Test(description = "check basic sending of a message with a topic and an object")
    public void checkObjectMessageSending() {
        kafka.send(producerRecord(new SomeSerializer(), draftDto)
            .topic("testTopic"));

        verify(producerWithDeserializedValue, times(1))
            .send(new ProducerRecord<>("testTopic",
                null,
                null,
                null,
                draftDto,
                null));
    }

    @Test(description = "check string message")
    public void checkStringSending() {
        kafka.send(producerRecord("I'm a String!")
            .topic("testTopic"));

        verify(producerRaw, times(1))
            .send(new ProducerRecord<>("testTopic",
                null,
                null,
                null,
                "I'm a String!",
                null));
    }

    @Test(dataProvider = "sendStringWithKyeData")
    public <K> void checkStringSendingWithKey(K key, Serializer<K> serializer, KafkaProducer<K, String> mock) {
        var recordStep = producerRecord("I'm a String!");
        if (nonNull(serializer)) {
            recordStep.setKey(key, serializer);
        } else {
            if (key instanceof String) {
                recordStep.setKey((String) key);
            } else {
                throw new IllegalArgumentException();
            }
        }

        kafka.send(recordStep
            .topic("testTopic"));

        verify(mock, times(1))
            .send(new ProducerRecord<>("testTopic",
                null,
                null,
                key,
                "I'm a String!",
                null));
    }

    @Test(dataProvider = "sendObjectWithKyeData")
    public <K> void checkObjectSendingWithKey(K key, Serializer<K> serializer, KafkaProducer<K, DraftDto> mock) {
        var recordStep = producerRecord(new SomeSerializer(), draftDto);
        if (nonNull(serializer)) {
            recordStep.setKey(key, serializer);
        } else {
            if (key instanceof String) {
                recordStep.setKey((String) key);
            } else {
                throw new IllegalArgumentException();
            }
        }

        kafka.send(recordStep
            .topic("testTopic"));

        verify(mock, times(1))
            .send(new ProducerRecord<>("testTopic",
                null,
                null,
                key,
                draftDto,
                null));
    }


    @Test
    public void otherParameterTest() {
        kafka.send(producerRecord("I'm a String!")
            .topic("testTopic")
            .callback(customCallBack)
            .setKey("Some String")
            .partition(1)
            .timestamp(10L)
            .header("Header key", "Value1")
            .header(new RecordHeader("Header key2", "Value2".getBytes())));

        verify(producerRaw, times(1))
            .send(new ProducerRecord<>("testTopic",
                    1,
                    10L,
                    "Some String",
                    "I'm a String!",
                    List.of(new RecordHeader("Header key", "Value1".getBytes()), new RecordHeader("Header key2", "Value2".getBytes()))),
                customCallBack);
    }

    @Test
    public void defaultParametersTest() {
        DEFAULT_TOPIC_FOR_SEND.accept("testTopic");
        KAFKA_CALLBACK.accept(DefaultCallBackSupplier.class);

        kafka.send(producerRecord(new SomeSerializer(), draftDto)
            .setKey(draftDto, new SomeSerializer()));

        verify(producerWithDeserializedKeyAndValue, times(1))
            .send(new ProducerRecord<>("testTopic",
                    null,
                    null,
                    draftDto,
                    draftDto,
                    null),
                CALLBACK);
    }

    @Test
    public void defineAdditionalProperty() {
        var requestSizeConf = "123";
        kafka.send(producerRecord("I'm a String!")
            .topic("testTopic")
            .setProperty(MAX_REQUEST_SIZE_CONFIG, requestSizeConf));

        assertThat(producerProperties, hasEntry(
            equalTo(MAX_REQUEST_SIZE_CONFIG),
            equalTo(requestSizeConf)));
    }
}
