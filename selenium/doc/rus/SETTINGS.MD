# Настройки интеграции с Selenium WebDriver для дальнейшей работы

[Подробнее о механизме настроек](./../../../core.api/doc/rus/SETTINGS.MD).

Описанный ниже набор настроек/свойств дополняет [базовый набор настроек/свойств](./../../../core.api/doc/rus/SETTINGS.MD#Основной-набор-настроексвойств), 
которые могут быть включены в `neptune.global.properties` или в `neptune.properties`, или использованы в качестве 
свойств/переменных окружения в той среде, в которой запускаются тесты:

## Запуск WebDriver'а

### WEB_DRIVER_TO_LAUNCH

<details>
  <summary>Нажми, чтобы прочесть</summary>

Настройка указывает класс того WebDriver'а, который должен быть использован в тестах.
Доступные значения (объекты перечисления `ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers`):

- `REMOTE_DRIVER` - запуск `org.openqa.selenium.remote.RemoteWebDriver`
- `CHROME_DRIVER` - запуск `org.openqa.selenium.chrome.ChromeDriver`
- `EDGE_DRIVER` - запуск `org.openqa.selenium.edge.EdgeDriver`
- `FIREFOX_DRIVER` - запуск `org.openqa.selenium.firefox.FirefoxDriver`
- `IE_DRIVER` - запуск `org.openqa.selenium.ie.InternetExplorerDriver`
- `OPERA_DRIVER` - запуск `org.openqa.selenium.opera.OperaDriver`
- `SAFARI_DRIVER` - запуск `org.openqa.selenium.safari.SafariDriver`


Пример настройки:

```properties
#Значение свойства указывается так
WEB_DRIVER_TO_LAUNCH=CHROME_DRIVER
```

```java
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;

import static ru.tinkoff.qa.neptune.selenium.properties
        .SupportedWebDriverProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        SupportedWebDrivers supportedWebDriver = SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY.get();
    }
}
```
Значение по умолчанию `CHROME_DRIVER`.

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-возврата-объектов-перечислений)

</details>

### Настройки Capabilities

<details>
  <summary>Нажми, чтобы прочесть</summary>

