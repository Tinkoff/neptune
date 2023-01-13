# Mock MVC. Тело ответа

Интеграция _Neptune_ и _Mock MVC_ имеет дополнительные опции, связанные с работой с сериализованным / десериализованным
телом ответа и его валидацией.

Важно:

- [Сериализация и десериализация](./../../core/serialize_deserialize.rst)
- [Свойства интеграции Neptune и Spring Mock MVC](./settings.md)
- [Шаги, возвращающие объекты](../../quick_start/steps/pattern_steps/get_step/index.md)

## Использование java-объектов для описания ожидаемого тела ответа

```java
import static ru.tinkoff.qa.neptune.spring.mock.mvc
    .GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test(destription = "Пример с дефолтным сериализатором")
    public void myTest1() {
        mockMvcGet(
            response(post("/some/path")
                .contentType(APPLICATION_JSON)
                .content("{\"someString\":\"1\"}"))
                //указываем dto в качестве ожидаемого тела ответа
                .expectContent(new SomeResponseDTO())
        );
    }

    @Test(destription = "Пример с указанием сериализатора")
    public void myTest2() {
        mockMvcGet(
            response(post("/some/path")
                .contentType(APPLICATION_JSON)
                .content("{\"someString\":\"1\"}"))
                //указываем dto в качестве ожидаемого тела ответа
                .expectContent(new SomeResponseDTO(),
                    //и указываем сериализатор
                    new MyDataTransformer())
        );
    }
}
```

## Получение тела ответа

### В виде текста

```java
import static ru.tinkoff.qa.neptune.spring.mock.mvc
    .GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        String body = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания
                //ответа
                //
                //указываем что нужно просто получить текстовое 
                .thenGetStringContent() //содержимое тела ответа
                //Можно указать один или несколько критериев, 
                //которым должно соответствовать
                //текстовое содержимое тела ответа.
                // Так же доступны criteriaOr(criteria...), 
                // criteriaOnlyOne(criteria...)
                // criteriaNot(criteria...)
                .criteria("Описание критерия получаемого текста", s -> {
                    /*предикат, как работает критерий*/
                })
                //Можно указать, что должно быть выброшено исключение,
                .throwOnNoResult()); // если у ответа нет тела или тело 
        // не соответствует перечисленным критериям
    }
}
```

### В виде java-объекта

Примеры с дефолтным десериализатором

```java
import static ru.tinkoff.qa.neptune.spring.mock.mvc
    .GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest1() {
        Dto body = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                //класс десериализованного тела
                .thenGetBody(Dto.class)
                //Можно указать один или несколько критериев, 
                //которым должно соответствовать
                //десериализованое тело ответа.
                // Так же доступны criteriaOr(criteria...), 
                // criteriaOnlyOne(criteria...)
                // criteriaNot(criteria...)
                .criteria("Описание критерия, " +
                    "которому должно соответствовать тело", dto -> {
                    /*предикат, как работает критерий*/
                })
                //Можно указать, что должно быть выброшено исключение, 
                .throwOnNoResult()); // если у ответа нет тела 
        // или оно десериализовалось в объект, 
        // который не соответствует перечисленным критериям

        GenericDto<SomeType> body2 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                //тип десериализованного тела
                .thenGetBody(new TypeReference<GenericDto<SomeType>>() {})
                //
                //прочие параметры
        );

        String value = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                //описание объекта, который следует получить 
                .thenGetValue("Value of the field 'stringValue'",
                    //класс десериализованного тела
                    Dto.class,
                    //описание получаемого результата в виде функции
                    Dto::getStringValue)
                //
                //прочие параметры
        );

        String value2 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetValue("Value of the field 'stringValue'",
                    //тип десериализованного тела
                    new TypeReference<GenericDto<SomeType>>() {},
                    GenericDto::getStringValue)
                //
                //прочие параметры
        );
    }
}
``` 

Примеры с указанием десериализатора

```java
import static ru.tinkoff.qa.neptune.spring.mock.mvc
    .GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest2() {
        Dto body = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetBody(
                    //указываем десериализатор
                    new MyDataTransformer(),
                    Dto.class)
                //
                //прочие параметры
        );

        GenericDto<SomeType> body2 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetBody(
                    new MyDataTransformer(),
                    new TypeReference<GenericDto<SomeType>>() {})
                //
                //прочие параметры
        );

        String value = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetValue("Value of the field 'stringValue'",
                    new MyDataTransformer(),
                    Dto.class,
                    Dto::getStringValue)
                //
                //прочие параметры
        );

        String value2 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetValue("Value of the field 'stringValue'",
                    new MyDataTransformer(),
                    new TypeReference<GenericDto<SomeType>>() {},
                    GenericDto::getStringValue)
                //
                //прочие параметры
        );
    }
}
```

