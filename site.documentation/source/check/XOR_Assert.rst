.. code-block:: java
   :caption: Проверка значения матчерами, объединенными в XOR-выражение

   import static org.hamcrest.Matchers.*;
   import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
   import static ru.tinkoff.qa.neptune.check.MatchAction.match;
   import static ru.tinkoff.qa.neptune.check.MatchAction.matchOnlyOne;

   public class MyTest {

       @Test
       public void tezt() {
           int number; //= алгоритм и действия, чтобы получить число
           check(number,
                   match(greaterThan(0)),
                   //перечисляются 2 и более критерия.
                   // Проверяемые значения должны соответствовать только одному из них
                   matchOnlyOne(lessThan(1000)), greaterThan(100));

           //пример с пояснением. Оно должно описывать, ЧТО проверяется
           check("Число, которое было получено",
                   number,
                   match(greaterThan(0)),
                   matchOnlyOne(lessThan(1000)), greaterThan(100));
       }

   }