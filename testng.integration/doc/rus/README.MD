# Интеграция с TestNG

Данный модуль предназначен для интеграции Neptune и TestNG

## Maven

В pom.xml

```xml
    <dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>testng.integration</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

## Gradle

В build.gradle

```groovy
    dependencies {
        testCompile group: 'ru.tinkoff.qa.neptune', name: 'testng.integration', version: LATEST_RELEASE_OR_BETA_VERSION    
    }
```

## Настройки
[Подробнее о настройках](./../../../core.api/doc/rus/SETTINGS.MD)

Подробнее [о контекстах](./../../../core.api/doc/rus/IDEA.MD#Контекст) и об их [обновлении](./../../../core.api/doc/rus/REFRESHING_STOPPING.MD#Обновление)

Необходимо определиться, перед каким методами следует обновить котнекст (перегрузить/перезапустить браузер, проверить соединение с базами данных, и если
что-то отвалилось - создать новое, и т.п). Типы этих методов описываются перечислением `ru.tinkoff.qa.neptune.testng.integration.properties.RefreshEachTimeBefore`:
- `SUITE_STARTING` перед методами, отмеченными `@BeforeSuite`
- `ALL_TEST_STARTING` перед методами, отмеченными `@BeforeTest`
- `CLASS_STARTING` перед методами, отмеченными `@BeforeClass`
- `GROUP_STARTING` перед методами, отмеченными `@BeforeGroups`
- `BEFORE_METHOD_STARTING` перед методами, отмеченными `@BeforeMethod`
- `METHOD_STARTING` перед методами, отмеченными `@Test`

Значаение свойства/переменной окружения `TESTNG_REFRESH_BEFORE` должно быть равно одному из перечисленных выше элементов, или оно может включать в себя некоторые 
из перечисленных выше элементов

```properties
#обновление происходит 
TESTNG_REFRESH_BEFORE=CLASS_STARTING
```


## Пишем первый тест

Класс, описывающий тест, должен наследоваться от `BaseTestNgTest`

```java
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.testng.integration.BaseTestNgTest;

public class MyTest extends BaseTestNgTest {
    
    @Test
    public test() {
      //some checks
    }
}
```