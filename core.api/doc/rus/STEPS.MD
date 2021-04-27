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
    
    //Рекомендуемый способ доступа до объектов контекста
    public static MyTestContext myTestContext() {
        return getInstance(MyTestContext.class);
    }
    
    //Конструктор может содержать параметры
    protected MyTestContext(/*Параметры*/) {
        //Логика инициализации
    }

    //пример
    public Object getSomeResource() {
        return //Возврат какого-нибудь проинициализированного объекта
        //для использования в шагах (рекомендуется)
        //для использования напрямую (не рекомендуется)
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
        return //Возврат какого-нибудь проинициализированного объекта
        //для использования в шагах (рекомендуется)
        //для использования напрямую (не рекомендуется)
    }
}

```

Ниже речь пойдет о механизме, при помощи которого можно описать базовый набор шагов для того
или иного контекста.

### Шаги, которые возвращают результат

Для описания набора действий с возвратом некоторого результата в рамках некоторого действия используется 
наследники `ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier`. Ниже пример, демонстрирующий
работу и принцип данного класса.

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

#### Шаг, который возвращает объект (`java.lang.Object` и наследники).

Подходит для ситуаций, когда результат выполнения шага рассматривается как один объект, а не коллекция или массив 
объектов. 

- 1

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

- 2

#### Критерии

### Шаги, которые выполняют действие

### Присутствие и отсутствие

## Вложенность/глубина шагов