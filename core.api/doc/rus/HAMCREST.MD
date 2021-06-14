Neptune предоставляет свое расширение библиотеки [hamcrest.org](http://hamcrest.org/), которое совместимо со стандартным 
набором матчеров. 

## Оглавление

## Как создать матчер

### Базовый матчер NeptuneFeatureMatcher

```java
package org.mypack;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("some criteria with parameter {someParam}") //описание критерия и, кратко, того как это проверяется
//Описываем некий критерий проверки объектов типа T
public class MyMatcher<T> extends NeptuneFeatureMatcher<T> { //<--Нужно унаследовать этот класс
    // T - тип объектов, которые могут быть проверены данным матчером

    //значение, которое, помимо всего прочего,
    @DescriptionFragment(value = "someParam", makeReadableBy = ExampleGetParameterValue.class)
    //формирует динамическое название критерия проверки. Название фрагмента
    //должно соответствовать маске параметра, заключенного в {} (см. аннотацию Description над
    // классом). По умолчанию, используется значение, которое возвращает метод
    // toString() того объекта, который в данный момент является значением поля.
    //Но это значение не всегда бывает удобным для чтения. Для таких случаев можно
    //использовать классы, реализующие интерфейс
    // ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter,
    // цель которых - представить объект в читаемом формате.
    private final Object someParameter;

    @SafeVarargs //Можно унаследовать данный конструктор
    // - isNullSafe - означает, можно ли данным матчером проверять null. Если false - проверка null
    // автоматически будет отрицательной
    // - expectedTypes - набор/диапазон типов, которые расширяют T, объекты которых могут проверяться
    // данным матчером. Если проверяемое значения не является объектом классов, перечисленных в expectedTypes -
    // проверка автоматически будет отрицательной
    protected MyMatcher(boolean isNullSafe, Class<? extends T>... expectedTypes) {
        super(isNullSafe, expectedTypes);
    }

    //Можно унаследовать данный конструктор
    // - isNullSafe - означает, можно ли данным матчером проверять null. Если false - проверка null
    // автоматически будет отрицательной
    protected MyMatcher(boolean isNullSafe) {
        super(isNullSafe);
    }

    //тут создается объект матчера для последующего использования
    public static  <T> Matcher<T> myCriteriaMatcher(/*параметры, если нужно*/) {
        return new MyMatcher<T>(/*передаем все необходимые параметры*/);
    }

    @Override
    protected boolean featureMatches(T toMatch) {
        //описывается алгоритм проверки значения типа T на соответствие критериям
        return //вернется true, если проверка была успешной;
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

Для чего нужна аннотация `@Description` и как ей пользоваться:
- [Для шагов](./STEPS.MD#Для-шагов)
- [Для критериев](./STEPS.MD#Для-критериев)
- [Аннотации для описания названий произвольных объектов](./STEPS.MD#Для-произвольного-объекта)
- [Локализация](./LOCALIZATION.MD)


Пример использования матчера:

```java
package org.mypack;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mypack.MyMatcher.myCriteriaMatcher;

//пример использования
public class MyTest {

    @Test
    public void test() {
        T t = //инициализация;
        assertThat("Пояснение, что за значение тут проверяется", 
                t, myCriteriaMatcher(/*Если надоб передаем параметры*/));
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Описание несоответствия

```java
package org.mypack;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;

public class MyMatcher<T> extends NeptuneFeatureMatcher<T> {


    @Override
    protected boolean featureMatches(T toMatch) {
        //Алгоритм проверки
        if (/*проходит по критерию*/) {
            return true;
        }
        else {
            //формируем описание несоответствия
            Description d = new StringDescription().appendText("Не соответствует критерию");
            appendMismatchDescription(d); //и запоминаем его
            return false;
        }
    }
}
```

И теперь 

```java
package org.mypack;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mypack.MyMatcher.myCriteriaMatcher;

//пример использования
public class MyTest {

    @Test
    public void test() {
        T t = //инициализация;
        assertThat("Пояснение, что за значение тут проверяется", 
                t, myCriteriaMatcher(/*Если надоб передаем параметры*/));
        
        //если проверка окажется неуспешной, тогда в результате будет выброшено AssertError c текстом 
        //"Не соответствует критерию"
    }
}
```

Если проверка делегируется другому матчеру

```java
package org.mypack;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;

public class MyMatcher<T> extends NeptuneFeatureMatcher<T> {
    
    private final Matcher<? super T> delegateTo;


    @Override
    protected boolean featureMatches(T toMatch) {
        //Алгоритм проверки
        if (delegateTo.matches(toMatch)) {
            return true;
        }
        else {
            //можно запомнить описание несоответствия, которое формирует матчер-делегат
            appendMismatchDescription(delegateTo, toMatch);
            return false;
        }
    }
}
```

Что если описание несоответствия нужно переиспользовать полностью или как шаблон? Тогда

```java
package org.mypack;

import ru.tinkoff.qa.neptune.core.api.hamcrest.MatchObjectName;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("Проверяемый объект или свойство. {details}") //Название/описание шаблонизируемого объекта или свойства,
// которое может проверятся разными матчерами
public class MyMatchedObject extends MatchObjectName { //<--Нужно унаследовать этот класс

    //значение, которое, помимо всего прочего,
    @DescriptionFragment(value = "details", makeReadableBy = ExampleGetParameterValue.class)
    //формирует динамическое название проверяемого объекта или свойства. Название фрагмента
    //должно соответствовать маске параметра, заключенного в {} (см. аннотацию Description над
    // классом). По умолчанию, используется значение, которое возвращает метод
    // toString() того объекта, который в данный момент является значением поля.
    //Но это значение не всегда бывает удобным для чтения. Для таких случаев можно
    //использовать классы, реализующие интерфейс
    // ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter,
    // цель которых - представить объект в читаемом формате.
    private final Object details;

    public MyMatchedObject(Object details) {
        this.details = details;
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

Для чего нужна аннотация `@Description` и как ей пользоваться:
- [Для шагов](./STEPS.MD#Для-шагов)
- [Для критериев](./STEPS.MD#Для-критериев)
- [Аннотации для описания названий произвольных объектов](./STEPS.MD#Для-произвольного-объекта)
- [Локализация](./LOCALIZATION.MD)

```java
package org.mypack;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("{object} не соответствует: {matcher}") //Шаблонное описание несоответствия,
// которое может проверятся разными матчерами
public final class MyMismatchDescriber extends MismatchDescriber { //<--Нужно унаследовать этот класс

    @DescriptionFragment("object")
    private final MyMatchedObject object;

    @DescriptionFragment("matcher")
    private final Matcher<?> matcher;

    public MyMismatchDescriber(MyMatchedObject object, Matcher<?> matcher) {
        this.object = object;
        this.matcher = matcher;
    }
}
```

```java
package org.mypack;

import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;

public class MyMatcher<T> extends NeptuneFeatureMatcher<T> {


    @Override
    protected boolean featureMatches(T toMatch) {
        //Алгоритм проверки
        if (/*проходит по критерию*/) {
            return true;
        }
        else {
            //запоминаем выявленное несоответствие
            appendMismatchDescription(new MyMismatchDescriber(new MyMatchedObject(/* параметры */), this));
            return false;
        }
    }
}
```

Что если матчер выявляет за одну проверку несколько несоответствий? Тогда

```java
package org.mypack;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;

public class MyMatcher<T> extends NeptuneFeatureMatcher<T> {

    //тут создается объект матчера для последующего использования
    public static  <T> Matcher<T> myCriteriaMatcher(/*параметры, если нужно*/) {
        return new MyMatcher<T>(/*передаем все необходимые параметры*/);
    }

    @Override
    protected boolean featureMatches(T toMatch) {
        var result = true;
        //Алгоритм проверки
        if (/*не проходит по одной проверке*/) {
            result = false;
            appendMismatchDescription(/*подходящие параметры*/); //добавляем описание несоответствия
        }

        if (/*не проходит по другой проверке*/) {
            result = false;
            appendMismatchDescription(/*подходящие параметры*/); //добавляем описание несоответствия
        }

        return result;
    }
}

```

И теперь 

```java
package org.mypack;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mypack.MyMatcher.myCriteriaMatcher;

//пример использования
public class MyTest {

    @Test
    public void test() {
        T t = //инициализация;
        assertThat("Пояснение, что за значение тут проверяется", 
                t, myCriteriaMatcher(/*Если надоб передаем параметры*/));
        
        //если проверка окажется неуспешной, тогда в результате будет выброшено AssertError c текстом,
        //Описывающим все выявленные несоответствия
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Стандартные матчеры

### Матчеры Iterable<?>

```java
package org.mypack;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher.eachOfIterable;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher.iterableIncludes;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher.iterableIncludesInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.iterableHasItem;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.iterableHasItems;

public class MyTest {

    private Iterable<Integer> i = List.of(1, 2, 3);


    @Test(description = "проверка на то, что что Iterable<> состоит только из " +
            "перечисленных элементов в указанном порядке")
    public void test() {
        assertThat(i, iterableInOrder(1, 2, 3));
    }

    @Test(description = "проверка на то, что что Iterable<> состоит только из " +
            "перечисленных элементов в указанном порядке" +
            "элементы указаны в виде матчеров-критериев, которым они должны соответствовать")
    public void test2() {
        assertThat(i, iterableInOrder(lessThan(2), greaterThan(1), instanceOf(Integer.class)));
    }

    @Test(description = "проверка на то, что что Iterable<> состоит только из " +
            "перечисленных элементов. Порядок их следования значения не имеет.")
    public void test3() {
        assertThat(i, iterableOf(2, 1, 3));
    }

    @Test(description = "проверка на то, что что Iterable<> состоит только из " +
            "перечисленных элементов. Порядок их следования значения не имеет." +
            "элементы указаны в виде матчеров-критериев, которым они должны соответствовать")
    public void test4() {
        assertThat(i, iterableOf(greaterThan(2), lessThan(2), instanceOf(Integer.class)));
    }


    @Test(description = "проверка на то, что что Iterable<> включает " +
            "перечисленные элементы в указанном порядке")
    public void test5() {
        assertThat(i, iterableIncludesInOrder(2, 3));
    }

    @Test(description = "проверка на то, что что Iterable<> включает " +
            "перечисленные элементы в указанном порядке." +
            "элементы указаны в виде матчеров-критериев, которым они должны соответствовать")
    public void test6() {
        assertThat(i, iterableIncludesInOrder(greaterThan(1), instanceOf(Integer.class)));
    }


    @Test(description = "проверка на то, что что Iterable<> включает " +
            "перечисленные элементы. Порядок их следования значения не имеет")
    public void test7() {
        assertThat(i, iterableIncludes(3, 1));
    }

    @Test(description = "проверка на то, что что Iterable<> включает " +
            "перечисленные элементы. Порядок их следования значения не имеет." +
            "элементы указаны в виде матчеров-критериев, которым они должны соответствовать")
    public void test8() {
        assertThat(i, iterableIncludes(greaterThan(2), lessThan(2)));
    }

    @Test(description = "проверка на то, что что Iterable<> состоит из элементов, " +
            "каждый из которых соответствует перечисленным матчерам-критериям")
    public void test9() {
        assertThat(i, eachOfIterable(instanceOf(Integer.class), lessThan(5)));
    }

    @Test(description = "проверка на то, что что Iterable<> включает элемент")
    public void test10() {
        assertThat(i, iterableHasItem(1));
    }

    @Test(description = "проверка на то, что что Iterable<> включает элемент." +
            "элемент описан в виде матчеров-критериев")
    public void test11() {
        assertThat(i, iterableHasItem(greaterThan(0), lessThan(2)));
    }

    @Test(description = "Проверка на то, что что Iterable<> включает элементы." +
            "элементы описаны в виде матчеров-критериев.")
    public void test12() {
        assertThat(i, iterableHasItems(2, //сколько раз в Iterable<> должен встретиться объект,
                greaterThan(1), instanceOf(Integer.class)));

        assertThat(i, iterableHasItems(greaterThan(1), //сколько раз в Iterable<> должен встретиться объект
                // (описано критерием-матчером),
                greaterThan(1), instanceOf(Integer.class)));
    }

    @Test(description = "Проверка на то, что что Iterable<> включает элементы.")
    public void test13() {
        assertThat(i, iterableHasItems(1,//сколько раз в Iterable<> должен встретиться объект,
                1));

        assertThat(i, iterableHasItems(lessThan(2),//сколько раз в Iterable<> должен встретиться объект
                // (описано критерием-матчером),
                1));
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Матчеры массивов

```java
package org.mypack;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.arrayInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.arrayOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher.eachOfArray;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher.arrayIncludes;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher.arrayIncludesInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.arrayHasItem;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.arrayHasItems;

public class MyTest {

    private Integer[] i = new Integer[] {1, 2, 3};


    @Test(description = "проверка на то, что что массив состоит только из " +
            "перечисленных элементов в указанном порядке")
    public void test() {
        assertThat(i, arrayInOrder(1, 2, 3));
    }

    @Test(description = "проверка на то, что что массив состоит только из " +
            "перечисленных элементов в указанном порядке" +
            "элементы указаны в виде матчеров-критериев, которым они должны соответствовать")
    public void test2() {
        assertThat(i, arrayInOrder(lessThan(2), greaterThan(1), instanceOf(Integer.class)));
    }

    @Test(description = "проверка на то, что что массив состоит только из " +
            "перечисленных элементов. Порядок их следования значения не имеет.")
    public void test3() {
        assertThat(i, arrayOf(2, 1, 3));
    }

    @Test(description = "проверка на то, что что массив состоит только из " +
            "перечисленных элементов. Порядок их следования значения не имеет." +
            "элементы указаны в виде матчеров-критериев, которым они должны соответствовать")
    public void test4() {
        assertThat(i, arrayOf(greaterThan(2), lessThan(2), instanceOf(Integer.class)));
    }


    @Test(description = "проверка на то, что что массив включает " +
            "перечисленные элементы в указанном порядке")
    public void test5() {
        assertThat(i, arrayIncludesInOrder(2, 3));
    }

    @Test(description = "проверка на то, что что массив включает " +
            "перечисленные элементы в указанном порядке." +
            "элементы указаны в виде матчеров-критериев, которым они должны соответствовать")
    public void test6() {
        assertThat(i, arrayIncludesInOrder(greaterThan(1), instanceOf(Integer.class)));
    }


    @Test(description = "проверка на то, что что массив включает " +
            "перечисленные элементы. Порядок их следования значения не имеет")
    public void test7() {
        assertThat(i, arrayIncludes(3, 1));
    }

    @Test(description = "проверка на то, что что массив включает " +
            "перечисленные элементы. Порядок их следования значения не имеет." +
            "элементы указаны в виде матчеров-критериев, которым они должны соответствовать")
    public void test8() {
        assertThat(i, arrayIncludes(greaterThan(2), lessThan(2)));
    }

    @Test(description = "проверка на то, что что массив состоит из элементов, " +
            "каждый из которых соответствует перечисленным матчерам-критериям")
    public void test9() {
        assertThat(i, eachOfArray(instanceOf(Integer.class), lessThan(5)));
    }

    @Test(description = "проверка на то, что что массив включает элемент")
    public void test10() {
        assertThat(i, arrayHasItem(1));
    }

    @Test(description = "проверка на то, что что массив включает элемент." +
            "элемент описан в виде матчеров-критериев")
    public void test11() {
        assertThat(i, arrayHasItem(greaterThan(0), lessThan(2)));
    }

    @Test(description = "Проверка на то, что что массив включает элементы." +
            "элементы описаны в виде матчеров-критериев.")
    public void test12() {
        assertThat(i, arrayHasItems(2, //сколько раз в массиве должен встретиться объект,
                greaterThan(1), instanceOf(Integer.class)));

        assertThat(i, arrayHasItems(greaterThan(1), //сколько раз в массиве должен встретиться объект
                // (описано критерием-матчером),
                greaterThan(1), instanceOf(Integer.class)));
    }

    @Test(description = "Проверка на то, что что массив включает элементы.")
    public void test13() {
        assertThat(i, arrayHasItems(1,//сколько раз в массиве должен встретиться объект,
                1));

        assertThat(i, arrayHasItems(lessThan(2),//сколько раз в массиве должен встретиться объект
                // (описано критерием-матчером),
                1));
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Матчеры Map<?,?>

```java
package org.mypack;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher.eachEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher.eachEntryKey;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher.eachEntryValue;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher.mapIncludes;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher.mapIncludesInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.mapHasEntries;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.mapHasEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.mapHasEntryKey;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.mapHasEntryKeys;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.mapHasEntryValue;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.mapHasEntryValues;

public class MyTest {

    private Map<Integer, Integer> m = new LinkedHashMap<>() {
        {
            put(1, 2);
            put(2, 3);
            put(3, 4);
        }
    };

    @Test(description = "проверка на то, что что Map<> состоит только из " +
            "перечисленных записей в указанном порядке")
    public void test() {
        assertThat(m, mapInOrder(
                mapEntry(1, 2),
                mapEntry(2, 3),
                mapEntry(3, 4)));
    }

    @Test(description = "проверка на то, что что Map<> состоит только из " +
            "перечисленных записей в указанном порядке" +
            "Записи указаны в виде матчеров-критериев, которым они должны соответствовать")
    public void test2() {
        assertThat(m, mapInOrder(
                mapEntry(lessThan(2), greaterThan(1)),
                mapEntry(lessThan(3), greaterThan(2)),
                mapEntry(lessThan(4), greaterThan(3))));
    }

    @Test(description = "проверка на то, что что Map<> состоит только из " +
            "перечисленных записей. Порядок их следования значения не имеет.")
    public void test3() {
        assertThat(m, mapOf(
                mapEntry(1, 2),
                mapEntry(3, 4),
                mapEntry(2, 3)));
    }

    @Test(description = "проверка на то, что что Map<> состоит только из " +
            "перечисленных записей. Порядок их следования значения не имеет." +
            "Записи указаны в виде матчеров-критериев, которым они должны соответствовать")
    public void test4() {
        assertThat(m, mapOf(
                mapEntry(lessThan(2), greaterThan(1)),
                mapEntry(lessThan(4), greaterThan(3)),
                mapEntry(lessThan(3), greaterThan(2))));
    }


    @Test(description = "проверка на то, что что Map<> включает " +
            "перечисленные записи в указанном порядке")
    public void test5() {
        assertThat(m, mapIncludesInOrder(
                mapEntry(2, 3),
                mapEntry(3, 4)));
    }

    @Test(description = "проверка на то, что что Map<> включает " +
            "перечисленные записи в указанном порядке." +
            "Записи указаны в виде матчеров-критериев, которым они должны соответствовать")
    public void test6() {
        assertThat(m, mapIncludesInOrder(
                mapEntry(lessThan(2), greaterThan(1)),
                mapEntry(lessThan(3), greaterThan(2))));
    }


    @Test(description = "проверка на то, что что Map<> включает " +
            "перечисленные записи. Порядок их следования значения не имеет")
    public void test7() {
        assertThat(m, mapIncludes(
                mapEntry(3, 4),
                mapEntry(1, 2)));
    }

    @Test(description = "проверка на то, что что Map<> включает " +
            "перечисленные записи. Порядок их следования значения не имеет." +
            "Записи указаны в виде матчеров-критериев, которым они должны соответствовать")
    public void test8() {
        assertThat(m, mapIncludes(
                mapEntry(lessThan(2), greaterThan(1)),
                mapEntry(lessThan(4), greaterThan(3))));
    }

    @Test(description = "проверка на то, что что Map<> состоит записей, " +
            "каждая из которых соответствует перечисленным матчерам-критериям")
    public void test9() {
        //проверка каждой записи
        assertThat(m, eachEntry(instanceOf(Integer.class), instanceOf(Integer.class)));
        //проверка каждого ключа
        assertThat(m, eachEntryKey(instanceOf(Integer.class)));
        //проверка каждого значения
        assertThat(m, eachEntryValue(instanceOf(Integer.class)));
    }

    @Test(description = "проверка на то, что что Map<> включает запись")
    public void test10() {
        assertThat(m, mapHasEntry(1, 2)); //есть запись
        assertThat(m, mapHasEntryKey(1)); //есть ключ
        assertThat(m, mapHasEntryValue(2)); //есть значение
    }

    @Test(description = "проверка на то, что что Map<> включает запись." +
            "Запись описана в виде матчеров-критериев")
    public void test11() {
        //есть запись
        assertThat(m, mapHasEntry(greaterThan(0), //матчер для ключа
                lessThan(3), greaterThan(0))); //матчеры для значения

        assertThat(m, mapHasEntryKey(greaterThan(0), lessThan(2))); //есть ключ,
        // матчеры для ключа
        assertThat(m, mapHasEntryValue(greaterThan(0), lessThan(3))); //есть значение,
        // матчеры для значения


    }

    @Test(description = "Проверка на то, что что Map<> включает записи." +
            "Записи описаны в виде матчеров-критериев.")
    public void test12() {
        assertThat(m, mapHasEntries(2, //сколько раз должна встретиться запись,
                greaterThan(0), //матчер для ключа
                instanceOf(Integer.class), lessThan(4))); //матчеры для значения

        assertThat(m, mapHasEntries(greaterThan(1), //сколько раз должна встретиться запись (описано
                // критерием-матчером),
                greaterThan(0), //матчер для ключа
                instanceOf(Integer.class), lessThan(4))); //матчеры для значения

        //есть ключи,
        assertThat(m, mapHasEntryKeys(2, //сколько раз должен встретиться ключ,
                greaterThan(0), lessThan(3))); // матчеры для ключа

        //есть ключи,
        assertThat(m, mapHasEntryKeys(greaterThan(1), //сколько раз должен встретиться ключ (описано
                // критерием-матчером),
                greaterThan(0), lessThan(3))); // матчеры для ключа

        //есть значения,
        assertThat(m, mapHasEntryValues(2, //сколько раз должно встретиться значение,
                greaterThan(0), lessThan(4))); // матчеры для значения

        //есть значения,
        assertThat(m, mapHasEntryValues(greaterThan(1), //сколько раз должно встретиться значение (описано
                // критерием-матчером),
                greaterThan(0), lessThan(4))); // матчеры для значения

        //есть значения,
        assertThat(m, mapHasEntryValues(1, //сколько раз должно встретиться значение,
                2));
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Матчеры URI/URL

```java
package org.mypack;

import java.net.URI;

import static java.net.URI.create;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasHostMatcher.uriHasHost;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasHostMatcher.urlHasHost;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPathMatcher.uriHasPath;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPathMatcher.urlHasPath;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPortMatcher.uriHasPort;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPortMatcher.urlHasPort;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasProtocolMatcher.urlHasProtocol;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasQueryParameters.uriHasQueryParameter;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasQueryParameters.urlHasQueryParameter;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasQueryStringMatcher.uriHasQueryString;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasQueryStringMatcher.urlHasQueryString;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasReferenceMatcher.urlHasReference;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasSchemeMatcher.uriHasScheme;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasUserInfoMatcher.uriHasUserInfo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasUserInfoMatcher.urlHasUserInfo;

public class MyTest {

    private static final URI testURI = create("https://user:password@www.google.com:666/search?q=how+to+create+e2e+test#test");

    @Test(description = "Хост")
    public void test1() throws Exception {
        assertThat(testURI, uriHasHost("www.google.com"));
        assertThat(testURI, uriHasHost(containsString("google")));
        assertThat(testURI.toURL(), urlHasHost("www.google.com"));
        assertThat(testURI.toURL(), urlHasHost(containsString("google")));
    }

    @Test(description = "Path")
    public void test2() throws Exception {
        assertThat(testURI, uriHasPath("/search"));
        assertThat(testURI, uriHasPath(containsString("search")));
        assertThat(testURI.toURL(), urlHasPath("/search"));
        assertThat(testURI.toURL(), urlHasPath(containsString("search")));
    }

    @Test(description = "Порт")
    public void test3() throws Exception {
        assertThat(testURI, uriHasPort(666));
        assertThat(testURI, uriHasPort(greaterThan(665)));
        assertThat(testURI.toURL(), urlHasPort(666));
        assertThat(testURI.toURL(), urlHasPort(greaterThan(665)));
    }

    @Test(description = "Схема/Протокол")
    public void test4() throws Exception {
        assertThat(testURI, uriHasScheme("https"));
        assertThat(testURI.toURL(), urlHasProtocol("https"));
    }

    @Test(description = "Query")
    public void test5() throws Exception {
        assertThat(testURI, uriHasQueryString("q=how+to+create+e2e+test"));
        assertThat(testURI.toURL(), urlHasQueryString("q=how+to+create+e2e+test"));

        assertThat(testURI, uriHasQueryParameter("q", iterableInOrder("how+to+create+e2e+test")));
        assertThat(testURI.toURL(), urlHasQueryParameter("q", iterableInOrder("how+to+create+e2e+test")));
    }

    @Test(description = "Reference")
    public void test6() throws Exception {
        assertThat(testURI.toURL(), urlHasReference("test"));
    }

    @Test(description = "User info")
    public void test7() throws Exception {
        assertThat(testURI, uriHasUserInfo("user:password"));
        assertThat(testURI.toURL(), urlHasUserInfo("user:password"));
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Матчеры String

`org.hamcrest.*` имеет множество матчеров для проверки строк. Neptune имеет один дополнительный

```java
package org.mypack;

import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.arrayOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.text.StringContainsWithSeparator.withSeparator;


public class MyTest {

    private static final String TEST_STRING = "A1,A2,A3,A4,A5";

    @Test(description = "Проверка строки с разделителем")
    public void test1() throws Exception {
        assertThat(TEST_STRING, withSeparator(",", //разделитель
                "A1", "A2", "A3", "A4", "A5")); //проверка что строка с разделителем
        // содержит только указанные под-строки в указанном порядке 
        assertThat(TEST_STRING,
                withSeparator(",", //разделитель
                        "A1")); //проверка что строка с разделителем указанную подстроку
        
        assertThat(TEST_STRING, withSeparator(",", //разделитель
                arrayOf("A1", "A3", "A2", "A4", "A5"))); //матчер для массива строк
    }
}
```

Матчеры для массивов описаны [тут](#Матчеры-массивов)

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Объединения матчеров AND, OR, XOR, NOT

```java
package org.mypack;

import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.all.AllCriteriaMatcher.all;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.any.AnyMatcher.anyOne;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.not.NotMatcher.notOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.only.one.OnlyOneMatcher.onlyOne;

//пример использования
public class MyTest {

    @Test
    public void test() {
        T t = //инициализация;
                assertThat(t, all(matcher1, matcher3, matcher2, matcher4)); //значение должно соответствовать
        //всем перечисленным критериям

        assertThat(t, notOf(matcher1, matcher3, matcher2, matcher4)); //значение не должно соответствовать
        //ни одному из перечисленных критериев

        assertThat(t, anyOne(matcher1, matcher3, matcher2, matcher4)); //значение должно соответствовать
        //любому(любым) из перечисленных критериев

        assertThat(t, onlyOne(matcher1, matcher3, matcher2, matcher4)); //значение должно соответствовать
        //только одному из перечисленных критериев
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Матчер для POJO

```java
package org.mypack;

public class SomePojo {

    private Object a;

    private Object b;

    public Object getA() {
        return a;
    }

    public SomePojo setA(Object a) {
        this.a = a;
        return this;
    }

    private Object getB() {
        return b;
    }

    public SomePojo setB(Object b) {
        this.b = b;
        return this;
    }
}
```

```java
package org.mypack;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.pojo.PojoGetterReturnsMatcher.getterReturns;

//пример использования
public class MyTest {

    private static final SomePojo SOME_POJO = new SomePojo()
            .setA("AB")
            .setB(false);

    @Test(description = "Проверка значения, которое возвращает публичный get*-метод")
    public void test() {
        assertThat(SOME_POJO, getterReturns("getA", "AB")); //можно проверять на 
        // равенство указанному значению

        assertThat(SOME_POJO, getterReturns("getA",
                getterReturns("getA", startsWith("A"), endsWith("B")))); //можно проверять на 
        // соответствие переданным критериям
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Дополнительные матчеры, которые предоставляет Neptune

[Матчеры для валидации ответов http](./../../../http.api/doc/rus/HAMCREST.MD)

[Матчеры для front end вэб-приложений](./../../../selenium/doc/rus/HAMCREST.MD)

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
