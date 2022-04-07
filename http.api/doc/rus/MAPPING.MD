# Мэппинг http-сервисов

В [Отправка http запросов/получение ответов](REQUEST_RESPONSE.MD) был продемонстрирован механизм отправки http-запроса и
получение ответа для последующей валидации данных. Ничего дополнительно делать не надо, если:

- HTTP API в принципе не является объектом для тестирования, а используется как вспомогательный инструмент для
  приведения тестируемой системы в нужное состояние
- обращение к API тестируемого приложения/работающего приложения стороннего разработчика происходит редко
- API тестируемого приложения не имеет сложной логики/большого количества методов для обращения.

В остальных случаях интеграционное тестирование HTTP API может быть затруднительным:

- массивное описание запроса
- поддержка кода интеграционных тестов может быть усложнена из-за того что есть проблемы с повторным использованием
  какой-то части кода.

Ниже описание варианта решения, который предоставляет Neptune.

## Оглавление
* [Мэппинг](#Мэппинг)
  + [Методы](#Методы)
  + [Заголовки](#Заголовки)
  + [Путь](#Путь)
  + [Запрос](#Запрос)
  + [Тело](#Тело)
  + [Мэппинг Dto](#Мэппинг-Dto)
* [Инициализация объектов моделей сервисов в клиентском коде и их использование](#Инициализация-объектов-моделей-сервисов-в-клиентском-коде-и-их-использование)
  + [Дефолтный корневой URL](#Дефолтный-корневой-URL)
  + [Недефолтные корневые URL](#Недефолтные-корневые-URL)
* [Внедрение зависимостей](#Внедрение-зависимостей)
* [Интеграция мэппинга со Swagger 3.x.](#интеграция-мэппинга-со-swagger-3x)


## Мэппинг

HTTP API описывается как расширение интерфейса `ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI`, которое
декларирует только:

- методы, которые возвращают объекты `ru.tinkoff.qa.neptune.http.api.request.RequestBuilder` - представление запроса,
  который надо отправить
- методы, имеющие модификатор `default` и возвращающие `ru.tinkoff.qa.neptune.http.api.request.RequestBuilder`. Такой
  вариант возможен, когда HTTP-метод имеет набор параметров, у которых есть значения по умолчанию. В этом случае можно
  сделать дефолтный метод с сокращенной сигнатурой.
  [Статья про default методы Java v>=8](https://docs.oracle.com/javase/tutorial/java/IandI/defaultmethods.html)

```java
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;

import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.POST;

//HttpAPI с параметром. Немногие объявленные методы 
//работают по принципу Builder Pattern'а
public interface ServiceAPI extends HttpAPI<ServiceAPI> {

    //Правильно
    RequestBuilder post(String stringArg);

    //Правильно
    default RequestBuilder post() {
        return post("Foo bar");
    }

    //Неправильно. При попытке вызвать этот метод будет выброшено UnsupportedOperationException
    int getInt();

    //Неправильно. При попытке вызвать этот метод будет выброшено UnsupportedOperationException
    void doSomething();
}
```

Классы, объекты которых используются как сложные заголовки, параметры query-/path-секций и тела запросов, рекомендуется
наследовать от `ru.tinkoff.qa.neptune.http.api.mapping.MappedObject`. Классы, объекты которых используются как тела
ответов на запросы, тоже рекомендуется наследовать от `ru.tinkoff.qa.neptune.http.api.mapping.MappedObject`:

- для поддержания однообразия модели
- есть вероятность использования объектов этих классов в качестве параметров (header, query, path, body) запросов. Это
  зависит от конкретного API.

Интерфейс реализовывать не нужно (!). Ниже детали мэппинга.

### Методы


    

Каждый объявленный метод, не имеющий модификатор `default`, должен моделировать метод http-протокола и его вызов.
Методы, имеющие модификаторы `default`, выполняют вызов соответствующих методов с расширенной сигнатурой, используя
набор значений параметров по умолчанию, если он есть.

```java
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;

import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.*; //Данное 
//перечисление содержит элементы, соответствующие стандартным методам http-протокола, перечисленные 
// в разных версиях RFC 

public interface ServiceAPI extends HttpAPI<ServiceAPI> {

    //POST-метод
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething(String stringArg);

    //POST-метод выше. Представим что его единственного параметра есть 
    //значение по умолчанию - 'Foo bar'
    default RequestBuilder postSomething() {
        return postSomething("Foo bar");
    }

    //GET-метод
    @HttpMethod(httpMethod = GET)
    RequestBuilder getSomething();

    //PUT-метод
    @HttpMethod(httpMethod = PUT)
    RequestBuilder putSomething();

    //DELETE-метод
    @HttpMethod(httpMethod = DELETE)
    RequestBuilder deleteSomething();

    //PATCH-метод
    @HttpMethod(httpMethod = PATCH)
    RequestBuilder patchSomething();

    //HEAD-метод
    @HttpMethod(httpMethod = HEAD)
    RequestBuilder headSomething();

    //OPTIONS-метод
    @HttpMethod(httpMethod = OPTIONS)
    RequestBuilder optionsSomething();

    //TRACE-метод
    @HttpMethod(httpMethod = TRACE)
    RequestBuilder traceSomething();

    //Для случая, когда в RFC нет нужного метода
    @HttpMethod(httpMethodStr = "CUSTOM_METHOD")
    RequestBuilder customMethod();
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)


### Заголовки


  

Если метод имеет постоянные заголовки (например Content-Type), то

```java
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.Header;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;

import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.POST;

public interface ServiceAPI extends HttpAPI<ServiceAPI> {

    @Header(name = "Content-Type", headerValues = "application/json")
    //ниже пример, если заголовок имеет несколько постоянных значений 
    @Header(name = "header1", headerValues = {"abc", "one more value"})
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething(String stringArg);
}
```

Если параметр сигнатуры метода нужно представить как заголовок, то

```java
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.header.HeaderParameter;

import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.POST;

public interface ServiceAPI extends HttpAPI<ServiceAPI> {

    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething(
            @HeaderParameter(headerName = "header1") String stringArg1, //заголовок, который необязателен, 
            // может быть передан null
            @HeaderParameter(headerName = "header2", required = true) Integer intArg2, //заголовок, который обязателен
            @HeaderParameter(headerName = "header3") SomeObjectClass objArg3, //в качестве заголовка можно передать 
            // какой-либо объект. В данном примере должно соблюдаться  условие: строковое представление объекта
            // (вызов метода toString()) должно быть корректно интерпретировано как значение заголовка 
            @HeaderParameter(headerName = "header4") Object[] arrArg4, //можно передать множественное значение заголовка
            //в виде массива. Массив должен состоять из объектов, чьи строковые представления
            // (вызов метода toString()) корректно интерпретируются как значения заголовка 
            @HeaderParameter(headerName = "header5") Iterable<?> iterableArg5 //аналогично примеру с массивом
    );
}
```

Могут быть ситуации, когда в качестве заголовка нужно передать некоторый POJO, который наследует
`ru.tinkoff.qa.neptune.http.api.mapping.MappedObject`, или объект `java.util.Map`. Тогда

```java
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.header.HeaderParameter;

import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.POST;

public interface ServiceAPI extends HttpAPI<ServiceAPI> {

    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething(
            //Если передать объект Map, то он будет преобразован в строку вида
            //`key1,value1,key2,value2` и т.д
            @HeaderParameter(headerName = "header1") Map<?, ?> mapArg1,
            //explode = true означает "развернуть" объект
            //Если передать объект Map, то он будет преобразован в строку вида
            //`key1=value1,key2=value2` и т.д
            @HeaderParameter(headerName = "header2", explode = true) Map<?, ?> mapArg2
    );

    //Все тоже самое будет работать и для наследника 
    // ru.tinkoff.qa.neptune.http.api.mapping.MappedObjectru.tinkoff.qa.neptune.http.api.mapping.MappedObject
    //только вместо ключей будут использованы представления полей
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)


### Путь

```java
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.URIPath;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.path.PathParameter;

import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.POST;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.path.PathStyles.LABEL;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.path.PathStyles.MATRIX;

public interface ServiceAPI extends HttpAPI<ServiceAPI> {

    //Формирует path-часть для URI запроса
    //В данном примере продемонстрирована ситуация, когда путь постоянный
    @URIPath("/some/path")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething();

    //Формирует path-часть для URI запроса
    //В данном примере продемонстрирована ситуация, когда путь имеет переменную часть
    //Имена параметров пути должны соответствовать тому, что заключено в {}.
    //Каждый параметр сигнатуры метода, который отвечает за мэппинг пути, должен иметь уникальное имя 
    //параметра пути.
    @URIPath("/some/path/to/{target}/{target2}/{optional}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething(
            @PathParameter(name = "target") String pathArgument, //данный параметр обязателен 
            @PathParameter(name = "optional", required = false) Integer pathOptArgument, //данный параметр не является 
            //обязательным. Это значит, если его значение будет == null, то из пути будет исключена часть, 
            //представленная как {optional}
            @PathParameter(name = "target2") SomeObjClass pathObjArgument //можно передать 
            // какой-либо объект. В данном примере должно соблюдаться  условие: строковое представление объекта
            // (вызов метода toString()) должно быть корректно интерпретировано как часть пути
    );

    //В представленных выше примерах в path-выражение будут подставлены строковые значения переданных объектов
    //Ниже менее типовые варианты 
    //_______________________________________________________________________________________________________

    //Если в качестве target передать строку `ABC`
    //то получится следующий путь: /some/path/to/.ABC
    @URIPath("/some/path/to/{target}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething2(@PathParameter(name = "target", style = LABEL) String pathArgument);

    //Если в качестве target передать строку `ABC`
    //то получится следующий путь: /some/path/to/;target=ABC
    @URIPath("/some/path/to/{target}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething3(@PathParameter(name = "target", style = MATRIX) String pathArgument);
}
```

Могут быть ситуации, когда в качестве параметра пути нужно передать массив или коллекцию. Тогда

```java
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.URIPath;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.path.PathParameter;

import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.POST;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.path.PathStyles.LABEL;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.path.PathStyles.MATRIX;

public interface ServiceAPI extends HttpAPI<ServiceAPI> {

    //Получится путь вида
    ///some/path/to/elem1,elem2,elem3
    @URIPath("/some/path/to/{arrayParam}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething(@PathParameter(name = "arrayParam") Object[] array);

    //Получится путь вида
    ///some/path/to/.elem1.elem2.elem3
    @URIPath("/some/path/to/{arrayParam}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething2(@PathParameter(name = "arrayParam", style = LABEL) Object[] array);

    //Получится путь вида
    ///some/path/to/;arrayParam=elem1,elem2,elem3
    @URIPath("/some/path/to/{arrayParam}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething3(@PathParameter(name = "arrayParam", style = MATRIX) Object[] array);

    //explode = true означает "развернуть" объект
    //Получится путь вида
    ///some/path/to/;arrayParam=elem1;arrayParam=elem2;arrayParam=elem3
    @URIPath("/some/path/to/{arrayParam}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething3(@PathParameter(name = "arrayParam", style = MATRIX, explode = true) Object[] array);

    //Зарезервированные символы перекодируются
}
```

Могут быть ситуации, когда в качестве параметра пути нужно передать некоторый POJO, который наследует
`ru.tinkoff.qa.neptune.http.api.mapping.MappedObject`, или объект `java.util.Map`. Тогда

```java
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.URIPath;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.path.PathParameter;

import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.POST;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.path.PathStyles.LABEL;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.path.PathStyles.MATRIX;

public interface ServiceAPI extends HttpAPI<ServiceAPI> {

    //Получится путь вида
    ///some/path/to/key1,value1,key2,value2,key3,value3
    @URIPath("/some/path/to/{objParam}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething(@PathParameter(name = "objParam") Map<?, ?> map);

    //explode = true означает "развернуть" объект
    //Получится путь вида
    ///some/path/to/key1=value1,key2=value2,key3=value3
    @URIPath("/some/path/to/{objParam}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething2(@PathParameter(name = "objParam", explode = true) Map<?, ?> map);

    //Получится путь вида
    ///some/path/to/.key1.value1.key2.value2.key3.value3
    @URIPath("/some/path/to/{objParam}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething3(@PathParameter(name = "objParam", style = LABEL) Map<?, ?> map);

    //explode = true означает "развернуть" объект
    //Получится путь вида
    ///some/path/to/.key1=value1.key2=value2.key3=value3
    @URIPath("/some/path/to/{objParam}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething4(@PathParameter(name = "objParam", style = LABEL, explode = true) Map<?, ?> map);

    //Получится путь вида
    ///some/path/to/;objParam=key1,value1,key2,value2,key3,value3
    @URIPath("/some/path/to/{objParam}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething3(@PathParameter(name = "objParam", style = MATRIX) Map<?, ?> map);

    //explode = true означает "развернуть" объект
    //Получится путь вида
    ///some/path/to/;key1=value1;key2=value2;key3=value3
    @URIPath("/some/path/to/{objParam}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething4(@PathParameter(name = "objParam", style = MATRIX, explode = true) Map<?, ?> map);

    //Все тоже самое будет работать и для наследника 
    // ru.tinkoff.qa.neptune.http.api.mapping.MappedObjectru.tinkoff.qa.neptune.http.api.mapping.MappedObject
    //только вместо ключей будут использованы представления полей  

    //Зарезервированные символы перекодируются
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)


### Запрос

```java
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form.FormParam;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.query.QueryParameter;

import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.POST;

public interface ServiceAPI extends HttpAPI<ServiceAPI> {

    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething(
            @QueryParameter(name = "param1") String queryArgument, //данный параметр обязателен 
            @QueryParameter(name = "param2", required = false) Integer queryOptArgument, //данный параметр не является 
            //обязательным. Это значит, если его значение будет == null, то в запрос он не будет добавлен
            @QueryParameter(name = "param3") SomeObjClass queryObjArgument, //можно передать 
            // какой-либо объект. В данном примере должно соблюдаться  условие: строковое представление объекта
            // (вызов метода toString()) должно быть корректно интерпретировано как значение параметра запроса
            //-------------------------------------------------------------------------------------------------
            //Если строка содержит в себе зарезервированные символы, то эти символы не будут 
            //перекодированы в query-части URI-запроса
            @QueryParameter(name = "param4") @FormParam(allowReserved = true) String queryReservedArgument
    );
}
```

Могут быть ситуации, когда в качестве параметра пути нужно передать массив или коллекцию. Тогда

```java
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form.FormParam;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.query.QueryParameter;

import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.POST;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form.FormStyles.SPACE_DELIMITED;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form.FormStyles.PIPE_DELIMITED;

public interface ServiceAPI extends HttpAPI<ServiceAPI> {

    //Получится следующий параметр запроса (arrayParam)
    //arrayParam=elem1&arrayParam=elem2&arrayParam=elem3
    //Зарезервированные символы в значениях будут перекодированы
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething(@QueryParameter(name = "arrayParam") Object[] array);

    //Получится следующий параметр запроса (arrayParam)
    //arrayParam=elem1&arrayParam=elem2&arrayParam=elem3
    //Зарезервированные символы в значениях будут сохранены
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething2(@QueryParameter(name = "arrayParam") @FormParam(allowReserved = true) Object[] array);

    //explode = false означает, что объект-массив будет как-бы "сжат", т.е. записан в одну строку с запятой 
    // в качестве разделителя 
    // 
    //Получится следующий параметр запроса (arrayParam)
    //arrayParam=elem1,elem2,elem3
    //Зарезервированные символы в значениях будут перекодированы
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething3(@QueryParameter(name = "arrayParam") @FormParam(explode = false) Object[] array);

    //Аналогично примеру postSomething3.
    //Зарезервированные символы в значениях будут сохранены
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething4(@QueryParameter(name = "arrayParam")
                                  @FormParam(explode = false, allowReserved = true) Object[] array);

    //Аналогично примеру postSomething3. Параметр запроса arrayParam будет записан 
    //в одну строку с пробелом ("%20") в качестве разделителя. 
    // Если  explode = true, то style будет проигнорирован. И результат будет аналогичен postSomething
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething5(@QueryParameter(name = "arrayParam")
                                  @FormParam(style = SPACE_DELIMITED, explode = false) Object[] array);

    //Аналогично примеру postSomething3. Параметр запроса arrayParam будет записан 
    //в одну строку с пробелом ("%20") в качестве разделителя и с сохранением зарезервированных символов как есть. 
    // Если  explode = true, то style будет проигнорирован. И результат будет аналогичен postSomething2
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething6(@QueryParameter(name = "arrayParam")
                                  @FormParam(style = SPACE_DELIMITED, explode = false, allowReserved = true) Object[] array);

    //Аналогично примеру postSomething3. Параметр запроса arrayParam будет записан 
    //в одну строку с вертикальной чертой ("%7C") в качестве разделителя. 
    // Если  explode = true, то style будет проигнорирован. И результат будет аналогичен postSomething
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething7(@QueryParameter(name = "arrayParam")
                                  @FormParam(style = PIPE_DELIMITED, explode = false) Object[] array);

    //Аналогично примеру postSomething3. Параметр запроса arrayParam будет записан 
    //в одну строку с вертикальной чертой ("%7C") в качестве разделителя и с сохранением зарезервированных символов как есть. 
    // Если  explode = true, то style будет проигнорирован. И результат будет аналогичен postSomething2
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething6(@QueryParameter(name = "arrayParam")
                                  @FormParam(style = PIPE_DELIMITED, explode = false, allowReserved = true) Object[] array);
}
```

Могут быть ситуации, когда в качестве параметра query-части нужно передать некоторый POJO, который наследует
`ru.tinkoff.qa.neptune.http.api.mapping.MappedObject`, или объект `java.util.Map` тогда

```java
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form.FormParam;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.query.QueryParameter;

import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.POST;

public interface ServiceAPI extends HttpAPI<ServiceAPI> {

    //Запрос будет выглядеть как строка вида key1=value1&key2=value2 и т.д. 
    //Зарезервированные символы в значениях будут перекодированы
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething(@QueryParameter(name = "objParam") Map<?, ?> map);

    //Запрос будет выглядеть как строка вида key1=value1&key2=value2 и т.д. 
    //Зарезервированные символы в значениях будут сохранены
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething2(@QueryParameter(name = "objParam") @FormParam(allowReserved = true) Map<?, ?> map);

    //Запрос будет выглядеть как строка вида objParam=key1,value1,key2,value2 и т.д. 
    //Зарезервированные символы в значениях будут перекодированы
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething3(@QueryParameter(name = "objParam") @FormParam(explode = false) Map<?, ?> map);

    //Запрос будет выглядеть как строка вида objParam=key1,value1,key2,value2 и т.д. 
    //Зарезервированные символы в значениях будут сохранены
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething4(@QueryParameter(name = "objParam")
                                  @FormParam(explode = false, allowReserved = true) Map<?, ?> map);

    //Все тоже самое будет работать и для наследника 
    // ru.tinkoff.qa.neptune.http.api.mapping.MappedObjectru.tinkoff.qa.neptune.http.api.mapping.MappedObject
    //только вместо ключей будут использованы представления полей
}
```

`@FormParam(style = SPACE_DELIMITED)` и `@FormParam(style = PIPE_DELIMITED)` нельзя использовать для `MappedObject`
и `java.util.Map`. Существует еще один стиль формы -
`ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form.FormStyles.DEEP_OBJECT`. Он был разработан
специально для таких случаев. Но пока есть сложности, связанные с представлением объектов разной вложенности для разного
рода back-end'ов. В данный момент использовать этот стиль не рекомендуется, _это тема для доработки/разработки фичи._

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)


### Тело

О поддерживаемых форматах тел http-запросов можно прочитать [тут](REQUEST_RESPONSE.MD#Тело-запроса-на-примере-POST).
Мэппинг поддерживает все описанные форматы, кроме пользовательских.
Задача: [#211](https://github.com/tinkoff/neptune/issues/211)

Любое тело запроса, кроме `multipart/form-data` можно представить в модели следующим образом

```java
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import ru.tinkoff.qa.neptune.http.api.mapping.MappedObject;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.Body;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.BodyParamFormat;

import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.POST;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.BodyDataFormat.JSON;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.BodyDataFormat.XML;

public interface ServiceAPI extends HttpAPI<ServiceAPI> {

    //передача текста в качестве тела POST-запроса
    //параметр обязателен. Значение не должно == null
    @HttpMethod(httpMethod = POST)
    RequestBuilder postStringBody(@Body String body);

    //передача текста в качестве тела POST-запроса
    //параметр необязателен (isRequired = false). Если значение == null, то будет отправлен запрос с пустым телом
    @HttpMethod(httpMethod = POST)
    RequestBuilder postStringBody2(@Body(isRequired = false) String body);

    //передача массива байтов в качестве тела POST-запроса
    @HttpMethod(httpMethod = POST)
    RequestBuilder postByteBody(@Body byte[] body);

    //передача файла в качестве тела POST-запроса
    @HttpMethod(httpMethod = POST)
    RequestBuilder postFileBody(@Body File body);

    //передача файла в качестве тела POST-запроса
    @HttpMethod(httpMethod = POST)
    RequestBuilder postFileBody(@Body Path body);

    //передача объекта org.w3c.dom.Document в качестве тела POST-запроса
    @HttpMethod(httpMethod = POST)
    RequestBuilder postW3CBody(@Body org.w3c.dom.Document body);

    //вариант передачи данных application/x-www-form-urlencoded
    //такой способ может пригодиться, когда передается неструктурированный 
    //и не имеющий четких правил и ограничений набор параметров формы
    @HttpMethod(httpMethod = POST)
    RequestBuilder postForm(@Body FormParameter... body);

    //вариант передачи данных application/x-www-form-urlencoded
    //такой способ может пригодиться, когда передается 
    //и не имеющий четких правил и ограничений параметр формы
    @HttpMethod(httpMethod = POST)
    RequestBuilder postForm(@Body FormParameter body);

    //вариант передачи данных application/x-www-form-urlencoded
    //такой способ может пригодиться, когда передается неструктурированный 
    //и не имеющий четких правил и ограничений набор параметров формы
    @HttpMethod(httpMethod = POST)
    RequestBuilder postForm(@Body List<FormParameter> body);

    //передача потока данных в качестве тела POST-запроса
    @HttpMethod(httpMethod = POST)
    RequestBuilder postStream(@Body InputStream body);

    //передача потока данных в качестве тела POST-запроса
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSupplier(@Body Supplier<InputStream> body);

    //передача сериализованного в JSON-строку объекта в качестве тела POST-запроса
    @HttpMethod(httpMethod = POST)
    RequestBuilder postJson(@Body @BodyParamFormat(format = JSON) SomeObject body);

    //передача сериализованного в XML-строку объекта в качестве тела POST-запроса
    @HttpMethod(httpMethod = POST)
    RequestBuilder postXml(@Body @BodyParamFormat(format = XML) SomeObject body);

    //передача сериализованного в JSON-строку объекта в качестве тела POST-запроса
    //В данном примере упрощенно представлен пример использования MappedObject в качестве тела запроса
    //Данный способ является более предпочтительным
    @HttpMethod(httpMethod = POST)
    RequestBuilder postJson(@Body @BodyParamFormat(format = JSON) MappedObject body);

    //передача сериализованного в XML-строку объекта в качестве тела POST-запроса
    //В данном примере упрощенно представлен пример использования MappedObject в качестве тела запроса
    //Данный способ является более предпочтительным
    @HttpMethod(httpMethod = POST)
    RequestBuilder postXml(@Body @BodyParamFormat(format = XML) MappedObject body);

    //передача сериализованного в строку JSON-array формата объекта в качестве тела POST-запроса
    //Тоже самое актуально для массивов
    @HttpMethod(httpMethod = POST)
    RequestBuilder postListJson(@Body @BodyParamFormat(format = JSON) List<Object> body);

    //передача сериализованного в JSON-строку объекта в качестве тела POST-запроса
    @HttpMethod(httpMethod = POST)
    RequestBuilder postJsonMixin(@Body @BodyParamFormat(format = JSON, mixIns = MixInClass1.class) SomeObject body);

    //передача сериализованного в XML-строку объекта в качестве тела POST-запроса
    @HttpMethod(httpMethod = POST)
    RequestBuilder postXmlMixin(@Body @BodyParamFormat(format = XML, mixIns = MixInClass2.class) SomeObject body);

    //передача строкового представления объекта класса SomeObject в качестве тела POST-запроса
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomeBody(@Body SomeObject body);
}
```

Примеры `postJsonMixin` и `postXmlMixin` -
читать [Jackson Mixins](https://github.com/FasterXML/jackson-docs/wiki/JacksonMixInAnnotations)

**Ниже примеры для тел формата `application/x-www-form-urlencoded`**

```java
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.url.encoded.URLEncodedParameter;

public interface ServiceAPI extends HttpAPI<ServiceAPI> {

    //Передаваемая форма состоит из 3-х параметров: 
    // - form_object_param1 - обязательный. В качестве значения будет использовано строковое представление объекта 
    // - form_int_param2 - необязательный. Если в качестве значения будет передан null, тогда параметр будет исключен из формы 
    // - form_str_param3 - обязательный
    @HttpMethod(httpMethod = POST)
    RequestBuilder postForm(@URLEncodedParameter(name = "form_object_param1") Object param1,
                            @URLEncodedParameter(name = "form_int_param2", isRequired = false) Integer param2,
                            @URLEncodedParameter(name = "form_str_param3") String param3);
}
```

Для параметров `application/x-www-form-urlencoded`-форм действуют те же правила, что и для query-частей запросов.
Поэтому аннотацию `@URLEncodedParameter` можно комбинировать с аннотацией `@FormParam`. Примеры и схематичные результаты
того, что получается, см в главе про [запросы](#Запрос).

Так же можно передавать параметры формы в специфическом формате - JSON или XML. В этом случае можно комбинировать
аннотацию `@URLEncodedParameter`
с `@BodyParamFormat`. См. примеры использования аннотации `@Body` выше.

Комбинация всех трех аннотаций `@URLEncodedParameter`, `@FormParam`, `@BodyParamFormat` - <font color='red'>не
работает</font>.

```java
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.url.encoded.URLEncodedParameter;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.BodyParamFormat;

import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.POST;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.BodyDataFormat.JSON;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form.FormStyles.PIPE_DELIMITED;

public interface ServiceAPI extends HttpAPI<ServiceAPI> {

    //Передаваемая форма состоит из 3-х параметров: 
    // - form_object_param1 - обязательный. В качестве значения будет использовано строковое представление объекта 
    // - form_int_param2 - необязательный. Если в качестве значения будет передан null, тогда параметр будет исключен из формы 
    // - form_map_param3 - обязательный. Значение параметра будет записано в одну строку с вертикальной чертой ("%7C") в качестве 
    // разделителя и с перекодировкой зарезервированных символов. 
    @HttpMethod(httpMethod = POST)
    RequestBuilder postForm(@URLEncodedParameter(name = "form_object_param1") Object param1,
                            @URLEncodedParameter(name = "form_int_param2", isRequired = false) Integer param2,
                            @URLEncodedParameter(name = "form_map_param3")
                            @FormParam(style = PIPE_DELIMITED, explode = false) Map<?, ?> param3);

    //Передаваемая форма состоит из 3-х параметров: 
    // - form_object_param1 - обязательный. В качестве значения будет использовано строковое представление объекта 
    // - form_int_param2 - необязательный. Если в качестве значения будет передан null, тогда параметр будет исключен из формы 
    // - form_map_param3 - обязательный. Значение параметра будет записано в виде строки JSON-формата.
    @HttpMethod(httpMethod = POST)
    RequestBuilder postForm2(@URLEncodedParameter(name = "form_object_param1") Object param1,
                             @URLEncodedParameter(name = "form_int_param2", isRequired = false) Integer param2,
                             @URLEncodedParameter(name = "form_map_param3")
                             @BodyParamFormat(format = JSON) Map<?, ?> param3);
}
```

**Ниже примеры для тел формата `multipart/form-data`**

```java
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.multipart.MultiPartBody;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.BodyParamFormat;

import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.POST;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.BodyDataFormat.JSON;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form.FormStyles.PIPE_DELIMITED;

public interface ServiceAPI extends HttpAPI<ServiceAPI> {

    @HttpMethod(httpMethod = POST)
    RequestBuilder postMultipart(
            @MultiPartBody(name = "test_file", contentTransferEncoding = BINARY) File file, //Указываем имя части 'test_file'
            //Передаем файл 
            //и указываем заголовок Content-Transfer-Encoding со значением 'binary'

            @MultiPartBody(name = "test_json") //Указываем имя части 'test_json'
            @BodyParamFormat(format = JSON) //Объект будет сериализован в json-строку
            //Заголовку Content-Type будет присвоено значение `application/json`
            @DefineContentType(contentType = "application/json") SomeObjectClass someObject,

            @MultiPartBody(name = "test_binary") //Указываем имя части 'test_binary'
            @DefineFileName(fileName = "tezzt_file") //Присваиваем fileName части значение tezzt_file
            @DefineContentType byte[] binary, //Передаем массив байтов

            @MultiPartBody(name = "test_file2") //Указываем имя части 'test_file2'
            @DefineFileName(useGivenFileName = true) Path path, //в качестве fileName будет использовано имя 
            //переданного файла. Опция useGivenFileName=true работает для аргументов типа File и Path

            @MultiPartBody(name = "test_binary2") //Указываем имя части 'test_binary2'
            @DefineFileName //Присваиваем fileName части случайно сгенерированное значение
            @DefineContentType byte[] binary2, //Передаем массив байтов

            @MultiPartBody(name = "test_file3") //Указываем имя части 'test_file3'
            @DefineContentType Path path2, //Передаем файл
            //Выше продемонстрированные примеры - примеры обязательных параметров

            @MultiPartBody(name = "test_file4", isRequired = false) //Пример необязательного параметра
            @DefineFileName(fileName = "tezzt_file") Path path3); //Это часть может быть не включена в составное тело 
    //если значение параметра == null

    //В качестве аргументов, моделирующих части multipart/form-data-тел, могут использоваться аргументы типов 
    //String, byte[], File, Path, InputStream. Любые другие типы так же могут быть использованы при условии
    //что нужно передать строковые представления этих объектов, или эти объекты будут сериализованы в JSON/XML.
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)


### Мэппинг Dto

DTO - Data Transferring Object, т.е. объекты, которые моделируют параметры Path и Query - частей URI http-запросов, а
так же заголовки запросов, тела запросов и ответов на отправленные запросы.

Как было сказано выше, для описания таких объектов рекомендуется наследовать
класс `ru.tinkoff.qa.neptune.http.api.mapping.MappedObject`:

- данные объекты имеют механизмы конвертации в объект `java.util.Map`
- обеспечивают единообразие и использование объектов одного и того же класса в качестве разных (по назначению)
  параметров http-запросов.

Пример ниже


  

```java
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.*;
import ru.tinkoff.qa.neptune.http.api.mapping.MappedObject;

//Данный объект может быть использован как тело запроса/ответа,
//так и как параметр запроса/пути или заголовок. 
//Для мэппинга полей используются аннотации из пакета com.fasterxml.jackson.annotation
//Если один и тот же объект сериализуется как в JSON, так и в XML строку, 
//то можно совместно использовать как аннотации из com.fasterxml.jackson.annotation, 
//так и из пакета com.fasterxml.jackson.dataformat.xml.annotation или 
// javax.xml.bind.annotation
@JsonPropertyOrder({"someNumber", "someString", "someArray", "nested", "nestedNext"})
public class SomeDto extends MappedObject {

    @JsonProperty("someNumber")
    private Integer someNum;

    private String someString;

    private List<String> someArray;


    public SomeDto setSomeNum(Integer someNum) {
        this.someNum = someNum;
        return this;
    }

    public String getSomeString() {
        return someString;
    }

    public SomeDto setSomeString(String someString) {
        this.someString = someString;
        return this;
    }

    public SomeDto setSomeArray(List<String> someArray) {
        this.someArray = someArray;
        return this;
    }

    public Integer getSomeNum() {
        return someNum;
    }

    public List<String> getSomeArray() {
        return someArray;
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Инициализация объектов моделей сервисов в клиентском коде и их использование

Важно предварительно ознакомиться:

- [Отправка настроенного http-запроса на примере GET](./REQUEST_RESPONSE.MD#Отправка-настроенного-http-запроса-на-примере-GET)
- [Получение данных тела ответа на http запрос](./REQUEST_RESPONSE.MD#Получение-данных-тела-ответа-на-http-запрос)
- [Связывание классов](./../../../core.api/doc/rus/CLASS_BINDING.MD)

Предположим, мы описали API нескольких сервисов. Пусть они выглядят так (для наглядности).

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.Body;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.URIPath;

import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.POST;

public interface ServiceAPI1 extends HttpAPI<ServiceAPI1> {

    @URIPath("/path/to/point1")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething(@Body String text);

    //Другие методы тоже как-то описаны
}
```

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.URIPath;

import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.GET;

public interface ServiceAPI2 extends HttpAPI<ServiceAPI2> {

    @URIPath("/path/to/{point}")
    @HttpMethod(httpMethod = GET)
    RequestBuilder getSomething(@PathParameter(name = "point") String point);

    //Другие методы тоже как-то описаны
}
```

Ниже примеры того, как создавать объекты классов, моделирующих API тестируемых back-end'ов, и использовать их в
клиентском коде / коде тестов.


```java
import java.net.URI;
import java.net.http.HttpResponse;

import org.my.pack.*;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com");
        //так же endPoint можно передать как String и java.net.URL
        //ВАЖНО: передаваемое значение должно быть следующего вида - `схема\протокол`://`хост`:`порт если есть/фрагмент/пути/если/надо`

        var service1 = createAPI(ServiceAPI1.class, endPoint); //пример инициализации переменных
        var service2 = createAPI(ServiceAPI2.class, endPoint); //можно инициализировать как поля 


        HttpResponse<String> response1 = http().responseOf(
                service1.postSomething("Some text"),
                //Ответ вернется c телом, интерпретированным/прочитанным как текст
                ofString());

        HttpResponse<String> response2 = http().responseOf(
                service2.getSomething("endPoint"),
                //Ответ вернется c телом, интерпретированным/прочитанным как текст
                ofString());
    }
}
```

На самом деле не все можно описать с помощью средств мэппинга. Например то что клиент должен дожидаться ответ с кодом
100 от сервера, или то что необходима версия http протокола 2.0 для общения с одним из сервисов (напомню, что свойство
[DEFAULT_HTTP_PROTOCOL_VERSION](./SETTINGS.MD#default_http_protocol_version), определяет версию протокола обобщенно,
как-бы для всех сервисов). Ниже пример того, как это можно настроить.

```java
import java.net.URI;
import java.net.http.HttpResponse;

import org.my.pack.*;

import static java.net.http.HttpClient.Version.HTTP_2;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com");

        var service1 = createAPI(ServiceAPI1.class, endPoint);
        var service2 = createAPI(ServiceAPI2.class, endPoint);


        HttpResponse<String> response1 = http().responseOf(
                service1.postSomething("Some text").expectContinue(true),
                ofString());

        HttpResponse<String> response2 = http().responseOf(
                service2.getSomething("endPoint").version(HTTP_2),
                ofString());
    }
}
```

Это может быть неудобно, когда выполняется часто для одних и тех же сервисов.
В [Отправка настроенного http-запроса на примере GET](./REQUEST_RESPONSE.MD#Отправка-настроенного-http-запроса-на-примере-GET)
описан пример использования интерфейса `ru.tinkoff.qa.neptune.http.api.request.RequestTuner`.

```java
import org.my.pack.*;

import ru.tinkoff.qa.neptune.http.api.request.RequestSettings;
import ru.tinkoff.qa.neptune.http.api.request.RequestTuner;

public class RequestTuner1 implements RequestTuner {

    @Override
    public void setUp(RequestSettings<?> requestSettings) {
        requestSettings.expectContinue(true);
    }
}
```

```java
import org.my.pack.*;

import ru.tinkoff.qa.neptune.http.api.request.RequestSettings;
import ru.tinkoff.qa.neptune.http.api.request.RequestTuner;

import static java.net.http.HttpClient.Version.HTTP_2;

public class RequestTuner2 implements RequestTuner {

    @Override
    public void setUp(RequestSettings<?> requestSettings) {
        requestSettings.version(HTTP_2);
    }
}
```

и тогда

```java
import java.net.URI;
import java.net.http.HttpResponse;

import org.my.pack.*;

import static java.net.http.HttpClient.Version.HTTP_2;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com");

        //Через метод useForRequestBuilding можно указать несколько классов, через запятую. Классы можно указывать
        //если они имеют публичные конструкторы без параметров, или не имеют объявленных конструкторов совсем
        var service1 = createAPI(ServiceAPI1.class, endPoint).useForRequestBuilding(RequestTuner1.class);
        //В противном случае, можно создавать объекты и передавать объекты классов
        var service2 = createAPI(ServiceAPI2.class, endPoint).useForRequestBuilding(RequestTuner2.class);


        HttpResponse<String> response1 = http().responseOf(
                service1.postSomething("Some text"),
                ofString());

        HttpResponse<String> response2 = http().responseOf(
                service2.getSomething("endPoint"),
                ofString());
    }
}
```

Можно выполнять мэппинг классов, реализующих интерфейс `ru.tinkoff.qa.neptune.http.api.request.RequestTuner` на классы,
моделирующие API тестируемых сервисов.

```java
import org.my.pack.*;

import ru.tinkoff.qa.neptune.http.api.request.RequestSettings;
import ru.tinkoff.qa.neptune.http.api.request.RequestTuner;
import ru.tinkoff.qa.neptune.core.api.binding.Bind;

@Bind(to = ServiceAPI1.class) //жесткая привязка к определенному классу
//если что-то похожее нужно для еще одного сервиса то
//@Bind(to = SomeAPI.class)
public class RequestTuner1 implements RequestTuner {
    //на реализации фокусироваться не будем
}
```

```java
import org.my.pack.*;

import ru.tinkoff.qa.neptune.http.api.request.RequestSettings;
import ru.tinkoff.qa.neptune.http.api.request.RequestTuner;
import ru.tinkoff.qa.neptune.core.api.binding.Bind;

import static java.net.http.HttpClient.Version.HTTP_2;

//Еще один вариант привязки, чем-то напоминает wildcard
@Bind(to = HttpAPI.class, //указывается обобщенный интерфейс, моделирующий API сервисов
        withSubclasses = true, //указываем, что применимо для интерфейсов-наследников
        exclude = ServiceAPI1.class) //кроме перечисленных, имеет смысл если withSubclasses = true
public class RequestTuner2 implements RequestTuner {
    //на реализации фокусироваться не будем
}
```

И тогда

```java
import java.net.URI;
import java.net.http.HttpResponse;

import org.my.pack.*;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com");

        var service1 = createAPI(ServiceAPI1.class, endPoint);
        var service2 = createAPI(ServiceAPI2.class, endPoint);


        HttpResponse<String> response1 = http().responseOf(
                service1.postSomething("Some text"),
                ofString());

        HttpResponse<String> response2 = http().responseOf(
                service2.getSomething("endPoint"),
                ofString());
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Дефолтный корневой URL

То что надо прочитать о [END_POINT_OF_TARGET_API](SETTINGS.MD#end_point_of_target_api). Предположим, что все сервисы
работают на одном и том же хосте+порте, и это отражено в свойстве `END_POINT_OF_TARGET_API`

Тогда

```java
import java.net.http.HttpResponse;

import org.my.pack.*;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;

public class MyTest {

    @Test
    public void myTest() {
        //var endPoint = URI.create("http://my.api.com");
        //можно больше не указывать

        var service1 = createAPI(ServiceAPI1.class);
        var service2 = createAPI(ServiceAPI2.class);


        HttpResponse<String> response1 = http().responseOf(
                service1.postSomething("Some text"),
                ofString());

        HttpResponse<String> response2 = http().responseOf(
                service2.getSomething("endPoint"),
                ofString());
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)


### Недефолтные корневые URL


А что если `ServiceAPI1` и `ServiceAPI2` всегда на разных хостах/портах?

Необходимо прочитать:

- [Механизм работы со свойствами/переменными окружения / среды](./../../../core.api/doc/rus/SETTINGS.MD) - полезно в целом
- [Свойства для создания и возврата объектов URL](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-создания-и-возврата-объектов-URL)
- [Связывание классов](./../../../core.api/doc/rus/CLASS_BINDING.MD)

Добавим возможность указывать/читать свойства/переменные окружения для специфических адресов хостов.

```java
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;

public enum ServiceRoot implements URLValuePropertySupplier {
    @PropertyDefaultValue("http://host1:1111") //Корневой URI/URL, используемый на дефлтном окружении/при отладке
    @PropertyName("ROOT_OF_SERVICE1") //Название свойства/переменной окружения
    SERVICE1,

    @PropertyDefaultValue("http://host2:2222") //Корневой URI/URL, используемый на дефлтном окружении/при отладке
    @PropertyName("ROOT_OF_SERVICE2") //Название свойства/переменной окружения
    SERVICE2
}
```

И тогда

```java
import java.net.http.HttpResponse;

import org.my.pack.*;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;

public class MyTest {

    @Test
    public void myTest() {

        var service1 = createAPI(ServiceAPI1.class, SERVICE1); //Указываем элементы, 
        var service2 = createAPI(ServiceAPI2.class, SERVICE2); //предоставляющие доступ к новым свойствам.

        HttpResponse<String> response1 = http().responseOf(
                service1.postSomething("Some text"),
                ofString());

        HttpResponse<String> response2 = http().responseOf(
                service2.getSomething("endPoint"),
                ofString());
    }
}
```

Можно выполнять мэппинг новых свойств на классы, моделирующие API тестируемых сервисов.

```java
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;
import ru.tinkoff.qa.neptune.core.api.binding.Bind;

public enum ServiceRoot implements URLValuePropertySupplier {
    @PropertyDefaultValue("http://host1:1111") //Корневой URI/URL, используемый на дефлтном окружении/при отладке
    @PropertyName("ROOT_OF_SERVICE1") //Название свойства/переменной окружения
    @Bind(to = ServiceAPI1.class) //жесткая привязка к определенному классу
    //если что-то похожее нужно для еще одного сервиса то
    //@Bind(to = SomeAPI.class)
    SERVICE1,

    @PropertyDefaultValue("http://host2:2222") //Корневой URI/URL, используемый на дефлтном окружении/при отладке
    @PropertyName("ROOT_OF_SERVICE2") //Название свойства/переменной окружения
    //Еще один вариант привязки, чем-то напоминает wildcard
    @Bind(to = HttpAPI.class, //указывается обобщенный интерфейс, моделирующий API сервисов
            withSubclasses = true, //указываем, что применимо для интерфейсов-наследников
            exclude = ServiceAPI1.class) //кроме перечисленных, имеет смысл если withSubclasses = true
    SERVICE2
}
```

И тогда

```java
import java.net.http.HttpResponse;

import org.my.pack.*;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;

public class MyTest {

    @Test
    public void myTest() {
        var service1 = createAPI(ServiceAPI1.class);
        var service2 = createAPI(ServiceAPI2.class);

        HttpResponse<String> response1 = http().responseOf(
                service1.postSomething("Some text"),
                ofString());

        HttpResponse<String> response2 = http().responseOf(
                service2.getSomething("endPoint"),
                ofString());
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)


[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Внедрение зависимостей

Основная статья - [внедрения зависимостей](./../../../core.api/doc/rus/DEPENDENCY_INJECTION.MD).

Модули, интегрирующие Neptune c различными фреймворками тестирования, имеют возможности использования механизмов внедрения
зависимостей:

- [Testng](./../../../testng.integration/doc/rus/README.MD)  
- [JUnit5](./../../../jupiter.integration/doc/rus/README.MD)

Интеграция Neptune и нативного http-клиента Java имеет свой механизм внедрения зависимостей, позволяющий
инициализировать объекты классов, моделирующих http API тестируемых приложений.


```java
import java.net.http.HttpResponse;

import org.my.pack.*;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    //Для того, чтобы поля были проинициализированы корректно,
    //Необходимо указать значения свойства END_POINT_OF_TARGET_API. 
    //Либо создать свои настройки, указывающие на корневой URI API тестируемых приложений,
    //выполнить привязку этих настроек к классам-моделям ServiceAPI1 и ServiceAPI2.
    //Если есть необходимость использовать RequestTuner для какого либо/всех приложений - 
    //реализовать и выполнить привязку к моделирующим классам.
    private ServiceAPI1 service1;
    private ServiceAPI2 service2;

    @Test
    public void myTest() {
        HttpResponse<String> response1 = http().responseOf(
                service1.postSomething("Some text"),
                ofString());

        HttpResponse<String> response2 = http().responseOf(
                service2.getSomething("endPoint"),
                ofString());
    }
}
```

Все необходимое описано в данном документе.



[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Интеграция мэппинга со Swagger 3.x.

Если используется [Swagger 3.0](https://swagger.io/specification/) для описания http API тестируемых приложений, то
совсем не нужно выполнять ручную работу для того, чтобы клиентский код мог взаимодействовать с ними/тестировать их.
Существует
[интеграция](./../../../neptune.swagger.codegen/doc/rus/README.MD), позволяющая создавать описанные выше модели автоматически.

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)