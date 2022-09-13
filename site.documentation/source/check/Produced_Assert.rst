.. code-block:: java
   :caption: Проверка не самого значения, а производного от него результата

   import static java.time.Duration.ofSeconds;
   import static org.hamcrest.Matchers.*;
   import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
   import static ru.tinkoff.qa.neptune.check.MatchAction.match;

   public class MyTest {

       @Test
       public void tezt() {
           Integer number; //= алгоритм и действия, чтобы получить число
           check(number,
                   match(greaterThan(0)),
                   match("Квадратный корень", //пояснение, ЧТО проверяется
                         number -> sqrt(number.doubleValue()), //функция для получения
                         //проверяемой производной величины
                         greaterThan(2D))); //матчер

           //пример с пояснением. Оно должно описывать, ЧТО проверяется
           check("Число, которое было получено",
                   number,
                   match(greaterThan(0)),
                   match("Квадратный корень", //пояснение, ЧТО проверяется
                         number -> sqrt(number.doubleValue()), //функция для получения
                         //проверяемой производной величины
                         greaterThan(2D))); //матчер

           //пример для кейса, когда нужно указать время
           Object obj; //= инициализация
           check("Проверяемый объект",
                   obj,
                   match("Какое-то вычислимое свойство",
                           o -> o.returnSomething(),
                           //указывается время,
                           ofSeconds(5), // за которое проверка
                           //должна завершиться успешно
                           matcher)); //матчер

           //аналогично для matchAny, matchNot, matchOnlyOne
       }

   }
