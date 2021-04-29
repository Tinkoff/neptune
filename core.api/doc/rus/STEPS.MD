# Инструменты построения/описания шагов

В данном документе описана реализация тезисов, перечисленных [здесь](./README.MD)

## Контекст

Контекст предназначен для инкапсуляции объектов, которые могут быть использованы как входные 
данные функций-шагов. Например, это могут быть:

- открытая сессия WebDriver'а, 
- открытые соединения с базами данных
- работающий http-клиент

и т.д.

Так же он отделяет логику шагов от логики инициализации ресурсов для их выполнения.

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;

//Формирует параметры вызова конструктора
//Предполагается, что для этого используются значения различных переменных окружения
//среды, в которой выполняются тесты
@CreateWith(provider = MyParameterProvider.class)
//Если конструктор аннотируемого класса не имеет параметров, аннотацию можно не
//указывать
public class MyTestContext extends Context<MyTestContext> { //generic сигнатура для того,
    //чтобы ограничить возможность шагам для других контекстов использовать
    //объекты данного класса

    //Рекомендуемый способ доступа до объектов контекста.
    //Тут все параметры передаются неявно.
    //Если для данного потока выполнения (Thread) уже был создан 
    //объект данного класса - вернется этот объект.
    public static MyTestContext myTestContext() {
        return getInstance(MyTestContext.class);
    }

    //Конструктор может содержать параметры
    protected MyTestContext(/*Параметры*/) {
        //Логика инициализации
    }

    // Простой пример того, как связать шаги, возвращающие результат, с данным контекстом,
    // и ограничить множество таких шагов только теми, которые могут выть выполнены в рамках данного контекста.
    public <T> T get(SequentialGetStepSupplier< //Об этом классе и его наследниках речь пойдет ниже
            MyTestContext, //контекст как входной параметр
            T, // тип возвращаемого результата
            ?,
            ?,
            ?> getStepSupplier) {
        //Шаг, возвращающий результат, выполняется так
        return getStepSupplier.get().apply(this);
    }

    // Простой пример того, как связать шаги, выполняющие действие, с данным контекстом,
    // и ограничить множество таких шагов только теми, которые могут выть выполнены в рамках данного контекста.
    public MyTestContext perform(SequentialActionSupplier< //Об этом классе и его наследниках речь пойдет ниже
            MyTestContext, //контекст как входной параметр
            ?,
            ?> sequentialActionSupplier) {
        //Шаг-действие выполняется так
        sequentialActionSupplier.get().performAction(this);
        return this;
    }

    public Object getSomeResource() {
        return //Возврат какого-нибудь проинициализированного объекта.
        //Рекомендуется для использования в реализациях шагов.
        //Можно использовать напрямую, но не рекомендуется
    }
}
```

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.context.ParameterProvider;

public class MyParameterProvider implements ParameterProvider {
    
    @Override
    public Object[] provide() {
        //Создает параметры вызова конструктора MyTestContext
    }
}
```

## Шаги

### Произвольные шаги, которые могут быть описаны в тесте

Данный механизм можно использовать, чтобы сгруппировать в коде/логе/репорте
множество простых действий или шагов в один произвольный шаг верхнего уровня.

```java
package org.my.pack;

import org.testng.annotations.Test;

import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

public class SomeTest {

    @Test
    public void test() {
        //Шаг просто выполняется и ничего не возвращает
        $("Given: что-то в некотором состоянии", () -> {
            //Некоторые действия
        });
        
        //Шаг выполняется и возвращает какое-то значение
        var result = $("When: что-то делаем и получаем", () -> {
            //Некоторые действия
            return //возврат некоторого значения
        });
        
        $("Then: проверяем то, что получили", () -> {
            assetThat(result, /*Параметры проверки*/);
        });
    }
}
```

Ниже речь пойдет о механизме, при помощи которого можно описать базовый набор шагов для того
или иного контекста.

### Шаги, которые возвращают результат

Для описания набора действий с возвратом некоторого результата в рамках некоторого действия используется 
наследники `ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier`. Ниже пример, демонстрирующий
работу и принцип данного класса.

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;

