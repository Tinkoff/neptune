# Работа с MockMVC

- О контекстах можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Контекст).
  Объект [контекста для работы со Spring MockMvc](https://tinkoff.github.io/neptune/spring.mock.mvc/ru/tinkoff/qa/neptune/spring/mock/mvc/MockMvcContext.html)
  в примерах ниже вызывается статическим методом `mockMvcGet`

- О принципах работы шагов, которые возвращают результат, можно прочитать
  [здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги-которые-возвращают-результат).

## Оглавление
- [Получение ответа](#Получение-ответа)
- [Получение данных тела ответа](#Получение-данных-тела-ответа)
    - [Десериализация](#Десериализация)
    - [Получение тела ответа в виде текста](#Получение-тела-ответа-в-виде-текста)
    - [Получение данных тела ответа как Java-объект](#Получение-данных-тела-ответа-как-Java-объект)
    - [Получение данных тела ответа как List](#Получение-данных-тела-ответа-как-List)
    - [Получение данных тела ответа в виде массива](#Получение-данных-тела-ответа-в-виде-массива)
    - [Получение данных тела ответа как Java-объект из Iterable](#Получение-данных-тела-ответа-как-Java-объект-из-Iterable)
    - [Получение данных тела ответа как Java-объект из массива](#Получение-данных-тела-ответа-как-Java-объект-из-массива)

## Получение ответа

```java
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        MockHttpServletResponse response = mockMvcGet(response(
                post("/some/path") //запрос
                        .contentType(APPLICATION_JSON) //и его параметры 
                        .contentType("{\"someString 1\"}"))
                .expect(StatusResultMatchers::isOk) //тут можно указать параметры ожидаемого ответа
                .expect(content -> content.string(containsString("SUCCESS"))) //проверка пройдет по всем 
                // указанным ожиданиям. Какие-то из них не будут удовлетворены,
                //будет выброшено AssertError с описанием всех несоответствий
                //Так же каждое отдельное несоответствие можно будет найти в отчете,
                //логе или консоли
                .expectStatus(200) //есть методы которые позволяют записывать  
                .expectContent(containsString("SUCCESS")) //ожидания в упрощенном виде        
        );
    }
}
```

Методы, с помощью которых можно описать ожидаемый ответ, можно 
[посмотреть в документации. Названия методов начинаются на _expect_](https://tinkoff.github.io/neptune/spring.mock.mvc/ru/tinkoff/qa/neptune/spring/mock/mvc/GetMockMvcResponseResultSupplier.html)

Так же можно передавать значения `@Autowired`- полей или переменных пипа `MockMvc`

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

  @Autowired //Или любой другой вариант
  private MockMvc mockMvc; //инициализации поля или переменной

  @Test
    public void myTest() {

        MockHttpServletResponse response = mockMvcGet(response(
                mockMvc, //явная передача объекта MockMvc
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
        );
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Получение данных тела ответа

### Десериализация

Рекомендуется указать значение свойства / переменной окружения [SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_DATA_TRANSFORMER](SETTINGS.MD#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_DATA_TRANSFORMER).
Если же этого не делать, тогда следует описать следующий класс

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

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение тела ответа в виде текста

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        String body = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetStringContent() //указываем что нужно просто получить текстовое 
                //содержимое тела ответа
                .criteria("Описание критерия получаемого текста", s -> /*предикат, как работает критерий*/) 
                //можно указать один или несколько критериев, которым должно соответствовать
                //текстовое содержимое тела ответа
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или тело не соответствует 
                //перечисленным критериям
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение данных тела ответа как Java-объект

[Десериализация](#Десериализация)

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        var transformer = new MyDataTransformer();
        
        Dto body = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetBody(transformer, //указываем с помощью какого объекта происходит десериализация
                        Dto.class) //указываем, в объект какого класса должно быть десериализовано тело ответа
                .criteria("Описание критерия, которому должно соответствовать тело", dto -> /*предикат, как работает критерий*/) 
                //можно указать один или несколько критериев, которым должно соответствовать
                //десериализованое тело ответа
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или десериализованое тело не соответствует 
                //перечисленным критериям
    }
}
```

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        var transformer = new MyDataTransformer();
        
        GenericDto<SomeType> body = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetBody(transformer, //указываем с помощью какого объекта происходит десериализация
                        new TypeReference<GenericDto<SomeType>>() {}) //указываем, в объект какого типа должно быть 
                        // десериализовано тело ответа
                .criteria("Описание критерия, которому должно соответствовать тело", dto -> /*предикат, как работает критерий*/) 
                //можно указать один или несколько критериев, которым должно соответствовать
                //десериализованое тело ответа
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или десериализованое тело не соответствует 
                //перечисленным критериям
    }
}
```

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        var transformer = new MyDataTransformer();
        
        String value = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetValue("Value of the field 'stringValue'", //описание объекта, который следует получить
                        transformer, //указываем с помощью какого объекта происходит десериализация
                        Dto.class, //указываем, в объект какого класса должно быть десериализовано тело ответа
                        Dto::getStringValue) //описание получаемого результата в виде функции
                .criteria("Описание критерия, которому должен соответствовать результат", s -> /*предикат, как работает критерий*/) 
                //можно указать один или несколько критериев, которым должен соответствовать результат
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или не был получен результат, соответствующий
                //перечисленным критериям
    }
}
```

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        var transformer = new MyDataTransformer();
        
        String value = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetValue("Value of the field 'stringValue'", //описание объекта, который следует получить
                        transformer, //указываем с помощью какого объекта происходит десериализация
                        new TypeReference<GenericDto<SomeType>>() {}, //указываем, в объект какого типа должно быть 
                        // десериализовано тело ответа
                        GenericDto::getStringValue) //описание получаемого результата в виде функции
                .criteria("Описание критерия, которому должен соответствовать результат", s -> /*предикат, как работает критерий*/) 
                //можно указать один или несколько критериев, которым должен соответствовать результат
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или не был получен результат, соответствующий
                //перечисленным критериям
    }
}
```

Можно указать значение свойства / переменной окружения [SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_DATA_TRANSFORMER](SETTINGS.MD#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_DATA_TRANSFORMER).
И тогда

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        
        Dto body = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetBody(Dto.class) //указываем, в объект какого класса должно быть десериализовано тело ответа
                .criteria("Описание критерия, которому должно соответствовать тело", dto -> /*предикат, как работает критерий*/) 
                //можно указать один или несколько критериев, которым должно соответствовать
                //десериализованое тело ответа
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или десериализованое тело не соответствует 
                //перечисленным критериям
    }
}
```

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        
        GenericDto<SomeType> body = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetBody(new TypeReference<GenericDto<SomeType>>() {}) //указываем, в объект какого типа должно быть 
                        // десериализовано тело ответа
                .criteria("Описание критерия, которому должно соответствовать тело", dto -> /*предикат, как работает критерий*/) 
                //можно указать один или несколько критериев, которым должно соответствовать
                //десериализованое тело ответа
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или десериализованое тело не соответствует 
                //перечисленным критериям
    }
}
```

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        
        String value = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetValue("Value of the field 'stringValue'", //описание объекта, который следует получить
                        Dto.class, //указываем, в объект какого класса должно быть десериализовано тело ответа
                        Dto::getStringValue) //описание получаемого результата в виде функции
                .criteria("Описание критерия, которому должен соответствовать результат", s -> /*предикат, как работает критерий*/) 
                //можно указать один или несколько критериев, которым должен соответствовать результат
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или не был получен результат, соответствующий
                //перечисленным критериям
    }
}
```

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        String value = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetValue("Value of the field 'stringValue'", //описание объекта, который следует получить
                        new TypeReference<GenericDto<SomeType>>() {}, //указываем, в объект какого типа должно быть 
                        // десериализовано тело ответа
                        GenericDto::getStringValue) //описание получаемого результата в виде функции
                .criteria("Описание критерия, которому должен соответствовать результат", s -> /*предикат, как работает критерий*/) 
                //можно указать один или несколько критериев, которым должен соответствовать результат
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или не был получен результат, соответствующий
                //перечисленным критериям
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение данных тела ответа как List

[Десериализация](#Десериализация)

```java
import java.util.List;

import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

  @Test
  public void myTest() {
    var transformer = new MyDataTransformer();

    List<Dto> body = mockMvcGet(response(
            post("/some/path")
                    .contentType(APPLICATION_JSON)
                    .contentType("{\"someString 1\"}"))
            .expectStatus(200)
            .expectContent(containsString("SUCCESS"))
            .thenGetList("List of dto's", //описание списка, который следует получить
                    transformer, //указываем с помощью какого объекта происходит десериализация
                    DtoList.class) //указываем, в объект какого класса должно быть десериализовано тело ответа
            //public class DtoList extends ArrayList<Dto>
            .criteria("Описание критерия, которому должен соответствовать " +
                            "каждый элемент, который попадет в результирующий список",
                    dto -> /*предикат, как работает критерий*/)
            //можно указать один или несколько критериев, которым должен соответствовать
            //каждый элемент результирующего списка
            .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
    // если у ответа нет тела или результирующий список пустой (не было ни одного элемента, 
    //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой список)
  }
}
```

```java
import java.util.List;

import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

  @Test
  public void myTest() {
      var transformer = new MyDataTransformer();
      
      List<Dto> body = mockMvcGet(response(
              post("/some/path")
                      .contentType(APPLICATION_JSON)
                      .contentType("{\"someString 1\"}"))
              .expectStatus(200)
              .expectContent(containsString("SUCCESS"))
              .thenGetList("List of dto's", //описание списка, который следует получить
                      transformer, //указываем с помощью какого объекта происходит десериализация
                      new TypeReference<List<Dto>>() {}) //указываем, в объект 
                      // какого типа должно быть десериализовано тело ответа
              .criteria("Описание критерия, которому должен соответствовать " + 
                      "каждый элемент, который попадет в результирующий список",
                      dto -> /*предикат, как работает критерий*/)
              //можно указать один или несколько критериев, которым должен соответствовать
              //каждый элемент результирующего списка
              .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
              // если у ответа нет тела или результирующий список пустой (не было ни одного элемента, 
              //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой список)      
  }
}
```

```java
import java.util.List;

import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        var transformer = new MyDataTransformer();
        
        List<Object> value = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetList("Value of the field 'listValue'", //описание списка, который следует получить
                        transformer, //указываем с помощью какого объекта происходит десериализация
                        Dto.class, //указываем, в объект какого класса должно быть десериализовано тело ответа
                        Dto::listValue) //описание получения списка в виде функции
                .criteria("Описание критерия, которому должен соответствовать " +
                        "каждый элемент, который попадет в результирующий список",
                        o -> /*предикат, как работает критерий*/)
                //можно указать один или несколько критериев, которым должен соответствовать
                //каждый элемент результирующего списка
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или результирующий список пустой (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой список)
    }
}
```

```java
import java.util.List;

import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        var transformer = new MyDataTransformer();

        List<Object> value = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetList("Value of the field 'listValue'", //описание списка, который следует получить
                        transformer, //указываем с помощью какого объекта происходит десериализация
                        new TypeReference<GenericDto<SomeType>>() {}, //указываем, в объект какого типа должно быть 
                        // десериализовано тело ответа
                        GenericDto::listValue) //описание получения списка в виде функции
                .criteria("Описание критерия, которому должен соответствовать " +
                        "каждый элемент, который попадет в результирующий список",
                        o -> /*предикат, как работает критерий*/)
                //можно указать один или несколько критериев, которым должен соответствовать
                //каждый элемент результирующего списка
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или результирующий список пустой (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой список)
    }
}
```

Можно указать значение свойства / переменной окружения [SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_DATA_TRANSFORMER](SETTINGS.MD#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_DATA_TRANSFORMER).
И тогда

```java
import java.util.List;

import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

  @Test
  public void myTest() {

    List<Dto> body = mockMvcGet(response(
            post("/some/path")
                    .contentType(APPLICATION_JSON)
                    .contentType("{\"someString 1\"}"))
            .expectStatus(200)
            .expectContent(containsString("SUCCESS"))
            .thenGetList("List of dto's", //описание списка, который следует получить
                    DtoList.class) //указываем, в объект какого класса должно быть десериализовано тело ответа
            //public class DtoList extends ArrayList<Dto>
            .criteria("Описание критерия, которому должен соответствовать " +
                            "каждый элемент, который попадет в результирующий список",
                    dto -> /*предикат, как работает критерий*/)
            //можно указать один или несколько критериев, которым должен соответствовать
            //каждый элемент результирующего списка
            .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
    // если у ответа нет тела или результирующий список пустой (не было ни одного элемента, 
    //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой список)
  }
}
```

```java
import java.util.List;

import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

  @Test
  public void myTest() {
      
      List<Dto> body = mockMvcGet(response(
              post("/some/path")
                      .contentType(APPLICATION_JSON)
                      .contentType("{\"someString 1\"}"))
              .expectStatus(200)
              .expectContent(containsString("SUCCESS"))
              .thenGetList("List of dto's", //описание списка, который следует получить
                      new TypeReference<List<Dto>>() {
                      }) //указываем, в объект 
                      // какого типа должно быть десериализовано тело ответа
              .criteria("Описание критерия, которому должен соответствовать " + 
                      "каждый элемент, который попадет в результирующий список",
                      dto -> /*предикат, как работает критерий*/)
              //можно указать один или несколько критериев, которым должен соответствовать
              //каждый элемент результирующего списка
              .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
              // если у ответа нет тела или результирующий список пустой (не было ни одного элемента, 
              //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой список)      
  }
}
```

```java
import java.util.List;

import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        
        List<Object> value = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetList("Value of the field 'listValue'", //описание списка, который следует получить
                        Dto.class, //указываем, в объект какого класса должно быть десериализовано тело ответа
                        Dto::listValue) //описание получения списка в виде функции
                .criteria("Описание критерия, которому должен соответствовать " +
                        "каждый элемент, который попадет в результирующий список",
                        o -> /*предикат, как работает критерий*/)
                //можно указать один или несколько критериев, которым должен соответствовать
                //каждый элемент результирующего списка
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или результирующий список пустой (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой список)
    }
}
```

```java
import java.util.List;

import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {

        List<Object> value = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetList("Value of the field 'listValue'", //описание списка, который следует получить
                        new TypeReference<GenericDto<SomeType>>() {
                        }, //указываем, в объект какого типа должно быть 
                        // десериализовано тело ответа
                        GenericDto::listValue) //описание получения списка в виде функции
                .criteria("Описание критерия, которому должен соответствовать " +
                        "каждый элемент, который попадет в результирующий список",
                        o -> /*предикат, как работает критерий*/)
                //можно указать один или несколько критериев, которым должен соответствовать
                //каждый элемент результирующего списка
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или результирующий список пустой (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой список)
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение данных тела ответа в виде массива

[Десериализация](#Десериализация)

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        var transformer = new MyDataTransformer();

        Dto[] body = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetArray("Array of dto's", //описание массива, который следует получить
                        transformer, //указываем с помощью какого объекта происходит десериализация
                        Dto[].class) //указываем, в объект какого класса должно быть десериализовано тело ответа
                .criteria("Описание критерия, которому должен соответствовать " +
                        "каждый элемент, который попадет в результирующий массив", 
                        dto -> /*предикат, как работает критерий*/) 
                //можно указать один или несколько критериев, которым должен соответствовать
                //каждый элемент результирующего массива
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или результирующий массив пустой (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой массив)
    }
}
```

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

  @Test
  public void myTest() {
      var transformer = new MyDataTransformer();
      
      GenericDto<SomeType>[] body = mockMvcGet(response(
              post("/some/path")
                      .contentType(APPLICATION_JSON)
                      .contentType("{\"someString 1\"}"))
              .expectStatus(200)
              .expectContent(containsString("SUCCESS"))
              .thenGetArray("Array of dto's", //описание массива, который следует получить
                      transformer, //указываем с помощью какого объекта происходит десериализация
                      new TypeReference<GenericDto<SomeType>[]>() {}) //указываем, в объект 
                      // какого типа должно быть десериализовано тело ответа
              .criteria("Описание критерия, которому должен соответствовать " + 
                      "каждый элемент, который попадет в результирующий массив",
                      dto -> /*предикат, как работает критерий*/)
              //можно указать один или несколько критериев, которым должен соответствовать
              //каждый элемент результирующего массива
              .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
              // если у ответа нет тела или результирующий массив пустой (не было ни одного элемента, 
              //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой массив)      
  }
}
```

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        var transformer = new MyDataTransformer();
        
        Object[] value = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetArray("Value of the field 'arrayValue'", //описание массива, который следует получить
                        transformer, //указываем с помощью какого объекта происходит десериализация
                        Dto.class, //указываем, в объект какого класса должно быть десериализовано тело ответа
                        Dto::arrayValue) //описание получения массива в виде функции
                .criteria("Описание критерия, которому должен соответствовать " +
                        "каждый элемент, который попадет в результирующий массив",
                        o -> /*предикат, как работает критерий*/)
                //можно указать один или несколько критериев, которым должен соответствовать
                //каждый элемент результирующего массива
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или результирующий массив пустой (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой массив)
    }
}
```

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        var transformer = new MyDataTransformer();

        Object[] value = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetList("Value of the field 'arrayValue'", //описание массива, который следует получить
                        transformer, //указываем с помощью какого объекта происходит десериализация
                        new TypeReference<GenericDto<SomeType>>() {}, //указываем, в объект какого типа должно быть 
                        // десериализовано тело ответа
                        GenericDto::arrayValue) //описание получения массив в виде функции
                .criteria("Описание критерия, которому должен соответствовать " +
                        "каждый элемент, который попадет в результирующий массив",
                        o -> /*предикат, как работает критерий*/)
                //можно указать один или несколько критериев, которым должен соответствовать
                //каждый элемент результирующего массива
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или результирующий массив пустой (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой массив)
    }
}
```

