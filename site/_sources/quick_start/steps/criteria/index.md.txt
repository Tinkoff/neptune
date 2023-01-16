# О критериях

C помощью критериев уточняется результат, который должен вернуть шаг. Примеры, того, как используются критерии:
- [Шаблонизированные шаги, возвращающие результат (get-step)](./../pattern_steps/get_step/index.md)
- [Шаблонизированные произвольные шаги](./../fluent_steps/fluent_pattern_steps.md)

```java
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

public class MyTest {
    
    @Test
    public void myTest() {

        var result = someContext()
                .get(something(/*необходимые параметры*/)
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
                .criteria(condition("One more criteria", o -> {/* предикат*/}))
            ); 
    }
}
```

Некоторые модули Neptune включают утильные классы, играющие роль библиотек часто используемых критериев, позволяющие
то же самое описывать как в примере ниже

```java
import static ru.tinkoff.qa.neptune.some.module.pack.SomeCriteriaUtil.*;

public class MyTest {
    
    @Test
    public void myTest() {

        var result = someContext()
                .get(something(/*необходимые параметры*/)
                .criteria(someCriteria(/*необходимые параметры*/))
                .criteria(oneMoreCriteria(/*необходимые параметры*/))
            ); 
    }
}
```

Описание дополнительных опций, которые предоставляют критерии

```{toctree}

AND_junction.rst
OR_junction.rst
XOR_junction.rst
inversion.rst
```