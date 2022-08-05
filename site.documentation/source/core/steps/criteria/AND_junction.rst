Объединение AND
===============

.. code-block:: java
   :caption: Последовательное объединение

   package org.my.pack;

   import static org.my.pack.MyCriteriaLib.oneMoreCriteria;
   import static org.my.pack.MyCriteriaLib.someCriteria;
   import static org.my.pack.MyGetStepSupplier.something;
   import static org.my.pack.MyTestContext.myTestContext;

    public class SomeTest {

        @Test
        public void test() {
            var result = myTestContext().retrieve(something(1, 2, 3)
                    .criteria(someCriteria("A", "B")) //В данном примере
                    //все указанные критерии объединяются
                    .criteria(oneMoreCriteria(4, 5))); // в одно AND-выражение.
        }
   }

.. code-block:: java
   :caption: Явное объединение нескольких критериев в один

   package org.my.pack;

   import static org.my.pack.MyCriteriaLib.oneMoreCriteria;
   import static org.my.pack.MyCriteriaLib.someCriteria;
   import static org.my.pack.MyGetStepSupplier.something;
   import static org.my.pack.MyTestContext.myTestContext;
   import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.AND;

   public class SomeTest {

       @Test
       public void test() {
           var result = myTestContext().retrieve(something(1, 2, 3)
                   .criteria(AND(someCriteria("A", "B"), oneMoreCriteria(4, 5))));
       }
   }