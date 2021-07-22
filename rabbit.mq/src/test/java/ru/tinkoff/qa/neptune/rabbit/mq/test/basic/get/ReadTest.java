package ru.tinkoff.qa.neptune.rabbit.mq.test.basic.get;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DefaultMapper;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DraftDto;

import java.io.IOException;

import static java.time.Duration.ofSeconds;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetSupplier.valueOf;

public class ReadTest extends BaseRabbitMqTest {

    @Test(description = "Checking method call with default mapper")
    public void readTest1() throws IOException {
        rabbitMqStepContext.read(valueOf("queue", true, DraftDto.class).timeOut(ofSeconds(1)));

        verify(channel, atLeast(2)).basicGet("queue", true);
    }

    @Test(description = "Checking method call with custom mapper")
    public void readTest2() throws IOException {
        rabbitMqStepContext.read(valueOf("test", true, DraftDto.class).timeOut(ofSeconds(1)), new DefaultMapper());

        verify(channel, atLeast(2)).basicGet("test", true);
    }
}
