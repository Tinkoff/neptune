.. code-block:: java
   :caption: Вычисление значения вместе с проверкой

    import static org.hamcrest.Matchers.*;
    import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.evaluateAndCheck;
    import static ru.tinkoff.qa.neptune.check.MatchAction.match;

    public class MyTest {

        @Test
        public void tezt() {
            //Данная фича нужна скорее для красоты лога/отчета.
            //Шаг с проверкой скрывает внутри себя под-шаги,
            //часть из которых - вычисление проверяемого значения,
            //остальные - проверки
            evaluateAndCheck("Полученное число", () -> {
                        //алгоритм и действия, чтобы получить число
                        return number;
                    },
                    match(greaterThan(0)),
                    match(lessThan(1000)));
        }

    }