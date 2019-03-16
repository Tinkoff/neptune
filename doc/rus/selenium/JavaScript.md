# Выполнение javaScript (в браузере)

| Возможность использовать критерии 	| Объединение критериев 	| Возможность указывать индивидуальный таймаут 	| Если желаемое значение не получено                                   	| Игнорируемые исключения                            	| Данные, которые могут быть  приложены к отчетам (по умолчанию)  	|
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
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
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
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
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
                .timeOut(ofSeconds(5)) //Время на получение результата, отличающегося от null
                .pollingInterval(ofMillis(100))); //как часто выполнять скрипт. Внутри промежутка времени,
        // указанного выше. Указывать не обязательно
    }
}
```













## Выполнение асинхронного javaScript

---
См. также [Шаги возвращающие результат](/doc/rus/core/Steps.md#Шаги-возвращающие-результат)

См. также [Построение цепочек шагов, возвращающих результат](/doc/rus/core/Steps.md#Построение-цепочек-шагов,-возвращающих-результат)