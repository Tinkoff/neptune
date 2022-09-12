.. code-block:: java
   :caption: пример

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

   @SequentialGetStepSupplier.DefineFromParameterName("Custom From")
   public class MyGetStepSupplier
       /*extends a subclass of SequentialGetStepSupplier*/
   {

   }