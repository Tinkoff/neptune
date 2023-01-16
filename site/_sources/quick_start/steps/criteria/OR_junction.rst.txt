Объединение OR
===============

.. code-block:: java
   :caption: Примеры

   package org.my.pack;


   import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.OR;
   import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

   public class SomeTest {

       @Test
       public void test() {
           var result = myTestContext().retrieve(something(1, 2, 3)
                   .criteria(OR(
                        condition("Some criteria", o -> {/* предикат*/}),
                        condition("One more criteria", o -> {/* предикат*/})
                   ))
                   //тоже самое, что строкой выше, но боле удобным способом
                   .criteriaOr(
                        condition("Some criteria", o -> {/* предикат*/}),
                        condition("One more criteria", o -> {/* предикат*/})
                   )
           );
       }
   }