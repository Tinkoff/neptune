# Инструменты построения/описания шагов

В данном документе описана реализация тезисов, перечисленных [здесь](./README.MD)

## Оглавление
- [Контекст](#Контекст)
- [Шаги](#Шаги)
    - [Произвольные шаги, которые могут быть описаны в тесте](#Произвольные-шаги-которые-могут-быть-описаны-в-тесте)
    - [Шаги, которые возвращают результат](#Шаги-которые-возвращают-результат)
        - [Шаг, который возвращает объект (`java.lang.Object` и его наследники).](#Шаг-который-возвращает-объект-javalangObject-и-его-наследники)
        - [Шаг, который возвращает один из объектов, входящих в состав Iterable<?>.](#Шаг-который-возвращает-один-из-объектов-входящих-в-состав-iterable)
        - [Шаг, который возвращает один из объектов, входящих в состав массива.](#Шаг-который-возвращает-один-из-объектов-входящих-в-состав-массива)
        - [Шаг, который возвращает Iterable<?>.](#Шаг-который-возвращает-iterable)
        - [Шаг, который возвращает массив.](#Шаг-который-возвращает-массив)
    - [Шаги, которые выполняют действие](#Шаги-которые-выполняют-действие)
    - [Критерии](#Критерии)
        - [Объединение критериев в один логическим И(AND)](#Объединение-критериев-в-один-логическим-И-and)
        - [Объединение критериев в один логическим ИЛИ (OR)](#Объединение-критериев-в-один-логическим-ИЛИ-or)
        - [Объединение критериев в один исключающим ИЛИ (XOR)](#Объединение-критериев-в-один-исключающим-ИЛИ-xor)
        - [Инверсия критериев (NOT)](#Инверсия-критериев-not)
    - [Присутствие и отсутствие](#Присутствие-и-отсутствие)
        - [Присутствие/появление](#Присутствиепоявление)
        - [Отсутствие/исчезновение](#Отсутствиеисчезновение)
- [Аннотации](#Аннотации)
    - [Название](#Название)
        - [Для шагов](#Для-шагов)
        - [Для критериев](#Для-критериев)
        - [Для произвольного объекта](#Для-произвольного-объекта)
    - [Параметры](#Параметры)
        - [Предопределенные параметры шагов, возвращающих результат](#Предопределенные-параметры-шагов-возвращающих-результат)
        - [Предопределенные параметры шагов, выполняющих действия](#Предопределенные-параметры-шагов-выполняющих-действия)
        - [Другие параметры](#Другие-параметры)
    - [Выбрасываемые исключения](#Выбрасываемые-исключения)
    - [Аттачи](#Аттачи)
        - [Аттачи для шагов, которые возвращают результат](#Аттачи-для-шагов-которые-возвращают-результат)
        - [Аттачи для шагов, которые выполняют действие](#Аттачи-для-шагов-которые-выполняют-действие)
    - [Вложенность / глубина шагов](#Вложенность--глубина-шагов)

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

@CreateWith(provider = MyParameterProvider.class) //<-- Формирует параметры вызова конструктора
//Предполагается, что для этого используются значения различных переменных окружения
//среды, в которой выполняются тесты
//
//Если конструктор класса не имеет параметров, аннотацию можно не
//указывать
public class MyTestContext extends Context<MyTestContext> { //generic сигнатура для того,
    //чтобы предотвратить возможность шагам для других контекстов использовать
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
    public <T> T retrieve(SequentialGetStepSupplier< //Об этом классе и его наследниках речь пойдет ниже
            MyTestContext, //контекст как входной параметр
            T, // тип возвращаемого результата
            ?,
            ?,
            ?> getStepSupplier) {
        //Шаг, возвращающий результат, выполняется так.
        return get(getStepSupplier); //Данный метод имеет модификаторы protected и final, и предназначен только для 
        //перекрытия внутри методов, как в этом примере
    }

    // Простой пример того, как связать шаги, выполняющие действие, с данным контекстом,
    // и ограничить множество таких шагов только теми, которые могут выть выполнены в рамках данного контекста.
    public MyTestContext execute(SequentialActionSupplier< //Об этом классе и его наследниках речь пойдет ниже
            MyTestContext, //контекст как входной параметр
            ?,
            ?> sequentialActionSupplier) {
        //Шаг-действие выполняется так
        return perform(sequentialActionSupplier); //Данный метод имеет модификаторы protected и final, и предназначен только для 
        //перекрытия внутри методов, как в этом примере
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
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Шаги

### Произвольные шаги, которые могут быть описаны в тесте

Данный механизм можно использовать, чтобы сгруппировать в коде/логе/репорте
множество простых действий или шагов в один произвольный шаг верхнего уровня.

```java
package org.my.pack;

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

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Шаги, которые возвращают результат

Для описания набора действий с возвратом некоторого результата, в рамках некоторого контекста, используется 
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
// построенного шага не было получено значение, соответствующее указанным критериям, и за указанное время.
//Указывается класс исключения, у которого есть публичный конструктор с параметром типа
//String (текст сообщения). В данном примере может быть выброшено IllegalStateException,
//с текстом Can't get: <описание получаемого значения и параметров шага>
//
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
        var result = myTestContext().retrieve(something(1, 2, 3)
                //Можно указать критерий результата, который надо получить
                .criteria("Some criteria", o -> )
                //если надо
                .criteria("One more criteria", o -> ) //и еще один
                //можно указать время, за которое нужно получить требуемый результат
                .timeOut(ofSeconds(10))
                //паузу между попытками получить требуемые данные, в рамках указанного 
                //времени ожидания
                .pollingInterval(ofMillis(500))
                //Может быть так, что в процессе выполнения шага (при какой-то из попыток получить результат),
                //возникает исключение, потому что в данный момент нужное значение не вычислимо, т.к. 
                //тестируемая система работает не синхронно с нашим тестом, и в данный (какой-то) момент времени
                //нет условий для получения нужного результата. Чтобы избежать ложных срабатываний теста, можно
                //указать исключения, которые следует игнорировать. Можно указать конкретные классы, или 
                //классы-родители выбрасываемых исключений
                .addIgnored(RuntimeException.class)
                .throwOnNoResult()); //Если ничего походящего/значимого построенный шаг не вернет, 
        // и в этом случая нужно выбросить исключение и уронить тест. 
        // Будет выброшено исключения, указанное аннотацией
        //ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData над классом из 
        //примера
    }
}
```

Данный пример демонстрирует принцип работы. В большинстве случаев рекомендуется воспользоваться одним из 
классов — наследников `ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier` согласно одной из часто 
встречающихся ситуаций.

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

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
    //данных у текущего шага, и возвращает значение того же типа, что и значение-"посредник"
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
package org.my.pack;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.my.pack.MyGetStepSupplier.something;
import static org.my.pack.MyGetSomeOtherValue.someOtherValue;
import static org.my.pack.MyTestContext.myTestContext;

public class SomeTest {

    @Test
    public void test() {
        var result = myTestContext().retrieve(something(1, 2, 3)
                .criteria("Some criteria", o -> )
                .criteria("One more criteria", o -> )
                .timeOut(ofSeconds(10))
                .pollingInterval(ofMillis(500))
                .from(someOtherValue()) // <--!!!!!!!!!!!!!
                .throwOnNoResult());
    }
}
```
</details>

Дальнейшие примеры работают по тем же принципам, что и примеры выше. Поэтому в примерах ниже больше 
фокус на принципиальных отличиях.

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

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

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

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

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

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

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

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
public class MyGetStepSupplier<T, M> extends SequentialGetStepSupplier.GetArrayChainedStepSupplier<
        MyTestContext,
        M,
        T,
        MyGetStepSupplier<T, M>>
{

    protected MyGetStepSupplier() {
        this(myTestContext -> {
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

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Шаги, которые выполняют действие

Для описания набора действий без возврата результата, в рамках некоторого контекста, используется
наследники `ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier`. Ниже пример, демонстрирующий
работу и принцип данного класса.

<details>
  <summary>Нажми, чтобы прочесть</summary>


```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.Action;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import java.util.function.Function;

// Данный класс строит шаг, который выполняет некоторое действие без возврата конечного результата.
// Входным значением является объект-контекст, содержащий нужные ресурсы для выполнения.
// С его помощью получается некоторое значение, которое служит объектом для выполнения действия
@Description("Something with parameters {a}, {b}, {c}") //Заголовок/название шага, об этом будет ниже
public class MyActionStepSupplier<T> extends SequentialActionSupplier<
        MyTestContext, //Тип входного параметра для результирующей функции-шага.
        //Как правило это контекст, в рамках которого данное действие может быть выполнено
        T, //Тип результата, который должна возвращать функция-шаг
        MyActionStepSupplier<T>> //здесь желательного указывать тип самого класса или расширяющий
        //тип. Наследники SequentialActionSupplier работают как Builder, и многие методы
        //возвращают ссылку на объект, от которого ини были вызваны
{

    @DescriptionFragment("a") //параметры, которые формируют
    private final Object a;
    //название шага и используются для выполнения действия
    @DescriptionFragment("b")
    private final Object b;
    //Об этом будет ниже
    @DescriptionFragment("c")
    private final Object c;

    public MyActionStepSupplier(Object a, Object b, Object c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    //возвращает представление/построитель шага
    public static <T> MyActionStepSupplier<T> someAction(Object a, Object b, Object c) {
        return new MyActionStepSupplier<>(a, b, c);
    }

    //C помощью метода `performOn` можно указать шаг, у которого на выходе получается значение,
    //которое является объектом для выполнения действия.
    //У этого шага тип входного параметра тот же, что и у текущего.
    //
    // Оба шага образуют иерархическую последовательность выполнения
    //действий, т.е. в рамках данного шага будет выполнен тот, что был указан при
    //вызове метода.
    //
    //Не обязательно данный метод перекрывать и делать его публичным. Данным методом
    //можно воспользоваться при реализации внутренней логики класса, например в конструкторе
    //или статическом методе выше.
    public MyActionStepSupplier<T> on(SequentialGetStepSupplier<MyTestContext, T, ?, ?, ?> getStep) {
        return super.performOn(getStep);
    }

    //Аналогично методу выше. Только вместо подготовленного шага используем
    //некую функцию, которая принимает на вход значение того же типа, что и тип входных
    //данных у текущего шага, и возвращает значение того же типа, что и тип значения-объекта 
    //реализуемого действия
    public MyActionStepSupplier<T> on(Function<MyTestContext, T> f) {
        return super.performOn(f);
    }

    //Аналогично методам выше. В данном случае значение того же типа, что и значение-объект
    //реализуемого действия, используется напрямую.
    public MyActionStepSupplier<T> on(T t) {
        return super.performOn(t);
    }

    // здесь можно описать что-то, что выполнятся до того, как действие шага начинается.
    // По умолчанию данный метод пустой, но не абстрактный, т.к. в большинстве случаев
    // никаких дополнительных действий выполнять не нужно.
    @Override
    protected void onStart(MyTestContext myTestContext) {
        //Какая-то логика
    }

    // здесь можно описать действия, выполняемые после того, как было выброшено
    // исключение.
    // По умолчанию данный метод пустой, но не абстрактный, т.к. в большинстве случаев
    // никаких дополнительных действий выполнять не нужно.
    @Override
    protected void onFailure(MyTestContext myTestContext, Throwable throwable) {
        //Какая-то логика
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
}
```

</details>

Пример связи контекста и шага в тесте

```java
package org.my.pack;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.my.pack.MyActionStepSupplier.someAction;
import static org.my.pack.MyGetStepSupplier.something;
import static org.my.pack.MyTestContext.myTestContext;

public class SomeTest {

    @Test
    public void test() {
        var result = myTestContext().execute(someAction(1, 2, 3) //указываем действие 
                //ниже указываем то, как найти-получить объект для его выполнения
                .on(something(1, 2, 3)
                        //Можно указать критерий результата, который надо получить
                        .criteria("Some criteria", o ->)
                        //если надо
                        .criteria("One more criteria", o ->) //и еще один
                        //можно указать время, за которое нужно получить требуемый результат
                        .timeOut(ofSeconds(10))
                        //паузу между попытками получить требуемые данные, в рамках указанного 
                        .pollingInterval(ofMillis(500))
                        //времени ожидания
                        .throwOnNoResult())); //Если ничего походящего/значимого шаг не вернул, и тут нужно выбросить
        //исключение и уронить тест. 


    }
}
```
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Критерии

Критерии нужны для того, чтобы уточнить значение, которое должен вернуть [шаг, возвращающий результат](#Шаги,-которые-возвращают-результат). 
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
                //Объект ru.tinkoff.qa.neptune.core.api.steps.Criteria создается внутри вызванного метода.
                .criteria("Some criteria", o -> {/* предикат, т.е выражение, возвращающее boolean */})
                //Критерий, записанный в свободной форме, в виде пояснительной строки и предиката.
                //Все это явно передано в виде объекта ru.tinkoff.qa.neptune.core.api.steps.Criteria,
                //созданного статическим методом ru.tinkoff.qa.neptune.core.api.steps.Criteria#condition
                .criteria(condition("One more criteria", o -> {/* предикат, т.е выражение, возвращающее boolean */})));
    }
}
```

Критерии можно указывать в свободной форме, в виде пояснительной строки и предиката.

Ниже пример того, что можно сделать, если один и тот же критерий используется многократно.

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

public final class MyCriteriaLib {

    private MyCriteriaLib() {
        super();
    }

    @Description("Some criteria with parameters: {param1}, {param2}") //Заголовок/название критерия, об этом будет ниже
    public static <T> Criteria<T> someCriteria(@DescriptionFragment("param1") Object param1, //параметры, которые формируют
                                               @DescriptionFragment("param2") Object param2) { //название шага. Об этом будет ниже
        return condition(t -> /* предикат, т.е выражение, возвращающее boolean */);
    }

    @Description("One more criteria with parameters: {param1}, {param2}")
    public static <T> Criteria<T> oneMoreCriteria(@DescriptionFragment("param1") Object param1,
                                                  @DescriptionFragment("param2") Object param2) {
        return condition(t -> /* предикат, т.е выражение, возвращающее boolean */);
    }
}

```

и тогда

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

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

#### Объединение критериев в один логическим И(AND) 

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
                .criteria(someCriteria("A", "B")) //В данном примере все указанные критерии объединяются 
                .criteria(oneMoreCriteria(4, 5))); // в одно И-выражение.
    }
}
```

Но если надо явно объединить несколько критериев в один, тогда

```java
package org.my.pack;

import static org.my.pack.MyCriteriaLib.oneMoreCriteria;
import static org.my.pack.MyCriteriaLib.someCriteria;
import static org.my.pack.MyGetStepSupplier.something;
import static org.my.pack.MyTestContext.myTestContext;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.AND;

public class SomeTest {

    @Test
    public void test() {
        var result = myTestContext().retrieve(something(1, 2, 3)
                .criteria(AND(someCriteria("A", "B"), oneMoreCriteria(4, 5))));
    }
}
```
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

#### Объединение критериев в один логическим ИЛИ (OR)

```java
package org.my.pack;

import static org.my.pack.MyCriteriaLib.oneMoreCriteria;
import static org.my.pack.MyCriteriaLib.someCriteria;
import static org.my.pack.MyGetStepSupplier.something;
import static org.my.pack.MyTestContext.myTestContext;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.OR;

public class SomeTest {

    @Test
    public void test() {
        var result = myTestContext().retrieve(something(1, 2, 3)
                .criteria(OR(someCriteria("A", "B"), oneMoreCriteria(4, 5))));
    }
}
```
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

#### Объединение критериев в один исключающим ИЛИ (XOR)

```java
package org.my.pack;

import static org.my.pack.MyCriteriaLib.oneMoreCriteria;
import static org.my.pack.MyCriteriaLib.someCriteria;
import static org.my.pack.MyGetStepSupplier.something;
import static org.my.pack.MyTestContext.myTestContext;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.XOR;

public class SomeTest {

    @Test
    public void test() {
        var result = myTestContext().retrieve(something(1, 2, 3)
                .criteria(XOR(someCriteria("A", "B"), oneMoreCriteria(4, 5))));
    }
}
```
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

#### Инверсия критериев (NOT)

```java
package org.my.pack;

import static org.my.pack.MyCriteriaLib.oneMoreCriteria;
import static org.my.pack.MyCriteriaLib.someCriteria;
import static org.my.pack.MyGetStepSupplier.something;
import static org.my.pack.MyTestContext.myTestContext;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.NOT;

public class SomeTest {

    @Test
    public void test() {
        var result = myTestContext().retrieve(something(1, 2, 3)
                .criteria(someCriteria("A", "B"))
                .criteria(NOT(oneMoreCriteria(4, 5)))); //<-- критерий инвертирован
    }
}
```

```java
package org.my.pack;

import static org.my.pack.MyCriteriaLib.oneMoreCriteria;
import static org.my.pack.MyCriteriaLib.someCriteria;
import static org.my.pack.MyGetStepSupplier.something;
import static org.my.pack.MyTestContext.myTestContext;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.NOT;

public class SomeTest {

    @Test
    public void test() {
        var result = myTestContext().retrieve(something(1, 2, 3)
                .criteria(NOT(someCriteria("A", "B"), oneMoreCriteria(4, 5))));
        //И-выражение, в котором каждый из критериев инвертирован
    }
}
```
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Присутствие и отсутствие

[Контекст](#Контекст) может работать с шагами, определяющими присутствие или отсутствие чего-либо. Это актуально для контекстов, 
которые описывают такие вещи, как UI приложения, где элементы могут как появляться так и исчезать, в зависимости от пользовательских 
действий. Или дата-стораджи в связке с back-end приложения. И цель выполнения таких шагов — убедиться, что что-то либо 
присутствует/появилось, либо отсутствует/пропало (получить true или false).

Методы, которые работают с такими функциями-шагам, имеют модификаторы `protected` и `final`. Они актуальны не для 
каждого контекста. В тех контекстах, где нужна такая возможность, эти методы можно переиспользовать `public`- методами, 
в теле которых выполняются проверки на наличие/отсутствие.

#### Присутствие/появление

<details>
  <summary>Нажми, чтобы прочесть</summary>

Доработаем контекст

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;

@CreateWith(provider = MyParameterProvider.class)
public class MyTestContext extends Context<MyTestContext> {

    public static MyTestContext myTestContext() {
        return getInstance(MyTestContext.class);
    }

    //Конструктор может содержать параметры
    protected MyTestContext(/*Параметры*/) {
        //Логика инициализации
    }

    public <T> T retrieve(SequentialGetStepSupplier<MyTestContext, T, ?, ?, ?> getStepSupplier) {
        return get(getStepSupplier);
    }

    public MyTestContext execute(SequentialActionSupplier<MyTestContext, ?, ?> sequentialActionSupplier) {
        return perform(sequentialActionSupplier);
    }

    public Object getSomeResource() {
        return //Возврат какого-нибудь проинициализированного объекта
        //для использования в шагах (рекомендуется)
        //для использования напрямую (не рекомендуется)
    }

    // Проверяет на присутствие/появление некоторого значения, описываемого шагом, возвращающим результат
    // Вернет true, если шаг за отведенное время вернет значение, соответствующее переданным критериям.
    // Если переданный шаг вернет null/пустые коллекцию или массив - результат будет false
    //
    // В метод передается список классов исключений, которые могут возникать в ходе выполнения указанного шага,
    // и которые должны быть проигнорированы.
    @SafeVarargs
    public final boolean presence(SequentialGetStepSupplier<MyTestContext, ?, ?, ?, ?> getStepSupplier,
                                  Class<? extends Throwable>... toIgnore) {
        return super.presenceOf(getStepSupplier, toIgnore); //Данный метод имеет модификаторы protected и final,
        // и предназначен только для 
        //перекрытия внутри методов, как в этом примере
    }

    // Проверяет на присутствие/появление некоторого значения, описываемого шагом, возвращающим результат.
    // Вернет true, если переданный шаг за отведенное время вернет значение, соответствующее указанным критериям.
    // Если переданный шаг вернет null/пустые коллекцию или массив - будет выброшено исключение.
    //
    // В метод передается список классов исключений, которые могут возникать в ходе выполнения указанного шага,
    // и которые должны быть проигнорированы.
    @SafeVarargs
    public final boolean presenceOrThrow(SequentialGetStepSupplier<MyTestContext, ?, ?, ?, ?> getStepSupplier,
                                         Class<? extends Throwable>... toIgnore) {
        return super.presenceOfOrThrow(getStepSupplier, toIgnore); //Данный метод имеет модификаторы protected и final, 
        // и предназначен только для 
        //перекрытия внутри методов, как в этом примере
    }
}
```

Пример использования

```java
package org.my.pack;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.my.pack.MyCriteriaLib.oneMoreCriteria;
import static org.my.pack.MyCriteriaLib.someCriteria;
import static org.my.pack.MyGetStepSupplier.something;
import static org.my.pack.MyTestContext.myTestContext;

public class SomeTest {

    @Test
    public void test() {

        //должно вернется true или false
        boolean result = myTestContext().presence(something(1, 2, 3)
                .criteria(someCriteria("A", "B")) //теперь это критерии
                .criteria(oneMoreCriteria(4, 5)) //того, что должно присутствовать/появиться
                .timeOut(ofSeconds(10)) //теперь это время ожидания появления
                .pollingInterval(ofMillis(500)));

        //должно вернется true или выброситься исключение
        boolean result2 = myTestContext().presenceOrThrow(something(1, 2, 3)
                .criteria(someCriteria("A", "B")) //теперь это критерии
                .criteria(oneMoreCriteria(4, 5)) //того, что должно присутствовать/появиться
                .timeOut(ofSeconds(10)) //теперь это время ожидания появления
                .pollingInterval(ofMillis(500)));
    }
}
```
</details>

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

#### Отсутствие/исчезновение

<details>
  <summary>Нажми, чтобы прочесть</summary>

Доработаем контекст

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;

import java.time.Duration;

@CreateWith(provider = MyParameterProvider.class)
public class MyTestContext extends Context<MyTestContext> {

    public static MyTestContext myTestContext() {
        return getInstance(MyTestContext.class);
    }

    //Конструктор может содержать параметры
    protected MyTestContext(/*Параметры*/) {
        //Логика инициализации
    }

    public <T> T retrieve(SequentialGetStepSupplier<MyTestContext, T, ?, ?, ?> getStepSupplier) {
        return get(getStepSupplier);
    }

    public MyTestContext execute(SequentialActionSupplier<MyTestContext, ?, ?> sequentialActionSupplier) {
        return perform(sequentialActionSupplier);
    }

    public Object getSomeResource() {
        return //Возврат какого-нибудь проинициализированного объекта
        //для использования в шагах (рекомендуется)
        //для использования напрямую (не рекомендуется)
    }

    // Проверяет на отсутствие/исчезновение чего-либо, описываемого шагом, возвращающим результат.
    // Вернет true, если шаг за отведенное время перестанет возвращать значение, соответствующее переданным критериям,
    // (т.е вернет null/пустые коллекцию или массив). В противном случае - результат будет false.
    //
    // В метод передается время ожидания того, что значение, описанное объектом класса SequentialGetStepSupplier,
    // перестанет возвращаться. Если у переданного объекта SequentialGetStepSupplier был вызван метод `.timeOut(Duration)` -
    // это время игнорируется.
    //
    public boolean absence(SequentialGetStepSupplier<MyTestContext, ?, ?, ?, ?> getStepSupplier, //описание того, что должно
                           //отсутствовать или пропасть
                           Duration timeOut) { //время на то, чтобы что-то исчезло
        return super.absenceOf(getStepSupplier, timeOut); //Данный метод имеет модификаторы protected и final,
        // и предназначен только для
        //перекрытия внутри методов, как в этом примере
    }

    // Проверяет на отсутствие/исчезновение чего-либо, описываемого шагом, возвращающим результат.
    // Вернет true, если шаг за отведенное время перестанет возвращать значение, соответствующее переданным критериям,
    // (т.е вернет null/пустые коллекцию или массив). В противном случае - будет выброшено исключение.
    //
    // В метод передается время ожидания того, что значение, описанное объектом класса SequentialGetStepSupplier,
    // перестанет возвращаться. Если у переданного объекта SequentialGetStepSupplier был вызван метод `.timeOut(Duration)` -
    // это время игнорируется.
    public boolean absenceOrThrow(SequentialGetStepSupplier<MyTestContext, ?, ?, ?, ?> getStepSupplier, //описание того, что должно
                                  //отсутствовать или пропасть
                                  Duration timeOut) { //время на то, чтобы что-то исчезло
        return super.absenceOfOrThrow(getStepSupplier, timeOut); //Данный метод имеет модификаторы protected и final,
        // и предназначен только для
        //перекрытия внутри методов, как в этом примере
    }
}
```

Пример использования

```java
package org.my.pack;

import static java.time.Duration.ofSeconds;
import static org.my.pack.MyCriteriaLib.oneMoreCriteria;
import static org.my.pack.MyCriteriaLib.someCriteria;
import static org.my.pack.MyGetStepSupplier4.something;
import static org.my.pack.MyTestContext.myTestContext;

public class SomeTest {

    @Test
    public void test() {

        //должно вернется true или false
        boolean result = myTestContext().absence(something(1, 2, 3)
                        .criteria(someCriteria("A", "B")) //теперь это критерии
                        .criteria(oneMoreCriteria(4, 5)), //того, что должно отсутствовать/исчезнуть
                        //.timeOut(ofSeconds(10)) //<-- Игнорируется
                ofSeconds(10)); //время на то, чтобы описанное значение пропало

        //должно вернется true или выброситься исключение
        boolean result2 = myTestContext().absence(something(1, 2, 3)
                        .criteria(someCriteria("A", "B")) //теперь это критерии
                        .criteria(oneMoreCriteria(4, 5)), //того, что должно отсутствовать/исчезнуть
                        //.timeOut(ofSeconds(10)) //<-- Игнорируется
                ofSeconds(10)); //время на то, чтобы описанное значение пропало
    }
}
```
</details>

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)


## Аннотации

Ниже представлены аннотации, которые используются при описании шагов и критериев. Параметры, передаваемые этими 
аннотациями, наследуются от класса-родителя к классу-наследнику. Так же есть возможность перекрытия с помощью 
объявления какой-то из этих аннотаций в классе-наследнике.

### Название

Параметры, переданные через аннотации `ru.tinkoff.qa.neptune.core.api.steps.annotations.Description`, 
`ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier.DefineGetImperativeParameterName` и 
`ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier.DefinePerformImperativeParameterName` используются 
[локализацией](./LOCALIZATION.MD).

#### Для шагов 

Пример ниже актуален для классов — наследников `SequentialGetStepSupplier` и `SequentialActionSupplier`.

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("Something with parameters [{a}, {b}, {c}]") //Общее название действия, которое должно быть
//выполнено, или описание значения, которое должно быть получено. Это название/описание может быть как
// статическим, так и динамическим. В случае с динамическим названием/описанием, параметры заключаются в {}
@SequentialGetStepSupplier.DefineGetImperativeParameterName("Calculate:") // Дополнительный параметр, который как бы
// поясняет, что этот шаг будет делать ("Получить:", "Рассчитать:" и т.п.). Используется для наследников класса
// SequentialGetStepSupplier.
// Данный параметр необязателен, его можно не указывать. Если его не указать, то по умолчанию императив будет - "Get:".
@SequentialActionSupplier.DefinePerformImperativeParameterName("Do:") // Дополнительный параметр, который как бы
// поясняет, что этот шаг будет делать ("Сделать:", "Выполнить:" и т.п.). Используется для наследников класса
// SequentialActionSupplier.
// Данный параметр необязателен, его можно не указывать. Если его не указать, то по умолчанию императив будет - "Perform:".
public class MyStepClass /*extends a subclass of SequentialGetStepSupplier, or SequentialActionSupplier*/
{
    @DescriptionFragment("a") //значение, которое, помимо всего прочего,
    //формирует динамическое название действия/описание значения. Название фрагмента
    //должно соответствовать маске параметра, заключенного в {} (см. аннотацию Description над
    // классом)
    private final Object a;

    @DescriptionFragment("b") //значение, которое, помимо всего прочего,
    //формирует динамическое название действия/описание значения. Название фрагмента
    //должно соответствовать маске параметра, заключенного в {} (см. аннотацию Description над
    // классом)
    private final Object b;

    //значение, которое, помимо всего прочего,
    @DescriptionFragment(value = "c", makeReadableBy = ExampleGetParameterValue.class)
    //формирует динамическое название действия/описание значения. Название фрагмента
    //должно соответствовать маске параметра, заключенного в {} (см. аннотацию Description над
    // классом). По умолчанию, используется значение, которое возвращает метод
    // toString() того объекта, который в данный момент является значением поля.
    //Но это значение не всегда бывает удобным для чтения. Для таких случаев можно
    //использовать классы, реализующие интерфейс
    // ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter,
    // цель которых - представить объект в читаемом формате.
    private final Object c;

    public MyStepClass(Object a, Object b, Object c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    //Шаг, который получится из вернувшегося объекта, в момент выполнения будет иметь заголовок:
    //`Calculate: Something with parameters [1, 2, 3]` или `Do: Something with parameters [1, 2, 3]`
    // (в зависимости от класса-предка).
    //Т.е. используются данные, объявленные над классом и полями
    public static MyStepClass myStep() {
        return new MyStepClass(1, 2, 3);
    }

    //Аналогично примеру выше. используются данные, объявленные над классом и полями
    public static MyStepClass myStep(String param) {
        return new MyStepClass("A", "B", param);
    }

    //Ниже пример того, как можно перекрыть общее название действия, которое должно быть
    //выполнено, или описание значения, которое должно быть получено.
    //Переопределенное название/описание может быть как
    //статическим, так и динамическим. В случае с динамическим названием/описанием,
    // параметры заключаются в {}.
    //
    // Шаг, который получится из вернувшегося объекта, в момент выполнения будет иметь заголовок:
    //`Calculate: Something else with parameters [<...>]` или `Do: Something else with parameters [<...>]`
    // (в зависимости от класса-предка).
    //Т.е. используются данные, объявленные над классом (императивы) и над методом.
    @Description("Something else with parameters [{b}, {c}]")
    public static MyStepClass myStep(
            @DescriptionFragment( //значение, которое, помимо всего прочего,
                    value = "b", //формирует динамическое название действия/описание значения. Название фрагмента
                    //должно соответствовать маске параметра, заключенного в {} (см. аннотацию Description над
                    // методом).
                    makeReadableBy = ExampleGetParameterValue.class) boolean param, //По умолчанию, используется значение,
            // которое возвращает метод toString() того объекта, который в данный момент
            // является значением параметра вызова метода.
            //Но это значение не всегда бывает удобным для чтения. Для таких случаев можно
            //использовать классы, реализующие интерфейс
            // ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter,
            // цель которых - представить объект в читаемом формате.
            @DescriptionFragment("c") String param2) //значение, которое, помимо всего прочего,
    //формирует динамическое название действия/описание значения. Название фрагмента
    //должно соответствовать маске параметра, заключенного в {} (см. аннотацию Description над
    // методом)
    {
        return new MyStepClass(1, param, param2);
    }
}
```

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

public class ExampleGetParameterValue 
        implements ParameterValueGetter<Object> //Данный интерфейс имеет тип-параметр.
    //Реализация должна иметь тот же тип-парамер (либо родительский тип), 
    // что и тип поля класса / параметра сигнатуры метода,
    //для которых данная реализация используется.
{
    @Override
    public String getParameterValue(Object fieldValue) {
        return /*Преобразование объекта в читаемую строку*/;
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

#### Для критериев

В данном примере описания критериев, которые подготовлены для многократного повторного использования.

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

public final class MyCriteriaLib {

    private MyCriteriaLib() {
        super();
    }

    //Название критерия может быть статическим или динамическим.
    //В случае с динамическим названием/описанием, параметры заключаются в {}
    @Description("Some criteria with parameters: {param1}, {param2}")
    public static <T> Criteria<T> someCriteria(
            @DescriptionFragment( //значение, которое, помимо всего прочего,
                    value = "param1", //формирует динамическое название критерия. Название фрагмента
                    //должно соответствовать маске параметра, заключенного в {} (см. аннотацию Description над
                    // методом).
                    makeReadableBy = ExampleGetParameterValue.class) Object param1, //По умолчанию, используется значение,
            // которое возвращает метод toString() того объекта, который в данный момент
            // является значением параметра вызова метода.
            //Но это значение не всегда бывает удобным для чтения. Для таких случаев можно
            //использовать классы, реализующие интерфейс
            // ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter,
            // цель которых - представить объект в читаемом формате.
            @DescriptionFragment("param2") Object param2) //значение, которое, помимо всего прочего,
    //формирует динамическое название критерия. Название фрагмента
    //должно соответствовать маске параметра, заключенного в {} (см. аннотацию Description над
    // методом
    {
        return condition(t -> /* предикат, т.е выражение, возвращающее boolean */);
    }

    @Description("One more criteria with parameters: {param1}, {param2}")
    public static <T> Criteria<T> oneMoreCriteria(@DescriptionFragment("param1") Object param1,
                                                  @DescriptionFragment("param2") Object param2) {
        return condition(t -> /* предикат, т.е выражение, возвращающее boolean */);
    }
}
```

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

public class ExampleGetParameterValue 
        implements ParameterValueGetter<Object> //Данный интерфейс имеет тип-параметр.
        //Реализация должна иметь тот же тип-парамер (либо родительский тип), 
        // что и тип поля класса / параметра сигнатуры метода,
        //для которых данная реализация используется.
{
    @Override
    public String getParameterValue(Object fieldValue) {
        return /*Преобразование объекта в читаемую строку*/;
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

#### Для произвольного объекта

Данный механизм можно использовать, если требуется перевод/локализация объектов класса, который находится на поддержке
и не входит в описанные выше примеры.

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;

@Description("Статическое или динамическое строковое представление объекта")
public class MyCustomObject {

    public String toString() {
        //Вызов метода, предназначенного для перевода/локализации
        return translate(this);
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Параметры

Параметры, переданные через аннотации `ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier.DefineTimeOutParameterName`,
`ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier.DefinePollingTimeParameterName`, 
`ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier.DefineCriteriaParameterName`, 
`ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier.DefineFromParameterName`, 
`ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier.DefineResultDescriptionParameterName`, 
`ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier.DefinePerformOnParameterName` и 
`ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter` используются [локализацией](./LOCALIZATION.MD).

#### Предопределенные параметры шагов, возвращающих результат

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;

//Аннотации, приведенные в данном примере, добавляют пояснительную информацию к шагу,
//который строится объектом данного класса. Эта информация будет выведена на консоль/в отчет о тестировании,
// когда шаг начнет свое выполнение.
//Данные параметры - это стандартный набор параметров, которыми может обладать
//любой шаг, построенный объектом класса, который расширяет `SequentialGetStepSupplier`.
@SequentialGetStepSupplier.DefineTimeOutParameterName("Timeout") //Название параметра, кратко именующее время ожидания нужного 
// результата
@SequentialGetStepSupplier.DefinePollingTimeParameterName("Sleep time") //Название параметра, кратко именующее паузу между попытками
//получить нужный результат
@SequentialGetStepSupplier.DefineCriteriaParameterName("Result criteria") //Название параметра, кратко именующее критерий нужного
//результата
@SequentialGetStepSupplier.DefineFromParameterName("From") //Название параметра, кратко именующее значение (вычисленное от контекста
// прямо или опосредованно), от которого вычисляется результат шага, который строится данным классом. Данная аннотация актуальна
// для классов-наследников:
// - GetObjectChainedStepSupplier
// - GetObjectFromIterableChainedStepSupplier
// - GetObjectFromArrayChainedStepSupplier
// - GetIterableChainedStepSupplier
// - GetArrayChainedStepSupplier
@SequentialGetStepSupplier.DefineResultDescriptionParameterName("Resulted value") //Пояснение к результату, который должен
// возвращать шаг, построенный объектом данного класса. Эта информация выводится на консоль/в отчет о тестировании,
// когда шаг завершается.
@IncludeParamsOfInnerGetterStep //Данная аннотация актуальна для классов-наследников:
// - GetObjectChainedStepSupplier
// - GetObjectFromIterableChainedStepSupplier
// - GetObjectFromArrayChainedStepSupplier
// - GetIterableChainedStepSupplier
// - GetArrayChainedStepSupplier
//Присутствие данной аннотации означает, что если результат вычисляется через контекст опосредованно,
//и способ вычисления значения-посредника передан через объект класса-наследника SequentialGetStepSupplier,
//то параметры переданного объекта будут включены в параметры шага, который построил объект данного класса.
public class MyGetStepSupplier<T, M, P>
        extends SequentialGetStepSupplier<MyTestContext, T, M, P, MyGetStepSupplier<T, M, P>> //Такое наследование просто,
        //чтобы схематично описать общий случай. Актуальные варианты ниже:
        //extends SequentialGetStepSupplier.GetObjectStepSupplier<>
        //extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<>
        //extends SequentialGetStepSupplier.GetObjectFromIterableStepSupplier<>
        //extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<>
        //extends SequentialGetStepSupplier.GetObjectFromArrayStepSupplier<>
        //extends SequentialGetStepSupplier.GetObjectFromArrayChainedStepSupplier<>
        //extends SequentialGetStepSupplier.GetIterableStepSupplier<>
        //extends SequentialGetStepSupplier.GetIterableChainedStepSupplier<>
        //extends SequentialGetStepSupplier.GetArrayStepSupplier<>
        //extends SequentialGetStepSupplier.GetArrayChainedStepSupplier<>
{

    @Override //в момент выполнения к шагу добавится параметр с названием `Timeout`
    public MyGetStepSupplier<T, M, P> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override //в момент выполнения к шагу добавится параметр с названием `Sleep time`
    public MyGetStepSupplier<T, M, P> pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    @Override //в момент выполнения к шагу добавится параметр с названием `Result criteria`.
    //Если параметр с таким названием уже есть - добавится `Result criteria 2`, `Result criteria 3`
    // и т.д.
    public MyGetStepSupplier<T, M, P> criteria(Criteria<? super P> criteria) {
        return super.criteria(criteria);
    }

    @Override //в момент выполнения к шагу добавится параметр с названием `Result criteria`.
    //Если параметр с таким названием уже есть - добавится `Result criteria 2`, `Result criteria 3`
    // и т.д.
    public MyGetStepSupplier<T, M, P> criteria(String description, Predicate<? super P> predicate) {
        return super.criteria(description, predicate);
    }

    @Override //в момент выполнения к шагу добавится параметр с названием `From`. Т.к.
    //над классом стоит аннотация IncludeParamsOfInnerGetterStep, то в параметры построенного шага
    //будут включены параметры переданного через данный метод объекта.
    protected GetObjectChainedStepSupplier from(SequentialGetStepSupplier<MyTestContext, ? extends M, ?, ?, ?> from) {
        return super.from(from);
    }

    @Override //в момент выполнения к шагу добавится параметр с названием `From`
    // при условии, что `toString()` класса переданной функции перекрыт и возвращает читабельный
    // текст
    protected GetObjectChainedStepSupplier from(Function<MyTestContext, ? extends M> from) {
        return super.from(from);
    }

    @Override //в момент выполнения к шагу добавится параметр с названием `From`
    // при условии, что `toString()` класса переданного объекта перекрыт и возвращает читабельный
    // текст
    protected GetObjectChainedStepSupplier from(M from) {
        return super.from(from);
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

#### Предопределенные параметры шагов, выполняющих действия

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;

import java.util.function.Function;

@SequentialActionSupplier.DefinePerformOnParameterName("Perform on:") //Название параметра, кратко именующее значение (вычисленное от контекста
// прямо или опосредованно), которое является объектом для выполняемого действия
@IncludeParamsOfInnerGetterStep //Присутствие данной аннотации означает, что если
// способ вычисления значения, являющегося объектом для выполняемого действия,
// передан через объект класса-наследника SequentialGetStepSupplier,
// то параметры переданного объекта будут включены в параметры шага, который построил объект данного класса.
public class MyActionStepSupplier<T> extends SequentialActionSupplier<MyTestContext, T, MyActionStepSupplier<T>> {

    //в момент выполнения к шагу добавится параметр с названием `Perform on:`. Т.к.
    //над классом стоит аннотация IncludeParamsOfInnerGetterStep, то в параметры построенного шага
    //будут включены параметры переданного через данный метод объекта.
    public MyActionStepSupplier<T> on(SequentialGetStepSupplier<MyTestContext, T, ?, ?, ?> getStep) {
        return super.performOn(getStep);
    }

    //в момент выполнения к шагу добавится параметр с названием `Perform on:`
    // при условии, что `toString()` класса переданной функции перекрыт и возвращает читабельный
    // текст
    public MyActionStepSupplier<T> on(Function<MyTestContext, T> f) {
        return super.performOn(f);
    }

    //в момент выполнения к шагу добавится параметр с названием `Perform on:`
    // при условии, что `toString()` класса переданного объекта перекрыт и возвращает читабельный
    // текст
    public MyActionStepSupplier<T> on(T t) {
        return super.performOn(t);
    }


    @Override
    protected void howToPerform(T value) {
        //логика шага
    }
}
```
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

#### Другие параметры

Выше описано, как обозначить стандартный набор параметров для шагов определенных классов. 
Ниже описано, как обозначить набор кастомных параметров.

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;


import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

public class MyStepClass /*extends a subclass of SequentialGetStepSupplier, or SequentialActionSupplier*/
{

    @StepParameter("Parameter A") //Так обозначается имя параметра,
    // значение которого должно использоваться в процессе выполнения шага.
    // Параметр и его значение будут выведены в консоль или отчет о проведении теста.
    private Object a;

    @StepParameter(value = "Parameter B",
            doNotReportNullValues = true) //Если на момент выполнения построенного шага 
    //это значение не будет заполнено, тогда оно не будет выведено в консоль или отчет 
    // о проведении теста.
    private Object b;

    @StepParameter(value = "Parameter C",
            makeReadableBy = ExampleGetParameterValue.class) //По умолчанию, используется значение, 
    // которое возвращает метод toString() того объекта, который в данный момент является значением поля.
    //Но это значение не всегда бывает удобным для чтения. Для таких случаев можно
    //использовать классы, реализующие интерфейс
    // ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter,
    // цель которых - представить объект в читаемом формате.
    private Object c;

    //Иногда параметров может быть слишком много и не все они могут понадобиться, 
    // и может быть удобнее передавать их не по одному, а в виде POJO/бина. Тогда
    // можно реализовать класс объекта, пример которого будет приведен ниже.
    private ParameterPojo parameterPojo;

    public MyStepClass setA(Object a) {
        this.a = a;
        return this;
    }

    public MyStepClass setB(Object b) {
        this.b = b;
        return this;
    }

    public MyStepClass setC(Object c) {
        this.c = c;
        return this;
    }

    public MyStepClass setParameterPojo(ParameterPojo parameterPojo) {
        this.parameterPojo = parameterPojo;
        return this;
    }
}
```

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterWrapper;

public class ParameterPojo extends StepParameterWrapper { //класс объектов, цель которых - собирать 
    //все параметры определенного шага/набора шагов

    //все тоже самое, что в примере выше
    @StepParameter("Pojo Parameter 1")
    private Boolean param1;
    @StepParameter(value = "Pojo Parameter 2", doNotReportNullValues = true)
    private String param2;
    private Integer param3;

    public Boolean getParam1() {
        return param1;
    }

    public ParameterPojo setParam1(Boolean param1) {
        this.param1 = param1;
        return this;
    }

    public String getParam2() {
        return param2;
    }

    public ParameterPojo setParam2(String param2) {
        this.param2 = param2;
        return this;
    }
}
```

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

public class ExampleGetParameterValue 
        implements ParameterValueGetter<Object> //Данный интерфейс имеет тип-параметр.
        //Реализация должна иметь тот же тип-парамер (либо родительский тип), 
        // что и тип поля класса / параметра сигнатуры метода,
        //для которых данная реализация используется.
{
    @Override
    public String getParameterValue(Object fieldValue) {
        return /*Преобразование объекта в читаемую строку*/;
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Выбрасываемые исключения

Пример ниже актуален для классов — наследников `SequentialGetStepSupplier`. Параметр `startDescription` 
аннотации `ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData` используются [локализацией](./LOCALIZATION.MD).

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;

//Здесь описывается исключение, которое может быть выброшено, если в результате выполнения 
//построенного шага не было получено значение, соответствующее указанным критериям, и за указанное время.
//Указывается класс исключения, у которого есть публичный конструктор с параметром типа
//String (текст сообщения). В данном примере может быть выброшено IllegalStateException,
//с текстом Can't get: <описание получаемого значения и параметров шага>
//
//Аннотацию можно не указывать, т.к. по умолчанию может быть выброшено
// ru.tinkoff.qa.neptune.core.api.steps.NotPresentException с текстом "Not present: <текст>"
//
// Исключение выбрасывается, если у объекта данного данного класса был вызван метод
// `throwOnNoResult()` и построенный шаг вернул null (а так же пустые коллекцию/массив, в зависимости
// от того, от какого класса происходит наследование)
@ThrowWhenNoData(toThrow = IllegalStateException.class,
        startDescription = "Can't get:")
public class MyGetStepSupplier<T, M, P>
        extends SequentialGetStepSupplier<MyTestContext, T, M, P, MyGetStepSupplier<T, M, P>> //Такое наследование просто,
        //чтобы схематично описать общий случай. Актуальные варианты ниже:
        //extends SequentialGetStepSupplier.GetObjectStepSupplier<>
        //extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<>
        //extends SequentialGetStepSupplier.GetObjectFromIterableStepSupplier<>
        //extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<>
        //extends SequentialGetStepSupplier.GetObjectFromArrayStepSupplier<>
        //extends SequentialGetStepSupplier.GetObjectFromArrayChainedStepSupplier<>
        //extends SequentialGetStepSupplier.GetIterableStepSupplier<>
        //extends SequentialGetStepSupplier.GetIterableChainedStepSupplier<>
        //extends SequentialGetStepSupplier.GetArrayStepSupplier<>
        //extends SequentialGetStepSupplier.GetArrayChainedStepSupplier<>
{
    //реализация поведения
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

### Аттачи

Подробнее о механизме создания приложений к отчетам о проведении тестов [здесь](./EVENTS.MD#Аттачи). Ниже примеры того,
как он используется при описании построителей шагов.

Предположим, у нас есть следующие классы, объекты которых могут создавать приложения к отчетам. Примеры описаны схематично.

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

//Предположим, что объект-контекст может содержать данные,
// которые можно использовать как текстовое приложение к отчету о тесте
public class ExampleCaptor extends Captor<MyTestContext, String> {

    @Override
    public String getData(MyTestContext caught) {
        return //возврат какой-то строки
    }

    @Override
    public MyTestContext getCaptured(Object toBeCaptured) {
        return //убеждаемся, что объект типа MyTestContext / приводим объект к этому типу
        // или создаем объект этого типа
    }
}
```

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

public class ExampleCaptor2 extends Captor<T, String> {

    @Override
    public String getData(T caught) {
        return //возврат какой-то строки
    }

    @Override
    public T getCaptured(Object toBeCaptured) {
        return //убеждаемся, что объект типа T / приводим объект к этому типу
        // или создаем объект этого типа
    }
}
```

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

public class ExampleCaptor3 extends Captor<M, String> {

    @Override
    public String getData(M caught) {
        return //возврат какой-то строки
    }

    @Override
    public M getCaptured(Object toBeCaptured) {
        return //убеждаемся, что объект типа M / приводим объект к этому типу
        // или создаем объект этого типа
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

#### Аттачи для шагов, которые возвращают результат

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;

@CaptureOnSuccess(by = ExampleCaptor2.class /**Можно указать несколько классов через запятую. Указанные классы
не должны иметь объявленных конструкторов, или среди объявленных конструкторов должен быть тот, что не содержит параметров.
Может быть указан абстрактный класс. В этом случае неявно будут работать объекты его классов-наследников*/) //Здесь
// указывается список классов, объекты которых могут обработать результат построенного шага в момент завершения
// его выполнения. Объекты перечисленных классов создают аттачи, которые прикладываются к отчету о выполнении тестов
@CaptureOnFailure(by = ExampleCaptor3.class /**Можно указать несколько классов через запятую. Указанные классы
не должны иметь объявленных конструкторов, или среди объявленных конструкторов должен быть тот, что не содержит параметров.
Может быть указан абстрактный класс. В этом случае неявно будут работать объекты его классов-наследников*/) //Здесь
// указывается список классов, объекты которых могут обработать значение (контекст или значение-посредник,
// в зависимости от того, от какого класса был унаследован данный класс), с помощью которого построенный шаг
// должен был получить результат. Объекты перечисленных классов создают аттачи, которые прикладываются к отчету о
// выполнении тестов, когда построенный шаг выбросил исключение (либо это исключение возникло в ходе выполнения шага и
// его не удалось проигнорировать, либо это исключение возникло из-за того, что завершившийся шаг не вернул значимого
// результата, т.е. вернулся null или пустые массив / коллекция, в зависимости от того, от какого класса был унаследован
// данный класс)
public class MyGetStepSupplier<T, M, P>
        extends SequentialGetStepSupplier<MyTestContext, T, M, P, MyGetStepSupplier<T, M, P>> //Такое наследование просто,
        //чтобы схематично описать общий случай. Актуальные варианты ниже:
        //extends SequentialGetStepSupplier.GetObjectStepSupplier<>
        //extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<>
        //extends SequentialGetStepSupplier.GetObjectFromIterableStepSupplier<>
        //extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<>
        //extends SequentialGetStepSupplier.GetObjectFromArrayStepSupplier<>
        //extends SequentialGetStepSupplier.GetObjectFromArrayChainedStepSupplier<>
        //extends SequentialGetStepSupplier.GetIterableStepSupplier<>
        //extends SequentialGetStepSupplier.GetIterableChainedStepSupplier<>
        //extends SequentialGetStepSupplier.GetArrayStepSupplier<>
        //extends SequentialGetStepSupplier.GetArrayChainedStepSupplier<>
{
    //реализация поведения
}
```
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

#### Аттачи для шагов, которые выполняют действие

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;

@CaptureOnSuccess(by = ExampleCaptor2.class /**Можно указать несколько классов через запятую. Указанные классы
не должны иметь объявленных конструкторов, или среди объявленных конструкторов должен быть тот, что не содержит параметров.
Может быть указан абстрактный класс. В этом случае неявно будут работать объекты его классов-наследников*/) //Здесь
// указывается список классов, объекты которых могут обработать значение-объект выполнения действия построенного шага.
// Объекты перечисленных классов создают аттачи, которые прикладываются к отчету о выполнении тестов, если действие
// построенного шага завершилось успешно.
@CaptureOnFailure(by = {ExampleCaptor.class,  ExampleCaptor2.class}/**Можно указать несколько классов через запятую. 
Указанные классы не должны иметь объявленных конструкторов, или среди объявленных конструкторов должен быть тот,
что не содержит параметров. Может быть указан абстрактный класс. В этом случае неявно будут работать объекты его 
классов-наследников*/) //Здесь указывается список классов, объекты которых могут обработать значение-объект выполнения 
// действия построенного шага. Так же можно указать классы, объекты которых могут обработать объект-контекст выполнения 
// действия построенного шага. Объекты перечисленных классов создают аттачи, которые прикладываются к отчету о 
// выполнении тестов, если действие построенного шага завершилось выбрасыванием исключения.
public class MyActionStepSupplier<T> extends SequentialActionSupplier<MyTestContext, T, MyActionStepSupplier<T>>
{
    //реализация поведения
}
```
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Вложенность / глубина шагов

С помощью представленных выше классов можно описывать структурированные последовательности действий неограниченной глубины.
Не всегда бывает необходимо видеть все действия, которые происходят внутри того или иного шага. Построенный отчет о 
проведении теста, в котором отображено много технической/промежуточной информации о выполнении шагов, бывает неудобным 
для чтения.

Регулирование глубины отображения информации о выполнении шагов происходит с помощью [настроек](./SETTINGS.MD#TO_LIMIT_REPORT_DEPTH) 
и с помощью механизма, описанного ниже.

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

//С помощью данной аннотации можно указать максимальную глубину отображения шага, который строит данный 
//класс. И если шаг выполняется на уровне ниже, чем указанный, и свойство/переменная окружения 
// TO_LIMIT_REPORT_DEPTH = true, то шаг не будет отображен в консоли/построенном отчете о проведенном тесте,
//а так же не будут отображены шаги, выполненные в рамках этого шага.
@MaxDepthOfReporting(0) //Указанное значение должно быть в диапазоне от 0 до максимального значения Integer
//Аннотацию можно не указывать. Тогда глубина считается неограниченной.
public class MyGetStepSupplier<T, M, P>
        extends SequentialGetStepSupplier<MyTestContext, T, M, P, MyGetStepSupplier<T, M, P>> //Такое наследование просто,
        //чтобы схематично описать общий случай. Актуальные варианты ниже:
        //extends SequentialGetStepSupplier.GetObjectStepSupplier<>
        //extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<>
        //extends SequentialGetStepSupplier.GetObjectFromIterableStepSupplier<>
        //extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<>
        //extends SequentialGetStepSupplier.GetObjectFromArrayStepSupplier<>
        //extends SequentialGetStepSupplier.GetObjectFromArrayChainedStepSupplier<>
        //extends SequentialGetStepSupplier.GetIterableStepSupplier<>
        //extends SequentialGetStepSupplier.GetIterableChainedStepSupplier<>
        //extends SequentialGetStepSupplier.GetArrayStepSupplier<>
        //extends SequentialGetStepSupplier.GetArrayChainedStepSupplier<>
{

    @Description("Something by parameters {a}, {b}, {c}")
    public static <T, M> MyGetStepSupplier<T, M> something(
            @DescriptionFragment("a") Object a, //параметры, которые формируют
            @DescriptionFragment("b") Object b,  //название шага.
            @DescriptionFragment("c") Object c) { //Об этом будет ниже
        return //возвращает представление/построитель шага
    }
    
    //реализация поведения
}
```

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;

@MaxDepthOfReporting(0) //все тоже самое, что в примере выше.
public class MyActionStepSupplier<T> extends SequentialActionSupplier<MyTestContext, T, MyActionStepSupplier<T>>
{
    
    public static <T> MyActionStepSupplier<T> someAction(Object a, Object b, Object c) {
        return new MyActionStepSupplier<>(a, b, c);
    }

    //реализация поведения
    
    public MyActionStepSupplier<T> on(SequentialGetStepSupplier<MyTestContext, T, ?, ?, ?> getStep) {
        return super.performOn(getStep);
    }
    //реализация поведения
}
```

```java
package org.my.pack;

import static org.my.pack.MyGetStepSupplier.something;
import static org.my.pack.MyGetSomeOtherValue.someOtherValue;
import static org.my.pack.MyTestContext.myTestContext;

public class SomeTest {

    @Test
    public void test() {
        //шаг, построенный объектом, вернувшимся из вызова метода 
        // `something`, будет выведен на консоль/попадет в отчет,
        //т.к. здесь глубина его выполнения == 0
        var result = myTestContext().retrieve(something(1, 2, 3));
    }
}
```

```java
package org.my.pack;

import static org.my.pack.MyActionStepSupplier.someAction;
import static org.my.pack.MyGetStepSupplier.something;
import static org.my.pack.MyTestContext.myTestContext;

public class SomeTest {

    @Test
    public void test() {
        //шаг, построенный объектом, вернувшимся из вызова метода 
        // `someAction`, будет выведен на консоль/попадет в отчет,
        //т.к. здесь глубина его выполнения == 0
        var result = myTestContext().execute(someAction(1, 2, 3) //указываем действие 
                // шаг, построенный объектом, вернувшимся из вызова метода 
                // `something`, НЕ будет выведен на консоль/НЕ попадет в отчет,
                // т.к. здесь глубина его выполнения == 1, т.е. его 
                // выполнение происходит в рамках выполнения шага, строенного
                // объектом, вернувшимся из вызова метода `someAction`
                .on(something(1, 2, 3)));


    }
}
```

```java
package org.my.pack;

import static org.my.pack.MyActionStepSupplier.someAction;
import static org.my.pack.MyGetStepSupplier.something;
import static org.my.pack.MyTestContext.myTestContext;

import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

public class SomeTest {

    @Test
    public void test() {
        //На величину глубины не влияют шаги, созданные методами `$`
        $("Какой-то шаг", () -> {

            //шаг, построенный объектом, вернувшимся из вызова метода 
            // `someAction`, будет выведен на консоль/попадет в отчет,
            //т.к. здесь глубина его выполнения == 0
            var result = myTestContext().execute(someAction(1, 2, 3) //указываем действие 
                    // шаг, построенный объектом, вернувшимся из вызова метода 
                    // `something`, НЕ будет выведен на консоль/НЕ попадет в отчет,
                    // т.к. здесь глубина его выполнения == 1, т.е. его 
                    // выполнение происходит в рамках выполнения шага, строенного
                    // объектом, вернувшимся из вызова метода `someAction`
                    .on(something(1, 2, 3)));
            
            //дальнейшие действия
        });
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)