[Важная информация](#web_driver_to_launch)

Для указания `Capabilities` используются следующие свойства/настройки:

- `REMOTE_CAPABILITY_SUPPLIERS` - используется если `WEB_DRIVER_TO_LAUNCH=REMOTE_DRIVER`
- `CHROME_CAPABILITY_SUPPLIERS` - используется если `WEB_DRIVER_TO_LAUNCH=CHROME_DRIVER`
- `EDGE_CAPABILITY_SUPPLIERS` - используется если `WEB_DRIVER_TO_LAUNCH=EDGE_DRIVER`
- `FIREFOX_CAPABILITY_SUPPLIERS` - используется если `WEB_DRIVER_TO_LAUNCH=FIREFOX_DRIVER`
- `IE_CAPABILITY_SUPPLIERS` - используется если `WEB_DRIVER_TO_LAUNCH=IE_DRIVER`
- `OPERA_CAPABILITY_SUPPLIERS` - используется если `WEB_DRIVER_TO_LAUNCH=OPERA_DRIVER`
- `SAFARI_CAPABILITY_SUPPLIERS` - используется если `WEB_DRIVER_TO_LAUNCH=SAFARI_DRIVER`

Все перечисленные настройки/свойства используют в качестве значений строки, состоящие из полных имен классов-имплементоров
`ru.tinkoff.qa.neptune.selenium.properties.CapabilitySettingSupplier`, разделенных запятыми. 

Пример:

Предположим, необходимо запускать `ChromeDriver`. Существует ограниченный набор настроек `Capabilities`, которые могут 
быть использованы как по отдельности, так и в комбинации друг с другом, в зависимости от окружения для проведения тестов
с использованием front-end'а, специфики самих тестов и целей прогонов тестов. Опишем каждый из вариантов настройки `Capabilities`.

```java
package org.my.pack;

import org.openqa.selenium.chrome.ChromeOptions;
import ru.tinkoff.qa.neptune.selenium.properties.CapabilitySettingSupplier;

import java.util.HashMap;
import java.util.function.Consumer;

public class ChromeSettingsSupplierHeadless implements CapabilitySettingSupplier<ChromeOptions> {
    
    @Override //Созданный объект ChromeOptions дополняется настройками
    public Consumer<ChromeOptions> get() {
        return chromeOptions -> chromeOptions.addArguments("--headless")
                .addArguments("--no-sandbox")
                .addArguments("--disable-dev-shm-usage");
    }
}
```

```java
package org.my.pack;

import com.browserup.bup.client.ClientUtil;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import ru.tinkoff.qa.neptune.selenium.properties.CapabilitySettingSupplier;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

public class ChromeSettingsSupplierWithDefinedProxy implements CapabilitySettingSupplier<ChromeOptions> {

    @Override
    public Consumer<ChromeOptions> get() {
        return chromeOptions -> {
            Proxy tempProxy = ClientUtil.createSeleniumProxy(new InetSocketAddress("127.0.0.1", 8089));

            //Созданный объект ChromeOptions дополняется настройками
            chromeOptions.setCapability(CapabilityType.PROXY, tempProxy);
            chromeOptions.addArguments("--disable-extensions")
                    .addArguments("--accept-insecure-localhost")
                    .addArguments("--ignore-certificate-errors");
        };
    }
}
```

Предположим, по умолчанию (для локальной отладки тестов, на дефолтном окружении) используются оба описанных варианта. Тогда

```properties
#Значение свойства указывается так
CHROME_CAPABILITY_SUPPLIERS=org.my.pack.ChromeSettingsSupplierHeadless,org.my.pack.ChromeSettingsSupplierWithDefinedProxy
```

Если какой-то из пайплайнов требует запуск тестов, использующих один из вариантов `Capabilities`, тогда в переменных окружения
сборки можно указать 

```properties
#Headless Chrome, без прокси-сервера
CHROME_CAPABILITY_SUPPLIERS=org.my.pack.ChromeSettingsSupplierHeadless
```

```java
import org.openqa.selenium.MutableCapabilities;

import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        MutableCapabilities capabilities = CHROME.get();
    }
}
```

Если необходимо декомпозировать общий набор `Capabilities` как в примере выше, важно чтобы указанные параметры 
не противоречили друг другу.

Так же можно указать некоторые параметры `Capabilities`, не прибегая к описанному выше механизму. Данные параметры могут 
быть использованы независимо от используемого типа WebDriver'а. Список настроек ниже:

#### WEB_DRIVER_CAPABILITY_BROWSER_NAME

Указывает значение `browserName` для тех случаев, когда `WEB_DRIVER_TO_LAUNCH=REMOTE_DRIVER`. Во всех остальных случаях
настройка/свойство игнорируется.

```properties
#Значение свойства указывается так
WEB_DRIVER_TO_LAUNCH=REMOTE_DRIVER
WEB_DRIVER_CAPABILITY_BROWSER_NAME=chrome
```

```java
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes
        .CommonCapabilityProperties.BROWSER_NAME;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        String browserName = (String) BROWSER_NAME.get();
    }
}
```

#### WEB_DRIVER_CAPABILITY_PLATFORM_NAME

Указывает значение `platformName`. Значение должно соответствовать одному из элементов перечисления `org.openqa.selenium.Platform`, 
либо строковому представлению одного из элементов перечисления `org.openqa.selenium.Platform`.

```properties
#Значение свойства указывается так
WEB_DRIVER_CAPABILITY_PLATFORM_NAME=LINUX
```

```properties
#Значение свойства указывается так
WEB_DRIVER_CAPABILITY_PLATFORM_NAME=macOS 10.13
```

```java
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes
        .CommonCapabilityProperties.PLATFORM_NAME;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        String platformName = (String) PLATFORM_NAME.get();
    }
}
```

#### WEB_DRIVER_CAPABILITY_SUPPORTS_JAVASCRIPT

Указывает значение `javascriptEnabled`. Доступные значения - `true` или `false`.

```properties
#Значение свойства указывается так
WEB_DRIVER_CAPABILITY_SUPPORTS_JAVASCRIPT=false
```

```java
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes
        .CommonCapabilityProperties.SUPPORTS_JAVASCRIPT;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        Boolean enableJS = (Boolean) SUPPORTS_JAVASCRIPT.get();
    }
}
```

Значение по умолчанию - `true`

#### WEB_DRIVER_CAPABILITY_BROWSER_VERSION

Указывает значение `browserVersion`.

```properties
#Значение свойства указывается так
WEB_DRIVER_CAPABILITY_BROWSER_VERSION=88.1
```

```java
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes
        .CommonCapabilityProperties.BROWSER_VERSION;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        String version = (String) BROWSER_VERSION.get();
    }
}
```

</details>

## Параметры сессии

### FIND_ONLY_VISIBLE_ELEMENTS

<details>
  <summary>Нажми, чтобы прочесть</summary>

Настройка указывает на необходимость работать только с видимыми элементами страницы. Доступные значения - `true` (рекомендуемое 
для большинства случаев) или `false`. Если значение `true`, то работа будет выполняться только с видимыми на странице элементами,
`false` - со всеми. 

Пример настройки:

```properties
#Значение свойства указывается так
FIND_ONLY_VISIBLE_ELEMENTS=true
```

```java
import static ru.tinkoff.qa.neptune.selenium.properties
        .SessionFlagProperties.FIND_ONLY_VISIBLE_ELEMENTS;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        boolean onlyVisible = FIND_ONLY_VISIBLE_ELEMENTS.get();
    }
}
```

Если свойство не заполнено, то значение по умолчанию `false`.

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-возврата-Boolean)

