Сторонние свойства
==================

Многие сторонние инструменты для разработки приложений и их тестирования имеют свои наборы настроек, которые Neptune может
повторно использовать.

.. code-block:: java
   :caption: Объект класса, через который осуществляется доступ к сторонним свойствам

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.properties.PropertySource;

   public class MyPropertySource implements PropertySource {

       @Override
       public String getProperty(String property) {
           // Описываем то, как мы извлекаем значение стороннего свойства
       }

       @Override
       public boolean isPropertyDefined(String property) {
           //Описываем то, то как мы определяем, задана ли стороннее свойство
       }
   }

.. code-block:: none
   :caption: Настройка SPI в `main/resources/` или `test/resources/`

   $ META-INF
   ├── services
       ├── ru.tinkoff.qa.neptune.core.api.properties.PropertySource

В файл ``ru.tinkoff.qa.neptune.core.api.properties.PropertySource`` внести строку ``org.my.pack.MyPropertySource``