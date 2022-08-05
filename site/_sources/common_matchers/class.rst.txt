Для класса объекта
===================

.. code-block:: java
   :caption: Пример использования матчера для проверки равенства классов объектов

    package org.mypack;

    import static org.hamcrest.MatcherAssert.assertThat;
    import static ru.tinkoff.qa.neptune.core.api.hamcrest.ofclass
                .OfClassMatcher.isObjectOfClass;

    //пример использования
    public class MyTest {

        @Test(description = "Проверка того, что класс объекта равен ожидаемому классу")
        public void test() {
            var someObject = //инициализация или получение
            assertThat(someObject, isObjectOfClass(SomeClass.class));
        }
    }