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
`ru.tinkoff.qa.neptune.http.api.mapping.MappedObject`, или объект `java.util.Map`.

```java
import java.util.Map;

import ru.tinkoff.qa.neptune.http.api.mapping.MappedObject;

public class SomeClass {

    //Map, который собираемся передать как заголовок
    private final Map<?, ?> headerMap = new LinkedHashMap<>() {
        {
            put("someString", "String value");
            put("someNum", 10.1111D);
            put("someBool", true);
            put("someArray", List.of(1, "ABC", true));
        }
    };

    //объект, который будет передан как заголовок. Идентичен Map'у выше
    private final HeaderParameterObject testHeaderObject = new HeaderParameterObject().setSomeString("String value")
            .setSomeNum(10.1111D)
            .setSomeBool(true)
            .setSomeArray(new Object[]{1, "ABC", true});

    //модель объекта-заголовка
    public class HeaderParameterObject extends MappedObject {
        private String someString;

        private Number someNum;

        private Boolean someBool;

        private Object[] someArray;

        public String getSomeString() {
            return someString;
        }

        public HeaderParameterObject setSomeString(String someString) {
            this.someString = someString;
            return this;
        }

        public Number getSomeNum() {
            return someNum;
        }

        public HeaderParameterObject setSomeNum(Number someNum) {
            this.someNum = someNum;
            return this;
        }

        public Boolean getSomeBool() {
            return someBool;
        }

        public HeaderParameterObject setSomeBool(Boolean someBool) {
            this.someBool = someBool;
            return this;
        }

        public Object[] getSomeArray() {
            return someArray;
        }

        public HeaderParameterObject setSomeArray(Object[] someArray) {
            this.someArray = someArray;
            return this;
        }
    }
}
```

Тогда

```java
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.header.HeaderParameter;

import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.POST;

public interface ServiceAPI extends HttpAPI<ServiceAPI> {

    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething(
            //Если передать объект Map выше, то он будет преобразован в строку вида
            //`someString,String value,someNum,10.1111,someBool,true,someArray,1,ABC,true`
            @HeaderParameter(headerName = "header1") Map<?, ?> mapArg1,
            //аналогично примеру выше
            @HeaderParameter(headerName = "header2") HeaderParameterObject intArg2,
            //explode = true означает "развернуть" объект
            //Если передать объект Map выше, то он будет преобразован в строку вида
            //`"someString=String value,someNum=10.1111,someBool=true,someArray=1,ABC,true"`
            @HeaderParameter(headerName = "header3", explode = true) Map<?, ?> mapArg1,
            //аналогично примеру выше
            @HeaderParameter(headerName = "header4", explode = true) HeaderParameterObject intArg2
    );
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
            @PathParameter(name = "target2") SomeObjClass pathArgument //можно передать 
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

    //Если в качестве arrayParam передать массив new Object[] {1,2,"АБВ","ABC",true}
    //то получится следующий путь 
    //Без кодировки: /some/path/to/1,2,АБВ,ABC,true
    //С кодировкой: /some/path/to/1,2,%D0%90%D0%91%D0%92,ABC,true
    @URIPath("/some/path/to/{arrayParam}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething(@PathParameter(name = "arrayParam") Object[] array);

    //Если в качестве arrayParam передать массив new Object[] {1,2,"АБВ","ABC",true}
    //то получится следующий путь 
    //Без кодировки: /some/path/to/.1.2.АБВ.ABC.true
    //С кодировкой:  /some/path/to/.1.2.%D0%90%D0%91%D0%92.ABC.true
    @URIPath("/some/path/to/{arrayParam}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething2(@PathParameter(name = "arrayParam", style = LABEL) Object[] array);

    //Если в качестве arrayParam передать массив new Object[] {1,2,"АБВ","ABC",true}
    //то получится следующий путь 
    //Без кодировки: /some/path/to/;arrayParam=1,2,АБВ,ABC,true
    //С кодировкой:  /some/path/to/;arrayParam=1,2,%D0%90%D0%91%D0%92,ABC,true
    @URIPath("/some/path/to/{arrayParam}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething3(@PathParameter(name = "arrayParam", style = MATRIX) Object[] array);

    //explode = true означает "развернуть" объект
    //Если в качестве arrayParam передать массив new Object[] {1,2,"АБВ","ABC",true}
    //то получится следующий путь 
    //Без кодировки: /some/path/to/;arrayParam=1;arrayParam=2;arrayParam=АБВ;arrayParam=ABC;arrayParam=true
    //С кодировкой:  /some/path/to/;arrayParam=1;arrayParam=2;arrayParam=%D0%90%D0%91%D0%92;arrayParam=ABC;arrayParam=true
    @URIPath("/some/path/to/{arrayParam}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething3(@PathParameter(name = "arrayParam", style = MATRIX, explode = true) Object[] array);
}
```

Могут быть ситуации, когда в качестве параметра пути нужно передать некоторый POJO, который наследует
`ru.tinkoff.qa.neptune.http.api.mapping.MappedObject`, или объект `java.util.Map`.

