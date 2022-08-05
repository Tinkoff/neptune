.. code-block:: java
   :caption: Создаем простой контекст

    package org.my.pack;

    import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
    import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
    import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

    import static ru.tinkoff.qa.neptune.core.api.steps.context.ContextFactory.*;

    public class MyTestContext extends Context<MyTestContext> {//Базовый класс
        //В качестве generic-параметра следует указывать наследующий тип

        //Рекомендуемый способ доступа до объектов контекста.
        public static MyTestContext myTestContext() {
            return getCreatedContextOrCreate(MyTestContext.class);
        }

        //Класс должен иметь объявленный public конструктор без параметров,
        //или класс не должен иметь объявленные конструкторы
        public MyTestContext() {
            super();
            //тут можно инициализировать
            //необходимые ресурсы
        }

        // Простой пример того, как связать шаги,
        // возвращающие результат, с данным контекстом
        public <T> T retrieve(SequentialGetStepSupplier< //Общий класс
                //для всех подобных шагов
                MyTestContext, //контекст как входной параметр
                T, // тип возвращаемого результата
                ?,
                ?,
                ?> getStepSupplier) {
            //Шаг, возвращающий результат, выполняется так.
            return super.get(getStepSupplier); //Данный метод имеет
            //модификаторы protected и final,
            //и предназначен только для
            //внутреннего использования, как в этом примере
        }

        // Простой пример того, как связать шаги,
        // выполняющие действие, с данным контекстом
        public MyTestContext execute(SequentialActionSupplier< //Общий класс
                //для всех подобных шагов
                MyTestContext, //контекст как входной параметр
                ?,
                ?> sequentialActionSupplier) {
            //Шаг-действие выполняется так
            return super.perform(sequentialActionSupplier); //Данный метод имеет
            //модификаторы protected и final,
            //и предназначен только для
            //внутреннего использования, как в этом примере
        }

        //контекст может содержать произвольные
        //добавленные методы
        public Object getSomeResource() {
            return //Возврат какого-нибудь проинициализированного
            // или вычисленного объекта.
        }

        // Важно:
        // Методы, аналогичные приведенным ниже, имеет смысл добавлять,
        // если события на стороне проверяемой сущности происходят
        // асинхронно с выполняемым кодом тестов

        // Проверяет на присутствие/появление некоторого значения,
        // описываемого шагом, возвращающим результат.
        // Вернет true, если шаг вернет какое-то непустое значение,
        // иначе - false
        //
        // В метод передается список классов исключений,
        // которые могут возникать в ходе выполнения указанного шага,
        // и которые должны быть проигнорированы.
        @SafeVarargs
        public final boolean presence(
            SequentialGetStepSupplier<MyTestContext, ?, ?, ?, ?> getStepSupplier,
            Class<? extends Throwable>... toIgnore) {
            return super.presenceOf(getStepSupplier, toIgnore); //Данный метод имеет
            //модификаторы protected и final,
            //и предназначен только для
            //внутреннего использования, как в этом примере
        }

        // Проверяет на присутствие/появление некоторого значения,
        // описываемого шагом, возвращающим результат.
        // Вернет true, если шаг вернет какое-то непустое значение,
        // иначе - будет выброшено исключение.
        //
        // В метод передается список классов исключений,
        // которые могут возникать в ходе выполнения указанного шага,
        // и которые должны быть проигнорированы.
        @SafeVarargs
        public final boolean presenceOrThrow(
            SequentialGetStepSupplier<MyTestContext, ?, ?, ?, ?> getStepSupplier,
            Class<? extends Throwable>... toIgnore) {
            return super.presenceOfOrThrow(getStepSupplier, toIgnore); //Данный метод имеет
            //модификаторы protected и final,
            //и предназначен только для
            //внутреннего использования, как в этом примере
       }

       // Проверяет на отсутствие/исчезновение чего-либо,
       // описываемого шагом, возвращающим результат.
       // Вернет true, если шаг за отведенное время вернет какое-то
       // пустое значение, иначе - результат будет false.
       public boolean absence(
            SequentialGetStepSupplier<MyTestContext, ?, ?, ?, ?> getStepSupplier,
            Duration timeOut) { //время на то, чтобы что-то исчезло
            return super.absenceOf(getStepSupplier, timeOut); //Данный метод имеет
            //модификаторы protected и final,
            //и предназначен только для
            //внутреннего использования, как в этом примере
       }

       // Проверяет на отсутствие/исчезновение чего-либо,
       // описываемого шагом, возвращающим результат.
       // Вернет true, если шаг за отведенное время вернет какое-то
       // пустое значение, иначе - будет выброшено исключение.
       public boolean absenceOrThrow(
            SequentialGetStepSupplier<MyTestContext, ?, ?, ?, ?> getStepSupplier,
            Duration timeOut) { //время на то, чтобы что-то исчезло
            return super.absenceOfOrThrow(getStepSupplier, timeOut); //Данный метод имеет
            //модификаторы protected и final,
            //и предназначен только для
            //внутреннего использования, как в этом примере
       }
    }

