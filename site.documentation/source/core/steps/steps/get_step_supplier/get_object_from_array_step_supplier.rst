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

       //у объектов данного класса есть публичные методы,
       //с помощью которых можно указать критерии.
       //-------------------------------------------------------------------
       //В данной реализации с помощью критериев проверяется и фильтруется
       //каждый элемент массива T[]. Если в рамках отведенного времени будет
       //получен непустой массив элементов, каждый из которых соответствует
       //переданным критериям, то один из этих элементов возвращается в виде
       //результата, иначе - null или выбрасывается исключение
       //-------------------------------------------------------------------
       //
       //Как же доступны следующие опции:
       //1. returnItemOfIndex(int) - индекс элемента отфильтрованного
       //по критериям массива, который нужно взять
       //
       //2. returnIfEntireLength(ItemsCountCondition) - при достижении какого
       //количества элементов, которые соответствуют критериям, следует вернуть
       //результат
       //
       //3. returnOnCondition(Criteria<T[]> criteria),
       //returnOnCondition(String description, Predicate<T[]> criteria),
       //так же returnOnConditionOr(Criteria<T[]>... criteria),
       //returnOnConditionOnlyOne(Criteria<T[]>... criteria)
       //или returnOnConditionNot(Criteria<T[]>... criteria) с помощью
       //вызова этих методов перечисляются критерии, которым должен
       //соответствовать весь массив отфильтрованных элементов.
       //При достижении этого условия возвращается результат
       //
       //Если по каким либо параметрам 1,2 или 3 не удается получить
       //желаемый результат, то выбрасывается исключение

       //при необходимости, можно сделать доступными методы timeOut и pollingInterval
       //при необходимости, можно переопределить методы onStart, onSuccess, onFailure

       //добавляем свои методы, если нужны
   }