```java
import java.util.Map;

import ru.tinkoff.qa.neptune.http.api.mapping.MappedObject;

public class SomeClass {

    //Map, который собираемся передать как параметр пути
    private static final Map<?, ?> pathParamMap = new LinkedHashMap<>() {
        {
            put("someString", "String value");
            put("someNum", 10.1111D);
            put("someBool", true);
            put("someList", new Object[]{1, "ABC", "АБВ", true});
        }
    };

    //объект, который будет передан как параметр пути. Идентичен Map'у выше
    private final HeaderParameterObject testHeaderObject = new HeaderParameterObject().setSomeString("String value")
            .setSomeNum(10.1111D)
            .setSomeBool(true)
            .setSomeArray(new Object[]{1, "ABC", true});

    //модель объекта-параметра пути
    public static class PathParameterObject extends MappedObject {

        private String someString;

        private Number someNum;

        private Boolean someBool;

        private List<Object> someList;

        public String getSomeString() {
            return someString;
        }

        public PathParameterObject setSomeString(String someString) {
            this.someString = someString;
            return this;
        }

        public Number getSomeNum() {
            return someNum;
        }

        public PathParameterObject setSomeNum(Number someNum) {
            this.someNum = someNum;
            return this;
        }

        public Boolean getSomeBool() {
            return someBool;
        }

        public PathParameterObject setSomeBool(Boolean someBool) {
            this.someBool = someBool;
            return this;
        }

        public List<Object> getSomeList() {
            return someList;
        }

        public PathParameterObject setSomeList(List<Object> someList) {
            this.someList = someList;
            return this;
        }
    }
}
```

Тогда

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

    //Если в качестве objParam передать Map выше
    //то получится следующий путь 
    //Без кодировки: /some/path/to/someString,String value,someNum,10.1111,someBool,true,someList,1,ABC,АБВ,true
    //С кодировкой: /some/path/to/someString,String%20value,someNum,10.1111,someBool,true,someList,1,ABC,%D0%90%D0%91%D0%92,true
    //Тоже самое получится, если передать объект класса PathParameterObject, см. выше
    @URIPath("/some/path/to/{objParam}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething(@PathParameter(name = "objParam") Object array);

    //explode = true означает "развернуть" объект
    //Если в качестве objParam передать Map выше
    //то получится следующий путь 
    //Без кодировки: /some/path/to/someString=String value,someNum=10.1111,someBool=true,someList=1,ABC,АБВ,true
    //С кодировкой: /some/path/to/someString=String%20value,someNum=10.1111,someBool=true,someList=1,ABC,%D0%90%D0%91%D0%92,true
    //Тоже самое получится, если передать объект класса PathParameterObject, см. выше
    @URIPath("/some/path/to/{objParam}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething2(@PathParameter(name = "objParam", explode = true) Object array);

    //Если в качестве objParam передать Map выше
    //то получится следующий путь 
    //Без кодировки: /some/path/to/.someString.String value.someNum.10.1111.someBool.true.someList.1,ABC,АБВ,true
    //С кодировкой: /some/path/to/.someString.String%20value.someNum.10.1111.someBool.true.someList.1,ABC,%D0%90%D0%91%D0%92,true
    //Тоже самое получится, если передать объект класса PathParameterObject, см. выше
    @URIPath("/some/path/to/{objParam}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething3(@PathParameter(name = "objParam", style = LABEL) Object array);

    //explode = true означает "развернуть" объект
    //Если в качестве objParam передать Map выше
    //то получится следующий путь 
    //Без кодировки: /some/path/to/.someString=String value.someNum=10.1111.someBool=true.someList=1,ABC,АБВ,true
    //С кодировкой: /some/path/to/.someString=String%20value.someNum=10.1111.someBool=true.someList=1,ABC,%D0%90%D0%91%D0%92,true
    //Тоже самое получится, если передать объект класса PathParameterObject, см. выше
    @URIPath("/some/path/to/{objParam}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething4(@PathParameter(name = "objParam", style = LABEL, explode = true) Object array);

    //Если в качестве objParam передать Map выше
    //то получится следующий путь 
    //Без кодировки: /some/path/to/;objParam=someString,String value,someNum,10.1111,someBool,true,someList,1,ABC,АБВ,true
    //С кодировкой: /some/path/to/;objParam=someString,String%20value,someNum,10.1111,someBool,true,someList,1,ABC,%D0%90%D0%91%D0%92,true
    //Тоже самое получится, если передать объект класса PathParameterObject, см. выше
    @URIPath("/some/path/to/{objParam}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething3(@PathParameter(name = "objParam", style = MATRIX) Object array);

    //explode = true означает "развернуть" объект
    //Если в качестве objParam передать Map выше
    //то получится следующий путь 
    //Без кодировки: /some/path/to/;someString=String value;someNum=10.1111;someBool=true;someList=1,ABC,АБВ,true
    //С кодировкой: /some/path/to/;someString=String%20value;someNum=10.1111;someBool=true;someList=1,ABC,%D0%90%D0%91%D0%92,true
    //Тоже самое получится, если передать объект класса PathParameterObject, см. выше
    @URIPath("/some/path/to/{objParam}")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething4(@PathParameter(name = "objParam", style = MATRIX, explode = true) Object array);
}
```

</details>

### Запрос (Query-часть URI запроса)

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java

```

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