.. code-block:: java
   :caption: пример

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

   @MaxDepthOfReporting(0) //Указанное значение должно быть
   //в диапазоне от 0 до максимального значения Integer
   public class MyStepClass
       /*extends a subclass of SequentialGetStepSupplier, or SequentialActionSupplier*/
   {

   }