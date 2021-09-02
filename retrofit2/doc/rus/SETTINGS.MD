# Настройки для дальнейшей работы

[Подробнее о механизме настроек](./../../../core.api/doc/rus/SETTINGS.MD).

Описанный ниже набор настроек/свойств дополняет [базовый набор настроек/свойств](./../../../core.api/doc/rus/SETTINGS.MD#Основной-набор-настроексвойств),
которые могут быть включены в `neptune.global.properties` или в `neptune.properties`, или использованы в качестве
свойств/переменных окружения/среды в той среде, в которой запускаются тесты.

## Оглавление
- [DEFAULT_RETROFIT_URL](#default_retrofit_url)
- [Недефолтные корневые URL](#Недефолтные-корневые-url)
- [DEFAULT_RETROFIT](#default_retrofit)
- [Недефолтные настройки Retrofit](#Недефолтные-настройки-retrofit)

## DEFAULT_RETROFIT_URL

Настройка указывает URL сервисов по умолчанию. Это корневой адрес приложения/приложений формате `схема или протокол`://`хост:порт если есть/фрагмент/пути/если/надо/`.

Пример настройки:

```properties
#Значение свойства указывается так
DEFAULT_RETROFIT_URL=http://my.api.domain.com:2000/
```

```java
import java.net.URL;

import static ru.tinkoff.qa.neptune.retrofit2.properties.DefaultRetrofitURLProperty.DEFAULT_RETROFIT_URL_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        URL defaultUrl = DEFAULT_RETROFIT_URL_PROPERTY.get();
    }
}
```

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-создания-и-возврата-объектов-URL)

Данная настройка очень полезна для [инициализации полей](CALL_RESULTS.MD#Объявление-полей-типов-моделей-API) в тестовом классе.

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)


## Недефолтные корневые URL

Полезная информация:

- [Механизм работы со свойствами/переменными окружения / среды](./../../../core.api/doc/rus/SETTINGS.MD) - полезно в целом
- [Свойства для создания и возврата объектов URL](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-создания-и-возврата-объектов-URL)
- [Связывание классов](./../../../core.api/doc/rus/CLASS_BINDING.MD)

В тестах возможна необходимость взаимодействовать с сервисами, чей хост / корневой URL отличается от [дефолтного](#DEFAULT_RETROFIT_URL).

Предположим, у нас есть следующие модели тестируемых API, которые деплоятся на другой хост. 

```java
import retrofit2.http.GET;
import ru.tinkoff.qa.neptune.retrofit2.tests.DtoObject;

import java.util.List;

public interface SomeServiceAPI {

    @GET("/get/something")
    List<DtoObject> getSomething();
}
```

```java
import retrofit2.http.GET;
import ru.tinkoff.qa.neptune.retrofit2.tests.DtoObject;

import java.util.List;

public interface ExtendedServiceAPI 
        extends SomeServiceAPI { //наследование для демонстрации возможностей

    @GET("/get/something/else")
    List<DtoObject> getSomethingElse();
}
```

```java
import retrofit2.http.GET;
import ru.tinkoff.qa.neptune.retrofit2.tests.DtoObject;

import java.util.List;

public interface AnotherServiceAPI 
        extends SomeServiceAPI { //наследование для демонстрации возможностей

    @GET("/get/some/more")
    List<DtoObject> getSomeMore();
}
```

Добавим возможность указывать/читать свойства/переменные окружения для специфических адресов хостов API выше.

```java
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;
import ru.tinkoff.qa.neptune.core.api.binding.Bind;

public enum ServiceRoot implements URLValuePropertySupplier {
    @PropertyDefaultValue("http://host1:1111") //Корневой URI/URL, используемый на дефлтном окружении/при отладке
    @PropertyName("ROOT_OF_SERVICE1") //Название свойства/переменной окружения
    @Bind(to = AnotherServiceAPI.class) //жесткая привязка к определенному классу
    //если что-то похожее нужно для еще одного сервиса то
    //@Bind(to = SomeAPI.class)
    SERVICE1,

    @PropertyDefaultValue("http://host2:2222") //Корневой URI/URL, используемый на дефлтном окружении/при отладке
    @PropertyName("ROOT_OF_SERVICE2") //Название свойства/переменной окружения
    //Еще один вариант привязки, чем-то напоминает wildcard
    @Bind(to = SomeServiceAPI.class, //указывается обобщенный интерфейс, моделирующий API сервисов
            withSubclasses = true, //указываем, что применимо для интерфейсов-наследников
            exclude = AnotherServiceAPI.class) //кроме перечисленных, имеет смысл если withSubclasses = true
    SERVICE2
}
```

Данный механизм очень полезен для [инициализации полей](CALL_RESULTS.MD#Объявление-полей-типов-моделей-API) в тестовом классе.

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## DEFAULT_RETROFIT

Настройка предоставляет объект `ru.tinkoff.qa.neptune.retrofit2.service.setup.RetrofitBuilderSupplier`, который выполняет
подготовку объекта `retrofit2.Retrofit.Builder` для инициализации моделей API.

Пример настройки:

```java
package org.my.pack;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import ru.tinkoff.qa.neptune.retrofit2.service.setup.RetrofitBuilderSupplier;

public class MyRetrofitBuilderSupplier extends RetrofitBuilderSupplier {

    @Override
    protected Retrofit.Builder prepareRetrofitBuilder() {
        return new Retrofit.Builder() //далее указываем специфические настройки Retrofit.
                //Если где-то уже есть фабрика таких настроек, ее можно повторно использовать
                ;
    }

    @Override
    protected OkHttpClient.Builder prepareClientBuilder() {
        return new OkHttpClient().newBuilder() //далее указываем специфические настройки 
        //http-клиента. Если где-то уже есть фабрика таких настроек, ее можно повторно использовать
        ;
    }
}
```

```properties
#Значение свойства указывается так
DEFAULT_RETROFIT=org.my.pack.MyRetrofitBuilderSupplier
```

```java
import org.my.pack.MyRetrofitBuilderSupplier;

import static ru.tinkoff.qa.neptune.retrofit2.properties.DefaultRetrofitProperty.DEFAULT_RETROFIT_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        MyRetrofitBuilderSupplier r = (MyRetrofitBuilderSupplier) DEFAULT_RETROFIT_PROPERTY.get();
    }
}
```

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-создания-и-возврата-объектов)

Данная настройка очень полезна для [инициализации полей](CALL_RESULTS.MD#Объявление-полей-типов-моделей-API) в тестовом классе.

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Недефолтные настройки Retrofit

Полезная информация:

- [Связывание классов](./../../../core.api/doc/rus/CLASS_BINDING.MD)

В тестах возможна необходимость взаимодействовать с сервисами, для которых настройки Retrofit отличается от [дефолтных](#DEFAULT_RETROFIT).

Предположим, у нас есть следующие модели тестируемых API, которые требуют специфических настроек Retrofit.

```java
import retrofit2.http.GET;
import ru.tinkoff.qa.neptune.retrofit2.tests.DtoObject;

import java.util.List;

public interface SomeServiceAPI {

    @GET("/get/something")
    List<DtoObject> getSomething();
}
```

```java
import retrofit2.http.GET;
import ru.tinkoff.qa.neptune.retrofit2.tests.DtoObject;

import java.util.List;

public interface ExtendedServiceAPI 
        extends SomeServiceAPI { //наследование для демонстрации возможностей

    @GET("/get/something/else")
    List<DtoObject> getSomethingElse();
}
```

```java
import retrofit2.http.GET;
import ru.tinkoff.qa.neptune.retrofit2.tests.DtoObject;

import java.util.List;

public interface AnotherServiceAPI 
        extends SomeServiceAPI { //наследование для демонстрации возможностей

    @GET("/get/some/more")
    List<DtoObject> getSomeMore();
}
```

Описываем настройки Retrofit и связываем их с классами выше.

```java
package org.my.pack;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import ru.tinkoff.qa.neptune.retrofit2.service.setup.RetrofitBuilderSupplier;

@Bind(to = AnotherServiceAPI.class) //жесткая привязка к определенному классу
//если что-то похожее нужно для еще одного сервиса то
//@Bind(to = SomeAPI.class)
public class MyRetrofitBuilderSupplier extends RetrofitBuilderSupplier {

    @Override
    protected Retrofit.Builder prepareRetrofitBuilder() {
        return new Retrofit.Builder() //далее указываем специфические настройки Retrofit.
                //Если где-то уже есть фабрика таких настроек, ее можно повторно использовать
                ;
    }

    @Override
    protected OkHttpClient.Builder prepareClientBuilder() {
        return new OkHttpClient().newBuilder() //далее указываем специфические настройки 
        //http-клиента. Если где-то уже есть фабрика таких настроек, ее можно повторно использовать
        ;
    }
}
```

```java
package org.my.pack;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import ru.tinkoff.qa.neptune.retrofit2.service.setup.RetrofitBuilderSupplier;

@Bind(to = SomeServiceAPI.class, //указывается обобщенный интерфейс, моделирующий API сервисов
        withSubclasses = true, //указываем, что применимо для интерфейсов-наследников
        exclude = AnotherServiceAPI.class) //кроме перечисленных, имеет смысл если withSubclasses = true
public class MyRetrofitBuilderSupplier extends RetrofitBuilderSupplier {

    @Override
    protected Retrofit.Builder prepareRetrofitBuilder() {
        return new Retrofit.Builder() //далее указываем специфические настройки Retrofit.
                //Если где-то уже есть фабрика таких настроек, ее можно повторно использовать
                ;
    }

    @Override
    protected OkHttpClient.Builder prepareClientBuilder() {
        return new OkHttpClient().newBuilder() //далее указываем специфические настройки 
        //http-клиента. Если где-то уже есть фабрика таких настроек, ее можно повторно использовать
        ;
    }
}
```

Данный механизм очень полезен для [инициализации полей](CALL_RESULTS.MD#Объявление-полей-типов-моделей-API) в тестовом классе.

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
