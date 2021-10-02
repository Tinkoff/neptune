# Настройки Spring Mock MVC модуля для дальнейшей работы

[Подробнее о механизме настроек](./../../../core.api/doc/rus/SETTINGS.MD).

Описанный ниже набор настроек/свойств
дополняет [базовый набор настроек/свойств](./../../../core.api/doc/rus/SETTINGS.MD#Основной-набор-настроексвойств),
которые могут быть включены в `neptune.global.properties` или в `neptune.properties`, или использованы в качестве
свойств/переменных окружения/среды в той среде, в которой запускаются тесты.

## Оглавление

- [SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_DATA_TRANSFORMER](#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_DATA_TRANSFORMER)

## SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_DATA_TRANSFORMER

Основная статья: [Сериализация и десериализация](./../../../core.api/doc/rus/SERIALIZATION_DESERIALIZATION.MD)

```java
import org.my.pack;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

public class MyDataTransformer implements DataTransformer {


    @Override
    public <T> T deserialize(String message, Class<T> cls) {
        //тут описываем механизм десериализации
        //Желательно повторно использовать те сущности и механизмы, 
        //которые используются тестируемым приложением
    }

    @Override
    public <T> T deserialize(String string, TypeReference<T> type) {
        //тут описываем механизм десериализации
        //Желательно повторно использовать те сущности и механизмы, 
        //которые используются тестируемым приложением
    }

    @Override
    public String serialize(Object obj) {
        //тут описываем механизм сериализации
        //Желательно повторно использовать те сущности и механизмы, 
        //которые используются тестируемым приложением
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
        .SpringMockMvcDefaultResponseBodyTransformer.SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        DataTransformer transformer = SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_TRANSFORMER.get();
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)