Объединения матчеров AND, OR, XOR, NOT
======================================

.. code-block:: java
   :caption: Примеры объединений матчеров

    package org.mypack;

    import static org.hamcrest.MatcherAssert.assertThat;
    import static ru.tinkoff.qa.neptune.core.api.hamcrest.common
                .all.AllCriteriaMatcher.all;
    import static ru.tinkoff.qa.neptune.core.api.hamcrest.common
                .any.AnyMatcher.anyOne;
    import static ru.tinkoff.qa.neptune.core.api.hamcrest.common
                .not.NotMatcher.notOf;
    import static ru.tinkoff.qa.neptune.core.api.hamcrest.common
                .only.one.OnlyOneMatcher.onlyOne;

    //пример использования
    public class MyTest {

        @Test
        public void test() {
            T t = //инициализация;

            //значение должно соответствовать всем перечисленным критериям
            assertThat(t, all(matcher1, matcher3, matcher2, matcher4));

            //значение не должно соответствовать ни одному из перечисленных критериев
            assertThat(t, notOf(matcher1, matcher3, matcher2, matcher4));

            //значение должно соответствовать любому (любым) из перечисленных критериев
            assertThat(t, anyOne(matcher1, matcher3, matcher2, matcher4));

            //значение должно соответствовать только одному из перечисленных критериев
            assertThat(t, onlyOne(matcher1, matcher3, matcher2, matcher4));
        }
    }