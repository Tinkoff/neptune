.. code-block:: java
   :caption: Реализация автоматического завершения / остановки контекста

    package org.my.pack;

    import ru.tinkoff.qa.neptune.core.api.cleaning.Stoppable;
    import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

    public class MyTestContext extends Context<MyTestContext>
            implements Stoppable { // <---Необходимо реализовать данный интерфейс

        @Override
        public void stop() {
            //логика освобождения ресурсов, занятых объектом контекста,
            //или уничтожения внутренних объектов, инкапсулированных объектом контекста
        }
    }