[О поиске элементов страницы](./ELEMENTS_SEARCHING.MD)

</details>

### Проксирование трафика браузера

#### USE_BROWSER_PROXY

<details>
  <summary>Нажми, чтобы прочесть</summary>

Настройка указывает на необходимость использовать локальный автоматически запущенный прокси-сервер для отслеживания 
трафика браузера. Доступные значения - `true` или `false`. Если значение `true`, то будет происходить запись трафика, 
`false` - запись трафика происходить не будет.

Пример настройки:

```properties
#Значение свойства указывается так
USE_BROWSER_PROXY=true
```

```java
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.USE_BROWSER_PROXY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        boolean useProxy = USE_BROWSER_PROXY.get();
    }
}
```

Если свойство не заполнено, то значение по умолчанию `false`.

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-возврата-Boolean)

[О работе с записанным трафиком](./BROWSER_PROXY.MD)

</details>

#### PROXY_URL_PROPERTY

<details>
  <summary>Нажми, чтобы прочесть</summary>

Http-адрес удаленного прокси-сервера, через который должен идти трафик браузера. Данная настройка опциональна и имеет смысл, 
если [USE_BROWSER_PROXY](#use_browser_proxy)=`true` и через [настройки Capabilities](#Настройки-Capabilities) не передается
объект [org.openqa.selenium.Proxy](https://www.selenium.dev/documentation/en/webdriver/http_proxies/). При такой конфигурации
локальный прокси-сервер встает первым в очередь указанных прокси-серверов. 

Пример настройки:

```properties
#Значение свойства указывается так
PROXY_URL_PROPERTY=http://somehost:2222
```

```java
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.PROXY_URL_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        URL externalProxyURL = PROXY_URL_PROPERTY.get();
    }
}
```

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-создания-и-возврата-объектов-URL)

[О работе с записанным трафиком](./BROWSER_PROXY.MD)

</details>

### KEEP_WEB_DRIVER_SESSION_OPENED

<details>
  <summary>Нажми, чтобы прочесть</summary>

Настройка указывает на необходимость сохранять открытой сессию WebDriver'а настолько долго, насколько это возможно. Доступные 
значения - `true` или `false`. Если значение `true`, то ранее созданная сессия WebDriver'а будет держаться открытой и 
завершится по следующим причинам: 
- завершение полного набора тестов
- открытый браузер простаивает и указаны настройки/свойства для [освобождение ресурсов](./../../../core.api/doc/rus/SETTINGS.MD#Освобождение-ресурсов). 
  
  Подробная статья описывающая [остановку/завершение контекста](./../../../core.api/doc/rus/REFRESHING_STOPPING.MD#Сброс/Остановка).
  
  Статья [про контексты](./../../../core.api/doc/rus/STEPS.MD#Контекст)
- по причинам, не зависящим от Neptune. Например, завершение/обрыв сессии на стороне Selenium Grid или Selenoid

Если значение `false`, то созданная сессия WebDriver'а будет завершаться каждый раз в момент [обновления контекста](./../../../core.api/doc/rus/REFRESHING_STOPPING.MD#Обновление).

В любом случае, если сессия ранее была завершена, и в данный момент нужен открытый браузер — будет открыта новая сессия. 

Пример настройки:

```properties
#Значение свойства указывается так
KEEP_WEB_DRIVER_SESSION_OPENED=true
```

```java
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.KEEP_WEB_DRIVER_SESSION_OPENED;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        boolean keepSession = KEEP_WEB_DRIVER_SESSION_OPENED.get();
    }
}
```

Если свойство не заполнено, то значение по умолчанию `false`.

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-возврата-Boolean)

</details>

### FORCE_WINDOW_MAXIMIZING_ON_START

<details>
  <summary>Нажми, чтобы прочесть</summary>

Настройка указывает на необходимость развората окна браузера на весь монитор при старте новой сессии WebDriver'а. Доступные 
значения - `true` или `false`. Если значение `true`, то при старте новой сессии WebDriver'а окно браузера будет развернуто 
на весь монитор.

Пример настройки:

```properties
#Значение свойства указывается так
FORCE_WINDOW_MAXIMIZING_ON_START=true
```

```java
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.FORCE_WINDOW_MAXIMIZING_ON_START;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        boolean maximize = FORCE_WINDOW_MAXIMIZING_ON_START.get();
    }
}
```

Если свойство не заполнено, то значение по умолчанию `false`.

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-возврата-Boolean)
</details>

## BASE_WEB_DRIVER_URL

<details>
  <summary>Нажми, чтобы прочесть</summary>

Настройка указывает URL стартовой страницы приложения/корневой URL, по которому приложение доступно для открытия в браузере, 
обычно в формате `схема или протокол`://`хост:порт если есть/фрагмент/пути/если/надо`.

Пример настройки:

```properties
#Значение свойства указывается так
BASE_WEB_DRIVER_URL=http://google.com
```

```java
import java.net.URL;

import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        URL baseUrl = BASE_WEB_DRIVER_URL_PROPERTY.get();
    }
}
```

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-создания-и-возврата-объектов-URL)

[О навигации](./NAVIGATION.MD)

[Об управлении контентом](./CONTENT_MANAGEMENT.MD)
</details>

## REMOTE_WEB_DRIVER_URL

<details>
  <summary>Нажми, чтобы прочесть</summary>

[Важная информация](#web_driver_to_launch)

Настройка указывает URL хаба `Selenium Grid` или `Selenoid`, который используется для открытия браузера на удаленном окружении.
Данное свойство/настройка используется тогда, когда `WEB_DRIVER_TO_LAUNCH=REMOTE_DRIVER`, в остальных случаях игнорируется.

Пример настройки:

```properties
#Значение свойства указывается так
REMOTE_WEB_DRIVER_URL=http://host:5555/wd/hub
```

```java
import java.net.URL;

import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.REMOTE_WEB_DRIVER_URL_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        URL remoteUrl = REMOTE_WEB_DRIVER_URL_PROPERTY.get();
    }
}
```

Если свойство не указано, и `WEB_DRIVER_TO_LAUNCH=REMOTE_DRIVER`, тогда происходит локальный старт Selenium-сервера (`org.openqa.selenium.remote.server.SeleniumServer`), 
и неявно используются адрес, по которому доступен локальный хаб.

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-создания-и-возврата-объектов-URL)

[Selenium Grid](https://www.selenium.dev/documentation/en/grid/)

[Selenoid](https://aerokube.com/selenoid/)
</details>

## Время ожидания по умолчанию

### Ожидание элемента(ов)

<details>
  <summary>Нажми, чтобы прочесть</summary>

Свойства `WAITING_FOR_ELEMENTS_TIME_UNIT` и `WAITING_FOR_ELEMENTS_TIME` определяют время ожидания появления элементов страницы 
по умолчанию. Для свойства `WAITING_FOR_ELEMENTS_TIME_UNIT` следует указать значение, соответствующее одному из элементов 
перечисления `java.time.temporal.ChronoUnit`, для свойства `WAITING_FOR_ELEMENTS_TIME` следует указать значение, которое 
может быть прочитано как положительное число типа `java.lang.Long`.

```properties
#Укажем тайм аут ожидания появления элементов на странице
#5 секунд
WAITING_FOR_ELEMENTS_TIME_UNIT=SECONDS
WAITING_FOR_ELEMENTS_TIME=5
```

```java
import java.time.Duration;

import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.ELEMENT_WAITING_DURATION;

public class SomeClass {

    public void someVoid() {
        //пример доступа до величины тайм аута ожидания появления элементов на странице
        Duration timeOut = ELEMENT_WAITING_DURATION.get();
    }
}
```

Время ожидания появления элементов страницы по умолчанию = 1 минуте.

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-возврата-величины-продолжительности)

[Поиск элементов страницы](./ELEMENTS_SEARCHING.MD)
</details>

### Ожидание фрейма

<details>
  <summary>Нажми, чтобы прочесть</summary>

Свойства `WAITING_FOR_FRAME_SWITCHING_TIME_UNIT` и `WAITING_FOR_FRAME_SWITCHING_TIME` определяют время ожидания появления 
фрейма/возможности переключиться во фрейм. Для свойства `WAITING_FOR_FRAME_SWITCHING_TIME_UNIT` следует указать значение, 
соответствующее одному из элементов перечисления `java.time.temporal.ChronoUnit`, для свойства `WAITING_FOR_FRAME_SWITCHING_TIME` 
следует указать значение, которое может быть прочитано как положительное число типа `java.lang.Long`.

```properties
#Укажем тайм аут ожидания появления фрейма/возможности переключиться во фрейм
#5 секунд
WAITING_FOR_FRAME_SWITCHING_TIME_UNIT=SECONDS
WAITING_FOR_FRAME_SWITCHING_TIME=5
```

```java
import java.time.Duration;

import static ru.tinkoff.qa.neptune.selenium.properties
        .WaitingProperties.WAITING_FRAME_SWITCHING_DURATION;

public class SomeClass {

    public void someVoid() {
        //пример доступа до величины тайм аута ожидания 
        //появления фрейма/возможности переключиться во фрейм
        Duration timeOut = WAITING_FRAME_SWITCHING_DURATION.get();
    }
}
```

Время ожидания появления фрейма/возможности переключиться 
во фрейм по умолчанию = 1 минуте.

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-возврата-величины-продолжительности)

[Фреймы](./FRAMES.MD)
</details>

### Ожидание окна

<details>
  <summary>Нажми, чтобы прочесть</summary>

Свойства `WAITING_FOR_WINDOWS_TIME_UNIT` и `WAITING_FOR_WINDOWS_TIME` определяют время ожидания появления окна/вкладки браузера. 
Для свойства `WAITING_FOR_WINDOWS_TIME_UNIT` следует указать значение, соответствующее одному из элементов перечисления 
`java.time.temporal.ChronoUnit`, для свойства `WAITING_FOR_WINDOWS_TIME` следует указать значение, которое может быть 
прочитано как положительное число типа `java.lang.Long`.

```properties
#Укажем тайм аут ожидания появления окна/вкладки браузера
#5 секунд
WAITING_FOR_WINDOWS_TIME_UNIT=SECONDS
WAITING_FOR_WINDOWS_TIME=5
```

```java
import java.time.Duration;

import static ru.tinkoff.qa.neptune.selenium.properties
        .WaitingProperties.WAITING_WINDOW_TIME_DURATION;

public class SomeClass {

    public void someVoid() {
        //пример доступа до величины тайм аута ожидания 
        //появления окна/вкладки браузера
        Duration timeOut = WAITING_WINDOW_TIME_DURATION.get();
    }
}
```

Время ожидания появления окна/вкладки браузера по умолчанию = 1 минуте.

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-возврата-величины-продолжительности)