//Данный класс строит шаг, работающий по принципу функции.
// Входным значением является объект-контекст, содержащий нужные ресурсы для выполнения.
// Выходное значение получается напрямую от входного, или опосредованно через значение типа M,
// полученного от входного значения.
//
// На получение результата типа T от M нужно некоторое время, с некоторой паузой между попытками
// получить желаемый результат.
//
// Результат типа T должен соответствовать некоторым критериям, которые проверяют результат по значению
// типа P.

//Здесь описывается исключение, которое может быть выброшено, если в результате выполнения
//не было получено значение, соответствующее указанным критериям, и за указанное время.
//Указывается класс исключения, у которого есть публичный конструктор с параметром типа
//String (текст сообщения). В данном примере может быть выброшено IllegalStateException,
//с текстом Can't get: <описание получаемого значения и параметров шага, об этом будет ниже>
//Аннотацию можно не указывать, т.к. по умолчанию может быть выброшено
// ru.tinkoff.qa.neptune.core.api.steps.NotPresentException с текстом "Not present: <текст>"
@ThrowWhenNoData(toThrow = IllegalStateException.class, startDescription = "Can't get:")
public class MyGetStepSupplier<T, M, P>
        extends SequentialGetStepSupplier<
        MyTestContext, //Тип входного параметра для результирующей функции-шага.
        //Как правило это контекст, в рамках которого данное действие может быть выполнено
        T, //Тип результата, который должна возвращать функция-шаг
        M, //Часто бывает так, что результат получается не через прямые вычисления от входного
        //параметра, а через некоторое промежуточное значение, полученное от входного параметра.
        //Данный параметр - это тип промежуточного значения, от которого может быть получен нужный
        //результат. Данный параметр необходим, чтобы, входным параметром могла быть функция
        //Function<MyTestContext, M>, построенная аналогичным GetStepSupplier'ом
        P, //Тип значения, которое используется при проверке результата через указанные критерии
        MyGetStepSupplier<T, M, P>> //здесь желательного указывать тип самого класса или расширяющий
        //тип. Наследники SequentialGetStepSupplier работают как Builder, и многие методы
        //возвращают ссылку на объект, от которого ини были вызваны
{

    @Override
    protected Function<M, T> getEndFunction() {
        return //подготовка функции, которая НЕПОСРЕДСТВЕННО возвращает
        //конечный результат
    }


    @Description("Something by parameters {a}, {b}, {c}") //Заголовок/название шага, об этом будет ниже
    public static <T, M, P> MyGetStepSupplier<T, M, P> something(
            @DescriptionFragment("a") Object a, //параметры, которые формируют 
            @DescriptionFragment("b") Object b,  //название шага.
            @DescriptionFragment("c") Object c) { //Об этом будет ниже
        return //возвращает представление/построитель шага
    }

    //Если конечный результат, соответствующий критериям, может быть
    //возвращен не сразу, то следует предусмотреть возможность
    //указывать приемлемый интервал времени, за который этот результат
    //должен быть получен. Как в примере ниже.
    //Функция, возвращенная методом `protected Function<M, T> getEndFunction()`
    //будет выполняться циклически в рамках указанного интервала времени,
    //пока не будет получен нужный результат, или не будет выброшено исключение,
    //которое нельзя игнорировать.
    @Override
    public MyGetStepSupplier<T, M, P> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    //Если между попытками получить необходимый результат нужно делать паузу
    //(в рамках отведенного времени), то надо иметь возможность
    //указывать продолжительность этой паузы. Как в примере ниже.
    @Override
    public MyGetStepSupplier<T, M, P> pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    // В процессе вычислений функция, возвращенная методом `protected Function<M, T> getEndFunction()`,
    // может возвращать результат, который на самом деле не соответствует некоторым нашим ожиданиям.
    // Если такое может происходить, следует предусмотреть возможность указывать критерии, которым
    // должен соответствовать возвращаемый результат.
    @Override
    public MyGetStepSupplier<T, M, P> criteria(Criteria<? super P> criteria) {
        return super.criteria(criteria);
    }

    // Аналогично примеру выше. В данном случае критерий задается в виде предиката с пояснительным
    // описанием критерия
    @Override
    public MyGetStepSupplier<T, M, P> criteria(String description, Predicate<? super P> predicate) {
        return super.criteria(description, predicate);
    }

    // здесь можно описать действия, выполняемые после того, как промежуточное значение
    // типа M было получено, до того как вычисление результата началось.
    // По умолчанию данный метод пустой, но не абстрактный, т.к. в большинстве случаев
    // никаких дополнительных действий выполнять не нужно.
    // Объект-посредник можно проигнорировать. Но если с ним что-то надо сделать в этом
    // методе важно знать, что сюда может прийти null, пустой массив или объект Iterable,
    // если в ходе его вычисления ничего подходящего не было получено.
    @Override
    protected void onStart(M m) {
        //Какая-то логика
    }

    // здесь можно описать действия, выполняемые после того, как результат был вычислен,
    // и никакое исключение не было выброшено.
    // По умолчанию данный метод пустой, но не абстрактный, т.к. в большинстве случаев
    // никаких дополнительных действий выполнять не нужно.
    // Результирующее значение можно проигнорировать. Но если с ним что-то надо сделать в этом
    // методе важно знать, что сюда может прийти null, пустой массив или объект Iterable,
    // если за указанное время ничего подходящее под описанные критерии не было получено.
    @Override
    protected void onSuccess(T t) {
        //Какая-то логика
    }


    // здесь можно описать действия, выполняемые после того, как было выброшено
    // исключение, которое либо не удалось проигнорировать в процессе вычисления,
    // либо было выброшено потому, что выполнение шага не дало ожидаемого результата.
    // Объект-посредник можно проигнорировать. Но если с ним что-то надо сделать в этом
    // методе важно знать, что сюда может прийти null, пустой массив или объект Iterable,
    // если в ходе его вычисления ничего подходящего не было получено.
    @Override
    protected void onFailure(M m, Throwable throwable) {
        //Какая-то логика
    }

    @Override //Результирующая функция-шаг возвращается данным методом.
    //Добавлено для наглядности
    public Function<MyTestContext, T> get() {
        return super.get();
    }
}
```
</details>

Пример связи контекста и шага в тесте

```java
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.my.pack.MyGetStepSupplier.something;
import static org.my.pack.MyTestContext.myTestContext;

