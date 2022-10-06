.. code-block:: java
   :caption: Шаг, который возвращает непустой (!=null) объект из коллекции или java.lang.Iterable. Коллекция/Iterable получается не прямым вычислением, а с помощью цепочки выполнения шагов / вычислительных функций

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
   import java.util.function.Function;

   public class MyGetStepSupplier<T, M> extends
           SequentialGetStepSupplier
           .GetObjectFromIterableChainedStepSupplier<
                MyTestContext,
                T,
                M,
                MyGetStepSupplier<T, M>> {

       protected MyGetStepSupplier() {
           this(m -> { //описываем функцию,
               //которая выполняет действия,
               return //и возвращает коллекцию или Iterable<T>,
               //откуда будет взят подходящий по критериям
               //элемент
           });
       }

       protected <S extends Iterable<T>> MyGetStepSupplier(
           Function<M, S> originalFunction) {
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
       //с помощью которых можно указать критерии.
       //-------------------------------------------------------------------
       //В данной реализации с помощью критериев проверяется и фильтруется
       //каждый элемент Iterable<T>. Если в рамках отведенного времени будет
       //получен непустой набор элементов, каждый из которых соответствует
       //переданным критериям, то один из этих элементов возвращается в виде
       //результата, иначе - null или выбрасывается исключение
       //-------------------------------------------------------------------
       //
       //Как же доступны следующие опции:
       //1. returnItemOfIndex(int) - индекс элемента отфильтрованного
       //по критериям набора, который нужно взять
       //
       //2. returnIfEntireSize(ItemsCountCondition) - при достижении какого
       //количества элементов, которые соответствуют критериям, следует вернуть
       //результат
       //
       //3. returnOnCondition(Criteria<Iterable<T>> criteria),
       //returnOnCondition(String description, Predicate<Iterable<T>> criteria),
       //так же returnOnConditionOr(Criteria<Iterable<T>>... criteria),
       //returnOnConditionOnlyOne(Criteria<Iterable<T>>... criteria)
       //или returnOnConditionNot(Criteria<Iterable<T>>... criteria) с помощью
       //вызова этих методов перечисляются критерии, которым должен
       //соответствовать весь набор отфильтрованных элементов.
       //При достижении этого условия возвращается результат
       //
       //Если по каким либо параметрам 1,2 или 3 не удается получить
       //желаемый результат, то выбрасывается исключение

       //при необходимости, можно сделать доступными методы timeOut и pollingInterval
       //при необходимости, можно переопределить методы onStart, onSuccess, onFailure

       //добавляем свои методы, если нужны
   }