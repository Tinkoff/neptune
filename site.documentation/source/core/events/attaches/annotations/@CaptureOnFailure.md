# @CaptureOnFailure

Перечисляет классы, расширяющие [Captor](./../index.rst), чьи объекты создают аттачи, когда щаг завершился выбросом
исключения.

## @CaptureOnFailure для [get-step](./../../../steps/steps/get_step_supplier/index.md)

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;

//Тут следует указывать классы, чьи объекты могут получить данные 
//из M (сделать аттач из значения, от которого нужно было получить 
//результат)
//
// Здесь можно указать несколько классов.
// Можно указать абстрактные классы,
// расширяющие Captor<M, ?>.
// Если в classpath есть их неабстрактные наследники,
// тогда они будут использованы автоматически.
//
// ВАЖНО: Наследники Captor<R, ?> не
// должны иметь объявленных конструкторов,
// или у них должны быть объявлены публичные конструкторы без параметров.
@CaptureOnFailure(by = ExampleCaptor2.class)
public class MyGetStepSupplier<T, R, M, P>
        //любой подходящий наследник
        extends SequentialGetStepSupplier<T, R, M, P, MyGetStepSupplier<T, R, M, P>> {

    //Так же можно делать аттачи из значений нестатических полей
    //заполняемых в ходе выполнения шага
    @CaptureOnFailure(by = {ExampleCaptor4.class})
    private V someValue;
    
    //реализация поведения
}
```

## @CaptureOnFailure для [action-step](./../../../steps/steps/action_supplier.md)

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;

//Тут следует указывать классы, чьи объекты могут получить данные 
//из T (сделать аттач из значения-входного параметра выполнения действия)
//
// Здесь можно указать несколько классов.
// Можно указать абстрактные классы,
// расширяющие Captor<T, ?>.
// Если в classpath есть их неабстрактные наследники,
// тогда они будут использованы автоматически.
//
// ВАЖНО: Наследники Captor<R, ?> не
// должны иметь объявленных конструкторов,
// или у них должны быть объявлены публичные конструкторы без параметров.
@CaptureOnFailure(by = ExampleCaptor.class)
public class MyActionStepSupplier<T, R> 
    extends SequentialActionSupplier<T, R, MyActionStepSupplier<T, R>> {

    //Так же можно делать аттачи из значений нестатических полей
    //заполняемых в ходе выполнения шага
    @CaptureOnFailure(by = {ExampleCaptor4.class})
    private V someValue;

    //реализация поведения
}
```

