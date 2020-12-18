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

<details>
    <summary>Нажми, чтобы прочесть</summary>

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

</details>

### Заголовки (Header)

<details>
  <summary>Нажми, чтобы прочесть</summary>

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

</details>

### Путь (Path-часть URI запроса)

<details>
  <summary>Нажми, чтобы прочесть</summary>

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

</details>

### Запрос (Query-часть URI запроса)

<details>
  <summary>Нажми, чтобы прочесть</summary>

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
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form.FormStyles.PIPE;

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
                                  @FormParam(style = PIPE, explode = false) Object[] array);

    //Аналогично примеру postSomething3. Параметр запроса arrayParam будет записан 
    //в одну строку с вертикальной чертой ("%7C") в качестве разделителя и с сохранением зарезервированных символов как есть. 
    // Если  explode = true, то style будет проигнорирован. И результат будет аналогичен postSomething2
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething6(@QueryParameter(name = "arrayParam")
                                  @FormParam(style = PIPE, explode = false, allowReserved = true) Object[] array);
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

`@FormParam(style = SPACE_DELIMITED)` и `@FormParam(style = PIPE)` нельзя использовать для `MappedObject`
и `java.util.Map`. Существует еще один стиль формы -
`ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form.FormStyles.DEEP_OBJECT`. Он был разработан
специально для таких случаев. Но пока есть сложности, связанные с представлением объектов разной вложенности для разного
рода back-end'ов. В данный момент использовать этот стиль не рекомендуется, _это тема для доработки/разработки фичи._

</details>

### Тело

О поддерживаемых форматах тел http-запросов можно прочитать [тут](REQUEST_RESPONSE.MD#Тело-запроса-на-примере-POST).
Мэппинг поддерживает все описанные форматы, кроме пользовательских. Задача: [#211](https://github.com/TinkoffCreditSystems/neptune/issues/211)

<details>
  <summary>Нажми, чтобы прочесть</summary>

</details>

## Дефолтный корневой URL

<details>
    <summary>Нажми, чтобы прочесть</summary>

</details>

## Недефолтные корневые URL

<details>
    <summary>Нажми, чтобы прочесть</summary>

</details>

## Внедрение зависимостей

<details>
    <summary>Нажми, чтобы прочесть</summary>

</details>