[Окна/Вкладки](./WINDOWS.MD)
</details>

### Ожидание алерта

<details>
  <summary>Нажми, чтобы прочесть</summary>

Свойства `WAITING_FOR_ALERTS_TIME_UNIT` и `WAITING_FOR_ALERTS_TIME` определяют время ожидания появления алерта.
Для свойства `WAITING_FOR_ALERTS_TIME_UNIT` следует указать значение, соответствующее одному из элементов перечисления
`java.time.temporal.ChronoUnit`, для свойства `WAITING_FOR_ALERTS_TIME` следует указать значение, которое может быть
прочитано как положительное число типа `java.lang.Long`.

```properties
#Укажем тайм аут ожидания появления алерта
#5 секунд
WAITING_FOR_ALERTS_TIME_UNIT=SECONDS
WAITING_FOR_ALERTS_TIME=5
```

```java
import java.time.Duration;

import static ru.tinkoff.qa.neptune.selenium.properties
        .WaitingProperties.WAITING_ALERT_TIME_DURATION;

public class SomeClass {

    public void someVoid() {
        //пример доступа до величины тайм аута ожидания появления алерта
        Duration timeOut = WAITING_ALERT_TIME_DURATION.get();
    }
}
```

Время ожидания появления алерта по умолчанию = 1 минуте.

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-возврата-величины-продолжительности)

