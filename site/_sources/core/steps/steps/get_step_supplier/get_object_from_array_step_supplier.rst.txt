.. code-block:: java
   :caption: Шаг, который возвращает непустой (!=null) объект из массива

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
   import java.util.function.Function;

   public class MyGetStepSupplier<T> extends
           SequentialGetStepSupplier
           .GetObjectFromArrayStepSupplier<MyTestContext, T, MyGetStepSupplier<T>> {

       protected MyGetStepSupplier() {
           this(myTestContext -> { //описываем функцию,
               //которая выполняет действия,
               return //и возвращает массив,
               //откуда будет взят подходящий по критериям
               //элемент
           });
       }

       protected MyGetStepSupplier(Function<MyTestContext, T[]> originalFunction) {
           //либо передаем функцию снаружи
           super(originalFunction);
       }

       //при необходимости, можно сделать доступными методы timeOut и pollingInterval
       //при необходимости, можно переопределить методы onStart, onSuccess, onFailure

       //добавляем свои методы, если нужны
   }