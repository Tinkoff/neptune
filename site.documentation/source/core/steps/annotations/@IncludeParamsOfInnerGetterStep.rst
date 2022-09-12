@IncludeParamsOfInnerGetterStep
===============================

Присутствие данной аннотации означает, что в список параметров данного шага следует включить параметры (критерии, таймауты, параметры обозначенные разработчиком) get-step,
выполняющегося на один уровень ниже.

.. code-block:: java
   :caption: пример

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
   import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
   import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;

   @IncludeParamsOfInnerGetterStep
   public class MyStepClass
       /*extends a subclass of SequentialGetStepSupplier, or SequentialActionSupplier*/
   {

   }