public class SomeTest {

    @Test
    public void test() {
        var result = myTestContext().get(something(1, 2, 3)
                .criteria("Some criteria", o -> ) //Можно указать критерий результат, который надо получить
                //если надо
                .criteria("One more criteria", o -> ) //и еще один
                .timeOut(ofSeconds(10)) //можно указать время, за которое нужно получить требуемый резултат
                .pollingInterval(ofMillis(500)) //паузу между попытками получить требуемые данные, в рамках указанного 
                //времени ожидания
                .throwOnNoResult()); //Если ничего походящего/значимого шаг не вернул, и тут нужно выбросить
        //исключение и уронить тест. Будет выброшено исключения, указанное аннотацией
        //ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData над классом из 
        //примера
    }
}
```

Данный пример демонстрирует принцип работы. В большинстве случаев рекомендуется воспользоваться одним из 
классов — наследников `ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier` согласно одной из часто 
встречающихся ситуаций.

#### Шаг, который возвращает объект (`java.lang.Object` и его наследники).

Подходит для ситуаций, когда результат выполнения шага рассматривается как один объект, а не коллекция или массив 
объектов. 

- 1

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Так описывается шаг, возвращающий некий объект-результат. В данном случае наличие 
 * "посредника" не предусматривается. Критерии проверяют на соответствие сам объект,
 * который может быть возвращен в результате вычисления.
 */
@ThrowWhenNoData(toThrow = IllegalStateException.class, startDescription = "Can't get:")
public class MyGetStepSupplier<T> extends SequentialGetStepSupplier.GetObjectStepSupplier<
        MyTestContext,
        T,
        MyGetStepSupplier<T>>
{

    protected MyGetStepSupplier() {
        this(myTestContext -> {
            /**логика выполнения шага сводится к строчкам внутри этого лямбда-выражения**/
            var o = myTestContext.getSomeResource();
            ///дальнейшие вычисления
            return //возврат результата
        });
    }
    
    protected MyGetStepSupplier(Function<MyTestContext, T> originalFunction) {
        /**или к функции, переданной через данный конструктор**/
        super(originalFunction);
    }


    @Description("Something by parameters {a}, {b}, {c}")
    public static <T> MyGetStepSupplier<T> something(
            @DescriptionFragment("a") Object a, //параметры, которые формируют
            @DescriptionFragment("b") Object b,  //название шага.
            @DescriptionFragment("c") Object c) { //Об этом будет ниже
        return //возвращает представление/построитель шага
    }

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать время,
    //в пределах которого конечный результат должен быть получен
    @Override
    public MyGetStepSupplier<T> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать паузу между попытками
    //получить нужный конечный результат.
    //Попытки совершаются циклически в пределах времени, указанного в timeOut(Duration timeOut)
    @Override
    public MyGetStepSupplier<T> pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать критерии,
    //которым должен соответствовать конечный результат
    @Override
    public MyGetStepSupplier<T> criteria(Criteria<? super T> criteria) {
        return super.criteria(criteria);
    }

    // Аналогично примеру выше. В данном случае критерий задается в виде предиката с пояснительным
    // описанием критерия
    @Override
    public MyGetStepSupplier<T> criteria(String description, Predicate<? super T> predicate) {
        return super.criteria(description, predicate);
    }

    @Override //перекрывать необязательно, уже писали ранее. Для наглядности
    protected void onStart(MyTestContext m) {
        //Какая-то логика
    }

    @Override //перекрывать необязательно, уже писали ранее. Для наглядности
    protected void onSuccess(T t) {
        //Какая-то логика
    }


    @Override //перекрывать необязательно, уже писали ранее. Для наглядности
    protected void onFailure(MyTestContext m, Throwable throwable) {
        //Какая-то логика
    }
}
```
</details>

