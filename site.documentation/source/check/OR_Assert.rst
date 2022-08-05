.. code-block:: java
   :caption: Проверка значения матчерами, объединенными в OR-выражение

   import static org.hamcrest.Matchers.*;
   import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
   import static ru.tinkoff.qa.neptune.check.MatchAction.match;
   import static ru.tinkoff.qa.neptune.check.MatchAction.matchAny;

   public class MyTest {

       @Test
       public void tezt() {
           int number; //= алгоритм и действия, чтобы получить число
           check(number,
                   match(greaterThan(0)),
                   //перечисляются 2 и более критерия.
                   //Проверяемые значения должны соответствовать любому/любым из них
                   matchAny(lessThan(1000), greaterThanOrEqualTo(200)));

           //пример с пояснением. Оно должно описывать, ЧТО проверяется
           check("Число, которое было получено",
                   number,
                   match(greaterThan(0)),
                   matchAny(lessThan(1000), greaterThanOrEqualTo(200)));
       }

   }