### Как List

Примеры с дефолтным десериализатором

```java
import java.util.List;

import static ru.tinkoff.qa.neptune.spring.mock.mvc
    .GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest1() {
        List<Dto> body = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                //описание или название листа, 
                // который следует получить
                .thenGetList("List of dto's",
                    //класс десериализованного тела
                    DtoList.class) //public class DtoList 
                // extends ArrayList<Dto>
                //
                //
                //прочие параметры
        ); 

        List<Dto> body2 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetList("List of dto's",
                    //тип десериализованного тела
                    new TypeReference<List<Dto>>() {})
            //
            //прочие параметры
        );

        List<Object> value = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetList("Value of the field 'listValue'",
                    //класс десериализованного тела
                    Dto.class,
                    //описание получения листа в виде функции
                    Dto::listValue)
            //
            //прочие параметры
        );

        List<Object> value2 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetList("Value of the field 'listValue'",
                    //тип десериализованного тела
                    new TypeReference<GenericDto<SomeType>>() {},
                    GenericDto::listValue)
            //
            //прочие параметры
        );
    }
}
``` 

```{eval-rst}
.. include:: ../../shared_docs/steps_return_list_optiotal_parameters_sync.rst
```

Примеры с указанием десериализатора

```java
import java.util.List;

import static ru.tinkoff.qa.neptune.spring.mock.mvc
    .GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest1() {
        List<Dto> body = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetList("List of dto's",
                    //указываем десериализатор
                    new MyDataTransformer(),
                    DtoList.class)
            //
            //прочие параметры
        );

        List<Dto> body2 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetList("List of dto's",
                    new MyDataTransformer(),
                    new TypeReference<List<Dto>>() {})
            //
            //прочие параметры
        );

        List<Object> value = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetList("Value of the field 'listValue'",
                    new MyDataTransformer(),
                    Dto.class,
                    Dto::listValue)
            //
            //прочие параметры
        );

        List<Object> value2 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetList("Value of the field 'listValue'",
                    new MyDataTransformer(),
                    new TypeReference<GenericDto<SomeType>>() {},
                    GenericDto::listValue)
            //
            //прочие параметры
        );
    }
}
```

### Как массив

Примеры с дефолтным десериализатором

```java
import static ru.tinkoff.qa.neptune.spring.mock.mvc
    .GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest1() {
        Dto[] body = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                //описание или название массива, 
                // который следует получить
                .thenGetArray("Array of dto's",
                    //класс десериализованного тела
                    Dto[].class)
                //
                //прочие параметры
        );

        GenericDto<SomeType>[] body2 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetArray("Array of dto's",
                    //тип десериализованного тела
                    new TypeReference<GenericDto<SomeType>[]>() {})
                //
                //прочие параметры
        );

        Object[] value = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetArray("Value of the field 'arrayValue'",
                    //класс десериализованного тела
                    Dto.class,
                    //описание получения массива в виде функции
                    Dto::arrayValue)
                //
                //прочие параметры
        );

        Object[] value2 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetArray("Value of the field 'arrayValue'",
                    //тип десериализованного тела
                    new TypeReference<GenericDto<SomeType>>() {},
                    GenericDto::arrayValue)
                //
                //прочие параметры
        );
    }
}
``` 

```{eval-rst}
.. include:: ../../shared_docs/steps_return_array_optiotal_parameters_sync.rst
```

Примеры с указанием десериализатора

```java
import static ru.tinkoff.qa.neptune.spring.mock.mvc
    .GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest1() {
        Dto[] body = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetArray("Array of dto's",
                    //указываем десериализатор
                    new MyDataTransformer(),
                    Dto[].class)
                //
                //прочие параметры
        );

        GenericDto<SomeType>[] body2 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetArray("Array of dto's",
                    new MyDataTransformer(),
                    new TypeReference<GenericDto<SomeType>[]>() {})
                //
                //прочие параметры
        );

        Object[] value = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetArray("Value of the field 'arrayValue'",
                    new MyDataTransformer(),
                    Dto.class,
                    Dto::arrayValue)
                //
                //прочие параметры
        );

        Object[] value2 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetArray("Value of the field 'arrayValue'",
                    new MyDataTransformer(),
                    new TypeReference<GenericDto<SomeType>>() {},
                    GenericDto::arrayValue)
                //
                //прочие параметры
        );
    }
}
```

### Как элемент Iterable

Примеры с дефолтным десериализатором