- 2

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Так описывается шаг, возвращающий некий объект-результат. В данном случае предусматривается наличие
 * "посредника". Критерии проверяют на соответствие сам объект, который может быть возвращен в результате
 * вычисления.
 */
@ThrowWhenNoData(toThrow = IllegalStateException.class, startDescription = "Can't get:")
public class MyGetStepSupplier<T, M> extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<
        MyTestContext,
        T,
        M,
        MyGetStepSupplier<T, M>>
{

    protected MyGetStepSupplier() {
        this(m -> {
            /**логика выполнения шага сводится к строчкам внутри этого лямбда-выражения**/
            ///дальнейшие вычисления
            return //возврат результата
        });
    }

    protected MyGetStepSupplier(Function<M, T> originalFunction) {
        /**или к функции, переданной через данный конструктор**/
        super(originalFunction);
    }


    @Description("Something by parameters {a}, {b}, {c}")
    public static <T, M> MyGetStepSupplier<T, M> something(
            @DescriptionFragment("a") Object a, //параметры, которые формируют
            @DescriptionFragment("b") Object b,  //название шага.
            @DescriptionFragment("c") Object c) { //Об этом будет ниже
        return //возвращает представление/построитель шага
    }

    //здесь появляется новая опция.
    //Можно указать шаг, который должен предшествовать данному шагу.
    //У этого шага тип входного параметра тот же, что и у текущего,
    //а тип результирующего значения тот же, что и у значения-"посредника"
    //данного шага. Оба шага образуют иерархическую последовательность выполнения
    //действий, т.е. в рамках данного шага будет выполнен тот, что был указан при
    //вызове метода.
    //
    //Не обязательно данный метод перекрывать и делать его публичным. Данным методом
    //можно воспользоваться при реализации внутренней логики класса, например в конструкторе
    //или статическом методе выше.
    @Override
    protected MyGetStepSupplier<T, M> from(SequentialGetStepSupplier<MyTestContext, ? extends M, ?, ?, ?> from) {
        return super.from(from);
    }

    //Аналогично методу выше. Только вместо подготовленного шага используем
    //некую функцию, которая принимает на вход значение того же типа, что и тип входных
    //данных у этого шага, и возвращает значение того же типа, что и значение-"посредник"
    //данного шага
    @Override
    protected MyGetStepSupplier<T, M> from(Function<MyTestContext, ? extends M> from) {
        return super.from(from);
    }

    //Аналогично методам выше. В данном случае значение того же типа, что и значение-"посредник"
    //данного шага, используется напрямую.
    @Override
    protected MyGetStepSupplier<T, M> from(M from) {
        return super.from(from);
    }

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать время,
    //в пределах которого конечный результат должен быть получен
    @Override
    public MyGetStepSupplier<T, M> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать паузу между попытками
    //получить нужный конечный результат.
    //Попытки совершаются циклически в пределах времени, указанного в timeOut(Duration timeOut)
    @Override
    public MyGetStepSupplier<T, M> pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать критерии,
    //которым должен соответствовать конечный результат
    @Override
    public MyGetStepSupplier<T, M> criteria(Criteria<? super T> criteria) {
        return super.criteria(criteria);
    }

    // Аналогично примеру выше. В данном случае критерий задается в виде предиката с пояснительным
    // описанием критерия
    @Override
    public MyGetStepSupplier<T, M> criteria(String description, Predicate<? super T> predicate) {
        return super.criteria(description, predicate);
    }

    @Override //перекрывать необязательно, уже писали ранее. Для наглядности
    protected void onStart(M m) {
        //Какая-то логика
    }

    @Override //перекрывать необязательно, уже писали ранее. Для наглядности
    protected void onSuccess(T t) {
        //Какая-то логика
    }


    @Override //перекрывать необязательно, уже писали ранее. Для наглядности
    protected void onFailure(M m, Throwable throwable) {
        //Какая-то логика
    }
}
```

Пример связи контекста и шага в тесте

```java
import org.testng.annotations.Test;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.my.pack.MyGetStepSupplier.something;
import static org.my.pack.MyGetSomeOtherValue.someOtherValue;
import static org.my.pack.MyTestContext.myTestContext;

