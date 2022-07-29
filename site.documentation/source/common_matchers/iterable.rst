Для java.lang.Iterable
=======================

.. code-block:: java
   :caption: Примеры использования матчеров для Iterable

    package org.mypack;

    import java.util.List;

    import static org.hamcrest.MatcherAssert.assertThat;
    import static org.hamcrest.Matchers.*;
    import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                .SetOfObjectsConsistsOfMatcher.iterableInOrder;
    import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                .SetOfObjectsConsistsOfMatcher.iterableOf;
    import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                .SetOfObjectsEachItemMatcher.eachOfIterable;
    import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                SetOfObjectsIncludesMatcher.iterableIncludes;
    import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                .SetOfObjectsIncludesMatcher.iterableIncludesInOrder;
    import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                .SetOfObjectsItemsMatcher.iterableHasItem;
    import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                .SetOfObjectsItemsMatcher.iterableHasItems;

    public class MyTest {

        private final Iterable<Integer> i = List.of(1, 2, 3);


        @Test(description = "проверка на то, " +
                "что что Iterable<> состоит только из " +
                "перечисленных элементов в указанном порядке")
        public void test() {
            assertThat(i, iterableInOrder(1, 2, 3));
        }

        @Test(description = "проверка на то, " +
                "что что Iterable<> состоит только из " +
                "перечисленных элементов в указанном порядке." +
                "Элементы описаны матчерами, которым они должны соответствовать")
        public void test2() {
            assertThat(i, iterableInOrder(
                    lessThan(2),
                    greaterThan(1),
                    instanceOf(Integer.class)
            ));
        }

        @Test(description = "проверка на то, " +
                "что что Iterable<> состоит только из " +
                "перечисленных элементов." +
                "Порядок их следования значения не имеет.")
        public void test3() {
            assertThat(i, iterableOf(2, 1, 3));
        }

        @Test(description = "проверка на то, " +
                "что что Iterable<> состоит только из " +
                "перечисленных элементов. " +
                "Порядок их следования значения не имеет." +
                "элементы описаны матчерами, которым они должны соответствовать")
        public void test4() {
            assertThat(i, iterableOf(
                    greaterThan(2),
                    lessThan(2),
                    instanceOf(Integer.class)
            ));
        }


        @Test(description = "проверка на то, " +
                "что что Iterable<> включает " +
                "перечисленные элементы в указанном порядке")
        public void test5() {
            assertThat(i, iterableIncludesInOrder(2, 3));
        }

        @Test(description = "проверка на то, " +
                "что что Iterable<> включает " +
                "перечисленные элементы в указанном порядке." +
                "элементы описаны матчерами, которым они должны соответствовать")
        public void test6() {
            assertThat(i, iterableIncludesInOrder(
                    greaterThan(1),
                    instanceOf(Integer.class)
            ));
        }


        @Test(description = "проверка на то, " +
                "что что Iterable<> включает " +
                "перечисленные элементы. " +
                "Порядок их следования значения не имеет")
        public void test7() {
            assertThat(i, iterableIncludes(3, 1));
        }

        @Test(description = "проверка на то, " +
                "что что Iterable<> включает " +
                "перечисленные элементы. " +
                "Порядок их следования значения не имеет." +
                "элементы описаны матчерами, которым они должны соответствовать")
        public void test8() {
            assertThat(i, iterableIncludes(
                    greaterThan(2),
                    lessThan(2)
             ));
        }

        @Test(description = "проверка на то, " +
                "что что Iterable<> состоит из элементов, " +
                "каждый из которых соответствует указанным матчерам")
        public void test9() {
            assertThat(i, eachOfIterable(
                    instanceOf(Integer.class),
                    lessThan(5)
            ));
        }

        @Test(description = "проверка на то, " +
                "что что Iterable<> включает элемент")
        public void test10() {
            assertThat(i, iterableHasItem(1));
        }

        @Test(description = "проверка на то, " +
                "что что Iterable<> включает элемент." +
                "элемент описан матчерами, которым он должен соответствовать")
        public void test11() {
            assertThat(i, iterableHasItem(
                    greaterThan(0),
                    lessThan(2)
            ));
        }

        @Test(description = "Проверка на то, " +
                "что что Iterable<> включает элементы, соответствующие матчерам," +
                "указанное количество раз")
        public void test12() {
            assertThat(i, iterableHasItems(
                    2, //сколько раз в Iterable<> должен встретиться объект,
                    greaterThan(1),
                    instanceOf(Integer.class)
            ));

            assertThat(i, iterableHasItems(
                    //сколько раз в Iterable<> должен встретиться объект
                    greaterThan(1), //описано матчером
                    greaterThan(1),
                    instanceOf(Integer.class)
            ));
        }

        @Test(description = "Проверка на то, " +
                "что что Iterable<> включает элементы, равные указанному значению," +
                "указанное количество раз")
        public void test13() {
            assertThat(i, iterableHasItems(
                    1,//сколько раз в Iterable<> должен встретиться объект,
                    1)
            );

            assertThat(i, iterableHasItems(
                    //сколько раз в Iterable<> должен встретиться объект
                    lessThan(2), // описано матчером
                    1)
            );
        }
    }

