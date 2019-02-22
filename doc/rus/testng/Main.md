# Интеграция с TestNg.

Модуль разработан для интеграции с фреймворком TestNg. [API](https://tinkoffcreditsystems.github.io/neptune/testng.integration/) 
[Официальная документация TestNg](https://testng.org/doc/index.html).

# Начало работы

## Требования
 
 - Операционнаяя система - Windows/Mac Os X/Linux
 - Java Development Kit 11
 - [maven](https://maven.apache.org/) или [gradle](https://gradle.org/)
 
## Зависимости

### Maven

```xml
<dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>testng.integration</artifactId>
            <version>${neptune.version}</version>
        </dependency>
</dependencies>

``` 

### Gradle

`compile group: 'ru.tinkoff.qa.neptune', name: 'testng.integration', version: neptuneVersion`

## Далее

Далее в проекте следует 

```java
...
import ru.tinkoff.qa.neptune.testng.integration.BaseTestNgTest;
...
// Этот класс можно сделать базовым классом для всех тестов проекта, от которого можно наследоваться
//либо выполнять это действие для каждого теста
public class MyCustomizedTest extends BaseTestNgTest<BaseWebOfficeTest> {
    ...
}
```

**Необходимо для:** 

- инициализации внутренних контекстов. [О контекстах](/doc/rus/core/Context.md) 

Например

```java
public class MyStepContext implements ActionStepContext<SeleniumStepContext>, GetStepContext<SeleniumStepContext> {
    ...
}
```

и тогда

```java
...
import ru.tinkoff.qa.neptune.testng.integration.BaseTestNgTest;
...
public class MyCustomizedTest extends BaseTestNgTest<BaseWebOfficeTest> {
    ...
    private MyStepContext myContext; //для работы достаточно просто объявить поле
    
    @Test
    public void test() {
        ...
        var someValue = myContext.get("Какое-то значение", context -> {
            ....
        });
        myContext.perform("Какое-то действие", context -> {
            ....
        });
    }
}
```

- превращения теста в контекст более глобального уровня

```java
...
import ru.tinkoff.qa.neptune.testng.integration.BaseTestNgTest;
...
public class MyCustomizedTest extends BaseTestNgTest<BaseWebOfficeTest> {
    ...
    private MyStepContext myContext;
    
    @Test
    public void test() {
        ...
        var someValue =  get("Какое-то значение", testContext -> {
            var someStepValue = myContext.get("Какое-то промежуточное значение", context -> {
                        ....
                    });
        })
        
        perform("Какое-то действие", testContext -> {
           myContext.perform("Какое-то промежуточное действие", context -> {
                       ....
                   }); 
        });
    }
}
```

Это дает интересные возможности, например, для написания параметризорованных тестов. Например, есть тестовый сценарий, в котором логика прмерно одинакова, кроме кокого-то спуцифического действия. Тогда:

```java
...
import org.testng.annotations.DataProvider;
import ru.tinkoff.qa.neptune.core.api.steps.StepAction;
import ru.tinkoff.qa.neptune.testng.integration.BaseTestNgTest;
...
public class MyCustomizedTest extends BaseTestNgTest<BaseWebOfficeTest> {
    ...
    private MyStepContext myContext;
    
    @DataProvider
    public static Object[][] testDataProvider() {
        return new Object[][]{
            {action("Какое-то спефическое действие 1", (Consumer<MyCustomizedTest>) textContext -> {
                 textContext.myContext.perform("Какое-то действие", context -> {
                     ....
                 });
                 ...//прочие действия, которые включены в шаг
             })},
             {action("Какое-то спефическое действие 2", (Consumer<MyCustomizedTest>) textContext -> {
                 ...//действия, которые включены в шаг
             })},                    
        };
    }
    
    @Test(dataProvider = "testDataProvider")
    public void test(StepAction<MyCustomizedTest> action) {
        ... //тест начинается и идет
        perform(action);//вызов специфического действия
        ... //тест продолжается далее
    }
}
```






