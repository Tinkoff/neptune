package ru.tinkoff.qa.neptune.rabbit.mq.test.basic.get;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rabbitmq.client.GetResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.rabbit.mq.test.BaseRabbitMqTest;
import ru.tinkoff.qa.neptune.rabbit.mq.test.CustomMapper;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DefaultMapper;
import ru.tinkoff.qa.neptune.rabbit.mq.test.DraftDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetSupplier.rabbitBody;

public class ReadTest extends BaseRabbitMqTest {

    private final DraftDto dto = new DraftDto().setName("test");
    private final String body = new DefaultMapper().serialize(dto);

    private final List<DraftDto> dtos = List.of(new DraftDto().setName("test2"), new DraftDto().setName("test3"));
    private final String body2 = new DefaultMapper().serialize(dtos);

    @BeforeClass(dependsOnMethods = "setUp")
    public void beforeClass() throws Exception {
        when(channel.basicGet("queue", true))
                .thenReturn(new GetResponse(null, null, body.getBytes(StandardCharsets.UTF_8), 0));

        when(channel.basicGet("test", false))
                .thenReturn(new GetResponse(null, null, body.getBytes(StandardCharsets.UTF_8), 0));

        when(channel.basicGet("test2", true))
                .thenReturn(new GetResponse(null, null, body2.getBytes(StandardCharsets.UTF_8), 0));
    }

    @Test(description = "Checking method call with default mapper")
    public void readTest1() throws IOException {
        var dto = rabbitMqStepContext.read(rabbitBody("queue",
                true,
                DraftDto.class));

        verify(channel, times(1)).basicGet("queue", true);
        assertThat(dto.getName(), is("test"));
    }

    @Test(description = "Checking method call with custom mapper")
    public void readTest2() throws IOException {
        var dto = rabbitMqStepContext.read(rabbitBody(
                "test",
                false,
                DraftDto.class),
                new CustomMapper());

        verify(channel, times(1)).basicGet("test", false);
        assertThat(dto.getName(), is("PREFIXtest"));
    }

    @Test(description = "Checking method call with custom mapper")
    public void readTest3() {
        var dtos = rabbitMqStepContext.read(rabbitBody(
                "test2",
                true,
                new TypeReference<List<DraftDto>>() {
                }));

        assertThat(dtos, hasSize(2));
    }
}
