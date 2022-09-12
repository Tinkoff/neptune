@ThrowWhenNoData
================

Исключение, которое может быть выброшено, если в результате выполнения
шага не было получено непустое значение, соответствующее указанным критериям, за указанное время.

Используемые исключения должны иметь объявленный публичный конструктор с параметром типа ``String`` (сообщение)
или объявленный публичный конструктор с параметрами типа ``String`` (сообщение) и ``Throwable`` (причина).

Так же классы используемых исключений должны расширять ``RuntimeException``

.. code-block:: java
   :caption: пример

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;

   @ThrowWhenNoData(toThrow = IllegalStateException.class,
       startDescription = "Can't get:")
   public class MyGetStepSupplier
       /*extends a subclass of SequentialGetStepSupplier*/
   {

   }