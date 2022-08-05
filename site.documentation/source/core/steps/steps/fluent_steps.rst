.. code-block:: java
   :caption: Произвольный шаг, описанный в тесте

   package org.my.pack;

   import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

   public class SomeTest {

       @Test
       public void test() {
           //Шаг просто выполняется и ничего не возвращает
           $("Given: что-то в некотором состоянии", () -> {
              //Некоторые действия
           });

           //Шаг выполняется и возвращает какое-то значение
           var result = $("When: что-то делаем и получаем", () -> {
               //Некоторые действия
               return //возврат некоторого значения
           });

           $("Then: проверяем то, что получили", () -> {
               assetThat(result, /*Параметры проверки*/);
           });
       }
   }