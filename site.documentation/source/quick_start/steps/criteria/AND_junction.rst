Объединение AND
===============

.. code-block:: java
   :caption: Последовательное объединение

   package org.my.pack;

   import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;


    public class SomeTest {

        @Test
        public void test() {
            var result = someContext()
                .get(something(/*необходимые параметры*/)
                    .criteria("Some criteria", o -> {/* предикат*/}) //В данном примере
                    //все указанные критерии объединяются
                    // в одно AND-выражение.
                    .criteria(condition("One more criteria", o -> {/* предикат*/})));
        }
   }

.. code-block:: java
   :caption: Явное объединение нескольких критериев в один

   package org.my.pack;

   import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.AND;
   import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

   public class SomeTest {

       @Test
       public void test() {
           var result = myTestContext().retrieve(something(1, 2, 3)
                   .criteria(AND(
                        condition("Some criteria", o -> {/* предикат*/}),
                        condition("One more criteria", o -> {/* предикат*/})
                   ))
               );
       }
   }