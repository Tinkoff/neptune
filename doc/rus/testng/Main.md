# Интеграция с TestNg.

Модуль разработан для интеграции с фреймворком TestNg. [API](https://tinkoffcreditsystems.github.io/neptune/testng.integration/) 
[Официальная документация TestNg](https://testng.org/doc/index.html).

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
            <artifactId>testng.integration</artifactId>
            <version>${neptune.version}</version>
        </dependency>
</dependencies>

``` 

### Gradle

`compile group: 'ru.tinkoff.qa.neptune', name: 'testng.integration', version: neptuneVersion`

## Далее

### Далее в проекте следует 

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

Это дает интересные возможности, например, для написания параметризированных тестов. Например, есть тестовый сценарий, в котором логика примерно одинакова, кроме какого-то специфического действия. Тогда:

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

### Настройки 

В файле [general.properties](/doc/rus/core/Properties.md) можно указать значение свойства `testng.refresh.before`. 

[Некоторые контексты следует обновлять перед выполнением теста](/doc/rus/core/Context.md#Обновление-контекста) (перезапустить браузер, почистить куки, обновить соединение с базой данных и т.д.). 
Свойство `testng.refresh.before` позволяет указать, перед какими методами следует провести обновление. Это должны быть не статические методы, отмеченные аннотациями `@BeforeSuite` или `@BeforeTest`, или `@BeforeClass`, или `@BeforeMethod`, или `@Test`. [Аннотации TestNG](http://testng.org/doc/documentation-main.html#annotations).
Можно указать как одно значение свойства, так и несколько значений через запятую. Значения должны соответствовать именам элементов перечисления [RefreshEachTimeBefore](https://tinkoffcreditsystems.github.io/neptune/testng.integration/ru/tinkoff/qa/neptune/testng/integration/properties/RefreshEachTimeBefore.html). 
Обновление вызывается ОДИН раз перед тем, как очередной тест (не статический метод, отмеченный аннотацией `@Test`) запущен.

Примеры: 
- `testng.refresh.before = BEFORE_METHOD_STARTING` Означает, что обновление контекстов должно произойти ОДИН раз до запуска теста (не статического метода, отмеченного аннотацией `@Test`), перед ПЕРВЫМ в очереди запуска конфигурационным не статическим методом, отмеченным аннотацией `@BeforeMethod`
- `testng.refresh.before = BEFORE_METHOD_STARTING,CLASS_STARTING,SUITE_STARTING` Означает, что обновление контекстов должно произойти ОДИН раз до запуска теста (не статического метода, отмеченного аннотацией `@Test`), перед ПЕРВЫМ в очереди запуска конфигурационным не статическим методом, отмеченным аннотациями `@BeforeMethod` или `@BeforeClass`, или `@BeforeSuite`

По умолчанию, обновление контекстов происходит непосредственно перед выполнением теста (не статического метода, отмеченного аннотацией `@Test`). 

Указывать значения свойства `testng.refresh.before` следует если:

- в проекте присутствуют конфигурационные методы, использующие контексты, и необходимо чтобы контексты были приведены к состоянию, которое можно считать исходным.





