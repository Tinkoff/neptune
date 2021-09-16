# Отправка http запросов/получение ответов

Ниже примеры того, как происходит взаимодействие с тестируемыми API через модели Retrofit.

## Оглавление
- [Вступление](#Вступление)
- [Объявление полей типов-моделей API](#Объявление-полей-типов-моделей-API)
- [Получение результата вызова http-метода](#Получение-результата-вызова-http-метода)
  - [Получение данных тела ответа как Java-объект](#Получение-данных-тела-ответа-как-Java-объект)
    - [Получение данных тела ответа как Java-объект. Call](#Получение-данных-тела-ответа-как-Java-объект-Call])
    - [Получение данных тела ответа как Java-объект. Случай без использования Call](#Получение-данных-тела-ответа-как-Java-объект-Случай-без-использования-Call)
  - [Получение данных тела ответа как Iterable](#Получение-данных-тела-ответа-как-iterable)
    - [Получение данных тела ответа как Iterable. Call](#Получение-данных-тела-ответа-как-Iterable-Call)
    - [Получение данных тела ответа как Iterable. Случай без использования Call](#Получение-данных-тела-ответа-как-Iterable-Случай-без-использования-Call)
  - [Получение данных тела ответа в виде массива](#Получение-данных-тела-ответа-в-виде-массива)
    - [Получение данных тела ответа в виде массива. Call](#Получение-данных-тела-ответа-в-виде-массива-Call)
    - [Получение данных тела ответа в виде массива. Случай без использования Call](#Получение-данных-тела-ответа-в-виде-массива-Случай-без-использования-Call)
  - [Получение данных тела ответа как Java-объект из Iterable](#Получение-данных-тела-ответа-как-Java-объект-из-Iterable)
    - [Получение данных тела ответа как Java-объект из Iterable. Call](#Получение-данных-тела-ответа-как-Java-объект-из-Iterable-Call)
    - [Получение данных тела ответа как Java-объект из Iterable. Случай без использования Call](#Получение-данных-тела-ответа-как-Java-объект-из-Iterable-Случай-без-использования-Call)
  - [Получение данных тела ответа как Java-объект из массива](#Получение-данных-тела-ответа-как-Java-объект-из-массива)
    - [Получение данных тела ответа как Java-объект из массива. Call](#Получение-данных-тела-ответа-как-Java-объект-из-массива-Call)
    - [Получение данных тела ответа как Java-объект из массива. Случай без использования Call](#Получение-данных-тела-ответа-как-Java-объект-из-массива-Случай-без-использования-Call)

## Вступление

Для примеров используется следующее описание Dto:

```java
package org.my.pack;

import java.util.Map;

public class DtoObject {
    
    private Map<?, ?> object;
    
    //далее прочие поля
    

    //и прочие геттеры/сеттеры
    
    public Map<?, ?> getObject() {
        return object;
    }

    public DtoObject setObject(Map<?, ?> object) {
        this.object = object;
        return this;
    }
}
```

Для примеров используются следующие описания API:

- обычное для Retrofit2

```java
package org.my.pack;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface CallService {

    @GET("/get/json")
    Call<List<DtoObject>> getJson();

    @GET("/get/array/json")
    Call<DtoObject[]> getJsonArray();
}
```

- вариант, требующий особенной реализации интерфейса [CallAdapter.Factory](https://square.github.io/retrofit/2.x/retrofit/retrofit2/CallAdapter.Factory.htmll),
  которую нужно использовать по [умолчанию](SETTINGS.MD#DEFAULT_RETROFIT) или [специально](SETTINGS.MD#Недефолтные-настройки-Retrofit) для примера ниже

```java
package org.my.pack;

import retrofit2.http.GET;

import java.util.List;

public interface CallAdaptedService {

    @GET("/get/json")
    List<DtoObject> getJson();

    @GET("/get/array/json")
    DtoObject[] getJsonArray();
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Объявление полей типов-моделей API 

Основная статья - [внедрения зависимостей](./../../../core.api/doc/rus/DEPENDENCY_INJECTION.MD).

Модули, интегрирующие Neptune c различными фреймворками тестирования, имеют возможности использования механизмов внедрения
зависимостей:

- [Testng](./../../../testng.integration/doc/rus/README.MD)
- [JUnit5](./../../../jupiter.integration/doc/rus/README.MD)

Интеграция Neptune и Retrofit2 имеет свой механизм внедрения зависимостей, позволяющий инициализировать объекты классов, 
моделирующих API тестируемых приложений.

```java
import org.my.pack.*;
import ru.tinkoff.qa.neptune.retrofit2.service.setup.ApiService;

public class MyTest {
    
    @ApiService //присутствие данной аннотации означает
    //что поле будет проинициализировано как объект интерфейса, моделирующего API средствами Retrofit.
    //Для того, чтобы поле было проинициализировано корректно, необходимо указать настройки параметров
    //Retrofit и URL хоста, где задеплоен тестируемы Web-сервис
    private CallService service1;

    @UseRetrofitSettings(SyncAdaptorRetrofitBuilderSupplier.class) //данная аннотация нужна, если 
    //именно для этого теста нужно использовать специфически настроенный Http-клиент и/или Retrofit. 
    @ApiService
    private CallAdaptedService service2;
    
    //более ничего делать не надо
    @Test
    public void test() {
        //логика теста
    }
}
```

Полезные статьи:
- [Настройки для интеграции с Retrofit](./SETTINGS.MD)
- [Механизм работы со свойствами/переменными окружения / среды](./../../../core.api/doc/rus/SETTINGS.MD)

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Получение результата вызова http-метода

О принципах работы шагов, которые возвращают результат, можно прочитать 
[здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги-которые-возвращают-результат).

Описанный ниже механизм учитывает возможную необходимость:

- извлечь данные тела ответа, соответствующие определенным критериям
- извлечь данные тела ответа, который соответствует определенным критериям
- учитывать, что нужный ответ может быть получен не зразу, а спустя некоторое время (тестируемый back-end работает не 
  синхронно с тестирующим его кодом)

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение данных тела ответа как Java-объект

О критериях можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Критерии).

[Критерии для получения подходящего http ответа](https://tinkoffcreditsystems.github.io/neptune/retrofit2/ru/tinkoff/qa/neptune/retrofit2/criteria/ResponseCriteria.html)

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

#### Получение данных тела ответа как Java-объект. Call

```java
import java.util.List;
import org.my.pack.*;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectSupplier.*;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

public class MyTest {

    @ApiService
    private CallService service;

    @Test
    public void myTest() {
        //Возвращается тело ответа на запрос. Http ответ и его тело должны подходить под указанные критерии.
        List<DtoObject> result = retrofit().get(
                callBody(() -> callService.getJson()) //тут описывается вызов метода API
                //можно указать критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //можно указать критерий, которому должно соответствовать тело ответа
                .criteria("Количество элементов == 2", r -> r.size() == 2)
                //можно указать время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными
                //т.е. со статусом=200 и с телом подходящим под критерий
                .retryTimeOut(ofSeconds(10))
                //можно указать интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //можно указать, что должно быть выброшено исключение, если результат не был получен
                .throwOnNoResult());
    }
}
```

```java
import java.util.List;
import java.util.Objects;
import org.my.pack.*;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectSupplier.*;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

public class MyTest {

    @ApiService
    private CallService service;

    @Test
    public void myTest() {
        //Возвращается объект, получаемый из тела ответа или вычисляемый с использованием данных этого тела.
        //Http ответ и вычисленный объект должны подходить под указанные критерии. 
        List<Object> result = retrofit().get(callObject("Список значений из полей objects по ключу 'a'", //описываем 
                // то, что нужно получить
                () -> callService.getJson(), //тут описывается вызов метода API
                dtos -> dtos.stream() //алгоритм получения результирующего объекта
                        .map(dto -> dto.getObject().get("a"))
                        .filter(Objects::nonNull)
                        .collect(toList()))
                //можно указать критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //можно указать критерий тела ожидаемого ответа
                .callBodyCriteria("Количество элементов десериализованного тела ответа == 2", dtoObjects -> dtoObjects.size() == 2)
                //можно указать критерий, которому должен соответствовать полученный объект
                .criteria("Количество элементов == 2", r -> r.size() == 2)
                //можно указать время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными,
                //т.е. статус ответа == 200, и полученный/вычисленный с помощью тела ответа объект
                // подходит под указанный критерий
                .retryTimeOut(ofSeconds(10))
                //можно указать интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //можно указать, что должно быть выброшено исключение, если результат не был получен
                .throwOnNoResult());
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

#### Получение данных тела ответа как Java-объект. Случай без использования Call

```java
import java.util.List;
import org.my.pack.*;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectSupplier.*;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

public class MyTest {

    @ApiService
    private CallAdaptedService service;

    @Test
    public void myTest() {
        //Возвращается тело ответа на запрос. Http ответ и его тело должны подходить под указанные критерии.
        List<DtoObject> result = retrofit().get(
                body(() -> callService.getJson()) //тут описывается вызов метода API
                //можно указать критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //можно указать критерий, которому должно соответствовать тело ответа
                .criteria("Количество элементов == 2", r -> r.size() == 2)
                //можно указать время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными
                //т.е. со статусом=200 и с телом подходящим под критерий
                .retryTimeOut(ofSeconds(10))
                //можно указать интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //можно указать, что должно быть выброшено исключение, если результат не был получен
                .throwOnNoResult());
    }
}
```

```java
import java.util.List;
import java.util.Objects;
import org.my.pack.*;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectSupplier.*;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

public class MyTest {

    @ApiService
    private CallAdaptedService service;

    @Test
    public void myTest() {
        //Возвращается объект, получаемый из тела ответа или вычисляемый с использованием данных этого тела.
        //Http ответ и вычисленный объект должны подходить под указанные критерии. 
        List<Object> result = retrofit().get(object("Список значений из полей objects по ключу 'a'", //описываем то, 
                // что нужно получить
                () -> callService.getJson(), //тут описывается вызов метода API
                dtos -> dtos.stream() //алгоритм получения результирующего объекта
                        .map(dto -> dto.getObject().get("a"))
                        .filter(Objects::nonNull)
                        .collect(toList()))
                //можно указать критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //можно указать критерий тела ожидаемого ответа
                .callBodyCriteria("Количество элементов десериализованного тела ответа == 2", dtoObjects -> dtoObjects.size() == 2)
                //можно указать критерий, которому должен соответствовать полученный объект
                .criteria("Количество элементов == 2", r -> r.size() == 2)
                //можно указать время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными,
                //т.е. статус ответа == 200, и полученный/вычисленный с помощью тела ответа объект
                // подходит под указанный критерий
                .retryTimeOut(ofSeconds(10))
                //можно указать интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //можно указать, что должно быть выброшено исключение, если результат не был получен
                .throwOnNoResult());
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение данных тела ответа как Iterable

О критериях можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Критерии).

[Критерии для получения подходящего http ответа](https://tinkoffcreditsystems.github.io/neptune/retrofit2/ru/tinkoff/qa/neptune/retrofit2/criteria/ResponseCriteria.html)

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

#### Получение данных тела ответа как Iterable. Call

```java
import java.util.List;
import org.my.pack.*;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetIterableSupplier.*;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

public class MyTest {

    @ApiService
    private CallService service;

    @Test
    public void myTest() {
        //Возвращается Iterable или коллекция. Тело является коллекцией или объектом Iterable.
        //Http ответ и каждый элемент результирующей коллекции должны подходить под указанные критерии.
        //Из общего тела (Iterable/коллекции) вернется под-множество подходящих элементов.
        List<DtoObject> result = retrofit().get(callIterable("Результирующий лист", //описываем то, 
                // что нужно получить
                () -> callService.getJson()) //тут описывается вызов метода API
                //можно указать критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //можно указать критерий, которому должен соответствовать каждый элемент результирующей коллекции
                .criteria("Число элементов значения поля 'object' == 2", r -> r.getObject().size() == 2)
                //можно указать время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными
                //т.е. со статусом=200 и непустой коллекцией объектов, соответствующих переданному критерию
                .retryTimeOut(ofSeconds(10))
                //можно указать интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //можно указать, что должно быть выброшено исключение, если результат не был получен
                .throwOnNoResult());
    }
}
```

```java
import java.util.List;
import java.util.Objects;
import org.my.pack.*;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetIterableSupplier.*;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

public class MyTest {

    @ApiService
    private CallService service;

    @Test
    public void myTest() {
        // Возвращается Iterable или коллекция. Тело не обязательно является коллекцией 
        // или объектом Iterable. Промежуточная коллекция или Iterable получаются в ходе дополнительных 
        // вычислений. Http ответ и каждый элемент результирующей коллекции должны подходить под указанные критерии. 
        // Из коллекции вернется под-множество подходящих элементов.
        List<Object> result = retrofit().get(callIterable("Значения из полей objects по ключу 'a'", //описываем то, 
                // что нужно получить
                () -> callService.getJson(), //тут описывается вызов метода API
                dtos -> dtos.stream() //алгоритм получения результирующей коллекции
                        .map(dto -> dto.getObject().get("a"))
                        .filter(Objects::nonNull)
                        .collect(toList()))
                //можно указать критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //можно указать критерий тела ожидаемого ответа
                .callBodyCriteria("Количество элементов десериализованного тела ответа == 2", dtoObjects -> dtoObjects.size() == 2)
                //можно указать критерий, которому должен соответствовать каждый элемент результирующей коллекции
                .criteria("Строковое значение объекта начинается на 'a'",
                        o -> String.valueOf(o).startsWith("a"))
                //можно указать время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными
                //т.е. со статусом=200 и непустой коллекцией объектов, соответствующих переданному критерию
                .retryTimeOut(ofSeconds(10))
                //можно указать интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //можно указать, что должно быть выброшено исключение, если результат не был получен
                .throwOnNoResult());
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

#### Получение данных тела ответа как Iterable. Случай без использования Call

```java
import java.util.List;
import org.my.pack.*;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetIterableSupplier.*;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

public class MyTest {

  @ApiService
  private CallAdaptedService service;

  @Test
  public void myTest() {
    //Возвращается Iterable или коллекция. Тело является коллекцией или объектом Iterable.
    //Http ответ и каждый элемент результирующей коллекции должны подходить под указанные критерии.
    //Из общего тела (Iterable/коллекции) вернется под-множество подходящих элементов.
    List<DtoObject> result = retrofit().get(iterable("Результирующий лист", //описываем то, 
            // что нужно получить
            () -> callService.getJson()) //тут описывается вызов метода API
            //можно указать критерий ожидаемого ответа, т.е. код статуса должен быть 200
            .responseCriteria(statusCode(200))
            //можно указать критерий, которому должен соответствовать каждый элемент результирующей коллекции
            .criteria("Число элементов значения поля 'object' == 2", r -> r.getObject().size() == 2)
            //можно указать время на получение ожидаемого результата = 10 секунд
            //В течение этих 10 секунд запрос будет отправляться до тех пор,
            //пока не вернется ожидаемый ответ с ожидаемыми данными
            //т.е. со статусом=200 и непустой коллекцией объектов, соответствующих переданному критерию
            .retryTimeOut(ofSeconds(10))
            //можно указать интервал между попытками получить результат в пределах указанных 10 сек
            .pollingInterval(ofMillis(500))
            //можно указать, что должно быть выброшено исключение, если результат не был получен
            .throwOnNoResult());
  }
}
```


```java
import java.util.List;
import java.util.Objects;
import org.my.pack.*;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetIterableSupplier.*;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

public class MyTest {

  @ApiService
  private CallAdaptedService service;

  @Test
  public void myTest() {
    // Возвращается Iterable или коллекция. Тело не обязательно является коллекцией 
    // или объектом Iterable. Промежуточная коллекция или Iterable получаются в ходе дополнительных 
    // вычислений. Http ответ и каждый элемент результирующей коллекции должны подходить под указанные критерии. 
    // Из коллекции вернется под-множество подходящих элементов.
    List<Object> result = retrofit().get(iterable("Значения из полей objects по ключу 'a'", //описываем то, 
            // что нужно получить
            () -> callService.getJson(), //тут описывается вызов метода API
            dtos -> dtos.stream() //алгоритм получения результирующей коллекции
                    .map(dto -> dto.getObject().get("a"))
                    .filter(Objects::nonNull)
                    .collect(toList()))
            //можно указать критерий ожидаемого ответа, т.е. код статуса должен быть 200
            .responseCriteria(statusCode(200))
            //можно указать критерий тела ожидаемого ответа
            .callBodyCriteria("Количество элементов десериализованного тела ответа == 2", dtoObjects -> dtoObjects.size() == 2)
            //можно указать критерий, которому должен соответствовать каждый элемент результирующей коллекции
            .criteria("Строковое значение объекта начинается на 'a'",
                    o -> String.valueOf(o).startsWith("a"))
            //можно указать время на получение ожидаемого результата = 10 секунд
            //В течение этих 10 секунд запрос будет отправляться до тех пор,
            //пока не вернется ожидаемый ответ с ожидаемыми данными
            //т.е. со статусом=200 и непустой коллекцией объектов, соответствующих переданному критерию
            .retryTimeOut(ofSeconds(10))
            //можно указать интервал между попытками получить результат в пределах указанных 10 сек
            .pollingInterval(ofMillis(500))
            //можно указать, что должно быть выброшено исключение, если результат не был получен
            .throwOnNoResult());
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение данных тела ответа в виде массива

О критериях можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Критерии).

[Критерии для получения подходящего http ответа](https://tinkoffcreditsystems.github.io/neptune/retrofit2/ru/tinkoff/qa/neptune/retrofit2/criteria/ResponseCriteria.html)

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

#### Получение данных тела ответа в виде массива. Call

```java
import org.my.pack.*;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetArraySupplier.*;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

public class MyTest {

    @ApiService
    private CallService service;

    @Test
    public void myTest() {
        //Возвращается массив. Тело является массивом.
        //Http ответ и каждый элемент результирующего массива должны подходить под указанные критерии.
        //Из общего тела - массива вернется под-массив подходящих элементов.
        DtoObject[] result = retrofit().get(callArray("Результирующий массив", //описываем то, 
                // что нужно получить
                () -> callService.getJsonArray()) //тут описывается вызов метода API
                //можно указать критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //можно указать критерий, которому должен соответствовать каждый элемент результирующего массива
                .criteria("Число элементов значения поля 'object' == 2", r -> r.getObject().size() == 2)
                //можно указать время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными
                //т.е. со статусом=200 и непустым массивом объектов, соответствующих переданному критерию
                .retryTimeOut(ofSeconds(10))
                //можно указать интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //можно указать, что должно быть выброшено исключение, если результат не был получен
                .throwOnNoResult());
    }
}
```

```java
import java.util.Objects;
import org.my.pack.*;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetArraySupplier.*;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

public class MyTest {

    @ApiService
    private CallService service;

    @Test
    public void myTest() {
        // Возвращается массив. Тело не обязательно является массивом. 
        // Промежуточный массив получается в ходе дополнительных 
        // вычислений. Http ответ и каждый элемент результирующего массива должны подходить под указанные критерии. 
        // Из массива вернется под-массив подходящих элементов.
        Object[] result = retrofit().get(callArray("Значения из полей objects по ключу 'a'", //описываем то, 
                // что нужно получить
                () -> callService.getJson(), //тут описывается вызов метода API
                dtos -> dtos.stream() //алгоритм получения результирующего массива
                        .map(dto -> dto.getObject().get("a"))
                        .filter(Objects::nonNull)
                        .toArray())
                //можно указать критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //можно указать критерий тела ожидаемого ответа
                .callBodyCriteria("Количество элементов десериализованного тела ответа == 2", dtoObjects -> dtoObjects.size() == 2)
                //можно указать критерий, которому должен соответствовать каждый элемент результирующего массива
                .criteria("Строковое значение объекта начинается на 'a'",
                        o -> String.valueOf(o).startsWith("a"))
                //можно указать время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными
                //т.е. со статусом=200 и непустым массивом объектов, соответствующих переданному критерию
                .retryTimeOut(ofSeconds(10))
                //можно указать интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //можно указать, что должно быть выброшено исключение, если результат не был получен
                .throwOnNoResult());
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

#### Получение данных тела ответа в виде массива. Случай без использования Call

```java
import org.my.pack.*;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetArraySupplier.*;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

public class MyTest {

  @ApiService
  private CallAdaptedService service;

  @Test
  public void myTest() {
    //Возвращается массив. Тело является массивом.
    //Http ответ и каждый элемент результирующего массива должны подходить под указанные критерии.
    //Из общего тела - массива вернется под-массив подходящих элементов.
    DtoObject[] result = retrofit().get(array("Результирующий массив", //описываем то, 
            // что нужно получить
            () -> callService.getJsonArray()) //тут описывается вызов метода API
            //можно указать критерий ожидаемого ответа, т.е. код статуса должен быть 200
            .responseCriteria(statusCode(200))
            //можно указать критерий, которому должен соответствовать каждый элемент результирующего массива
            .criteria("Число элементов значения поля 'object' == 2", r -> r.getObject().size() == 2)
            //можно указать время на получение ожидаемого результата = 10 секунд
            //В течение этих 10 секунд запрос будет отправляться до тех пор,
            //пока не вернется ожидаемый ответ с ожидаемыми данными
            //т.е. со статусом=200 и непустым массивом объектов, соответствующих переданному критерию
            .retryTimeOut(ofSeconds(10))
            //можно указать интервал между попытками получить результат в пределах указанных 10 сек
            .pollingInterval(ofMillis(500))
            //можно указать, что должно быть выброшено исключение, если результат не был получен
            .throwOnNoResult());
  }
}
```


```java
import java.util.Objects;
import org.my.pack.*;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetArraySupplier.*;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

public class MyTest {

  @ApiService
  private CallAdaptedService service;

  @Test
  public void myTest() {
    // Возвращается массив. Тело не обязательно является массивом. 
    // Промежуточный массив получается в ходе дополнительных 
    // вычислений. Http ответ и каждый элемент результирующего массива должны подходить под указанные критерии. 
    // Из массива вернется под-массив подходящих элементов.
    Object[] result = retrofit().get(array("Значения из полей objects по ключу 'a'", //описываем то, 
            // что нужно получить
            () -> callService.getJson(), //тут описывается вызов метода API
            dtos -> dtos.stream() //алгоритм получения результирующего массива
                    .map(dto -> dto.getObject().get("a"))
                    .filter(Objects::nonNull)
                    .toArray())
            //можно указать критерий ожидаемого ответа, т.е. код статуса должен быть 200
            .responseCriteria(statusCode(200))
            //можно указать критерий тела ожидаемого ответа
            .callBodyCriteria("Количество элементов десериализованного тела ответа == 2", dtoObjects -> dtoObjects.size() == 2)
            //можно указать критерий, которому должен соответствовать каждый элемент результирующего массива
            .criteria("Строковое значение объекта начинается на 'a'",
                    o -> String.valueOf(o).startsWith("a"))
            //можно указать время на получение ожидаемого результата = 10 секунд
            //В течение этих 10 секунд запрос будет отправляться до тех пор,
            //пока не вернется ожидаемый ответ с ожидаемыми данными
            //т.е. со статусом=200 и непустым массивом объектов, соответствующих переданному критерию
            .retryTimeOut(ofSeconds(10))
            //можно указать интервал между попытками получить результат в пределах указанных 10 сек
            .pollingInterval(ofMillis(500))
            //можно указать, что должно быть выброшено исключение, если результат не был получен
            .throwOnNoResult());
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение данных тела ответа как Java-объект из Iterable

О критериях можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Критерии).

[Критерии для получения подходящего http ответа](https://tinkoffcreditsystems.github.io/neptune/retrofit2/ru/tinkoff/qa/neptune/retrofit2/criteria/ResponseCriteria.html)

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

#### Получение данных тела ответа как Java-объект из Iterable. Call

```java
import org.my.pack.*;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectFromIterableSupplier.*;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

public class MyTest {

    @ApiService
    private CallService service;

    @Test
    public void myTest() {
        //Возвращается элемент коллекции / Iterable. Тело является коллекцией или объектом Iterable.
        //Http ответ и хотя бы один из элементов коллекция/Iterable должны подходить под указанные критерии.
        //Из общего тела (Iterable/коллекции) вернется первый подходящий элемент.
        DtoObject result = retrofit().get(callIterableItem("Нужный элемент листа", //описываем то, 
                // что нужно получить
                () -> callService.getJson()) //тут описывается вызов метода API
                //можно указать критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //можно указать критерий, которому должен соответствовать результирующий элемент из коллекции
                .criteria("Число элементов значения поля 'object' == 2", r -> r.getObject().size() == 2)
                //можно указать время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными
                //т.е. со статусом=200 и непустой коллекцией объектов, в которой хотя бы один элемент 
                //соответствует переданному критерию
                .retryTimeOut(ofSeconds(10))
                //можно указать интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //можно указать, что должно быть выброшено исключение, если результат не был получен
                .throwOnNoResult());
    }
}
```

```java
import java.util.Objects;
import org.my.pack.*;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectFromIterableSupplier.*;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

public class MyTest {

    @ApiService
    private CallService service;

    @Test
    public void myTest() {
        // Возвращается элемент коллекции / Iterable. Тело не обязательно является коллекцией 
        // или объектом Iterable. Промежуточная коллекция или Iterable получаются в ходе дополнительных 
        // вычислений. Http ответ и хотя бы один из элементов коллекция/Iterable должны подходить под указанные 
        // критерии. Из коллекции вернется первый подходящий элемент.
        Object result = retrofit().get(callIterableItem("Значение из полей objects по ключу 'a'", //описываем то, 
                // что нужно получить
                () -> callService.getJson(), //тут описывается вызов метода API
                dtos -> dtos.stream() //алгоритм получения результирующей коллекции
                        .map(dto -> dto.getObject().get("a"))
                        .filter(Objects::nonNull)
                        .collect(toList()))
                //можно указать критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //можно указать критерий тела ожидаемого ответа
                .callBodyCriteria("Количество элементов десериализованного тела ответа == 2", dtoObjects -> dtoObjects.size() == 2)
                //можно указать критерий, которому должен соответствовать результирующий элемент из коллекции
                .criteria("Строковое значение объекта начинается на 'a'",
                        o -> String.valueOf(o).startsWith("a"))
                //можно указать время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными
                //т.е. со статусом=200 и непустой коллекцией объектов, в которой хотя бы один элемент 
                //соответствует переданному критерию
                .retryTimeOut(ofSeconds(10))
                //можно указать интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //можно указать, что должно быть выброшено исключение, если результат не был получен
                .throwOnNoResult());
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

#### Получение данных тела ответа как Java-объект из Iterable. Случай без использования Call

```java
import org.my.pack.*;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectFromIterableSupplier.*;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

public class MyTest {

  @ApiService
  private CallAdaptedService service;

  @Test
  public void myTest() {
    //Возвращается элемент коллекции / Iterable. Тело является коллекцией или объектом Iterable.
    //Http ответ и хотя бы один из элементов коллекция/Iterable должны подходить под указанные критерии.
    //Из общего тела (Iterable/коллекции) вернется первый подходящий элемент.
    DtoObject result = retrofit().get(iterableItem("Нужный элемент листа", //описываем то, 
            // что нужно получить
            () -> callService.getJson()) //тут описывается вызов метода API
            //можно указать критерий ожидаемого ответа, т.е. код статуса должен быть 200
            .responseCriteria(statusCode(200))
            //можно указать критерий, которому должен соответствовать результирующий элемент из коллекции
            .criteria("Число элементов значения поля 'object' == 2", r -> r.getObject().size() == 2)
            //можно указать время на получение ожидаемого результата = 10 секунд
            //В течение этих 10 секунд запрос будет отправляться до тех пор,
            //пока не вернется ожидаемый ответ с ожидаемыми данными
            //т.е. со статусом=200 и непустой коллекцией объектов, в которой хотя бы один элемент 
            //соответствует переданному критерию
            .retryTimeOut(ofSeconds(10))
            //можно указать интервал между попытками получить результат в пределах указанных 10 сек
            .pollingInterval(ofMillis(500))
            //можно указать, что должно быть выброшено исключение, если результат не был получен
            .throwOnNoResult());
  }
}
```

```java
import java.util.Objects;
import org.my.pack.*;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectFromIterableSupplier.*;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

public class MyTest {

  @ApiService
  private CallAdaptedService service;

  @Test
  public void myTest() {
    // Возвращается элемент коллекции / Iterable. Тело не обязательно является коллекцией 
    // или объектом Iterable. Промежуточная коллекция или Iterable получаются в ходе дополнительных 
    // вычислений. Http ответ и хотя бы один из элементов коллекция/Iterable должны подходить под указанные 
    // критерии. Из коллекции вернется первый подходящий элемент.
    Object result = retrofit().get(iterableItem("Значение из полей objects по ключу 'a'", //описываем то, 
            // что нужно получить
            () -> callService.getJson(), //тут описывается вызов метода API
            dtos -> dtos.stream() //алгоритм получения результирующей коллекции
                    .map(dto -> dto.getObject().get("a"))
                    .filter(Objects::nonNull)
                    .collect(toList()))
            //можно указать критерий ожидаемого ответа, т.е. код статуса должен быть 200
            .responseCriteria(statusCode(200))
            //можно указать критерий тела ожидаемого ответа
            .callBodyCriteria("Количество элементов десериализованного тела ответа == 2", dtoObjects -> dtoObjects.size() == 2)
            //можно указать критерий, которому должен соответствовать результирующий элемент из коллекции
            .criteria("Строковое значение объекта начинается на 'a'",
                    o -> String.valueOf(o).startsWith("a"))
            //можно указать время на получение ожидаемого результата = 10 секунд
            //В течение этих 10 секунд запрос будет отправляться до тех пор,
            //пока не вернется ожидаемый ответ с ожидаемыми данными
            //т.е. со статусом=200 и непустой коллекцией объектов, в которой хотя бы один элемент 
            //соответствует переданному критерию
            .retryTimeOut(ofSeconds(10))
            //можно указать интервал между попытками получить результат в пределах указанных 10 сек
            .pollingInterval(ofMillis(500))
            //можно указать, что должно быть выброшено исключение, если результат не был получен
            .throwOnNoResult());
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение данных тела ответа как Java-объект из массива

О критериях можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Критерии).

[Критерии для получения подходящего http ответа](https://tinkoffcreditsystems.github.io/neptune/retrofit2/ru/tinkoff/qa/neptune/retrofit2/criteria/ResponseCriteria.html)

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

#### Получение данных тела ответа как Java-объект из массива. Call

```java
import org.my.pack.*;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectFromArraySupplier.*;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

public class MyTest {

    @ApiService
    private CallService service;

    @Test
    public void myTest() {
        //Возвращается элемент массива. Тело является массивом.
        //Http ответ и хотя бы один из элементов массива должны подходить под указанные критерии.
        //Из общего тела-массива вернется первый подходящий элемент.
        DtoObject result = retrofit().get(callArrayItem("Нужный элемент массива", //описываем то, 
                // что нужно получить
                () -> callService.getJsonArray()) //тут описывается вызов метода API
                //можно указать критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //можно указать критерий, которому должен соответствовать результирующий элемент массива
                .criteria("Число элементов значения поля 'object' == 2", r -> r.getObject().size() == 2)
                //можно указать время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными
                //т.е. со статусом=200 и непустым массивом объектов, в котором хотя бы один элемент 
                //соответствует переданному критерию
                .retryTimeOut(ofSeconds(10))
                //можно указать интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //можно указать, что должно быть выброшено исключение, если результат не был получен
                .throwOnNoResult());
    }
}
```

```java
import java.util.Objects;
import org.my.pack.*;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectFromArraySupplier.*;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

public class MyTest {

    @ApiService
    private CallService service;

    @Test
    public void myTest() {
        // Возвращается элемент массива. Тело не обязательно является массивом. 
        // Промежуточный массив получается в ходе дополнительных вычислений. 
        // Http ответ и хотя бы один из элементов массива должны подходить под указанные 
        // критерии. Из массива вернется первый подходящий элемент.
        Object result = retrofit().get(callArrayItem("Значение из полей objects по ключу 'a'", //описываем то, 
                // что нужно получить
                () -> callService.getJson(), //тут описывается вызов метода API
                dtos -> dtos.stream() //алгоритм получения результирующего массива
                        .map(dto -> dto.getObject().get("a"))
                        .filter(Objects::nonNull)
                        .toArray())
                //можно указать критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //можно указать критерий тела ожидаемого ответа
                .callBodyCriteria("Количество элементов десериализованного тела ответа == 2", dtoObjects -> dtoObjects.size() == 2)
                //можно указать критерий, которому должен соответствовать результирующий элемент массива
                .criteria("Строковое значение объекта начинается на 'a'",
                        o -> String.valueOf(o).startsWith("a"))
                //можно указать время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными
                //т.е. со статусом=200 и непустым массивом объектов, в котором хотя бы один элемент 
                //соответствует переданному критерию
                .retryTimeOut(ofSeconds(10))
                //можно указать интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //можно указать, что должно быть выброшено исключение, если результат не был получен
                .throwOnNoResult());
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

#### Получение данных тела ответа как Java-объект из массива. Случай без использования Call

```java
import org.my.pack.*;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectFromArraySupplier.*;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

public class MyTest {

  @ApiService
  private CallAdaptedService service;

  @Test
  public void myTest() {
    //Возвращается элемент массива. Тело является массивом.
    //Http ответ и хотя бы один из элементов массива должны подходить под указанные критерии.
    //Из общего тела-массива вернется первый подходящий элемент.
    DtoObject result = retrofit().get(arrayItem("Нужный элемент массива", //описываем то, 
            // что нужно получить
            () -> callService.getJsonArray()) //тут описывается вызов метода API
            //можно указать критерий ожидаемого ответа, т.е. код статуса должен быть 200
            .responseCriteria(statusCode(200))
            //можно указать критерий, которому должен соответствовать результирующий элемент массива
            .criteria("Число элементов значения поля 'object' == 2", r -> r.getObject().size() == 2)
            //можно указать время на получение ожидаемого результата = 10 секунд
            //В течение этих 10 секунд запрос будет отправляться до тех пор,
            //пока не вернется ожидаемый ответ с ожидаемыми данными
            //т.е. со статусом=200 и непустым массивом объектов, в котором хотя бы один элемент 
            //соответствует переданному критерию
            .retryTimeOut(ofSeconds(10))
            //можно указать интервал между попытками получить результат в пределах указанных 10 сек
            .pollingInterval(ofMillis(500))
            //можно указать, что должно быть выброшено исключение, если результат не был получен
            .throwOnNoResult());
  }
}
```

```java
import java.util.Objects;
import org.my.pack.*;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.retrofit2.steps.GetObjectFromArraySupplier.*;
import static ru.tinkoff.qa.neptune.retrofit2.criteria.ResponseCriteria.*;

public class MyTest {

  @ApiService
  private CallAdaptedService service;

  @Test
  public void myTest() {
    // Возвращается элемент массива. Тело не обязательно является массивом. 
    // Промежуточный массив получается в ходе дополнительных вычислений. 
    // Http ответ и хотя бы один из элементов массива должны подходить под указанные 
    // критерии. Из массива вернется первый подходящий элемент.
    Object result = retrofit().get(arrayItem("Значение из полей objects по ключу 'a'", //описываем то, 
            // что нужно получить
            () -> callService.getJson(), //тут описывается вызов метода API
            dtos -> dtos.stream() //алгоритм получения результирующего массива
                    .map(dto -> dto.getObject().get("a"))
                    .filter(Objects::nonNull)
                    .toArray())
            //можно указать критерий ожидаемого ответа, т.е. код статуса должен быть 200
            .responseCriteria(statusCode(200))
            //можно указать критерий тела ожидаемого ответа
            .callBodyCriteria("Количество элементов десериализованного тела ответа == 2", dtoObjects -> dtoObjects.size() == 2)
            //можно указать критерий, которому должен соответствовать результирующий элемент массива
            .criteria("Строковое значение объекта начинается на 'a'",
                    o -> String.valueOf(o).startsWith("a"))
            //можно указать время на получение ожидаемого результата = 10 секунд
            //В течение этих 10 секунд запрос будет отправляться до тех пор,
            //пока не вернется ожидаемый ответ с ожидаемыми данными
            //т.е. со статусом=200 и непустым массивом объектов, в котором хотя бы один элемент 
            //соответствует переданному критерию
            .retryTimeOut(ofSeconds(10))
            //можно указать интервал между попытками получить результат в пределах указанных 10 сек
            .pollingInterval(ofMillis(500))
            //можно указать, что должно быть выброшено исключение, если результат не был получен
            .throwOnNoResult());
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)