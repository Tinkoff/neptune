@DefinePerformOnParameterName
=============================

Название параметра, обозначающего значение, которое является объектом для выполняемого действия

.. code-block:: java
   :caption: пример

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;

   @SequentialActionSupplier.DefinePerformOnParameterName("Perform on:")
   public class MyActionStepSupplier
       /*extends a subclass of SequentialActionSupplier*/
   {

   }