.. code-block:: java
   :caption: пример использования контекста

   import static java.time.Duration.ofMillis;
   import static java.time.Duration.ofSeconds;

   import static java.time.Duration.ofMillis;
   import static java.time.Duration.ofSeconds;
   import static org.my.pack.MyActionStepSupplier.someAction;
   import static org.my.pack.MyGetStepSupplier.something;
   import static org.my.pack.MyTestContext.myTestContext;

   public class SomeTest {

       @Test
       public void test() {

           //пример вызова выполнения шага с возвратом значения
           var result = myTestContext().retrieve(something(1, 2, 3) //шаг с параметрами
                   // указываем критерии, которым
                   // должен соответствовать результирующий объект,
                   // или его элементы, если возвращается
                   // коллекция или массив
                   .criteria("Some criteria", o -> /*предикат*/)
                   .criteria("Some criteria 2", o -> /*предикат*/)
                   //можно указать время, за которое нужно получить требуемый результат
                   .timeOut(ofSeconds(10))
                   //паузу между попытками получить требуемые данные, в рамках указанного
                   //времени ожидания
                   .pollingInterval(ofMillis(500))
                   .throwOnNoResult()); //когда нужно выбросить исключение
                   // и уронить тест в случае, если шаг вернет какое-то пустое значение


           //пример вызова выполнения шага-действия
           myTestContext().execute(someAction(1, 2, 3) //шаг с параметрами
                   //шаг может выполнять действие над значением, полученным
                   //из другого шага / цепочки шагов
                   .on(something(1, 2, 3)
                           .criteria("Some criteria", o -> /*предикат*/)
                           .timeOut(ofSeconds(10))
                           .pollingInterval(ofMillis(500))
                           .throwOnNoResult())
           );

           //примеры получение данных о присутствии / появлении чего либо
           boolean presence = myTestContext().presence(something(1, 2, 3)
                .criteria("Some criteria", o -> /*предикат*/) //теперь это критерии
                .criteria("Some criteria 2", o -> /*предикат*/) //того, что должно
                // присутствовать/появиться
                .timeOut(ofSeconds(10)) //теперь это время ожидания появления
                .pollingInterval(ofMillis(500))
           );


           boolean presence2 = myTestContext().presenceOrThrow(something(1, 2, 3)
                .criteria("Some criteria", o -> /*предикат*/)
                .criteria("Some criteria 2", o -> /*предикат*/)
                .timeOut(ofSeconds(10))
                .pollingInterval(ofMillis(500))
           );


           //примеры получение данных об отсутствии / исчезновении чего либо
           boolean result = myTestContext().absence(something(1, 2, 3)
                .criteria("Some criteria", o -> /*предикат*/) //теперь это критерии
                .criteria("Some criteria 2", o -> /*предикат*/) //того, что должно
                // отсутствовать/исчезнуть
                //.timeOut(ofSeconds(10)) //<-- Игнорируется
                ofSeconds(10) //время на то, чтобы описанное значение пропало
           );

           boolean result2 = myTestContext().absence(something(1, 2, 3)
                .criteria("Some criteria", o -> /*предикат*/)
                .criteria("Some criteria 2", o -> /*предикат*/)
                // отсутствовать/исчезнуть
                //.timeOut(ofSeconds(10)) //<-- Игнорируется
                ofSeconds(10));

       }
   }

