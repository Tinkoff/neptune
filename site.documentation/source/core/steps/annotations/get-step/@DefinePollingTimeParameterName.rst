@DefinePollingTimeParameterName
===============================

Название параметра, обозначающего паузу между попытками получить нужный результат в рамках отведенного
на выполнение шага времени

.. code-block:: java
   :caption: пример

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

   @SequentialGetStepSupplier.DefinePollingTimeParameterName("Custom sleep time")
   public class MyGetStepSupplier
       /*extends a subclass of SequentialGetStepSupplier*/
   {

   }