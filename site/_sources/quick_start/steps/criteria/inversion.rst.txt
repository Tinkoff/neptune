Инверсия критериев
==================

.. code-block:: java
   :caption: Примеры

    package org.my.pack;

    import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.NOT;
    import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

    public class SomeTest {

        @Test
        public void test() {
            var result = myTestContext().retrieve(something(1, 2, 3)
                    .criteria(NOT(condition("Some criteria", o -> {/* предикат*/}))) //критерий инвертирован
                    .criteria(NOT(condition("One more criteria", o -> {/* предикат*/}))) //<-- критерий инвертирован
                    //тоже самое, что и выше, но боле удобным способом
                    .criteriaNot(condition("Some criteria", o -> {/* предикат*/}))
                    //инверсия каждого переданного критерия
                    .criteriaNot(
                        condition("Some criteria", o -> {/* предикат*/}),
                        condition("One more criteria", o -> {/* предикат*/})
                    )
            );
        }
    }