public class SomeTest {

    @Test
    public void test() {
        var result = myTestContext().get(something(1, 2, 3)
                .criteria("Some criteria", o -> )
                //если надо
                .criteria("One more criteria", o -> )
                .timeOut(ofSeconds(10))
                .pollingInterval(ofMillis(500))
                .from(someOtherValue()) // <--!!!!!!!!!!!!!
                .throwOnNoResult());
    }
}
```

Дальнейшие примеры работают по тем же принципам, что и примеры выше. Поэтому в примерах ниже больше 
фокус на принципиальных отличиях.

</details>

#### Шаг, который возвращает один из объектов, входящих в состав Iterable<?>.

Подходит для ситуаций, когда в результате вычислений получается объект `java.lang.Iterable`, из состава 
которого необходимо вернуть только один объект (первый попавшийся или подходящий под некоторые критерии)

- 1

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;

import java.time.Duration;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Так описывается шаг, возвращающий некий объект-результат, выбранный из коллекции или другого java.lang.Iterable.
 * В данном случае наличие "посредника" не предусматривается, т.е. коллекция получается напрямую от входного параметра.
 * Критерии проверяют на соответствие все объекты, входящие в коллекцию, и если такой объект был найден - он возвращается
 * в качестве результата.
 */
@ThrowWhenNoData(toThrow = IllegalStateException.class, startDescription = "Can't get:")
public class MyGetStepSupplier<T> extends SequentialGetStepSupplier.GetObjectFromIterableStepSupplier<
        MyTestContext,
        T,
        MyGetStepSupplier<T>>
{

    protected MyGetStepSupplier() {
        this(myTestContext -> {
            var o = myTestContext.getSomeResource();
            ///дальнейшие вычисления
            return //возврат КОЛЛЕКЦИИ или какого либо другого Iterable
        });
    }

    protected MyGetStepSupplier(Function<MyTestContext, Collection<T>> originalFunction) {
        /**или к функции, переданной через данный конструктор**/
        super(originalFunction);
    }

    //..........

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать критерии,
    //которым должен соответствовать конечный результат
    @Override
    public MyGetStepSupplier<T> criteria(Criteria<? super T> criteria) {
        return super.criteria(criteria);
    }

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать критерии,
    //которым должен соответствовать конечный результат
    @Override
    public MyGetStepSupplier<T> criteria(String description, Predicate<? super T> predicate) {
        return super.criteria(description, predicate);
    }
}
```
</details>

