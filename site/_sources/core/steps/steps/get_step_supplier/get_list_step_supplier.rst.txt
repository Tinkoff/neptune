.. code-block:: java
   :caption: Шаг, который возвращает непустой (!=null и .size() > 0) лист из элементов коллекции/Iterable.

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
   import java.util.function.Function;

   public class MyGetStepSupplier<T, S extends Iterable<T>> extends
           SequentialGetStepSupplier
           .GetListStepSupplier<MyTestContext, S, T, MyGetStepSupplier<T, S>> {

       protected MyGetStepSupplier() {
           this(myTestContext -> { //описываем функцию,
               //которая выполняет действия,
               return //и возвращает коллекцию или Iterable<T> (S)
               //из элементов, которые после фильтрации по критериям
               //соберутся в результирующий лист
           });
       }

       protected MyGetStepSupplier(Function<MyTestContext, S> originalFunction) {
           //либо передаем функцию снаружи
           super(originalFunction);
       }

       //при необходимости, можно сделать доступными методы timeOut и pollingInterval
       //при необходимости, можно переопределить методы onStart, onSuccess, onFailure

       //добавляем свои методы, если нужны
   }