Хуки
=====

Перед выполнением теста последовательно может выполняться множество `@Before*`-методов, которые готовят данные или
окружение. Бывает так, что механизм / логика некоторых из этих действий всегда одни и те же, что ведет к
дублированию кода или чрезмерному использованию механизма наследования.

Чтобы этого избежать, можно использовать хуки.

.. code-block:: java
   :caption: Хук

    package org.my.pack;

    import ru.tinkoff.qa.neptune.core.api.hooks.ExecutionHook;
    import ru.tinkoff.qa.neptune.core.api.hooks.HookOrder;

    import java.lang.reflect.Method;

    @HookOrder(priority = 1) //одновременно может работать несколько хуков,
    //и может понадобиться выстроить их выполнение в определенную последовательность.
    //С помощью данной аннотации можно указывать приоритет выполнения того или иного хука.
    //Чем меньше цифра, тем выше приоритет. Можно указать значение [0, 127].
    //Аннотацию можно не указывать, в этом случае
    //хуку присваивается низший приоритет (127).
    public class MyHook implements ExecutionHook {

        @Override
        public void executeMethodHook(
                    Method method, //метод, перед которым данный хук должен быть выполнен.
                    // Можно использовать аннотации над методом или другую метаинформацию
                    Object on, //объект класса-теста, перед методом которого
                    // данный хук должен быть выполнен. Эта информация может быть полезна.
                    boolean isTest) { //метод - тест или нет.
            //логика предварительного действия
        }
    }

.. code-block:: none
   :caption: Настройка SPI в `main/resources/` или `test/resources/`

   $ META-INF
   ├── services
       ├── ru.tinkoff.qa.neptune.core.api.hooks.ExecutionHook

В файл ``ru.tinkoff.qa.neptune.core.api.hooks.ExecutionHook`` внести строку ``org.my.pack.MyHook``