Можно указать значение свойства / переменной окружения [SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_DATA_TRANSFORMER](SETTINGS.MD#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_DATA_TRANSFORMER).
И тогда

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

  @Test
  public void myTest() {

    Dto[] body = mockMvcGet(response(
            post("/some/path")
                    .contentType(APPLICATION_JSON)
                    .contentType("{\"someString 1\"}"))
            .expectStatus(200)
            .expectContent(containsString("SUCCESS"))
            .thenGetArray("Array of dto's", //описание массива, который следует получить
                    Dto[].class) //указываем, в объект какого класса должно быть десериализовано тело ответа
            .criteria("Описание критерия, которому должен соответствовать " +
                            "каждый элемент, который попадет в результирующий массив",
                    dto -> /*предикат, как работает критерий*/)
            //можно указать один или несколько критериев, которым должен соответствовать
            //каждый элемент результирующего массива
            .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
    // если у ответа нет тела или результирующий массив пустой (не было ни одного элемента, 
    //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой массив)
  }
}
```

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

  @Test
  public void myTest() {
      
      GenericDto<SomeType>[] body = mockMvcGet(response(
              post("/some/path")
                      .contentType(APPLICATION_JSON)
                      .contentType("{\"someString 1\"}"))
              .expectStatus(200)
              .expectContent(containsString("SUCCESS"))
              .thenGetArray("Array of dto's", //описание массива, который следует получить
                      new TypeReference<GenericDto<SomeType>[]>() {}) //указываем, в объект 
                      // какого типа должно быть десериализовано тело ответа
              .criteria("Описание критерия, которому должен соответствовать " + 
                      "каждый элемент, который попадет в результирующий массив",
                      dto -> /*предикат, как работает критерий*/)
              //можно указать один или несколько критериев, которым должен соответствовать
              //каждый элемент результирующего массива
              .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
              // если у ответа нет тела или результирующий массив пустой (не было ни одного элемента, 
              //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой массив)      
  }
}
```

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        
        Object[] value = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetArray("Value of the field 'arrayValue'", //описание массива, который следует получить
                        Dto.class, //указываем, в объект какого класса должно быть десериализовано тело ответа
                        Dto::arrayValue) //описание получения массива в виде функции
                .criteria("Описание критерия, которому должен соответствовать " +
                        "каждый элемент, который попадет в результирующий массив",
                        o -> /*предикат, как работает критерий*/)
                //можно указать один или несколько критериев, которым должен соответствовать
                //каждый элемент результирующего массива
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или результирующий массив пустой (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой массив)
    }
}
```

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

  @Test
  public void myTest() {

    Object[] value = mockMvcGet(response(
            post("/some/path")
                    .contentType(APPLICATION_JSON)
                    .contentType("{\"someString 1\"}"))
            .expectStatus(200)
            .expectContent(containsString("SUCCESS"))
            .thenGetList("Value of the field 'arrayValue'", //описание массива, который следует получить
                    new TypeReference<GenericDto<SomeType>>() {
                    }, //указываем, в объект какого типа должно быть 
                    // десериализовано тело ответа
                    GenericDto::arrayValue) //описание получения массив в виде функции
            .criteria("Описание критерия, которому должен соответствовать " +
                            "каждый элемент, который попадет в результирующий массив",
                    o -> /*предикат, как работает критерий*/)
            //можно указать один или несколько критериев, которым должен соответствовать
            //каждый элемент результирующего массива
            .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
    // если у ответа нет тела или результирующий массив пустой (не было ни одного элемента, 
    //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой массив)
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение данных тела ответа как Java-объект из Iterable

[Десериализация](#Десериализация)

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        var transformer = new MyDataTransformer();

        Dto dto = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetValueFromIterable("A value from list of dto's", //описание объекта, который следует получить
                        transformer, //указываем с помощью какого объекта происходит десериализация
                        DtoList.class) //указываем, в объект какого класса должно быть десериализовано тело ответа
                        //public class DtoList extends ArrayList<Dto>
                .criteria("Описание критерия, которому должен соответствовать " +
                        "результирующий элемент из списка", 
                        dto -> /*предикат, как работает критерий*/) 
                //можно указать один или несколько критериев, которому должен соответствовать
                //результирующий элемент из списка
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или не был получен результирующий элемент (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой список)
    }
}
```

