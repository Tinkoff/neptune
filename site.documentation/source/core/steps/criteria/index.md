# Критерии

Критерии нужны для того, чтобы уточнить значение, которое должен вернуть [get-step](../steps/get_step_supplier/index.md).
Для этих целей используются объекты класса `ru.tinkoff.qa.neptune.core.api.steps.Criteria`.

```java
package org.my.pack;

import static org.my.pack.MyGetStepSupplier.something;
import static org.my.pack.MyTestContext.myTestContext;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

public class SomeTest {

    @Test
    public void test() {
        var result = myTestContext().retrieve(something(1, 2, 3)
                //Критерий, записанный в свободной форме,
                //в виде пояснительной строки и предиката.
                //Объект ru.tinkoff.qa.neptune.core.api.steps.Criteria 
                // создается внутри вызванного метода.
                .criteria("Some criteria", o -> {/* предикат*/})
                //Критерий, записанный в свободной форме, 
                // в виде пояснительной строки и предиката.
                //Все это явно передано в виде объекта 
                // ru.tinkoff.qa.neptune.core.api.steps.Criteria,
                //созданного статическим методом 
                // ru.tinkoff.qa.neptune.core.api.steps.Criteria#condition
                .criteria(condition("One more criteria", o -> {/* предикат*/})));
    }
}
```

Так же можно создавать util-классы, которые описывают часто используемые критерии:

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;

import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

public final class MyCriteriaLib {

    private MyCriteriaLib() {
        super();
    }
    
    public static <T> Criteria<T> someCriteria(Object param1,
                                               Object param2) {
        return condition(t -> /* предикат*/);
    }
    
    public static <T> Criteria<T> oneMoreCriteria(Object param1,
                                                  Object param2) {
        return condition(t -> /* предикат*/);
    }
}

```

И тогда

```java
package org.my.pack;

import static org.my.pack.MyCriteriaLib.oneMoreCriteria;
import static org.my.pack.MyCriteriaLib.someCriteria;
import static org.my.pack.MyGetStepSupplier.something;
import static org.my.pack.MyTestContext.myTestContext;

public class SomeTest {

    @Test
    public void test() {
        var result = myTestContext().retrieve(something(1, 2, 3)
                .criteria(someCriteria("A", "B"))
                .criteria(oneMoreCriteria(4, 5)));
    }
}
```

```{toctree}
:hidden:

AND_junction.rst
OR_junction.rst
XOR_junction.rst
inversion.rst
```

