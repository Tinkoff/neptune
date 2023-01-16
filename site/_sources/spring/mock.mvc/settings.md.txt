# Mock MVC. Свойства

Перечисленные ниже свойства дополняют [стандартный набор](./../../quick_start/settings/index.md)

## Настройка дефолтной сериализации / десериализации

Подробнее: 
- [Сериализация и десериализация](./../../core/serialize_deserialize.rst)

Данное свойство упрощает получение [данных десериализованного тела ответа](./bodydata.md) на запрос.

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

```properties
#Значение свойства указывается так
SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_DATA_TRANSFORMER=org.my.pack.MyDataTransformer
```

```java
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

import static ru.tinkoff.qa.neptune.spring.mock.mvc.properties
        .SpringMockMvcDefaultResponseBodyTransformer
        .SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        DataTransformer transformer = 
            SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get();
    }
}
```

Как вариант можно, можно использовать бины Spring-приложения

```java
import org.my.pack;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

import static ru.tinkoff.qa.neptune.spring.boot.starter.application
    .contexts.CurrentApplicationContextTestExecutionListener.getCurrentApplicationContext;

//Описываем сериализацию и десериализацию для текущего проекта
public class MyDataTransformer implements DataTransformer {


    @Override
    public <T> T deserialize(String message, Class<T> cls) {
        //текущий контекст приложения
        var context = getCurrentApplicationContext();

        //Далее извлекаем необходимые бины,
        //при помощи которых можно выполнить десериализацию
        return //возврат десериализации
    }

    @Override
    public <T> T deserialize(String string, TypeReference<T> type) {
        //текущий контекст приложения
        var context = getCurrentApplicationContext();

        //Далее извлекаем необходимые бины,
        //при помощи которых можно выполнить десериализацию
        return //возврат десериализации
    }

    @Override
    public String serialize(Object obj) {
        //текущий контекст приложения
        var context = getCurrentApplicationContext();

        //Далее извлекаем необходимые бины,
        //при помощи которых можно выполнить сериализацию
        return //возврат сериализации
    }
}
```

Полезная ссылка: [Neptune Spring boot starter](./../../../spring/spring.boot.sterter.md)