```java

import static ru.tinkoff.qa.neptune.spring.mock.mvc
    .GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest1() {
        Dto iterableItem = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                //описание объекта, который следует получить
                .thenGetValueFromIterable("A value from list of dto's",
                    //класс десериализованного тела
                    DtoList.class) //public class DtoList 
                //
                //прочие параметры            
        );

        Dto iterableItem2 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetValueFromIterable("A value from list of dto's",
                    //тип десериализованного тела
                    new TypeReference<List<Dto>>() {})
                //
                //прочие параметры
        );

        Object iterableItem3 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetValueFromIterable("A value from  the field 'listValue'",
                    //класс десериализованного тела
                    Dto.class,
                    //описание получения листа в виде функции
                    Dto::listValue)
                //
                //прочие параметры
        );

        Object iterableItem4 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetValueFromIterable("A value from  the field 'listValue'",
                    //тип десериализованного тела
                    new TypeReference<GenericDto<SomeType>>() {},
                    GenericDto::listValue)
                //
                //прочие параметры
        );
    }
}
``` 

```{eval-rst}
.. include:: ../../shared_docs/steps_return_iterable_item_optiotal_parameters_sync.rst
```

Примеры с указанием десериализатора

```java
import static ru.tinkoff.qa.neptune.spring.mock.mvc
    .GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest1() {
        Dto iterableItem = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetValueFromIterable("A value from list of dto's",
                    //указываем десериализатор
                    new MyDataTransformer(),
                    DtoList.class)
                //
                //прочие параметры
        );

        Dto iterableItem2 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetValueFromIterable("A value from list of dto's",
                    new MyDataTransformer(),
                    new TypeReference<List<Dto>>() {})
                //
                //прочие параметры
        );

        Object iterableItem3 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetValueFromIterable("A value from  the field 'listValue'",
                    new MyDataTransformer(),
                    Dto.class,
                    Dto::listValue)
                //
                //прочие параметры
        );

        List<Object> iterableItem4 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetValueFromIterable("A value from  the field 'listValue'",
                    new MyDataTransformer(),
                    new TypeReference<GenericDto<SomeType>>() {},
                    GenericDto::listValue)
                //
                //прочие параметры
        );
    }
}
```

### Как элемент массива

Примеры с дефолтным десериализатором

```java
import static ru.tinkoff.qa.neptune.spring.mock.mvc
    .GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest1() {
        Dto arrayItem = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                //описание объекта, который следует получить
                .thenGetValueFromArray("A value from array of dto's",
                    //класс десериализованного тела
                    Dto[].class)
                //
                //прочие параметры
        ); 

        GenericDto<SomeType> arrayItem2 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetValueFromArray("A value from array of dto's",
                    //тип десериализованного тела
                    new TypeReference<GenericDto<SomeType>[]>() {})
                //
                //прочие параметры
        );

        Object arrayItem3 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetValueFromArray("A value from  the field 'arrayValue'",
                    //класс десериализованного тела
                    Dto.class,
                    //описание получения массива в виде функции
                    Dto::arrayValue)
                //
                //прочие параметры
        );

        Object arrayItem4 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetValueFromArray("A value from  the field 'arrayValue'",
                    //тип десериализованного тела
                    new TypeReference<GenericDto<SomeType>>() {},
                    GenericDto::arrayValue)
                //
                //прочие параметры
        );
    }
}
``` 

```{eval-rst}
.. include:: ../../shared_docs/steps_return_array_item_optiotal_parameters_sync.rst
```

Примеры с указанием десериализатора

```java
import static ru.tinkoff.qa.neptune.spring.mock.mvc
    .GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest1() {
        Dto arrayItem = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetValueFromArray("A value from array of dto's",
                    //указываем десериализатор
                    new MyDataTransformer(),
                    Dto[].class)
                //
                //прочие параметры
        );

        GenericDto<SomeType> arrayItem2 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetValueFromArray("A value from array of dto's",
                    new MyDataTransformer(),
                    new TypeReference<GenericDto<SomeType>[]>() {})
                //
                //прочие параметры
        );

        Object arrayItem3 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetValueFromArray("A value from  the field 'arrayValue'",
                    new MyDataTransformer(),
                    Dto.class,
                    Dto::arrayValue)
                //
                //прочие параметры
        );

        Object arrayItem4 = mockMvcGet(
            response(/*параметры запроса*/)
                //указываем ожидания ответа
                //
                .thenGetValueFromArray("A value from  the field 'arrayValue'",
                    new MyDataTransformer(),
                    new TypeReference<GenericDto<SomeType>>() {},
                    GenericDto::arrayValue)
                //
                //прочие параметры
        );
    }
}
```
