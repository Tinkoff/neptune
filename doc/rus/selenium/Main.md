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

### TestNg

См. [TestNg](/doc/rus/testng/Main.md)

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.testng.integration.BaseTestNgTest;

public class SomeTest extends BaseTestNgTest<SomeTest> {
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
import ru.tinkoff.qa.neptune.testng.integration.BaseTestNgTest;

public class SomeBaseTest extends BaseTestNgTest<SomeBaseTest> {
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
[Контекст шагов для Selenium](/doc/rus/selenium/SeleniumStepContext.md)

[Настройки, которые позволят выполнять тесты в более правильном и удобном ключе](/doc/rus/selenium/Settings.md)

Элементы

- [Поиск элементов страницы](/doc/rus/selenium/SearchingForElements.md)
- [Клик по элементу](/doc/rus/selenium/Click.md)
- [Редактирование/Очистка/Получение/Валидация значения элемента](/doc/rus/selenium/ElementValues.md)
- [Получение/Валидация значений атрибутов и css](/doc/rus/selenium/ElementAttributesAndCSS.md)
- [Матчеры элементов](/doc/rus/selenium/ElementMatching.md)