[Алерты](./ALERTS.MD)
</details>

### Ожидание загрузки страницы

<details>
  <summary>Нажми, чтобы прочесть</summary>

Свойства `WAITING_FOR_PAGE_LOADED_TIME_UNIT` и `WAITING_FOR_PAGE_LOADED_TIME` определяют время ожидания полной загрузки 
страницы. Для свойства `WAITING_FOR_PAGE_LOADED_TIME_UNIT` следует указать значение, соответствующее одному из элементов 
перечисления `java.time.temporal.ChronoUnit`, для свойства `WAITING_FOR_PAGE_LOADED_TIME` следует указать значение, 
которое может быть прочитано как положительное число типа `java.lang.Long`.

```properties
#Укажем тайм аут ожидания полной загрузки страницы
#5 секунд
WAITING_FOR_PAGE_LOADED_TIME_UNIT=SECONDS
WAITING_FOR_PAGE_LOADED_TIME=5
```

```java
import java.time.Duration;

import static ru.tinkoff.qa.neptune.selenium.properties
        .WaitingProperties.WAITING_FOR_PAGE_LOADED_DURATION;

public class SomeClass {

    public void someVoid() {
        //пример доступа до величины тайм аута ожидания полной загрузки страницы
        Duration timeOut = WAITING_FOR_PAGE_LOADED_DURATION.get();
    }
}
```

