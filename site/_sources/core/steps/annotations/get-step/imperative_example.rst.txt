.. code-block:: java
   :caption: пример

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
   import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

   @SequentialGetStepSupplier.DefineGetImperativeParameterName("Calculate:")
   @Description("Some value")
   public class MyGetStepSupplier
       /*extends a subclass of SequentialGetStepSupplier*/
   {

   }