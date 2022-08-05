.. code-block:: java
   :caption: Множественная проверка значения

   import static org.hamcrest.Matchers.*;
   import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
   import static ru.tinkoff.qa.neptune.check.MatchAction.match;

   public class MyTest {

       @Test
       public void tezt() {
           int number; //= алгоритм и действия, чтобы получить число
           check(number,
                   match(greaterThan(0)),
                   match(lessThan(1000))); //Работает по принципу soft assert
           //т.е. будут проведены ВСЕ перечисленные выше проверки.
           //Если какие-то проверки не были успешными, тогда
           //в логе/отчете будут выведены неуспешные проверки по отдельности,
           //в самом конце выполнения метод check выбросит AssertError, в сообщении
           //которого будет агрегированная информация о всех неуспешных проверках

           //пример с пояснением. Оно должно описывать, ЧТО проверяется
           check("Число, которое было получено",
                   number,
                   match(greaterThan(0)),
                   match(lessThan(1000)));
       }

   }