```java
import java.util.List;

import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

  @Test
  public void myTest() {
      var transformer = new MyDataTransformer();

      Dto dto = mockMvcGet(response(
              post("/some/path")
                      .contentType(APPLICATION_JSON)
                      .contentType("{\"someString 1\"}"))
              .expectStatus(200)
              .expectContent(containsString("SUCCESS"))
              .thenGetValueFromIterable("A value from list of dto's", //описание объекта, который следует получить
                      transformer, //указываем с помощью какого объекта происходит десериализация
                      new TypeReference<List<Dto>>() {}) //указываем, в объект 
                      // какого типа должно быть десериализовано тело ответа
              .criteria("Описание критерия, которому должен соответствовать " + 
                      "результирующий элемент из списка", 
                      dto -> /*предикат, как работает критерий*/)
              //можно указать один или несколько критериев, которому должен соответствовать
              //результирующий элемент из списка
              .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
              // если у ответа нет тела или не был получен результирующий элемент (не было ни одного элемента, 
              //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой список)    
  }
}
```

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        var transformer = new MyDataTransformer();
        
        Object value = mockMvcGet(response(
                post("/some/path")
                        .contentType(APPLICATION_JSON)
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetValueFromIterable("A value from  the field 'listValue'", //описание объекта, который следует получить
                        transformer, //указываем с помощью какого объекта происходит десериализация
                        Dto.class, //указываем, в объект какого класса должно быть десериализовано тело ответа
                        Dto::listValue) //описание получения списка в виде функции
                .criteria("Описание критерия, которому должен соответствовать " +
                        "результирующий элемент из списка", 
                        o -> /*предикат, как работает критерий*/)
                //можно указать один или несколько критериев, которому должен соответствовать
                //результирующий элемент из списка
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или не был получен результирующий элемент (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой список)   
    }
}
```

```java
import java.util.List;