- 2

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Так описывается шаг, возвращающий некий объект-результат, выбранный из коллекции или другого java.lang.Iterable.
 * В данном случае наличие "посредника" предусматривается.
 * 
 * Критерии проверяют на соответствие все объекты, входящие в коллекцию, и если такой объект был найден - он возвращается
 * в качестве результата.
 */
@ThrowWhenNoData(toThrow = IllegalStateException.class, startDescription = "Can't get:")
public class MyGetStepSupplier<T, M> extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<
        MyTestContext,
        T,
        M,
        MyGetStepSupplier<T, M>>
{

    protected MyGetStepSupplier() {
        this(m -> {
            ///дальнейшие вычисления
            return //возврат КОЛЛЕКЦИИ или какого либо другого Iterable
        });
    }

    protected MyGetStepSupplier(Function<M, Collection<T>> originalFunction) {
        /**или к функции, переданной через данный конструктор**/
        super(originalFunction);
    }

    //..........

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать критерии,
    //которым должен соответствовать конечный результат
    @Override
    public MyGetStepSupplier<T, M> criteria(Criteria<? super T> criteria) {
        return super.criteria(criteria);
    }

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать критерии,
    //которым должен соответствовать конечный результат
    @Override
    public MyGetStepSupplier<T, M> criteria(String description, Predicate<? super T> predicate) {
        return super.criteria(description, predicate);
    }
}
```
</details>

#### Шаг, который возвращает один из объектов, входящих в состав массива.

Подходит для ситуаций, когда в результате вычислений получается массив, из состава которого необходимо вернуть только 
один объект (первый попавшийся или подходящий под некоторые критерии)

- 1

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Так описывается шаг, возвращающий некий объект-результат, выбранный из массива.
 * В данном случае наличие "посредника" не предусматривается, т.е. массив получается напрямую от входного параметра.
 * Критерии проверяют на соответствие все объекты, входящие в массив, и если такой объект был найден - он возвращается
 * в качестве результата.
 */
@ThrowWhenNoData(toThrow = IllegalStateException.class, startDescription = "Can't get:")
public class MyGetStepSupplier<T> extends SequentialGetStepSupplier.GetObjectFromArrayStepSupplier<
        MyTestContext,
        T,
        MyGetStepSupplier<T>>
{

    protected MyGetStepSupplier() {
        this(myTestContext -> {
            var o = myTestContext.getSomeResource();
            ///дальнейшие вычисления
            return //возврат МАССИВА
        });
    }

    protected MyGetStepSupplier(Function<MyTestContext, T[]> originalFunction) {
        /**или к функции, переданной через данный конструктор**/
        super(originalFunction);
    }

    //..........

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать критерии,
    //которым должен соответствовать конечный результат
    @Override
    public MyGetStepSupplier<T> criteria(Criteria<? super T> criteria) {
        return super.criteria(criteria);
    }

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать критерии,
    //которым должен соответствовать конечный результат
    @Override
    public MyGetStepSupplier<T> criteria(String description, Predicate<? super T> predicate) {
        return super.criteria(description, predicate);
    }
}
```
</details>

- 2

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Так описывается шаг, возвращающий некий объект-результат, выбранный из массива.
 * В данном случае наличие "посредника" предусматривается.
 * 
 * Критерии проверяют на соответствие все объекты, входящие в массив, и если такой объект был найден - он возвращается
 * в качестве результата.
 */
