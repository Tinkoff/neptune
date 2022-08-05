Для строки с разделителем
==========================

.. code-block:: java
   :caption: Примеры использования матчера для строки с разделителем

    package org.mypack;

    import static org.hamcrest.MatcherAssert.assertThat;
    import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                 .SetOfObjectsConsistsOfMatcher.*;
    import static ru.tinkoff.qa.neptune.core.api.hamcrest.text
                 .StringContainsWithSeparator.withSeparator;


    public class MyTest {

        private static final String TEST_STRING = "A1,A2,A3,A4,A5";

        @Test(description = "Проверка строки с разделителем")
        public void test1() {

            // проверка что строка с разделителем
            // содержит только указанные под-строки в указанном порядке
            assertThat(TEST_STRING, withSeparator(",", //разделитель
                    "A1", "A2", "A3", "A4", "A5"));

            //проверка что строка с разделителем
            //содержит указанную подстроку (целиком между символами-разделителями)
            assertThat(TEST_STRING,
                    withSeparator(",", //разделитель
                            "A1"));

            assertThat(TEST_STRING, withSeparator(",", //разделитель
                    //матчер для массива строк
                    arrayOf("A1", "A3", "A2", "A4", "A5")));
        }
    }