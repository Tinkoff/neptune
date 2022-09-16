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

       //у объектов данного класса есть публичные методы,
       //с помощью которых можно указать критерии.
       //-------------------------------------------------------------------
       //В данной реализации с помощью критериев проверяется и фильтруется
       //каждый элемент Iterable<T>. Если в рамках отведенного времени будет
       //получен непустой List<T> элементов, каждый из которых соответствует
       //переданным критериям, то такой List возвращается в виде результата,
       //иначе - пустой List<T> или null, или выбрасывается исключение
       //-------------------------------------------------------------------
       //
       //Как же доступны следующие опции:
       //1. returnListOfSize(int) - сколько элементов,
       //которые соответствуют критериям, нужно взять
       //
       //2. returnBeforeIndex(int) или
       //.returnAfterIndex(int) - до или после какого индекса следует
       //взять элементы из отфильтрованного по критериям List<T>
       //Вызов 1 или 2 обнуляет данные, переданные с помощью 3
       //
       //3. returnItemsOfIndexes(Integer...) - перечисление индексов
       //элементов отфильтрованного по критериям List<T>, которые следует
       //взять. Вызов данного метода обнуляет данные,
       //переданные с помощью 1 и 2
       //
       //4. returnIfEntireSize(ItemsCountCondition) - при достижении какого
       //количества элементов, которые соответствуют критериям, следует вернуть
       //отфильтрованный List<T> целиком или List<T> его отдельных элементов
       //
       //5. returnOnCondition(Criteria<List<T>> criteria),
       //returnOnCondition(String description, Predicate<List<T>> criteria),
       //так же returnOnConditionOr(Criteria<List<T>>... criteria),
       //returnOnConditionOnlyOne(Criteria<List<T>>... criteria)
       //или returnOnConditionNot(Criteria<List<T>>... criteria) с помощью
       //вызова этих методов перечисляются критерии, которым должен
       //соответствовать весь List<T> отфильтрованных элементов.
       //При достижении этого условия возвращается отфильтрованный List<T> целиком
       //или List<T> его отдельных элементов
       //
       //Если по каким либо параметрам 1,2,3,4 или 5 не удается получить
       //желаемый результат, то выбрасывается исключение

       //при необходимости, можно сделать доступными методы timeOut и pollingInterval
       //при необходимости, можно переопределить методы onStart, onSuccess, onFailure

       //добавляем свои методы, если нужны
   }