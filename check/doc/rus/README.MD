# Интеграция с Hamcrest

Данный модуль используется для связывания функциональности **Neptune** и **[Hamcrest matchers](http://hamcrest.org/JavaHamcrest/tutorial)**.

## Maven

В pom.xml

```xml
    <dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>check</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

## Gradle

В build.gradle

```groovy
    dependencies {
        testCompile group: 'ru.tinkoff.qa.neptune', name: 'check', version: LATEST_RELEASE_OR_BETA_VERSION    
    }
```

## Примеры

### Простой пример. Единичная проверка значения

```java
//...
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;

public class MyTest {
    
    @Test
    public void tezt() {
        int number; //= алгоритм и действия, чтобы получить число
        check(number, match(greaterThan(0)));
    }
    
}
```

Ниже пример с пояснением, **что мы проверяем**


```java
//...
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;

public class MyTest {
    
    @Test
    public void tezt() {
        int number; //= алгоритм и действия, чтобы получить число
        check("Число, которое было получено", 
                number, 
                match(greaterThan(0)));
    }
    
}
```

### Простой пример. Множественная проверка значения

```java
//...
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;

public class MyTest {
    
    @Test
    public void tezt() {
        int number; //= алгоритм и действия, чтобы получить число
        check(number, 
                match(greaterThan(0)),
                match(lessThan(1000))); //Работает по принципу soft assert
        //т.е. будут проведены ВСЕ перечисленные выше проверки.
        //Если какие-то проверки не были успешными, тогда
        //в логе/отчете будут выведены неуспешные проверки по отдельности,
        //в самом конце выполнения метод check выбросит AssertError, в сообщении
        //которого будет агрегированная информация о всех неуспешных проверках
    }
    
}
```

Ниже пример с пояснением, **что мы проверяем**


```java
//...
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;

public class MyTest {
    
    @Test
    public void tezt() {
        int number; //= алгоритм и действия, чтобы получить число
        check("Число, которое было получено", 
                number,
                match(greaterThan(0)),
                match(lessThan(1000))); //Работает по принципу soft assert
        //т.е. будут проведены ВСЕ перечисленные выше проверки.
        //Если какие-то проверки не были успешными, тогда
        //в логе/отчете будут выведены неуспешные проверки по отдельности,
        //в самом конце выполнения метод check выбросит AssertError, в сообщении
        //которого будет агрегированная информация о всех неуспешных проверках
    }
    
}
```

### Пример. Проверяется не само значение напрямую, а некий производный от него результат

```java
//...
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;

public class MyTest {
    
    @Test
    public void tezt() {
        Integer number; //= алгоритм и действия, чтобы получить число
        check(number, 
                match(greaterThan(0)),
                match("Квадратный корень", number -> sqrt(number.doubleValue()), greaterThan(2D))); //Здесь проверяется не 
                //само значение напрямую. В данном примере проверяется, что число положительное, и квадратный корень его
                //(та величина, которая вычисляется от проверяемого значения) больше 2-х
    }
    
}
```

Ниже пример с пояснением, **что мы проверяем**

```java
//...
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;

public class MyTest {
    
    @Test
    public void tezt() {
        Integer number; //= алгоритм и действия, чтобы получить число
        check("Число, которое было получено", 
                number, 
                match(greaterThan(0)),
                match("Квадратный корень", number -> sqrt(number.doubleValue()), greaterThan(2D))); //Здесь проверяется не 
                //само значение напрямую. В данном примере проверяется, что число положительное, и квадратный корень его
                //(та величина, которая вычисляется от проверяемого значения) больше 2-х
    }
    
}
```

### Пример. Вычисление значения вместе с проверкой


```java
//...
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.evaluateAndCheck;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;

public class MyTest {
    
    @Test
    public void tezt() {
        //Данная фича нужна скорее для красоты лога/отчета.
        //Шаг с проверкой скрывает внутри себя под-шаги, 
        //один из которых - вычисление проверяемого значения,
        //остальные - проверки
        evaluateAndCheck("Число, которое было получено", () -> {
                    //алгоритм и действия, чтобы получить число
                },
                number,
                match(greaterThan(0)),
                match(lessThan(1000))); 
    }
    
}
```