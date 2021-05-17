# Интеграция с Hamcrest

Данный модуль используется для связывания функциональности **Neptune** и **[Hamcrest matchers](http://hamcrest.org/JavaHamcrest/tutorial)**.

## Оглавление
- [Примеры](#Примеры)
  - [Простой пример. Единичная проверка значения](#Простой-пример.-Единичная-проверка-значения)
  - [Простой пример. Множественная проверка значения](#Простой-пример.-Множественная-проверка-значения)  
  - [Пример. Проверяется не само значение напрямую, а некий производный от него результат](#Пример.-Проверяется-не-само-значение-напрямую,-а-некий-производный-от-него-результат)
  - [Пример. Вычисление значения вместе с проверкой](#Пример.-Вычисление-значения-вместе-с-проверкой)   
- [Дополнительные матчеры, которые предоставляет Neptune](#Дополнительные-матчеры,-которые-предоставляет-Neptune)

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

[к оглавлению документа](#Оглавление)

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

[к оглавлению документа](#Оглавление)

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

[к оглавлению документа](#Оглавление)

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
        //часть из которых - вычисление проверяемого значения,
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

[к оглавлению документа](#Оглавление)

## Дополнительные матчеры, которые предоставляет Neptune

[Матчеры общего назначения. URL\URI](./../../../core.api/doc/rus/HAMCREST.MD)

[Матчеры для валидации ответов http](./../../../http.api/doc/rus/HAMCREST.MD)

[Матчеры для front end вэб-приложений](./../../../selenium/doc/rus/HAMCREST.MD)

[к оглавлению документа](#Оглавление)