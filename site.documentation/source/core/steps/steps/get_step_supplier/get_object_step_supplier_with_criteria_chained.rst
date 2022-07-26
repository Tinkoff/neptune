.. code-block:: java
   :caption: Шаг, который возвращает непустой (!=null) объект. Для шага предусматриваются критерии. Результирующее значение получается не прямым вычислением, а с помощью цепочки выполнения шагов / вычислительных функций

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
   import java.util.function.Function;

   public class MyGetStepSupplier<T, M> extends
           SequentialGetStepSupplier
           .GetObjectChainedStepSupplier<MyTestContext, T, M, MyGetStepSupplier<T, M>> {

       protected MyGetStepSupplier() {
           this(m -> { //описываем функцию,
               //которая выполняет действия,
               return //и возвращает результат T
           });
       }

       protected MyGetStepSupplier(Function<M, T> originalFunction) {
           //либо передаем функцию снаружи
           super(originalFunction);
       }

       //Здесь появляется новая опция.
       //Можно указать шаг, который должен предшествовать данному шагу.
       //
       //У переданного шага тип входного параметра должен быть тот же,
       //а тип результирующего значения - расширяющий тип значения-"посредника"
       //данного шага. Оба шага образуют иерархическую последовательность выполнения
       //действий, т.е. в рамках данного шага будет выполнен тот, что был указан при
       //вызове метода.
       //
       //Необязательно данный метод перекрывать и делать его публичным. Данным методом
       //можно воспользоваться при реализации внутренней логики класса или его объектов.
       @Override
       protected MyGetStepSupplier<T, M> from(
           SequentialGetStepSupplier<MyTestContext, ? extends M, ?, ?, ?> from) {
           return super.from(from);
       }

       //Аналогично методу выше. Вместо подготовленного шага используем функцию
       @Override
       protected MyGetStepSupplier<T, M> from(Function<MyTestContext, ? extends M> from) {
           return super.from(from);
       }

       //Аналогично методам выше. В данном случае значение "посредник"
       //данного шага используется напрямую.
       @Override
       protected MyGetStepSupplier<T, M> from(M from) {
           return super.from(from);
       }

       //у объектов данного класса есть публичные методы,
       //с помощью которых можно указать критерии

       //при необходимости, можно сделать доступными методы timeOut и pollingInterval
       //при необходимости, можно переопределить методы onStart, onSuccess, onFailure

       //добавляем свои методы, если нужны
   }