@ThrowWhenNoData(toThrow = IllegalStateException.class, startDescription = "Can't get:")
public class MyGetStepSupplier<T, M> extends SequentialGetStepSupplier.GetObjectFromArrayChainedStepSupplier<
        MyTestContext,
        T,
        M,
        MyGetStepSupplier<T, M>>
{

    protected MyGetStepSupplier() {
        this(m -> {
            ///дальнейшие вычисления
            return //возврат МАССИВА
        });
    }

    protected MyGetStepSupplier(Function<M, T[]> originalFunction) {
        /**или к функции, переданной через данный конструктор**/
        super(originalFunction);
    }

    //..........

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать критерии,
    //которым должен соответствовать конечный результат
    @Override
    public MyGetStepSupplier<T> criteria(Criteria<? super T> criteria) {
        return super.criteria(criteria);
    }

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать критерии,
    //которым должен соответствовать конечный результат
    @Override
    public MyGetStepSupplier<T> criteria(String description, Predicate<? super T> predicate) {
        return super.criteria(description, predicate);
    }
}
```
</details>

#### Шаг, который возвращает Iterable<?>.

Подходит для ситуаций, когда в результате вычислений получается объект `java.lang.Iterable`, и нужно его вернуть (целиком 
или оставив в его составе элементы, подходящие под некоторые критерии).

- 1

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Так описывается шаг, возвращающий коллекцию или другой java.lang.Iterable.
 * 
 * В данном случае наличие "посредника" не предусматривается, т.е. коллекция получается напрямую от входного параметра.
 * Критерии проверяют на соответствие все объекты, входящие в коллекцию, и если такие объекты были найдены - они возвращается
 * в качестве результата.
 * 
 * Если по истечении времени, отведенного на выполнение шага, коллекция/Iterable не были получены - может вернуться null 
 * (зависит от реализации шага).
 * Если по истечении времени, отведенного на выполнение шага, в коллекции/Iterable не было элементов, подходящих под критерии -
 * вернется пустая коллекция/Iterable
 */
@ThrowWhenNoData(toThrow = IllegalStateException.class, startDescription = "Can't get:")
public class MyGetStepSupplier<T, S extends Iterable<T>> extends SequentialGetStepSupplier.GetIterableStepSupplier<
        MyTestContext,
        S,
        T,
        MyGetStepSupplier<T, S>>
{

    protected MyGetStepSupplier() {
        this(myTestContext -> {
            var o = myTestContext.getSomeResource();
            ///дальнейшие вычисления
            return //возврат КОЛЛЕКЦИИ или какого либо другого Iterable
        });
    }

    protected MyGetStepSupplier(Function<MyTestContext, S> originalFunction) {
        /**или к функции, переданной через данный конструктор**/
        super(originalFunction);
    }

    //..........

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать критерии,
    //которым должен соответствовать конечный результат
    @Override
    public MyGetStepSupplier<T, S> criteria(Criteria<? super T> criteria) {
        return super.criteria(criteria);
    }

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать критерии,
    //которым должен соответствовать конечный результат
    @Override
    public MyGetStepSupplier<T, S> criteria(String description, Predicate<? super T> predicate) {
        return super.criteria(description, predicate);
    }
}
```
</details>

- 2

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Так описывается шаг, возвращающий коллекцию или другой java.lang.Iterable.
 * 
 * В данном случае наличие "посредника" предусматривается.
 * 
 * Критерии проверяют на соответствие все объекты, входящие в коллекцию, и если такие объекты были найдены - они возвращается
 * в качестве результата.
 * 
 * Если по истечении времени, отведенного на выполнение шага, коллекция/Iterable не были получены - может вернуться null 
 * (зависит от реализации шага).
 * Если по истечении времени, отведенного на выполнение шага, в коллекции/Iterable не было элементов, подходящих под критерии -
 * вернется пустая коллекция/Iterable
 */
