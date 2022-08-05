.. code-block:: java
   :caption: Проверка значения матчерами, объединенными в NOT-выражение

   import static org.hamcrest.Matchers.*;
   import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
   import static ru.tinkoff.qa.neptune.check.MatchAction.match;
   import static ru.tinkoff.qa.neptune.check.MatchAction.matchNot;

   public class MyTest {

       @Test
       public void tezt() {
           int number; //= алгоритм и действия, чтобы получить число
           check(number,
                   match(greaterThan(0)),
                   //перечисляются 1 и более критерия.
                   // Проверяемые значения не должны соответствовать ни одному из них
                   matchNot(lessThan(1000)));

           //пример с пояснением. Оно должно описывать, ЧТО проверяется
           check("Число, которое было получено",
                   number,
                   match(greaterThan(0)),
                   matchNot(lessThan(1000)));
       }

   }