import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        var transformer = new MyDataTransformer();

        Object value = mockMvcGet(response(
                post("/some/path")
                        .contentType(APPLICATION_JSON)
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetValueFromIterable("A value from  the field 'listValue'", //описание объекта, который следует получить
                        transformer, //указываем с помощью какого объекта происходит десериализация
                        new TypeReference<GenericDto<SomeType>>() {}, //указываем, в объект какого типа должно быть 
                        // десериализовано тело ответа
                        GenericDto::listValue) //описание получения списка в виде функции
                .criteria("Описание критерия, которому должен соответствовать " +
                        "результирующий элемент из списка", 
                        o -> /*предикат, как работает критерий*/)
                //можно указать один или несколько критериев, которому должен соответствовать
                //результирующий элемент из списка
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или не был получен результирующий элемент (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой список)
    }
}
```

Можно указать значение свойства / переменной окружения [SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_DATA_TRANSFORMER](SETTINGS.MD#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_DATA_TRANSFORMER).
И тогда

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        
        Dto dto = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetValueFromIterable("A value from list of dto's", //описание объекта, который следует получить
                        DtoList.class) //указываем, в объект какого класса должно быть десериализовано тело ответа
                        //public class DtoList extends ArrayList<Dto>
                .criteria("Описание критерия, которому должен соответствовать " +
                        "результирующий элемент из списка",  
                        dto -> /*предикат, как работает критерий*/) 
                //можно указать один или несколько критериев, которому должен соответствовать
                //результирующий элемент из списка
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или не был получен результирующий элемент (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой список)
    }
}
```

