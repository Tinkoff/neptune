# Свойства

Перечисленные ниже свойства дополняют [стандартный набор](./../../quick_start/settings/index.md)

## Настройка дефолтной сериализации / десериализации

Подробнее: [Сериализация и десериализация](./../../core/serialize_deserialize.rst)

Данное свойство упрощает получение [данных десериализованного тела ответа](./bodydata.md) на запрос.

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


