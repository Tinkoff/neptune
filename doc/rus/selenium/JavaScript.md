# Выполнение javaScript (в браузере)

Для выполнения javaScript (в браузере) используется [GetJavaScriptResultSupplier](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/java/script/GetJavaScriptResultSupplier.html)

| [Возможность использовать критерии](/doc/rus/core/Steps.md#Критерии)| [Объединение критериев](/doc/rus/core/Steps.md#Объединение-критериев)| [Возможность указывать индивидуальный таймаут](/doc/rus/core/Steps.md#Время-на-получение-значимого-результата)| Если желаемое значение не получено | [Игнорируемые исключения](/doc/rus/core/Steps.md#Игнорирование-выбрасываемых-исключений)| [Данные, которые могут быть  приложены к отчетам (по умолчанию)](/doc/rus/core/Steps.md#Данные,-которые-могут-быть-приложены-к-отчетам/логу)|
|-----------------------------------	|-----------------------	|----------------------------------------------	|----------------------------------------------------------------------	|----------------------------------------------------	|-----------------------------------------------------------------	|
|            Да                     	| AND, OR, XOR             	|                Да                          	| Возвращает null                                                      	| НЕТ 	                                                | Скриншоты. Текст                                       	        |


## Простое выполнение javaScript

```java
//пример: простое выполнение скрипта
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static ru.tinkoff.qa.neptune.selenium.functions.java.script.GetJavaScriptResultSupplier.javaScript;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //простое выполнение скрипта
        seleniumSteps.evaluate(javaScript("js script"));
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.functions.java.script.GetJavaScriptResultSupplier.javaScript;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //выполненение скрипта с условием возврата результата
        //Условие: Результат целое число, равное 2
        //если результат выполнения не соответствует условию,
        //вернется null 
        var value = seleniumSteps.evaluate(javaScript("js script")
                .criteria("Результат целое число, равное 2", o -> ofNullable(o)
                        .map(result -> {
                            if (Integer.class.isAssignableFrom(result.getClass())) {
                                return result.equals(2);
                            }
                            return false;
                        }).orElse(false)));
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static ru.tinkoff.qa.neptune.selenium.functions.java.script.GetJavaScriptResultSupplier.javaScript;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //простое выполнение скрипта
        //с выбрасываем исключения, если конечный результат равен null
        var value = seleniumSteps.evaluate(javaScript("js script") //если скрипт возвращает null, будет выброшено исключение
                .throwOnEmptyResult(() -> new IllegalStateException("Exception message")));
                //можно указать любой класс исключения, расширяющий RuntimeException
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.selenium.functions.java.script.GetJavaScriptResultSupplier.javaScript;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //простое выполнение скрипта
        //c указанием времени на получение результата, который отличается от null
        var value =  seleniumSteps.evaluate(javaScript("js script")
                .timeOut(ofSeconds(5)) //Время на получение результата, отличающегося от null
                .pollingInterval(ofMillis(100))); //как часто выполнять скрипт. Внутри промежутка времени, 
                // указанного выше. Указывать не обязательно
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.selenium.functions.java.script.GetJavaScriptResultSupplier.javaScript;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //простое выполнение скрипта
        //c указанием времени на получение результата, соответствующего условию
        //Условие: Результат целое число, равное 2
        //если результат выполнения не соответствует условию в конце отведенного времени,
        //вернется null
        var value =  seleniumSteps.evaluate(javaScript("js script")
                .criteria("Результат целое число, равное 2", o -> ofNullable(o)
                        .map(result -> {
                            if (Integer.class.isAssignableFrom(result.getClass())) {
                                return result.equals(2);
                            }
                            return false;
                        }).orElse(false))
                .timeOut(ofSeconds(5)) //Время на получение результата, соответствующего условию
                .pollingInterval(ofMillis(100))); //как часто выполнять скрипт. Внутри промежутка времени,
        // указанного выше. Указывать не обязательно
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.functions.java.script.GetJavaScriptResultSupplier.javaScript;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //простое выполнение скрипта
        //c указанием времени на получение результата, соответствующего условию
        //Условие: Результат целое число, равное 2
        //с выбрасываем исключения, если конечный результат не соответствует условию
        //по окончанию указанного времени
        var value =  seleniumSteps.evaluate(javaScript("js script")
                .criteria("Результат целое число, равное 2", o -> ofNullable(o)
                        .map(result -> {
                            if (Integer.class.isAssignableFrom(result.getClass())) {
                                return result.equals(2);
                            }
                            return false;
                        }).orElse(false))
                .timeOut(ofSeconds(5)) //Время на получение результата, соответствующего условию
                .pollingInterval(ofMillis(100)) //как часто выполнять скрипт. Внутри промежутка времени,
                // указанного выше. Указывать не обязательно

                //если скрипт возвращает null или результат, не соответствующий условию, будет выброшено исключение
                .throwOnEmptyResult(() -> new IllegalStateException("Exception message")));
                //можно указать любой класс исключения, расширяющий RuntimeException
    }
}
```

## Выполнение асинхронного javaScript

```java
//пример: простое выполнение асинхронного скрипта
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static ru.tinkoff.qa.neptune.selenium.functions.java.script.GetJavaScriptResultSupplier.asynchronousJavaScript;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //простое выполнение асинхронного скрипта
        seleniumSteps.evaluate(asynchronousJavaScript("asynchronous js script"));
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.functions.java.script.GetJavaScriptResultSupplier.asynchronousJavaScript;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //выполненение асинхронного скрипта с условием возврата результата
        //Условие: Результат целое число, равное 2
        //если результат выполнения не соответствует условию,
        //вернется null 
        var value = seleniumSteps.evaluate(asynchronousJavaScript("asynchronous js script")
                .criteria("Результат целое число, равное 2", o -> ofNullable(o)
                        .map(result -> {
                            if (Integer.class.isAssignableFrom(result.getClass())) {
                                return result.equals(2);
                            }
                            return false;
                        }).orElse(false)));
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static ru.tinkoff.qa.neptune.selenium.functions.java.script.GetJavaScriptResultSupplier.asynchronousJavaScript;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //простое выполнение асинхронного скрипта
        //с выбрасываем исключения, если конечный результат равен null
        var value = seleniumSteps.evaluate(asynchronousJavaScript("asynchronous js script") //если скрипт возвращает null, будет выброшено исключение
                .throwOnEmptyResult(() -> new IllegalStateException("Exception message")));
                //можно указать любой класс исключения, расширяющий RuntimeException
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.selenium.functions.java.script.GetJavaScriptResultSupplier.asynchronousJavaScript;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //простое выполнение асинхронного скрипта
        //c указанием времени на получение результата, который отличается от null
        var value =  seleniumSteps.evaluate(asynchronousJavaScript("asynchronous js script")
                .timeOut(ofSeconds(5)) //Время на получение результата, отличающегося от null
                .pollingInterval(ofMillis(100))); //как часто выполнять скрипт. Внутри промежутка времени, 
                // указанного выше. Указывать не обязательно
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.selenium.functions.java.script.GetJavaScriptResultSupplier.asynchronousJavaScript;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //простое выполнение асинхронного скрипта
        //c указанием времени на получение результата, соответствующего условию
        //Условие: Результат целое число, равное 2
        //если результат выполнения не соответствует условию в конце отведенного времени,
        //вернется null
        var value =  seleniumSteps.evaluate(asynchronousJavaScript("asynchronous js script")
                .criteria("Результат целое число, равное 2", o -> ofNullable(o)
                        .map(result -> {
                            if (Integer.class.isAssignableFrom(result.getClass())) {
                                return result.equals(2);
                            }
                            return false;
                        }).orElse(false))
                .timeOut(ofSeconds(5)) //Время на получение результата, соответствующего условию
                .pollingInterval(ofMillis(100))); //как часто выполнять скрипт. Внутри промежутка времени,
        // указанного выше. Указывать не обязательно
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.functions.java.script.GetJavaScriptResultSupplier.asynchronousJavaScript;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //простое выполнение асинхронного скрипта
        //c указанием времени на получение результата, соответствующего условию
        //Условие: Результат целое число, равное 2
        //с выбрасываем исключения, если конечный результат не соответствует условию
        //по окончанию указанного времени
        var value =  seleniumSteps.evaluate(asynchronousJavaScript("asynchronous js script")
                .criteria("Результат целое число, равное 2", o -> ofNullable(o)
                        .map(result -> {
                            if (Integer.class.isAssignableFrom(result.getClass())) {
                                return result.equals(2);
                            }
                            return false;
                        }).orElse(false))
                .timeOut(ofSeconds(5)) //Время на получение результата, соответствующего условию
                .pollingInterval(ofMillis(100)) //как часто выполнять скрипт. Внутри промежутка времени,
                // указанного выше. Указывать не обязательно

                //если скрипт возвращает null или результат, не соответствующий условию, будет выброшено исключение
                .throwOnEmptyResult(() -> new IllegalStateException("Exception message")));
                //можно указать любой класс исключения, расширяющий RuntimeException
    }
}
```

---
См. также [Шаги возвращающие результат](/doc/rus/core/Steps.md#Шаги-возвращающие-результат)

См. также [Построение цепочек шагов, возвращающих результат](/doc/rus/core/Steps.md#Построение-цепочек-шагов,-возвращающих-результат)