```java
import java.util.List;

import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

  @Test
  public void myTest() {

      Dto dto = mockMvcGet(response(
              post("/some/path")
                      .contentType(APPLICATION_JSON)
                      .contentType("{\"someString 1\"}"))
              .expectStatus(200)
              .expectContent(containsString("SUCCESS"))
              .thenGetValueFromIterable("A value from list of dto's", //описание объекта, который следует получить
                      new TypeReference<List<Dto>>() {}) //указываем, в объект 
                      // какого типа должно быть десериализовано тело ответа
              .criteria("Описание критерия, которому должен соответствовать " + 
                      "результирующий элемент из списка", 
                      dto -> /*предикат, как работает критерий*/)
              //можно указать один или несколько критериев, которому должен соответствовать
              //результирующий элемент из списка
              .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
              // если у ответа нет тела или не был получен результирующий элемент (не было ни одного элемента, 
              //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой список)    
  }
}
```

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        
        Object value = mockMvcGet(response(
                post("/some/path")
                        .contentType(APPLICATION_JSON)
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetValueFromIterable("A value from  the field 'listValue'", //описание объекта, который следует получить
                        Dto.class, //указываем, в объект какого класса должно быть десериализовано тело ответа
                        Dto::listValue) //описание получения списка в виде функции
                .criteria("Описание критерия, которому должен соответствовать " +
                        "результирующий элемент из списка", 
                        o -> /*предикат, как работает критерий*/)
                //можно указать один или несколько критериев, которому должен соответствовать
                //результирующий элемент из списка
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или не был получен результирующий элемент (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой список)   
    }
}
```

```java
import java.util.List;

