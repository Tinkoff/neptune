.. code-block:: java
   :caption: Простая проверка значения

   import static org.hamcrest.Matchers.*;
   import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
   import static ru.tinkoff.qa.neptune.check.MatchAction.match;

   public class MyTest {

       @Test
       public void tezt() {
           int number; //= алгоритм и действия, чтобы получить число
           check(number, match(greaterThan(0)));

           //пример с пояснением. Оно должно описывать, ЧТО проверяется
           check("Число, которое было получено",
                number,
                match(greaterThan(0)));
       }

   }
