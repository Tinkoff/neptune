Объединение XOR
===============

.. code-block:: java
   :caption: Примеры

   package org.my.pack;

   import static org.my.pack.MyCriteriaLib.oneMoreCriteria;
   import static org.my.pack.MyCriteriaLib.someCriteria;
   import static org.my.pack.MyGetStepSupplier.something;
   import static org.my.pack.MyTestContext.myTestContext;
   import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.ONLY_ONE;

   public class SomeTest {

      @Test
      public void test() {
         var result = myTestContext().retrieve(something(1, 2, 3)
                 .criteria(ONLY_ONE(someCriteria("A", "B"), oneMoreCriteria(4, 5)))
                 //тоже самое, что строкой выше, но боле удобным способом
                 .criteriaOnlyOne(someCriteria("A", "B"), oneMoreCriteria(4, 5)));
        }
   }