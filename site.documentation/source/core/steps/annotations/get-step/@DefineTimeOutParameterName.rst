@DefineTimeOutParameterName
===========================

Название параметра, обозначающего время, отведенное на получение непустого результата, соответствующего
указанным критериям

.. code-block:: java
   :caption: пример

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

   @SequentialGetStepSupplier.DefineTimeOutParameterName("Custom timeout")
   public class MyGetStepSupplier
       /*extends a subclass of SequentialGetStepSupplier*/
   {

   }

