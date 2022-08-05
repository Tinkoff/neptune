# @CaptureOnSuccess

Перечисляет классы, расширяющие [Captor](./../index.rst), чьи объекты создают аттачи, когда щаг выполняется успешно.

## @CaptureOnSuccess для [get-step](./../../../steps/steps/get_step_supplier/index.md)

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;

//Тут следует указывать классы, чьи объекты могут получить данные 
//из R (сделать аттач из результата выполнения)
//
// Здесь можно указать несколько классов.
// Можно указать абстрактные классы,
// расширяющие Captor<R, ?>.
// Если в classpath есть их неабстрактные наследники,
// тогда они будут использованы автоматически.
//
// ВАЖНО: Наследники Captor<R, ?> не
// должны иметь объявленных конструкторов,
// или у них должны быть объявлены публичные конструкторы без параметров.
@CaptureOnSuccess(by = ExampleCaptor3.class)
public class MyGetStepSupplier<T, R, M, P>
        //любой подходящий наследник
        extends SequentialGetStepSupplier<T, R, M, P, MyGetStepSupplier<T, R, M, P>> {

    //Так же можно делать аттачи из значений нестатических полей
    //заполняемых в ходе выполнения шага
    @CaptureOnSuccess(by = {ExampleCaptor4.class})
    private V someValue;
    
    //реализация поведения
}
```


## @CaptureOnSuccess для [action-step](./../../../steps/steps/action_supplier.md)

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;

//Тут следует указывать классы, чьи объекты могут получить данные 
//из R (сделать аттач из значения-объекта выполнения действия)
//
// Здесь можно указать несколько классов.
// Можно указать абстрактные классы,
// расширяющие Captor<R, ?>.
// Если в classpath есть их неабстрактные наследники,
// тогда они будут использованы автоматически.
//
// ВАЖНО: Наследники Captor<R, ?> не
// должны иметь объявленных конструкторов,
// или у них должны быть объявлены публичные конструкторы без параметров.
@CaptureOnSuccess(by = ExampleCaptor3.class)
public class MyActionStepSupplier<T, R> 
    extends SequentialActionSupplier<T, R, MyActionStepSupplier<T, R>> {

    //Так же можно делать аттачи из значений нестатических полей
    //заполняемых в ходе выполнения шага
    @CaptureOnSuccess(by = {ExampleCaptor4.class})
    private V someValue;

    //реализация поведения
}
```