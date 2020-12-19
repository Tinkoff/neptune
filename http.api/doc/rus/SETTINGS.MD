# Настройки http модуля для дальнейшей работы

[Подробнее о механизме настроек](./../../../core.api/doc/rus/SETTINGS.MD).

Следующие настройки могут быть включены в `neptune.global.properties` или в `neptune.properties`, или использованы в качестве свойств\переменных окружения в той среде,
в которой запускаются тесты:

## DEFAULT_HTTP_AUTHENTICATOR
<details>
  <summary>Нажми, чтобы прочесть</summary>

Настройка предоставляет объект `java.net.Authenticator`, подготовленный нужным образом, и который используется http клиентом по умолчанию.

Пример настройки:

```java
package org.my.pack;

import java.net.*;
import ru.tinkoff.qa.neptune.http.api.properties.authentification.DefaultHttpAuthenticatorProperty.AuthenticatorSupplier;

//Создаем поставщика, который будет поставлять объекты класса java.net.Authenticator, которые потребляет
//java.net.http.HttpClient;
public class MyAuthenticatorSupplier extends DefaultHttpAuthenticatorProperty.AuthenticatorSupplier {

    @Override
    public Authenticator get() {
        //Тут создается объект, который будет использован http клиентом
    }
}
```

```properties
#Значение свойства указывается так
DEFAULT_HTTP_AUTHENTICATOR=org.my.pack.MyAuthenticatorSupplier
```

```java
import java.net.*;

import static ru.tinkoff.qa.neptune.http.api.properties.authentification
        .DefaultHttpAuthenticatorProperty.DEFAULT_HTTP_AUTHENTICATOR_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        Authenticator authenticator = DEFAULT_HTTP_AUTHENTICATOR_PROPERTY.get().get();
    }
}
```

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-создания-и-возврата-объектов)

</details>

## DEFAULT_HTTP_COOKIE_HANDLER
<details>
  <summary>Нажми, чтобы прочесть</summary>

Настройка предоставляет объект `java.net.CookieManager`, подготовленный нужным образом, и который используется http клиентом по умолчанию.

Пример настройки:

```java
package org.my.pack;

import java.net.*;
import ru.tinkoff.qa.neptune.http.api.properties.cookies.DefaultHttpCookieManagerProperty.CookieManagerSupplier;

//Создаем поставщика, который будет поставлять объекты класса java.net.CookieManager, которые потребляет
//java.net.http.HttpClient;
public class MyCookieManagerSupplier extends DefaultHttpCookieManagerProperty.CookieManagerSupplier {

    @Override
    public CookieManager get() {
        //Тут создается объект, который будет использован http клиентом
    }
}
```

```properties
#Значение свойства указывается так
DEFAULT_HTTP_COOKIE_HANDLER=org.my.pack.MyCookieManagerSupplier
```

```java
import java.net.*;

import static ru.tinkoff.qa.neptune.http.api.properties.cookies
        .DefaultHttpCookieManagerProperty.DEFAULT_HTTP_COOKIE_MANAGER_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        CookieManager cookieManager = DEFAULT_HTTP_COOKIE_MANAGER_PROPERTY.get().get();
    }
}
```

Если свойство не заполнено, то используется объект `CookieManager`, созданный вызовом конструктора `new CookieManager()`, 
без каких либо дополнительных настроек.

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-создания-и-возврата-объектов)

</details>

## DEFAULT_HTTP_EXECUTOR
<details>
  <summary>Нажми, чтобы прочесть</summary>

Настройка предоставляет объект `java.util.concurrent.Executor`, подготовленный нужным образом, и который используется http клиентом по умолчанию.

Пример настройки:

```java
package org.my.pack;

import java.net.*;
import ru.tinkoff.qa.neptune.http.api.properties.executor.DefaultHttpExecutorProperty.ExecutorSupplier;

//Создаем поставщика, который будет поставлять объекты класса java.util.concurrent.Executor, которые потребляет
//java.net.http.HttpClient;
public class MyExecutorSupplier extends DefaultHttpExecutorProperty.ExecutorSupplier {

    @Override
    public Executor get() {
        //Тут создается объект, который будет использован http клиентом
    }
}
```

```properties
#Значение свойства указывается так
DEFAULT_HTTP_EXECUTOR=org.my.pack.MyExecutorSupplier
```

