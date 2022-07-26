Логгер событий
==============

.. code-block:: java
   :caption: Реализация интерфейса ``ru.tinkoff.qa.neptune.core.api.event.firing.EventLogger``, который фиксирует в логгере / отчете ход выполнения шагов

    package org.my.pack;

    import ru.tinkoff.qa.neptune.core.api.event.firing.EventLogger;

    import java.util.Map;

    public class MyEventLogger implements EventLogger { // реализовать данный интерфейс

        @Override
        public void fireTheEventStarting(String message, // заголовок шага
                                         //набор параметров шага
                                         Map<String, String> parameters) {
            //начало выполнения шага
        }

        @Override
        public void fireThrownException(Throwable throwable) {
            //если выполнение шага завершилось выбросом исключения
        }

        @Override
        public void fireReturnedValue(String resultDescription, // пояснение к результату
                                      Object returned) { //вернувшийся результат
            //если выполнение шага завершилось возвратом результата
        }

        @Override
        public void fireEventFinishing() {
            // Что происходит при завершении выполнения шага
        }

        @Override
        public void addParameters(Map<String, String> parameters) {
            //фиксирование параметров шага, которые не были переданы явно,
            //а были вычислены в ходе выполнения шага
        }
    }

.. code-block:: none
   :caption: Настройка SPI в `main/resources/` или `test/resources/`

   $ META-INF
   ├── services
       ├── ru.tinkoff.qa.neptune.core.api.event.firing.EventLogger

В файл ``ru.tinkoff.qa.neptune.core.api.event.firing.EventLogger`` внести строку ``org.my.pack.MyEventLogger``

