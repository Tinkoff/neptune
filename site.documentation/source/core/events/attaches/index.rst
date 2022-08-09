Аттачи
======

Ниже показан набор классов, который позволяет автоматизировать создание аттачей средмтвами *Neptune*

.. code-block:: java
   :caption: Пример класса, объекты которого извлекают данные для аттача

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
   import ru.tinkoff.qa.neptune.core.api.event.firing.CapturedDataInjector;

   import java.util.List;

   public class MyCaptor<T, S> extends Captor< // <- нужно унаследовать данный класс
       T, //тип объекта, к
       S> { //тип объекта-аттача

       //самый общий конструктор
       //Добавлено для наглядности
       protected MyCaptor(String message, //название/сообщение аттача
                         //коллекция объектов,
                         List<? extends CapturedDataInjector<S>> injectors) {
                         // которые добавляют
                         // аттачи непосредственно к отчету / логу.
           super(message, injectors);
       }

       /*
       //Рекомендуется, чтобы класс-наследник Captor
       //имел объявленный публичный конструктор без параметров,
       //или чтобы у этого класса не было объявленного конструктора вообще
       public MyCaptor() {
           super();
       }
       */

       @Override
       public S getData(T caught) {
           return // возвращает объект-аттач,
           // если такой объект нельзя получить, метод должен возвращать null
       }

       @Override
       public T getCaptured(Object toBeCaptured) {
           return // Возвращает объект, с помощью которого формируется аттач
           // если такой объект нельзя получить, метод должен возвращать null
       }
   }

.. code-block:: java
   :caption: Пример класса, объекты которого создают аттач

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.event.firing.CapturedDataInjector;

   //Класс объектов, которые добавляют аттач в лог / отчет о тесте.
   public class MyInjector<S>
           //нужно реализовать этот интерфейс
           implements CapturedDataInjector<S> {
           //S - Тип объекта, добавляемого как аттач

       @Override
       public void inject(S toBeInjected, String message) {
           //логика добавления приложения к отчету / логу
       }
   }

.. toctree::
   :hidden:

   description.md
   @UseInjectors.rst
   spi_setting.rst
   annotations/index.md
   file_attach.md
   picture_attach.md
   text_attach.md