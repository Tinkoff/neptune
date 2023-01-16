Объединение XOR
===============

.. code-block:: java
   :caption: Примеры

   package org.my.pack;

   import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.ONLY_ONE;
   import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

   public class SomeTest {

      @Test
      public void test() {
         var result = myTestContext().retrieve(something(1, 2, 3)
                 .criteria(ONLY_ONE(
                     condition("Some criteria", o -> {/* предикат*/}),
                     condition("One more criteria", o -> {/* предикат*/})
                 ))
                 //тоже самое, что строкой выше, но боле удобным способом
                 .criteriaOnlyOne(
                     condition("Some criteria", o -> {/* предикат*/}),
                     condition("One more criteria", o -> {/* предикат*/})
                 )
         );
      }
   }