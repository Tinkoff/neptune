# Контексты

Контекст или инкапсулированные в нем объекты являются аргументами для функций, формируемых 
[шаблонизированными шагами](../../../quick_start/steps/pattern_steps/index.md).

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

import static ru.tinkoff.qa.neptune.core.api.steps.context.ContextFactory.*;

public class MyTestContext extends Context<MyTestContext> {//Базовый класс
    //В качестве generic-параметра следует указывать наследующий тип

    //Рекомендуемый способ доступа до объектов контекста.
    public static MyTestContext myTestContext() {
        return getCreatedContextOrCreate(MyTestContext.class);
    }

    //Класс должен иметь объявленный public конструктор без параметров,
    //или класс не должен иметь объявленные конструкторы
    public MyTestContext() {
        super();
        //тут можно инициализировать
        //необходимые ресурсы
    }

    // Простой пример того, как связать шаги,
    // возвращающие результат, с данным контекстом
    public <T> T retrieve(SequentialGetStepSupplier< //Общий класс
        //для всех подобных шагов
        MyTestContext, //контекст как входной параметр
        T, // тип возвращаемого результата
        ?,
        ?,
        ?> getStepSupplier) {
        //Шаг, возвращающий результат, выполняется так.
        return super.get(getStepSupplier); //Данный метод имеет
        //модификаторы protected и final,
        //и предназначен только для
        //внутреннего использования, как в этом примере
    }

    // Простой пример того, как связать шаги,
    // выполняющие действие, с данным контекстом
    public MyTestContext execute(SequentialActionSupplier< //Общий класс
        //для всех подобных шагов
        MyTestContext, //контекст как входной параметр
        ?,
        ?> sequentialActionSupplier) {
        //Шаг-действие выполняется так
        return super.perform(sequentialActionSupplier); //Данный метод имеет
        //модификаторы protected и final,
        //и предназначен только для
        //внутреннего использования, как в этом примере
    }

    //контекст может содержать произвольные
    //добавленные методы
    public Object getSomeResource() {
        return //Возврат какого-нибудь проинициализированного
        // или вычисленного объекта.
    }
}
```

[Здесь](../steps/index.md) описание того, как создавать шаблонизированные шаги.

```{toctree}
:hidden:

refresh.rst
stopping.md
```