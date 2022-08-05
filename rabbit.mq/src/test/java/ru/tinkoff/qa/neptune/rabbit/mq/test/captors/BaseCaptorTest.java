package ru.tinkoff.qa.neptune.rabbit.mq.test.captors;

import org.testng.annotations.BeforeMethod;
import ru.tinkoff.qa.neptune.rabbit.mq.BaseRabbitMqPreparations;

import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;
import static ru.tinkoff.qa.neptune.rabbit.mq.test.captors.TestStringInjector.CAUGHT_MESSAGES;

public class BaseCaptorTest extends BaseRabbitMqPreparations {

    @BeforeMethod
    public void prepare() {
        DO_CAPTURES_OF_INSTANCE.accept(null);
        CAUGHT_MESSAGES.clear();
    }
}
