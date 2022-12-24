package ru.tinkoff.qa.neptune.kafka.captors;

import org.apache.kafka.common.TopicPartition;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import ru.tinkoff.qa.neptune.kafka.KafkaBasePreparations;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.anEmptyMap;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.*;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.kafka.captors.TestStringInjector.CAUGHT_MESSAGES;

public class BaseCaptorTest extends KafkaBasePreparations {

    TopicPartition topicPartition;

    @BeforeMethod
    public void prepare() {
        DO_CAPTURES_OF_INSTANCE.accept(null);
        CAUGHT_MESSAGES.clear();
    }

    @DataProvider
    public static Object[][] resultData() {
        return new Object[][]{
            {null, anEmptyMap()},
            {SUCCESS, not(anEmptyMap())},
            {FAILURE, anEmptyMap()},
            {SUCCESS_AND_FAILURE, not(anEmptyMap())},
        };
    }

    @DataProvider
    public static Object[][] noResultData() {
        return new Object[][]{
            {null},
            {SUCCESS},
            {FAILURE},
            {SUCCESS_AND_FAILURE},
        };
    }
}