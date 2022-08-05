Обновление
==========

Некоторые контексты необходимо обновлять перед началом теста / перед выполнением первого ``@Before*``-метода,
т.е. сбросить состояния каких-либо объектов, очистить закэшированную информацию, и т.д.

.. code-block:: java
   :caption: Реализация автоматического обновления контекста

    package org.my.pack;

    import ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable;
    import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

    public class MyTestContext extends Context<MyTestContext>
            // Необходимо реализовать данный интерфейс
            implements ContextRefreshable {

        @Override
        public void refreshContext() {
            //логика обновления ресурсов, занятых объектом контекста,
            //или обновления внутренних объектов, инкапсулированных объектом контекста
        }
    }