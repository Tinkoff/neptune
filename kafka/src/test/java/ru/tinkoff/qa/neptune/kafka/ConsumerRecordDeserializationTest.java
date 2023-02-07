package ru.tinkoff.qa.neptune.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.kafka.jackson.desrializer.KafkaJacksonModule;

import java.util.Date;
import java.util.Optional;

import static java.nio.ByteBuffer.allocateDirect;
import static org.apache.kafka.common.record.TimestampType.LOG_APPEND_TIME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ConsumerRecordDeserializationTest {

    @DataProvider
    public static Object[][] data() {
        var date = new Date();
        return new Object[][]{
            {new ConsumerRecord<>("testTopic", 1,
                1L,
                date.getTime(),
                LOG_APPEND_TIME,
                0,
                0,
                new DraftDto().setName("Some Key"),
                new DraftDto().setName("Some Value"),
                new RecordHeaders()
                    .add(new RecordHeader("header1", "value1".getBytes()))
                    .add(new RecordHeader("header1", "value2".getBytes()))
                    .add(new RecordHeader("header2", "value1".getBytes())),
                Optional.of(5)),
                "{\n" +
                    "  \"topic\" : \"testTopic\",\n" +
                    "  \"partition\" : 1,\n" +
                    "  \"leaderEpoch\" : 5,\n" +
                    "  \"offset\" : 1,\n" +
                    "  \"LogAppendTime\" : " + date.getTime() + ",\n" +
                    "  \"headers\" : {\n" +
                    "    \"header1\" : [ \"value1\", \"value2\" ],\n" +
                    "    \"header2\" : [ \"value1\" ]\n" +
                    "  },\n" +
                    "  \"key\" : \"{\\n  \\\"name\\\" : \\\"Some Key\\\"\\n}\",\n" +
                    "  \"value\" : \"{\\n  \\\"name\\\" : \\\"Some Value\\\"\\n}\"\n" +
                    "}"},

            {new ConsumerRecord<>("testTopic", 1,
                1L,
                new Date().getTime(),
                null,
                0,
                0,
                null,
                null,
                new RecordHeaders(),
                Optional.empty()),
                "{\n" +
                    "  \"topic\" : \"testTopic\",\n" +
                    "  \"partition\" : 1,\n" +
                    "  \"leaderEpoch\" : null,\n" +
                    "  \"offset\" : 1,\n" +
                    "  \"headers\" : { },\n" +
                    "  \"key\" : null,\n" +
                    "  \"value\" : null\n" +
                    "}"
            },

            {new ConsumerRecord<>("testTopic", 1,
                1L,
                new Date().getTime(),
                null,
                0,
                0,
                allocateDirect(5),
                allocateDirect(5),
                new RecordHeaders(),
                Optional.empty()),
                "{\n" +
                    "  \"topic\" : \"testTopic\",\n" +
                    "  \"partition\" : 1,\n" +
                    "  \"leaderEpoch\" : null,\n" +
                    "  \"offset\" : 1,\n" +
                    "  \"headers\" : { },\n" +
                    "  \"key\" : \"\\\"AAAAAAA=\\\"\",\n" +
                    "  \"value\" : \"\\\"AAAAAAA=\\\"\"\n" +
                    "}"
            },
        };
    }

    @Test(dataProvider = "data")
    public void deserializationTest(ConsumerRecord<?, ?> consumerRecord, String expected) throws Exception {
        var serialized = new ObjectMapper()
            .registerModule(new KafkaJacksonModule())
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(consumerRecord);

        assertThat(serialized, is(expected));
    }
}