```java
import java.net.*;

import static ru.tinkoff.qa.neptune.http.api.properties.executor
        .DefaultHttpExecutorProperty.DEFAULT_HTTP_EXECUTOR_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        Executor executor = DEFAULT_HTTP_EXECUTOR_PROPERTY.get().get();
    }
}
```

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-создания-и-возврата-объектов)
</details>

## DEFAULT_HTTP_PRIORITY
<details>
  <summary>Нажми, чтобы прочесть</summary>

Настройка определяет приоритет запросов, отправляемых http-клиентом. Актуально для HTTP 2.0. Значение должно быть в диапазоне 
от 1 до 256 (включительно).

Пример настройки:


```properties
#Значение свойства указывается так
DEFAULT_HTTP_PRIORITY=2
```

```java
import static ru.tinkoff.qa.neptune.http.api.properties.priority
        .DefaultHttpPriorityProperty.DEFAULT_HTTP_PRIORITY_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        int priority = DEFAULT_HTTP_PRIORITY_PROPERTY.get();
    }
}
```

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-возврата-чисел)
</details>

## DEFAULT_HTTP_PROTOCOL_VERSION
<details>
  <summary>Нажми, чтобы прочесть</summary>

Настройка указывает версию http-протокола. Доступные значения - `HTTP_1_1` и `HTTP_2` - объекты перечисления `java.net.http.HttpClient.Version`.


Пример настройки:

```properties
#Значение свойства указывается так
DEFAULT_HTTP_PROTOCOL_VERSION=HTTP_2
```

```java
import java.net.http.HttpClient;

import static ru.tinkoff.qa.neptune.http.api.properties.protocol.version
        .DefaultHttpProtocolVersionProperty.DEFAULT_HTTP_PROTOCOL_VERSION_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        HttpClient.Version version = DEFAULT_HTTP_PROTOCOL_VERSION_PROPERTY.get();
    }
}
```

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-возврата-объектов-перечислений)
</details>

## DEFAULT_HTTP_PROXY_SELECTOR
<details>
  <summary>Нажми, чтобы прочесть</summary>

Настройка предоставляет объект `java.net.ProxySelector`, подготовленный нужным образом, и который используется http клиентом по умолчанию.

Пример настройки:

```java
package org.my.pack;

import java.net.*;
import ru.tinkoff.qa.neptune.http.api.properties.proxy.DefaultHttpProxySelectorProperty.ProxySelectorSupplier;

//Создаем поставщика, который будет поставлять объекты класса java.net.ProxySelector, которые потребляет
//java.net.http.HttpClient;
public class MyProxySelectorSupplier extends DefaultHttpProxySelectorProperty.ProxySelectorSupplier {

    @Override
    public ProxySelector get() {
        //Тут создается объект, который будет использован http клиентом
    }
}
```

```properties
#Значение свойства указывается так
DEFAULT_HTTP_PROXY_SELECTOR=org.my.pack.MyProxySelectorSupplier
```

```java
import java.net.*;

import static ru.tinkoff.qa.neptune.http.api.properties.proxy
        .DefaultHttpProxySelectorProperty.DEFAULT_HTTP_PROXY_SELECTOR_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        ProxySelector selector = DEFAULT_HTTP_PROXY_SELECTOR_PROPERTY.get().get();
    }
}
```

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-создания-и-возврата-объектов)
</details>

## DEFAULT_HTTP_REDIRECT_POLICY
<details>
  <summary>Нажми, чтобы прочесть</summary>

Настройка политику перенаправления http-запросов по умолчанию. Доступные значения - `NEVER`, `ALWAYS` и `NORMAL` - объекты перечисления `java.net.http.HttpClient.Redirect`.


Пример настройки:

```properties
#Значение свойства указывается так
DEFAULT_HTTP_REDIRECT_POLICY=ALWAYS
```

```java
import java.net.http.HttpClient;

import static ru.tinkoff.qa.neptune.http.api.properties.redirect
        .DefaultHttpRedirectProperty.DEFAULT_HTTP_REDIRECT_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        HttpClient.Redirect redirectPolicy = DEFAULT_HTTP_REDIRECT_PROPERTY.get();
    }
}
```

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-возврата-объектов-перечислений)
</details>

## DEFAULT_HTTP_SSL_CONTEXT
<details>
  <summary>Нажми, чтобы прочесть</summary>

Настройка предоставляет объект `javax.net.ssl.SSLContext`, подготовленный нужным образом, и который используется http клиентом по умолчанию.

Пример настройки:

