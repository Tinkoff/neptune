# Выполнение действия (action-step)

Для этого используются наследники `ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier`.

Ниже пример, демонстрирующий работу и принцип данного класса.

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.Action;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

import java.util.function.Function;

public class MyActionStepSupplier<T> 
    extends SequentialActionSupplier<MyTestContext, T,
    //Здесь нужно указывать тип самого класса или расширяющий тип
    MyActionStepSupplier<T>>
    //Наследники MyActionStepSupplier работают как Builder, и многие методы
    //возвращают ссылку на объект, от которого ини были вызваны
{
    
    
    public static <T> MyActionStepSupplier<T> someAction(
            Object a, //опционально
            Object b,  //параметры шага
            Object c) {
        //возвращает объект шага
        return new MyActionStepSupplier<>(/*параметры конструктора*/);
    }

    @Override //в данном методе необходимо реализовать
    //действие, выполняемое шагом
    protected void howToPerform(T value) {
        //логика шага
    }

    @Override //Результирующее действие-шаг возвращается данным методом
    //Добавлено для наглядности
    public Action<MyTestContext> get() {
        return super.get();
    }

    //C помощью метода `performOn` можно указать шаг, результатом которого является значение
    //типа T
    //
    //У этого шага тип входного параметра тот же, что и у текущего.
    //
    // Оба шага образуют иерархическую последовательность выполнения
    //действий, т.е. в рамках данного шага будет выполнен тот, что был указан при
    //вызове метода.
    //
    //Необязательно метод `performOn` перекрывать и делать его публичным. Данным методом
    //можно воспользоваться при реализации внутренней логики класса или его объектов.
    public MyActionStepSupplier<T> on(SequentialGetStepSupplier<MyTestContext, T, ?, ?, ?> getStep) {
        return super.performOn(getStep);
    }

    //Аналогично методу выше. Вместо подготовленного шага используем функцию
    public MyActionStepSupplier<T> on(Function<MyTestContext, T> f) {
        return super.performOn(f);
    }

    //Аналогично методам выше. Объект типа T используется напрямую
    public MyActionStepSupplier<T> on(T t) {
        return super.performOn(t);
    }

    // Здесь можно описать действия, что выполнятся до начала шага.
    // По умолчанию данный метод пустой, но не абстрактный, т.к. в большинстве случаев
    // никаких дополнительных действий выполнять не нужно.
    @Override
    protected void onStart(MyTestContext myTestContext) {
        //Какая-то логика
    }

    // Здесь можно описать действия, выполняемые после того, как было выброшено
    // исключение.
    // По умолчанию данный метод пустой, но не абстрактный, т.к. в большинстве случаев
    // никаких дополнительных действий выполнять не нужно.
    @Override
    protected void onFailure(MyTestContext myTestContext, Throwable throwable) {
        //Какая-то логика
    }

    // Здесь описывается Map<String, String>, 
    // в котором ключ - название параметра, значение - значение параметра.
    // Не всегда параметры можно передать до начала выполнения шага. 
    // Некоторые значения могут получиться в процессе 
    // выполнения шага.
    @Override
    protected Map<String, String> additionalParameters() {
        //Возврат дополнительных динамических параметров, которые 
        // были рассчитаны уже в процессе выполнения шага
    }
}
```

Небольшое пояснение к примеру:

- данный класс строит объект, чей класс реализует функциональный интерфейс (`ru.tinkoff.qa.neptune.core.api.steps.Action`).
  Именно этот объект выполняет действие

- входным значением является объект-[контекст](../context/index.md), содержащий нужные ресурсы для выполнения.
  `T` - тип объекта, над которым выполняется действие. 

- объект типа `T` может быть вычислен с использованием входного параметра шага, или сформирован независимо от него,
  например, с целью передачи каких-либо параметров

  