Время ожидания полной загрузки страницы по умолчанию = 1 минуте.

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-возврата-величины-продолжительности)
</details>

## WEB_DRIVER_CREDENTIALS

<details>
  <summary>Нажми, чтобы прочесть</summary>

Настройка предоставляет объект `ru.tinkoff.qa.neptune.selenium.authentication.BrowserCredentials`, который выполняет логин/аутентификацию 
в приложении через браузер.

Пример настройки:

```java
package org.my.pack;

import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.selenium.authentication.BrowserCredentials;

//Класс, чьи объекты выполняют логин/аутентификацию в приложении через браузер. 
public class SomeBrowserCredentials extends BrowserCredentials<SomeCreds> {

  @Override
  protected boolean areCredentialsDifferent(SomeCreds credentials) {
    //проверка того, изменилась ли информация о пользователе или нет
  }

  @Override
  protected void authentication(WebDriver webDriver, boolean isNewSession) {
      //логика логина/аутентификацию
  }
}
```

```properties
#Значение свойства указывается так
WEB_DRIVER_CREDENTIALS=org.my.pack.SomeBrowserCredentials
```

```java
import org.my.pack.SomeBrowserCredentials;

import static ru.tinkoff.qa.neptune.selenium.properties
        .WebDriverCredentialsProperty.WEB_DRIVER_CREDENTIALS_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
      SomeBrowserCredentials creds = (SomeBrowserCredentials) WEB_DRIVER_CREDENTIALS_PROPERTY.get();
    }
}
```

[Подробнее о механизме работы подобных свойств](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-создания-и-возврата-объектов)

[Про логин/аутентификацию в браузере](./LOGIN_IN_BROWSER.MD)
</details>

## WEB_DRIVER_DEFAULT_AUTO_SCROLLER

<details>
  <summary>Нажми, чтобы прочесть</summary>

Настройка предоставляет объект `ru.tinkoff.qa.neptune.selenium.auto.scrolling.AutoScroller`, который выполняет автоскроллирование/
автоматический фокус на нужном элементе.

Пример настройки:

```java
package org.my.pack;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.auto.scrolling.AutoScroller;

//Класс, чьи объекты выполняют выполняет автоскроллирование/
//автоматический фокус на нужном элементе по умолчанию. 
public class SomeScrollWebElementIntoView extends AutoScroller {

  public MockScrollWebElementIntoView(WebDriver driver) {
    super(driver);
  }

  @Override
  protected void scrollIntoView(WebElement e) {
      //логика автоскроллирования/фокуса
  }
}
```

```properties
#Значение свойства указывается так
WEB_DRIVER_DEFAULT_AUTO_SCROLLER=org.my.pack.SomeScrollWebElementIntoView
```

```java
import org.my.pack.SomeScrollWebElementIntoView;

import static ru.tinkoff.qa.neptune.selenium.properties
        .DefaultScrollerProperty.DEFAULT_SCROLLER_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
      SomeScrollWebElementIntoView scroller = (SomeScrollWebElementIntoView) DEFAULT_SCROLLER_PROPERTY.get();
    }
}
```

[Про авто-скроллирование](./AUTO_SCROLLING.MD)

</details>