```java
package org.my.pack;

import java.net.ssl.*;
import ru.tinkoff.qa.neptune.http.api.properties.ssl.DefaultHttpSslContextProperty.SslContextSupplier;

//Создаем поставщика, который будет поставлять объекты класса javax.net.ssl.SSLContext, которые потребляет
//java.net.http.HttpClient;
public class MySslContextSupplier extends DefaultHttpSslContextProperty.SslContextSupplier {

    @Override
    public SSLContext get() {
        //Тут создается объект, который будет использован http клиентом
    }
}
```

```properties
#Значение свойства указывается так
DEFAULT_HTTP_SSL_CONTEXT=org.my.pack.MySslContextSupplier
```

```java
import java.net.ssl.*;

import static ru.tinkoff.qa.neptune.http.api.properties.ssl
        .DefaultHttpSslContextProperty.DEFAULT_HTTP_SSL_CONTEXT_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        SSLContext sslContext = DEFAULT_HTTP_SSL_CONTEXT_PROPERTY.get().get();
    }
}
```

Если свойство никак не настраивать, то по умолчанию в качестве значения свойства используются объекты класса `ru.tinkoff.qa.neptune.http.api.properties.ssl.AllTrustedSslContextSupplier`.
Данный поставщик возвращает объекты `javax.net.ssl.SSLContext`, которые принимают любые сертификаты.

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-создания-и-возврата-объектов)
</details>

## DEFAULT_HTTP_SSL_PARAMETERS
<details>
  <summary>Нажми, чтобы прочесть</summary>

Настройка предоставляет объект `javax.net.ssl.SSLParameters`, подготовленный нужным образом, и который используется http клиентом по умолчанию.

Пример настройки:

```java
package org.my.pack;

import java.net.ssl.*;
import ru.tinkoff.qa.neptune.http.api.properties.ssl.DefaultHttpSslParametersProperty.SslParametersSupplier;

//Создаем поставщика, который будет поставлять объекты класса javax.net.ssl.SSLParameters, которые потребляет
//java.net.http.HttpClient;
public class MySslParametersSupplier extends DefaultHttpSslParametersProperty.SslParametersSupplier {

    @Override
    public SSLParameters get() {
        //Тут создается объект, который будет использован http клиентом
    }
}
```

```properties
#Значение свойства указывается так
DEFAULT_HTTP_SSL_PARAMETERS=org.my.pack.MySslParametersSupplier
```

```java
import java.net.ssl.*;

import static ru.tinkoff.qa.neptune.http.api.properties.ssl
        .DefaultHttpSslParametersProperty.DEFAULT_HTTP_SSL_PARAMETERS_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        SSLParameters sslParameters = DEFAULT_HTTP_SSL_PARAMETERS_PROPERTY.get().get();
    }
}
```

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-создания-и-возврата-объектов)
</details>

## Время подключения http клиента к сервису по умолчанию
<details>
  <summary>Нажми, чтобы прочесть</summary>

Свойства `DEFAULT_HTTP_CONNECTION_TIME_UNIT` и `DEFAULT_HTTP_CONNECTION_TIME` определяют время подключения http-клиента к сервису по умолчанию.
Для свойства `DEFAULT_HTTP_CONNECTION_TIME_UNIT` следует указать значение, соответствующее одному из элементов перечисления `java.time.temporal.ChronoUnit`,
для свойства `DEFAULT_HTTP_CONNECTION_TIME` следует указать значение, которое может быть прочитано как положительное число типа `java.lang.Long`.

```properties
#Укажем тайм аут для подключения http клиента к сервису
#5 секунд
DEFAULT_HTTP_CONNECTION_TIME_UNIT=SECONDS
DEFAULT_HTTP_CONNECTION_TIME=5
```

```java
import java.time.Duration;

import static ru.tinkoff.qa.neptune.http.api.properties.time
        .DefaultConnectTimeOutProperty.DEFAULT_CONNECT_TIME_OUT_PROPERTY;

public class SomeClass {

    public void someVoid() {
        //пример доступа до величины тайм аута для подключения http клиента к сервису
        Duration timeOut = DEFAULT_CONNECT_TIME_OUT_PROPERTY.get();
    }
}
```

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-возврата-величины-продолжительности)
</details>

## Сериализация и десериализация тел http запросов и ответов

### JSON DEFAULT_JSON_MAPPER
<details>
  <summary>Нажми, чтобы прочесть</summary>

