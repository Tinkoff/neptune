@DefineCriteriaParameterName
============================

Название параметра, обозначающего критерии, которым должен соответствовать результат

.. code-block:: java
   :caption: пример

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

   @SequentialGetStepSupplier.DefineCriteriaParameterName("Custom result criteria")
   public class MyGetStepSupplier
       /*extends a subclass of SequentialGetStepSupplier*/
   {

   }