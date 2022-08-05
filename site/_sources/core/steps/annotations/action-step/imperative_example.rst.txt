.. code-block:: java
   :caption: пример

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
   import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

   @SequentialActionSupplier.DefinePerformImperativeParameterName("Do:")
   @Description("Some work")
   public class MyActionStepSupplier
       /*extends a subclass of SequentialActionSupplier*/
   {

   }