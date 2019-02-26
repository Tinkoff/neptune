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

## Далее

Далее достаточно

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

public class SomeTest {
    private SeleniumStepContext seleniumStepContext;
    
    //...
    @Test
    public void tezt() {
        seleniumStepContext./*...Дальнейшие действия*/
    }
}
```

или 

```java
//пример с наследованием
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

public class SomeBaseTest {
    private SeleniumStepContext seleniumStepContext;
    
    @Test
    public SeleniumStepContext browser() {
        return seleniumStepContext;
    }
}

//................

public class SomeTest extends SomeBaseTest {
    
    //...
    @Test
    public void tezt() {
        browser()./*...Дальнейшие действия*/
    }
}

```

[Есть ряд настроек, которые позволят выполнять тесты в более правильном и удобном ключе](/doc/rus/selenium/Settings.md)