Настройка предоставляет объект `com.fasterxml.jackson.databind.ObjectMapper`, подготовленный нужным образом. Этот объект будет 
использован по умолчанию для десериализации JSON-тел ответов сервисов на http-запросы и сериализации тел http-запросов в JSON.

Пример настройки:

```java
package org.my.pack;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.function.Supplier;

//Создаем поставщика, который будет поставлять объекты класса com.fasterxml.jackson.databind.ObjectMapper
public class MyObjectMapperSupplier implements Supplier<ObjectMapper> {

    @Override
    public ObjectMapper get() {
        //Тут создается объект, который будет использован http клиентом
    }
}
```

```properties
#Значение свойства указывается так
DEFAULT_JSON_MAPPER=org.my.pack.MyObjectMapperSupplier
```

```java
import com.fasterxml.jackson.databind.ObjectMapper;

import static ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper.JSON;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        ObjectMapper mapper = JSON.getMapper(); //Элемент JSON перечисления 
        // ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper работает по такому алгоритму: 
        // - оно пытается обратиться к свойству DEFAULT_JSON_MAPPER 
        // - если оно было настроено, тогда вернется то, что возвращает поставщик (методом get()) 
        // - если оно не было настроено, тогда вернется new ObjectMapper()
    }
}
```

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-создания-и-возврата-объектов)
</details>

### XML DEFAULT_XML_MAPPER
<details>
  <summary>Нажми, чтобы прочесть</summary>

Настройка предоставляет объект `com.fasterxml.jackson.dataformat.xml.XmlMapper`, подготовленный нужным образом. Этот объект будет
использован по умолчанию для десериализации XML-тел ответов сервисов на http-запросы и сериализации тел http-запросов в XML.

Пример настройки:

```java
package org.my.pack;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.util.function.Supplier;

//Создаем поставщика, который будет поставлять объекты класса com.fasterxml.jackson.dataformat.xml.XmlMapper
public class MyXmlMapperSupplier implements Supplier<XmlMapper> {

    @Override
    public XmlMapper get() {
        //Тут создается объект, который будет использован http клиентом
    }
}
```

```properties
#Значение свойства указывается так
DEFAULT_XML_MAPPER=org.my.pack.MyXmlMapperSupplier
```

```java
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import static ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper.XML;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        XmlMapper mapper = (XmlMapper) XML.getMapper(); //Элемент XML перечисления 
        // ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper работает по такому алгоритму: 
        // - оно пытается обратиться к свойству DEFAULT_XML_MAPPER 
        // - если оно было настроено, тогда вернется то, что возвращает поставщик (методом get()) 
        // - если оно не было настроено, тогда вернется new XmlMapper()
    }
}
```

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-создания-и-возврата-объектов)
</details>

## END_POINT_OF_TARGET_API
<details>
    <summary>Нажми, чтобы прочесть</summary>

Настройка указывает URL сервисов по умолчанию. Это корневой адрес приложения/приложений, обычно в формате `схема\протокол`://`хост`:`порт если есть`.

Пример настройки:

```properties
#Значение свойства указывается так
END_POINT_OF_TARGET_API=http://my.api.domain.com:2000
```

```java
import java.net.URL;

import static ru.tinkoff.qa.neptune.http.api.properties.end.point
        .DefaultEndPointOfTargetAPIProperty.DEFAULT_END_POINT_OF_TARGET_API_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        URL defaultUrl = DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.get();
    }
}
```

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-создания-и-возврата-объектов-URL)

Пользователь может самостоятельно создавать аналогичные типизированные свойства и привязывать их к сервисам, чей корневой URL
отличается от дефолтного. Об этих нюансах подробно в [Дефолтный корневой URL](./MAPPING.MD#Дефолтный-корневой-URL) и в [Недефолтные корневые URL](./MAPPING.MD#Недефолтные-корневые-URL)

</details>

## Http лог
<details>
    <summary>Нажми, чтобы прочесть</summary>

Neptune не имеет на поддержке такого свойства. `jdk.httpclient.HttpClient.log` - это стандартное свойство/настройка Java начиная с 11 версии.
Но его точно так же можно указать в `neptune.global.properties` или в `neptune.properties`.

```properties
#Логирование всех событий
jdk.httpclient.HttpClient.log=all
```

Включенный лог будет выводиться на консоль, а так же попадет в виде приложенных файлов в отчет о прохождении тестов, если выполнена 
[соответствующая настройка](./../../../core.api/doc/rus/EVENTS.MD#Когда-следует-создавать-аттачи-в-отчете-о-прохождении)

</details>