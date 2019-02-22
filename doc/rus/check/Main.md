# check

Модуль разработан для описания/выполнения проверок (assertion). [API](https://tinkoffcreditsystems.github.io/neptune/check/) 
В основу положено использование матчеров. [hamcrest.org](http://hamcrest.org/JavaHamcrest/)

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
            <artifactId>check</artifactId>
            <version>${neptune.version}</version>
        </dependency>
</dependencies>

``` 

### Gradle

`compile group: 'ru.tinkoff.qa.neptune', name: 'check', version: neptuneVersion`

# Инициализация контекста

Достаточно 

```java
public class Tezzt ....
   private CheckStepContext check;

   ....
   @Test
   public void tezzt() {
     ...
     check.verify(...)  
   }
```

или 

```java
public class Tezzt ....
   private CheckStepContext check;
   ....
   
   public CheckStepContext check() {
     return checkContext;
   }
   
   ...
   @Test
   public void tezzt() {
     ...
     check().verify(...)  
   }
```

# Варианты использования

- Обычная проверка. Без пояснения проверяемого значения.

```java
...
import static ru.tinkoff.qa.neptune.check.ThatValue.thatValue;
...

    @Test
    public void test() {
        ...
        check.verify(thatValue(someObject)
                .suitsCriteria(is(expectedValue)));
        ...
    }    
```

- Обычная проверка. С пояснением проверяемого значения.

```java
...
import static ru.tinkoff.qa.neptune.check.ThatValue.thatValue;
...

    @Test
    public void test() {
        ...
        check.verify(thatValue("Значение которое надо проверить", someObject)
                .suitsCriteria(is(expectedValue)));
        ...
    }            
```

- Обычная проверка. Без пояснения проверяемого значения. Множественные проверки значения.

```java
...
import static ru.tinkoff.qa.neptune.check.ThatValue.thatValue;
...

    @Test
    public void test() {
        ...
        check.verify(thatValue(someObject)
                .suitsCriteria(is(expectedValue))) //даже если эта проверка не пройдет
                .suitsCriteria(not(is(unexpectedValue))); // эта проверка выполнится
                //и итоговый AssertError будет перечислять все несоответствия
        ...
    }                     
```

- Обычная проверка. С пояснением проверяемого значения. Множественные проверки значения.

```java
...
import static ru.tinkoff.qa.neptune.check.ThatValue.thatValue;
...

    @Test
    public void test() {
        ...
        check.verify(thatValue("Значение которое надо проверить", someObject)
                .suitsCriteria(is(expectedValue))) //даже если эта проверка не пройдет
                .suitsCriteria(not(is(unexpectedValue))); // эта проверка выполнится
                //и итоговый AssertError будет перечислять все несоответствия
        ...
    }                     
```

- Проверка вычисляемого значения. Без пояснения проверяемого значения.

```java
...
import static ru.tinkoff.qa.neptune.check.ThatValue.thatValue;
...

    @Test
    public void test() {
        ...
        check.verify(thatValue(someObject)
                .suitsCriteria("Пояснение значения, вычисляемого от someObject",
                        o -> {...}, //фукнция, производящая получение значения от someObject 
                        is(expectedValue))); //матчер валидирует полученное функцией значение
        ...
    }                           
```

- Проверка вычисляемого значения. С пояснением проверяемого значения.

```java
...
import static ru.tinkoff.qa.neptune.check.ThatValue.thatValue;
...

    @Test
    public void test() {
        ...
        check.verify(thatValue("Значение которое надо проверить", someObject)
                .suitsCriteria("Пояснение значения, вычисляемого от someObject",
                        o -> {...}, //фукнция, производящая получение значения от someObject 
                        is(expectedValue))); //матчер валидирует полученное функцией значение
        ...
    }                           
```

- Объединение.

```java
...
import static ru.tinkoff.qa.neptune.check.ThatValue.thatValue;
...

    @Test
    public void test() {
        ...
        check.verify(thatValue("Значение которое надо проверить", someObject)
                .suitsCriteria(is(expectedValue)))
                .suitsCriteria(not(is(unexpectedValue)))
                
                .verify(thatValue("Значение2 которое надо проверить", someObject)
                     .suitsCriteria(is(expectedValue2)))
                     .suitsCriteria(not(is(unexpectedValue2)))
        ...
    }                           
```

- Нестандартная проверка

```java
...
import static ru.tinkoff.qa.neptune.check.ThatValue.thatValue;
...

    @Test
    public void test() {
        ...
        check.perform("Проверка некоторого объекта", сheckStepContext -> {
            if (..../*условие 1*/) {
                check.verify(thatValue("Значение которое надо проверить", someObject)
                         .suitsCriteria(is(expectedValue)));
            }
            else {
                check.verify(thatValue("Значение которое надо проверить", someObject)
                         .suitsCriteria(not(is(unexpectedValue)));
            }            
        }).verify(thatValue("Значение2 которое надо проверить", someObject2)
                 .suitsCriteria(is(expectedValue2)))
        //и т.д.      
    }                           
```