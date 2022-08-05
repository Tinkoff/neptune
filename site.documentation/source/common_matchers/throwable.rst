Для Throwable
==============

.. code-block:: java
   :caption: Примеры использования матчера для проверки сообщения

    package org.mypack;

    import static org.hamcrest.MatcherAssert.assertThat;
    import static ru.tinkoff.qa.neptune.core.api.hamcrest.throwable
        .ThrowableMessageMatcher.throwableHasMessage;
    import static org.hamcrest.Matchers.*;

    //пример использования
    public class MyTest {

        @Test(description = "Проверка сообщения выброшенного исключения")
        public void test() {
            Exception someException = //ловим исключение

            //проверка сообщения на строгое соответствие
            assertThat(someException, throwableHasMessage("Some message"));

            //проверка сообщения на соответствие одному-нескольким переданным критериям
            assertThat(someException, startsWith("Some"), endsWith("message"));
        }
    }

.. code-block:: java
   :caption: Примеры использования матчера для проверки причины

   package org.mypack;

   import java.io.IOException;

   import static org.hamcrest.MatcherAssert.assertThat;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.throwable
        .ThrowableIsCausedByMatcher.hasRootCause;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.throwable
        .ThrowableIsCausedByMatcher.hasPrimaryCause;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.throwable
        .ThrowableMessageMatcher.throwableHasMessage;
   import static org.hamcrest.Matchers.*;

   //пример использования
   public class MyTest {

     @Test(description = "Проверка самой первой (взятой методом .getCause()) " +
             "причины возникновения исключения")
     public void test() {
       Exception someException = //ловим исключение

       //проверка соответствия причины выброшенного исключения
       //одному-нескольким переданным критериям
       assertThat(someException, hasPrimaryCause(
               instanceOf(IOException.class),
               throwableHasMessage("Some message")
       ));

       //проверка класса причины выброшенного исключения
       assertThat(someException, hasPrimaryCause(IOException.class));

       //проверка класса и сообщения причины выброшенного исключения
       assertThat(someException, hasPrimaryCause(IOException.class, "Some message"));

       //проверка класса и соответствия одному-нескольким переданным критериям сообщения
       //причины выброшенного исключения
       assertThat(someException, hasPrimaryCause(
               IOException.class,
               startsWith("Some"),
               endsWith("message")
       ));
     }

     @Test(description = "Аналогично примеру выше. Проверяется, что среди исключений, " +
             "полученных последовательным вызовом .getCause(), есть то, " +
             "которое удовлетворяет переданным критериям")
     public void test2() {
       Exception someException = //ловим исключение

       assertThat(someException, hasRootCause(
               instanceOf(IOException.class),
               throwableHasMessage("Some message")
       ));

       assertThat(someException, hasRootCause(IOException.class));

       assertThat(someException, hasRootCause(IOException.class, "Some message"));

       assertThat(someException, hasRootCause(
               IOException.class,
               startsWith("Some"),
               endsWith("message")
       ));
     }
   }