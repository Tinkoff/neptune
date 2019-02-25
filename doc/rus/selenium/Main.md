# Интеграция с Selenium WebDriver API

Модуль разработан для интеграции с [Selenium WebDriver](https://www.seleniumhq.org/docs/03_webdriver.jsp). [API](https://tinkoffcreditsystems.github.io/neptune/selenium/)


# Начало работы

## Требования
 
 - Операционная система - Windows/Mac Os X/Linux
 - Java Development Kit 11
 - [maven](https://maven.apache.org/) или [gradle](https://gradle.org/)
 
## Зависимости

### Maven

```xml
<dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>selenium</artifactId>
            <version>${neptune.version}</version>
        </dependency>
</dependencies>

``` 

### Gradle

`compile group: 'ru.tinkoff.qa.neptune', name: 'selenium', version: neptuneVersion`

## Настройки

### Параметры запуска браузера

`web.driver.to.launch` - Класс вебдрайвера, который следует запустить. Значение должно соответствовать имени одного из элементов перечисления [SupportedWebDrivers](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/properties/SupportedWebDrivers.html).
Если настройка не установлена, то по умолчанию запускается `ChromeDriver` (браузер Chrome, локально)

Примеры: 
```properties
#В файле general.properties

web.driver.to.launch = FIREFOX_DRIVER
```

```java
//Программно
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers.CHROME_DRIVER;
import static ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDriverProperty.WEB_DRIVER_TO_LAUNCH;

//...
WEB_DRIVER_TO_LAUNCH.accept(CHROME_DRIVER.name());
```

```java
//Пример получения значения настройки
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.ELEMENT_WAITING_DURATION;

//...
var driverToLaunchSetting = WEB_DRIVER_TO_LAUNCH.get();
```

`web.driver.capability.browserName` - Имя браузера. Настройка имеет смысл, если выставлено значние `web.driver.to.launch = REMOTE_DRIVER`. Имя вызываемого браузера. Соответствует именам, перечисленным в [BrowserType](https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/remote/BrowserType.html):

|               |
|--------------:|
|        chrome |
|        safari |
| MicrosoftEdge |
|       firefox |
|      iexplore |
|    operablink |

Примеры: 
```properties
#В файле general.properties

web.driver.capability.browserName = safari
```

```java
//Программно
import static org.openqa.selenium.remote.BrowserType.CHROME;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CommonCapabilityProperties.BROWSER_NAME;

//...
BROWSER_NAME.accept(CHROME);
```

```java
//Пример получения значения настройки
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CommonCapabilityProperties.BROWSER_NAME;

//...
var browserNameSetting = BROWSER_NAME.get();
```

`web.driver.capability.javascriptEnabled` - Включить/выключить поддержку javascript. `true` - дефолтное значение

Примеры: 
```properties
#В файле general.properties

web.driver.capability.javascriptEnabled = true
```

```java
//Программно
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CommonCapabilityProperties.SUPPORTS_JAVASCRIPT;

//...
SUPPORTS_JAVASCRIPT.accept("true");
```

```java
//Пример получения значения настройки
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CommonCapabilityProperties.SUPPORTS_JAVASCRIPT;

//...
var jvaScriptSetting = SUPPORTS_JAVASCRIPT.get();
```


### Время ожидания элементов на странице

В файле [general.properties](/doc/rus/core/Properties.md) можно указать значение свойства:

- `waiting.for.elements.time.unit` - Имя одного из элементов перечисления [ChronoUnit](https://docs.oracle.com/javase/10/docs/api/java/time/temporal/ChronoUnit.html)
- `waiting.for.elements.time` -  целое число

Примеры: 
```properties
#В файле general.properties

waiting.for.elements.time.unit = SECONDS
waiting.for.elements.time = 30
```

```java
//Программно
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.ELEMENT_WAITING_TIME_UNIT;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.ELEMENT_WAITING_TIME_VALUE;

//...
ELEMENT_WAITING_TIME_UNIT.accept("SECONDS");
ELEMENT_WAITING_TIME_VALUE.accept("30");
```

Эти настройки нужны для того, чтобы установить дефолтное время ожидания появления элементов на странице. Если эти значения (или одно из них) не установлены, то используется время равное 1 минуте.

```java
//Пример получения значения настройки
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.ELEMENT_WAITING_DURATION;

//...
var duration = ELEMENT_WAITING_DURATION.get();
```

### Время ожидания алерта

- `waiting.alert.time.unit` -  Имя одного из элементов перечисления [ChronoUnit](https://docs.oracle.com/javase/10/docs/api/java/time/temporal/ChronoUnit.html)
- `waiting.alert.time` -  целое число

Примеры: 
```properties
#В файле general.properties
waiting.alert.time.unit = SECONDS
waiting.alert.time = 30
```

```java
//Программно
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.WAITING_ALERT_TIME_UNIT;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.WAITING_ALERT_TIME_VALUE;

//...
WAITING_ALERT_TIME_UNIT.accept("SECONDS");
WAITING_ALERT_TIME_VALUE.accept("30");
```
Эти настройки нужны в том случае, если в тестах необходимо обрабатывать алерты. При помощи этих настроек устанавливается дефолтное время ожидания появления алерта.  Если эти значения (или одно из них) не установлены, то используется время равное 1 минуте.

```java
//Пример получения значения настройки
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.WAITING_ALERT_TIME_DURATION;

//...
var duration = WAITING_ALERT_TIME_DURATION.get();
```

### Время ожидания окна/вкладки

- `waiting.window.time.unit` -  Имя одного из элементов перечисления [ChronoUnit](https://docs.oracle.com/javase/10/docs/api/java/time/temporal/ChronoUnit.html)
- `waiting.window.time` -  целое число

Примеры: 
```properties
#В файле general.properties
waiting.window.time.unit = SECONDS
waiting.window.time = 30
```

```java
//Программно
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.WAITING_WINDOW_TIME_UNIT;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.WAITING_WINDOW_TIME_VALUE;

//...
WAITING_WINDOW_TIME_UNIT.accept("SECONDS");
WAITING_WINDOW_TIME_VALUE.accept("30");
```
Эти настройки нужны в том случае, если в тестах необходимо обрабатывать более одного окна/вкладки браузера, и новые окна/вкладки появляются как результат интерактивного взаимодействия с приложением. При помощи этих настроек устанавливается дефолтное время ожидания появления нового окна/вкладки.  Если эти значения (или одно из них) не установлены, то используется время равное 1 минуте.
```java
//Пример получения значения настройки
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.WAITING_WINDOW_TIME_DURATION;

//...
var duration = WAITING_WINDOW_TIME_DURATION.get();
```

### Время ожидания фрейма

- `waiting.frame.switching.time.unit` -  Имя одного из элементов перечисления [ChronoUnit](https://docs.oracle.com/javase/10/docs/api/java/time/temporal/ChronoUnit.html)
- `waiting.frame.switching.time` -  целое число

Примеры: 
```properties
#В файле general.properties
waiting.frame.switching.time.unit = SECONDS
waiting.frame.switching.time = 30
```

```java
//Программно
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.WAITING_FRAME_SWITCHING_TIME_UNIT;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.WAITING_FRAME_SWITCHING_TIME_VALUE;

//...
WAITING_FRAME_SWITCHING_TIME_UNIT.accept("SECONDS");
WAITING_FRAME_SWITCHING_TIME_VALUE.accept("30");
```
Эти настройки нужны в том случае, если в тестах необходимо обрабатывать фреймы и переключаться в них. При помощи этих настроек устанавливается дефолтное время ожидания появления требуемого фрейма.  Если эти значения (или одно из них) не установлены, то используется время равное 1 минуте.
```java
//Пример получения значения настройки
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.WAITING_FRAME_SWITCHING_TIME_UNIT;

//...
var duration = WAITING_FRAME_SWITCHING_TIME_UNIT.get();
```

### Время ожидания загрузки страницы

- `waiting.for.page.loaded.time.unit` -  Имя одного из элементов перечисления [ChronoUnit](https://docs.oracle.com/javase/10/docs/api/java/time/temporal/ChronoUnit.html)
- `waiting.for.page.loaded.time` -  целое число

Примеры: 
```properties
#В файле general.properties
waiting.for.page.loaded.time.unit = SECONDS
waiting.for.page.loaded.time = 30
```

```java
//Программно
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.WAITING_FOR_PAGE_LOADED_TIME_UNIT;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.WAITING_FOR_PAGE_LOADED_TIME_VALUE;

//...
WAITING_FOR_PAGE_LOADED_TIME_UNIT.accept("SECONDS");
WAITING_FOR_PAGE_LOADED_TIME_VALUE.accept("30");
```

Эти настройки нужны для того, чтобы установить дефолтное время ожидания загрузки страницы. Если эти значения (или одно из них) не установлены, то используется время равное 1 минуте.
```java
//Пример получения значения настройки
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.WAITING_FOR_PAGE_LOADED_DURATION;

//...
var duration = WAITING_FOR_PAGE_LOADED_DURATION.get();
```