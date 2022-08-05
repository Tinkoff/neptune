Инверсия критериев
==================

.. code-block:: java
   :caption: Примеры

    package org.my.pack;

    import static org.my.pack.MyCriteriaLib.oneMoreCriteria;
    import static org.my.pack.MyCriteriaLib.someCriteria;
    import static org.my.pack.MyGetStepSupplier.something;
    import static org.my.pack.MyTestContext.myTestContext;
    import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.NOT;

    public class SomeTest {

        @Test
        public void test() {
            var result = myTestContext().retrieve(something(1, 2, 3)
                    .criteria(NOT(someCriteria("A", "B"))) //<-- критерий инвертирован
                    .criteria(NOT(oneMoreCriteria(4, 5))) //<-- критерий инвертирован
                    //тоже самое, что и выше, но боле удобным способом
                    .criteriaNot(someCriteria("A", "B"))
                    //инверсия каждого переданного критерия
                    .criteriaNot(someCriteria("A", "B"), oneMoreCriteria(4, 5)));
        }
    }