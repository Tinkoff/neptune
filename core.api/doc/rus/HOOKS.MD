# Хуки

Перед выполнением теста последовательно может выполняться множество `@Before*`-методов, которые готовят данные / 
окружения для этого теста. Но бывает так, что механизм / логика некоторых из этих действий всегда одни и те же, что ведет к
дублированию кода в `@Before*`-методах или чрезмерному использованию механизма наследования.

Чтобы этого избежать, можно поступить следующим образом:

- создать класс

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.hooks.ExecutionHook;
import ru.tinkoff.qa.neptune.core.api.hooks.HookOrder;

import java.lang.reflect.Method;

@HookOrder(priority = 1) //одновременно может работать несколько хуков,
//и может понадобиться выстроить их выполнение в определенную последовательность.
//С помощью данной аннотации можно указывать приоритет выполнения того или иного хука.
//Чем меньше цифра, тем выше приоритет. Можно указать значение [0, 127].
//Аннотацию можно не указывать, в этом случае хуку присваивается низший приоритет (127).
public class MyHook implements ExecutionHook {

    @Override
    public void executeMethodHook(Method method, //метод, перед которым данный хук должен быть выполнен.
                                  // Можно использовать аннотации над методом или другую метаинформацию
                                  // (java.reflection)
                                  Object on, //объект класса-теста, перед методом которого
                                  // данный хук должен быть выполнен. Эта информация может быть полезна.
                                  boolean isTest) { //метод - тест или нет.
        //логика предварительного действия
    }
}
```

- Настроить [SPI](https://docs.oracle.com/javase/tutorial/ext/basics/spi.html). В `resources` директорий `main` или `test`
  создать следующие директории и файлы

```
    META-INF
    |
    +---- services
             |
             +----ru.tinkoff.qa.neptune.core.api.hooks.ExecutionHook
```

- В файл `ru.tinkoff.qa.neptune.core.api.hooks.ExecutionHook` внести запись
  `org.my.pack.MyHook`

Данный механизм используют:
- [TestNG](./../../../testng.integration/doc/rus/README.MD)
- [JUnit5](./../../../jupiter.integration/doc/rus/README.MD)

[к списку документов](README.MD#Оглавление)