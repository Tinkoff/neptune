# Сериализация и десериализация

Инструменты и библиотеки Java, которые используются для сериализации и десериализации данных, очень разнообразны. Для 
того чтобы исключить сложности, связанные с этим, Neptune включает в себя описанный ниже механизм

```java
import org.my.pack;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

//Описываем сериализацию и десериализацию для текущего проекта
public class MyDataTransformer implements DataTransformer {


    @Override
    public <T> T deserialize(String message, Class<T> cls) {
        //десериализация с использованием класса объекта, 
        // в который должна быть преобразована строка
    }

    @Override //Для ссылки на нужный тип используется 
    // com.fasterxml.jackson.core.type.TypeReference
    public <T> T deserialize(String string, TypeReference<T> type) {
        //десериализация с использованием типа объекта, 
        // в который должна быть преобразована строка
    }

    @Override
    public String serialize(Object obj) {
        //сериализация объекта в строку
    }
}
```

Пример для `com.fasterxml.jackson`

```java
import org.my.pack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

public class MyJacksonDataTransformer implements DataTransformer {


    public MyJacksonDataTransformer() {
        mapper = new ObjectMapper()
        //Указываем нужные настройки
        ;
    }
    
    @Override
    public <T> T deserialize(String message, Class<T> cls) {
        try {
            return mapper.readValue(message, cls);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(String string, TypeReference<T> type) {
        try {
            return mapper.readValue(message, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String serialize(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
```

Пример для `com.google.gson`

```java
import org.my.pack;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

public class MyGsonDataTransformer implements DataTransformer {


    public MyGsonDataTransformer() {
        this.gson = new GsonBuilder()
                //Указываем нужные настройки
                .create();
    }

    @Override
    public <T> T deserialize(String message, Class<T> cls) {
        return gson.fromJson(message, cls);
    }

    @Override
    public <T> T deserialize(String string, TypeReference<T> type) {
        return gson.fromJson(string, type.getType());
    }

    @Override
    public String serialize(Object obj) {
        return gson.toJson(obj);
    }
}
```

И т.д. для различных форматов данных.

Если в проекте есть фабричные методы или классы, которые предоставляют / хранят настройки сериализации и
десериализации либо предоставляют / хранят сериализаторы и десериализаторы, то рекомендуется повторно их использовать 
при реализации интерфейса `ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer`

Где используется описанный механизм:

- [RabbitMQ](./../../../rabbit.mq/doc/rus/README.MD): 
  - [Настройки](./../../../rabbit.mq/doc/rus/SETTINGS.MD#RABBIT_MQ_DEFAULT_DATA_TRANSFORMER)
  - [Получение сообщения](./../../../rabbit.mq/doc/rus/FUNCTION.MD#Получение-сообщения)
  - [Публикация сообщений](./../../../rabbit.mq/doc/rus/FUNCTION.MD#Публикация-сообщений)
- [Kafka](./../../../kafka/doc/rus/README.MD):
  - [Настройки](./../../../kafka/doc/rus/SETTINGS.MD#KAFKA_DEFAULT_DATA_TRANSFORMER)
  - [Получение сообщения](./../../../kafka/doc/rus/FUNCTION.MD#Получение-сообщения)
  - [Публикация сообщений](./../../../kafka/doc/rus/FUNCTION.MD#Публикация-сообщений)
- [Selenium](./../../../selenium/doc/rus/README.MD):
  - [Настройки](./../../../selenium/doc/rus/SETTINGS.MD#PROXIED_TRAFFIC_BODY_TRANSFORMER)
  - [Mатчеры. Тело запроса и ответа](./../../../selenium/doc/rus/HAMCREST.MD#Тело-запроса-и-ответа)

[к списку документов](README.MD#Оглавление)