import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {

        Object value = mockMvcGet(response(
                post("/some/path")
                        .contentType(APPLICATION_JSON)
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetValueFromIterable("A value from  the field 'listValue'", //описание объекта, который следует получить
                        new TypeReference<GenericDto<SomeType>>() {}, //указываем, в объект какого типа должно быть 
                        // десериализовано тело ответа
                        GenericDto::listValue) //описание получения списка в виде функции
                .criteria("Описание критерия, которому должен соответствовать " +
                        "результирующий элемент из списка", 
                        o -> /*предикат, как работает критерий*/)
                //можно указать один или несколько критериев, которому должен соответствовать
                //результирующий элемент из списка
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или не был получен результирующий элемент (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой список)
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение данных тела ответа как Java-объект из массива

[Десериализация](#Десериализация)

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        var transformer = new MyDataTransformer();

        Dto dto = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetValueFromArray("A value from array of dto's", //описание объекта, который следует получить
                        transformer, //указываем с помощью какого объекта происходит десериализация
                        Dto[].class) //указываем, в объект какого класса должно быть десериализовано тело ответа
                .criteria("Описание критерия, которому должен соответствовать " +
                        "результирующий элемент из массива", 
                        dto -> /*предикат, как работает критерий*/) 
                //можно указать один или несколько критериев, которому должен соответствовать
                //результирующий элемент из массива
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или не был получен результирующий элемент (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой массив)
    }
}
```

```java
import java.util.List;

import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

  @Test
  public void myTest() {
      var transformer = new MyDataTransformer();

      GenericDto<SomeType> dto = mockMvcGet(response(
              post("/some/path")
                      .contentType(APPLICATION_JSON)
                      .contentType("{\"someString 1\"}"))
              .expectStatus(200)
              .expectContent(containsString("SUCCESS"))
              .thenGetValueFromArray("A value from array of dto's", //описание объекта, который следует получить
                      transformer, //указываем с помощью какого объекта происходит десериализация
                      new TypeReference<GenericDto<SomeType>[]>() {}) //указываем, в объект 
                      // какого типа должно быть десериализовано тело ответа
              .criteria("Описание критерия, которому должен соответствовать " + 
                      "результирующий элемент из массива", 
                      dto -> /*предикат, как работает критерий*/)
              //можно указать один или несколько критериев, которому должен соответствовать
              //результирующий элемент из массива
              .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
              // если у ответа нет тела или не был получен результирующий элемент (не было ни одного элемента, 
              //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой массив)    
  }
}
```

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        var transformer = new MyDataTransformer();
        
        Object value = mockMvcGet(response(
                post("/some/path")
                        .contentType(APPLICATION_JSON)
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetValueFromArray("A value from  the field 'arrayValue'", //описание объекта, который следует получить
                        transformer, //указываем с помощью какого объекта происходит десериализация
                        Dto.class, //указываем, в объект какого класса должно быть десериализовано тело ответа
                        Dto::arrayValue) //описание получения массива в виде функции
                .criteria("Описание критерия, которому должен соответствовать " +
                        "результирующий элемент из массива", 
                        o -> /*предикат, как работает критерий*/)
                //можно указать один или несколько критериев, которому должен соответствовать
                //результирующий элемент из массива
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или не был получен результирующий элемент (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой массив)   
    }
}
```

```java
import java.util.List;

import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        var transformer = new MyDataTransformer();

        Object value = mockMvcGet(response(
                post("/some/path")
                        .contentType(APPLICATION_JSON)
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetValueFromArray("A value from  the field 'arrayValue'", //описание объекта, который следует получить
                        transformer, //указываем с помощью какого объекта происходит десериализация
                        new TypeReference<GenericDto<SomeType>>() {}, //указываем, в объект какого типа должно быть 
                        // десериализовано тело ответа
                        GenericDto::listValue) //описание получения массива в виде функции
                .criteria("Описание критерия, которому должен соответствовать " +
                        "результирующий элемент из массива", 
                        o -> /*предикат, как работает критерий*/)
                //можно указать один или несколько критериев, которому должен соответствовать
                //результирующий элемент из массива
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или не был получен результирующий элемент (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой массив)
    }
}
```

Можно указать значение свойства / переменной окружения [SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_DATA_TRANSFORMER](SETTINGS.MD#SPRING_MOCK_MVC_DEFAULT_RESPONSE_BODY_DATA_TRANSFORMER).
И тогда

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {

        Dto dto = mockMvcGet(response(
                post("/some/path") 
                        .contentType(APPLICATION_JSON) 
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetValueFromArray("A value from array of dto's", //описание объекта, который следует получить
                        transformer, //указываем с помощью какого объекта происходит десериализация
                        Dto[].class) //указываем, в объект какого класса должно быть десериализовано тело ответа
                .criteria("Описание критерия, которому должен соответствовать " +
                        "результирующий элемент из массива", 
                        dto -> /*предикат, как работает критерий*/) 
                //можно указать один или несколько критериев, которому должен соответствовать
                //результирующий элемент из массива
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или не был получен результирующий элемент (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой массив)
    }
}
```

```java
import java.util.List;

import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

  @Test
  public void myTest() {

      GenericDto<SomeType> dto = mockMvcGet(response(
              post("/some/path")
                      .contentType(APPLICATION_JSON)
                      .contentType("{\"someString 1\"}"))
              .expectStatus(200)
              .expectContent(containsString("SUCCESS"))
              .thenGetValueFromArray("A value from array of dto's", //описание объекта, который следует получить
                      transformer, //указываем с помощью какого объекта происходит десериализация
                      new TypeReference<GenericDto<SomeType>[]>() {}) //указываем, в объект 
                      // какого типа должно быть десериализовано тело ответа
              .criteria("Описание критерия, которому должен соответствовать " + 
                      "результирующий элемент из массива", 
                      dto -> /*предикат, как работает критерий*/)
              //можно указать один или несколько критериев, которому должен соответствовать
              //результирующий элемент из массива
              .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
              // если у ответа нет тела или не был получен результирующий элемент (не было ни одного элемента, 
              //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой массив)    
  }
}
```

```java
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test
    public void myTest() {
        
        Object value = mockMvcGet(response(
                post("/some/path")
                        .contentType(APPLICATION_JSON)
                        .contentType("{\"someString 1\"}"))
                .expectStatus(200)
                .expectContent(containsString("SUCCESS"))
                .thenGetValueFromArray("A value from  the field 'arrayValue'", //описание объекта, который следует получить
                        transformer, //указываем с помощью какого объекта происходит десериализация
                        Dto.class, //указываем, в объект какого класса должно быть десериализовано тело ответа
                        Dto::arrayValue) //описание получения массива в виде функции
                .criteria("Описание критерия, которому должен соответствовать " +
                        "результирующий элемент из массива", 
                        o -> /*предикат, как работает критерий*/)
                //можно указать один или несколько критериев, которому должен соответствовать
                //результирующий элемент из массива
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела или не был получен результирующий элемент (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой массив)   
    }
}
```

```java
import java.util.List;

import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

  @Test
  public void myTest() {

    Object value = mockMvcGet(response(
            post("/some/path")
                    .contentType(APPLICATION_JSON)
                    .contentType("{\"someString 1\"}"))
            .expectStatus(200)
            .expectContent(containsString("SUCCESS"))
            .thenGetValueFromArray("A value from  the field 'arrayValue'", //описание объекта, который следует получить
                    transformer, //указываем с помощью какого объекта происходит десериализация
                    new TypeReference<GenericDto<SomeType>>() {}, //указываем, в объект какого типа должно быть 
                    // десериализовано тело ответа
                    GenericDto::listValue) //описание получения массива в виде функции
            .criteria("Описание критерия, которому должен соответствовать " +
                            "результирующий элемент из массива",
                    o -> /*предикат, как работает критерий*/)
            //можно указать один или несколько критериев, которому должен соответствовать
            //результирующий элемент из массива
            .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
            // если у ответа нет тела или не был получен результирующий элемент (не было ни одного элемента, 
            //который бы соответствовал перечисленным критериям, или тело десериализовалось в пустой массив)
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
