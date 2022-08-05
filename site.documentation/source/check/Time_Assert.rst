Иногда бывает так, что объект обладает некими мутабельными свойствами, значения которых меняются асинхронно спустя некоторое время

.. code-block:: java
   :caption: Проверки с использованием времени

   import static java.time.Duration.ofSeconds;
   import static org.hamcrest.Matchers.*;
   import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
   import static ru.tinkoff.qa.neptune.check.MatchAction.match;

   public class MyTest {

       @Test
       public void tezt() {
           T obj; //= инициализация
           check(obj, match(ofSeconds(5), matcher)); //указывается время,
           // за которое проверка должна завершиться успешно

           //пример с пояснением. Оно должно описывать, ЧТО проверяется
           check("Проверяемый объект",
                obj,
                match(ofSeconds(5), matcher));

           //аналогично для matchAny, matchNot, matchOnlyOne
       }

   }