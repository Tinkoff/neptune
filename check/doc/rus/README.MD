# Интеграция с Hamcrest

Данный модуль используется для связывания функциональности **Neptune** и **[Hamcrest matchers](http://hamcrest.org/JavaHamcrest/tutorial)**.

## Оглавление
- [Примеры](#Примеры)
  - [Проверка значения](#Проверка-значения)
  - [Множественная проверка значения](#Множественная-проверка-значения)
  - [Проверка значения матчерами, объединенными в OR-выражение](#Проверка-значения-матчерами-объединенными-в-OR-выражение)
  - [Проверка значения матчерами, объединенными в NOT-выражение](#Проверка-значения-матчерами-объединенными-в-NOT-выражение)
  - [Проверка значения матчерами, объединенными в XOR-выражение](#Проверка-значения-матчерами-объединенными-в-XOR-выражение)
  - [Проверки с использованием времени](#Проверки-с-использованием-времени)
  - [Проверяется не само значение напрямую, а некий производный от него результат](#Проверяется-не-само-значение-напрямую-а-некий-производный-от-него-результат)
  - [Вычисление значения вместе с проверкой](#Вычисление-значения-вместе-с-проверкой)
- [Mатчеры, которые предоставляет Neptune](#Mатчеры-которые-предоставляет-Neptune)

## Примеры

### Проверка значения

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

### Множественная проверка значения

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

### Проверка значения матчерами, объединенными в OR-выражение

```java
//...
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.check.MatchAction.matchAny;

public class MyTest {
    
    @Test
    public void tezt() {
        int number; //= алгоритм и действия, чтобы получить число
        check(number, 
                match(greaterThan(0)),
                matchAny(lessThan(1000), greaterThanOrEqualTo(200))); //перечисляются 2 и более 
      // критерия. Проверяемые значения должны соответствовать любому/любым из них
    }
    
}
```

Ниже пример с пояснением, **что мы проверяем**


```java
//...
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.check.MatchAction.matchAny;

public class MyTest {
    
    @Test
    public void tezt() {
        int number; //= алгоритм и действия, чтобы получить число
        check("Число, которое было получено", 
                number,
                match(greaterThan(0)),
                matchAny(lessThan(1000), greaterThanOrEqualTo(200))); //перечисляются 2 и более 
      // критерия. Проверяемые значения должны соответствовать любому/любым из них
    }
    
}
```

[к оглавлению документа](#Оглавление)


### Проверка значения матчерами, объединенными в NOT-выражение

```java
//...
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.check.MatchAction.matchNot;

public class MyTest {
    
    @Test
    public void tezt() {
        int number; //= алгоритм и действия, чтобы получить число
        check(number, 
                match(greaterThan(0)),
                matchNot(lessThan(1000))); //перечисляются 1 и более критериев. 
      // Проверяемые значения не должны соответствовать ни одному из них
    }
    
}
```

Ниже пример с пояснением, **что мы проверяем**


```java
//...
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.check.MatchAction.matchNot;

public class MyTest {
    
    @Test
    public void tezt() {
        int number; //= алгоритм и действия, чтобы получить число
        check("Число, которое было получено", 
                number,
                match(greaterThan(0)),
                matchNot(lessThan(1000))); //перечисляются 1 и более критериев. 
      // Проверяемые значения не должны соответствовать ни одному из них
    }
    
}
```

[к оглавлению документа](#Оглавление)

### Проверка значения матчерами, объединенными в XOR-выражение

```java
//...
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.check.MatchAction.matchOnlyOne;

public class MyTest {
    
    @Test
    public void tezt() {
        int number; //= алгоритм и действия, чтобы получить число
        check(number, 
                match(greaterThan(0)),
                matchOnlyOne(lessThan(1000)), greaterThan(1000)); //перечисляются 2 и более критериев. 
      // Проверяемые значения должны соответствовать только одному из них
    }
    
}
```

Ниже пример с пояснением, **что мы проверяем**


```java
//...
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.check.MatchAction.matchOnlyOne;

public class MyTest {
    
    @Test
    public void tezt() {
        int number; //= алгоритм и действия, чтобы получить число
        check("Число, которое было получено", 
                number,
                match(greaterThan(0)),
                matchOnlyOne(lessThan(1000)), greaterThan(1000)); //перечисляются 2 и более критериев. 
      // Проверяемые значения должны соответствовать только одному из них
    }
    
}
```

[к оглавлению документа](#Оглавление)

### Проверки с использованием времени

Иногда бывает так, что объект обладает некими мутабельными свойствами (например элементы пользовательского интерфейса 
меняют значения своих атрибутов после различных событий спустя некоторое непродолжительное время). Ниже примеры того,
как учесть это обстоятельство и указать время, в рамках которого проверка должна быть пройдена. 

```java
//...
import static java.time.Duration.ofSeconds;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;

public class MyTest {
    
    @Test
    public void tezt() {
        T obj; //= инициализация
        check(obj, match(ofSeconds(5), matcher)); //указывается время, 
      // за которое условие успешной проверки, описанное матчером, должно быть выполнено
    }
    
}
```

Ниже пример с пояснением, **что мы проверяем**


```java
//...
import static java.time.Duration.ofSeconds;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;

public class MyTest {
    
    @Test
    public void tezt() {
        T obj; //= инициализация
        check("Проверяемый объект",
                obj,
                match(ofSeconds(5), matcher)); //указывается время, 
      // за которое условие успешной проверки, описанное матчером, должно быть выполнено
    }
    
}
```

Это работает и для [matchAny](#Проверка-значения-матчерами-объединенными-в-OR-выражение), 
[matchNot](#Проверка-значения-матчерами-объединенными-в-NOT-выражение), 
[matchOnlyOne](#Проверка-значения-матчерами-объединенными-в-XOR-выражение). 

[к оглавлению документа](#Оглавление)


### Проверяется не само значение напрямую, а некий производный от него результат

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
                match("Квадратный корень", 
                        number -> sqrt(number.doubleValue()), 
                        greaterThan(2D))); //Здесь проверяется не 
                //само значение напрямую. В данном примере проверяется, что число положительное, и квадратный корень его
                //(та величина, которая вычисляется от проверяемого значения) больше 2-х
    }
    
}
```

Это работает и для [matchAny](#Проверка-значения-матчерами-объединенными-в-OR-выражение),
[matchNot](#Проверка-значения-матчерами-объединенными-в-NOT-выражение),
[matchOnlyOne](#Проверка-значения-матчерами-объединенными-в-XOR-выражение). В таких проверках можно указывать [время, 
в рамках которого проверки должны быть успешны](#Проверки-с-использованием-времени):


```java
//...
import static java.time.Duration.ofSeconds;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;

public class MyTest {
    
    @Test
    public void tezt() {
        Objecct obj; //= инициализация
        check("Проверяемый объект",
                obj,
                match("Какое-то вычислимое свойство", 
                        o -> {/*возврат результата*/},
                        ofSeconds(5), //указывается время, 
                        // за которое условие успешной проверки, описанное матчером, должно быть выполнено
                        matcher));
    }
}
```

[к оглавлению документа](#Оглавление)

### Вычисление значения вместе с проверкой


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

## Mатчеры, которые предоставляет Neptune

[Матчеры общего назначения](./../../../core.api/doc/rus/HAMCREST.MD)

[Матчеры для валидации ответов http](./../../../http.api/doc/rus/HAMCREST.MD)

[Матчеры для front end вэб-приложений](./../../../selenium/doc/rus/HAMCREST.MD)

[к оглавлению документа](#Оглавление)