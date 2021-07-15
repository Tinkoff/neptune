package ru.tinkoff.qa.neptune.rabbit.mq.test.basic.get;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DraftDto;

import java.io.IOException;

import static java.time.Duration.ofSeconds;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetSupplier.valueOf;

public class ReadTest extends BaseRabbitMqTest {

    @Test
    public void purgeTest1() throws IOException {
        rabbitMqStepContext.read(valueOf("queue", true, DraftDto.class).timeOut(ofSeconds(1)));

        verify(channel, atLeast(1)).basicGet("queue", true);
    }

    @Test
    public void purgeTest2() throws IOException {
        rabbitMqStepContext.read(valueOf("test", true, DraftDto.class).timeOut(ofSeconds(1)), new XmlMapper());

        verify(channel, atLeast(15)).basicGet("test", true);
    }
}
