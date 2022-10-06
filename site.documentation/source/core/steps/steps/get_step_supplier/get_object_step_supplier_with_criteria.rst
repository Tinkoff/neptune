.. code-block:: java
   :caption: Шаг, который возвращает непустой (!=null) объект

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
   import java.util.function.Function;

   public class MyGetStepSupplier<T> extends
           SequentialGetStepSupplier
           .GetObjectStepSupplier<MyTestContext, T, MyGetStepSupplier<T>> {

       protected MyGetStepSupplier() {
           this(myTestContext -> { //описываем функцию,
               //которая выполняет действия,
               return //и возвращает результат T
           });
       }

       protected MyGetStepSupplier(Function<MyTestContext, T> originalFunction) {
           //либо передаем функцию снаружи
           super(originalFunction);
       }

       //у объектов данного класса есть публичные методы,
       //с помощью которых можно указать критерии.
       //-------------------------------------------------------------------
       //В данной реализации с помощью критериев проверяется на соответствие
       //само значение типа T. Если в рамках отведенного времени такое значение
       //получается, то результат возвращается в виде объекта типа T, иначе - null,
       //или выбрасывается исключение

       //при необходимости, можно сделать доступными методы timeOut и pollingInterval
       //при необходимости, можно переопределить методы onStart, onSuccess, onFailure

       //добавляем свои методы, если нужны
   }