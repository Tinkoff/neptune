.. code-block:: java
   :caption: Шаг, который возвращает непустой (!=null) объект из коллекции или java.lang.Iterable

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
   import java.util.function.Function;

   public class MyGetStepSupplier<T> extends
           SequentialGetStepSupplier
           .GetObjectFromIterableStepSupplier<MyTestContext, T, MyGetStepSupplier<T>> {

       protected MyGetStepSupplier() {
           this(myTestContext -> { //описываем функцию,
               //которая выполняет действия,
               return //и возвращает коллекцию или Iterable<T>,
               //откуда будет взят первый подходящий по критериям
               //элемент
           });
       }

       protected <S extends Iterable<R>> MyGetStepSupplier(
           Function<MyTestContext, S> originalFunction) {
           //либо передаем функцию снаружи
           super(originalFunction);
       }

       //у объектов данного класса есть публичные методы,
       //с помощью которых можно указать критерии

       //при необходимости, можно сделать доступными методы timeOut и pollingInterval
       //при необходимости, можно переопределить методы onStart, onSuccess, onFailure

       //добавляем свои методы, если нужны
   }