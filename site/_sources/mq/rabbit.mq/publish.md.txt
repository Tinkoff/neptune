# Publish-операции

Полезные ссылки:
- [Свойство RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME](settings/RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME.md)
- [Свойство RABBIT_MQ_DEFAULT_EXCHANGE_NAME](settings/RABBIT_MQ_DEFAULT_EXCHANGE_NAME.md)
- [Шаги, выполняющие действие](../../quick_start/steps/pattern_steps/action_steps.md)

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.publish.RabbitMqPublishSupplier.rabbitTextMessage;

public class MyTest {

    @Test
    public void myTest() {
        //публикация сообщения, переданного текстом
        rabbitMq().publish(rabbitTextMessage("Hello world") //публикуемый текст
                .exchange("exchange1") //необязательный вызов
                //по умолчанию используется значение свойства RABBIT_MQ_DEFAULT_EXCHANGE_NAME
                .routingKey("routing_key1") //необязательный вызов
                //по умолчанию используется значение свойства RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME
                .immediate() //и при необходимости 
                .mandatory() //можно указать набор дополнительных 
                .header("header1", "value1") //параметров
                .replyTo("ReplyTo")
                .deliveryMode(1)
                .priority(2)
                .contentType("plain text")
                .contentEncoding("UTF-8")
                .correlationId("!@#4")
                .expiration("1234567")
                .messageId("hjhjkhjk")
                .timestamp(timeStamp)
                .userId("UserId")
                .appId("AppId")
                .clusterId("ClasterId"));
    }
}
```

## Публикация сериализованного объекта

Полезные ссылки:
- [Свойство RABBIT_MQ_DEFAULT_DATA_TRANSFORMER](settings/RABBIT_MQ_DEFAULT_DATA_TRANSFORMER.md)
- [Сериализация и десериализация](./../../core/serialize_deserialize.rst)

```java
import org.my.pack;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

//Описываем сериализацию и десериализацию для текущего проекта
public class MyDataTransformer implements DataTransformer {


    @Override
    public <T> T deserialize(String message, Class<T> cls) {
        //тут описываем механизм десериализации
    }

    @Override
    public <T> T deserialize(String string, TypeReference<T> type) {
        //тут описываем механизм десериализации
    }

    @Override
    public String serialize(Object obj) {
        //тут описываем механизм сериализации
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.publish.RabbitMqPublishSupplier.rabbitSerializedMessage;

public class MyTest {

    @Test
    public void myTest() {
        //передача объекта, сериализуемого в строку
        rabbitMq().publish(rabbitSerializedMessage(
                //объект, который надо превратить в сообщение
                new DraftDto().setName("test")) // и опубликовать
                //необязательный вызов
                //по умолчанию используется значение свойства RABBIT_MQ_DEFAULT_DATA_TRANSFORMER
                .withDataTransformer(new MyDataTransformer())
            //можно указать 
            //прочие дополнительные параметры
            //см пример выше
            );
    }
}
```