@ThrowWhenNoData(toThrow = IllegalStateException.class, startDescription = "Can't get:")
public class MyGetStepSupplier<T, M, S extends Iterable<T>> extends SequentialGetStepSupplier.GetIterableChainedStepSupplier<
        MyTestContext,
        S,
        M,
        T,
        MyGetStepSupplier<T, M, S>>
{

    protected MyGetStepSupplier() {
        this(m -> {
            ///дальнейшие вычисления
            return //возврат КОЛЛЕКЦИИ или какого либо другого Iterable
        });
    }

    protected MyGetStepSupplier(Function<M, S> originalFunction) {
        /**или к функции, переданной через данный конструктор**/
        super(originalFunction);
    }

    //..........

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать критерии,
    //которым должен соответствовать конечный результат
    @Override
    public MyGetStepSupplier<T, M, S> criteria(Criteria<? super T> criteria) {
        return super.criteria(criteria);
    }

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать критерии,
    //которым должен соответствовать конечный результат
    @Override
    public MyGetStepSupplier<T, M, S> criteria(String description, Predicate<? super T> predicate) {
        return super.criteria(description, predicate);
    }
}
```
</details>

#### Шаг, который возвращает массив.

Подходит для ситуаций, когда в результате вычислений получается массив, и нужно его вернуть (целиком
или оставив в его составе элементы, подходящие под некоторые критерии).

- 1

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Так описывается шаг, возвращающий массив.
 *
 * В данном случае наличие "посредника" не предусматривается, т.е. массив получается напрямую от входного параметра.
 * Критерии проверяют на соответствие все объекты, входящие в массив, и если такие объекты были найдены - они возвращается
 * в качестве результата.
 *
 * Если по истечении времени, отведенного на выполнение шага, массив не был получен - может вернуться null
 * (зависит от реализации шага).
 * Если по истечении времени, отведенного на выполнение шага, в массиве не было элементов, подходящих под критерии -
 * вернется пустой массив
 */
@ThrowWhenNoData(toThrow = IllegalStateException.class, startDescription = "Can't get:")
public class MyGetStepSupplier<T> extends SequentialGetStepSupplier.GetArrayStepSupplier<
        MyTestContext,
        T,
        MyGetStepSupplier<T>>
{

    protected MyGetStepSupplier() {
        this(myTestContext -> {
            var o = myTestContext.getSomeResource();
            ///дальнейшие вычисления
            return //возврат МАССИВА
        });
    }

    protected MyGetStepSupplier(Function<MyTestContext, T[]> originalFunction) {
        /**или к функции, переданной через данный конструктор**/
        super(originalFunction);
    }

    //..........

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать критерии,
    //которым должен соответствовать конечный результат
    @Override
    public MyGetStepSupplier<T> criteria(Criteria<? super T> criteria) {
        return super.criteria(criteria);
    }

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать критерии,
    //которым должен соответствовать конечный результат
    @Override
    public MyGetStepSupplier<T> criteria(String description, Predicate<? super T> predicate) {
        return super.criteria(description, predicate);
    }
}
```
</details>

- 2

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Так описывается шаг, возвращающий массив.
 *
 * В данном случае наличие "посредника" предусматривается.
 *
 * Критерии проверяют на соответствие все объекты, входящие в массив, и если такие объекты были найдены - они возвращается
 * в качестве результата.
 *
 * Если по истечении времени, отведенного на выполнение шага, массив не был получен - может вернуться null
 * (зависит от реализации шага).
 * Если по истечении времени, отведенного на выполнение шага, в массиве не было элементов, подходящих под критерии -
 * вернется пустой массив
 */
@ThrowWhenNoData(toThrow = IllegalStateException.class, startDescription = "Can't get:")
public class MyGetStepSupplier12<T, M> extends SequentialGetStepSupplier.GetArrayChainedStepSupplier<
        MyTestContext,
        M,
        T,
        MyGetStepSupplier12<T, M>>
{

    protected MyGetStepSupplier12() {
        this(myTestContext -> {
            ///дальнейшие вычисления
            return //возврат МАССИВА
        });
    }

    protected MyGetStepSupplier12(Function<M, T[]> originalFunction) {
        /**или к функции, переданной через данный конструктор**/
        super(originalFunction);
    }

    //..........

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать критерии,
    //которым должен соответствовать конечный результат
    @Override
    public MyGetStepSupplier12<T, M> criteria(Criteria<? super T> criteria) {
        return super.criteria(criteria);
    }

    //перекрывать нужно, если для этого шага может понадобиться возможность указывать критерии,
    //которым должен соответствовать конечный результат
    @Override
    public MyGetStepSupplier12<T, M> criteria(String description, Predicate<? super T> predicate) {
        return super.criteria(description, predicate);
    }
}
```
</details>

#### Критерии

### Шаги, которые выполняют действие

### Присутствие и отсутствие

## Вложенность/глубина шагов