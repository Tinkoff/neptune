@DefineResultDescriptionParameterName
=====================================

Пояснение к результату, который должен возвращать шаг.

.. code-block:: java
   :caption: пример

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

   @SequentialGetStepSupplier.DefineResultDescriptionParameterName("Resulted value")
   public class MyGetStepSupplier
       /*extends a subclass of SequentialGetStepSupplier*/
   {

   }