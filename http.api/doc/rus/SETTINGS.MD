# Настройки http модуля для дальнейшей работы

[Подробнее о настройках](./../../../core.api/doc/rus/SETTINGS.MD).

Следующие настройки могут быть включены в `neptune.global.properties` или в `neptune.properties`, или использованы в качестве свойств\переменных окружения в той среде,
в которой запускаются тесты:

## DEFAULT_HTTP_AUTHENTICATOR

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
import static ru.tinkoff.qa.neptune.http.api.properties.authentification
        .DefaultHttpAuthenticatorProperty.DEFAULT_HTTP_AUTHENTICATOR_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        var authenticator = DEFAULT_HTTP_AUTHENTICATOR_PROPERTY.get().get();
    }
}
```

## DEFAULT_HTTP_COOKIE_HANDLER

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
import static ru.tinkoff.qa.neptune.http.api.properties.cookies
        .DefaultHttpCookieManagerProperty.DEFAULT_HTTP_COOKIE_MANAGER_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        var cookieManager = DEFAULT_HTTP_COOKIE_MANAGER_PROPERTY.get().get();
    }
}
```