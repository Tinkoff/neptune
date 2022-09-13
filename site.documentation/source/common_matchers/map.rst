Для java.util.Map
=======================

.. code-block:: java
   :caption: Примеры использования матчеров для Map

   package org.mypack;

   import java.util.LinkedHashMap;
   import java.util.Map;

   import static org.hamcrest.MatcherAssert.assertThat;
   import static org.hamcrest.Matchers.*;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                .MapEntryMatcher.mapEntry;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                .SetOfObjectsConsistsOfMatcher.mapInOrder;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                .SetOfObjectsConsistsOfMatcher.mapOf;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                .SetOfObjectsEachItemMatcher.eachEntry;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                .SetOfObjectsEachItemMatcher.eachEntryKey;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                .SetOfObjectsEachItemMatcher.eachEntryValue;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                .SetOfObjectsIncludesMatcher.mapIncludes;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                .SetOfObjectsIncludesMatcher.mapIncludesInOrder;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                .SetOfObjectsItemsMatcher.mapHasEntries;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                .SetOfObjectsItemsMatcher.mapHasEntry;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                .SetOfObjectsItemsMatcher.mapHasEntryKey;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                .SetOfObjectsItemsMatcher.mapHasEntryKeys;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                .SetOfObjectsItemsMatcher.mapHasEntryValue;
   import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables
                .SetOfObjectsItemsMatcher.mapHasEntryValues;

   public class MyTest {

     private final Map<Integer, Integer> m = new LinkedHashMap<>() {
       {
         put(1, 2);
         put(2, 3);
         put(3, 4);
       }
     };

     @Test(description = "проверка на то, " +
                "что что Map<> состоит только из " +
                "перечисленных записей в указанном порядке")
     public void test() {
       assertThat(m, mapInOrder(
               mapEntry(1, 2),
               mapEntry(2, 3),
               mapEntry(3, 4)));
     }

     @Test(description = "проверка на то, " +
                "что что Map<> состоит только из " +
                "перечисленных записей в указанном порядке" +
                "Записи описаны матчерами, которым они должны соответствовать")
     public void test2() {
       assertThat(m, mapInOrder(
               mapEntry(lessThan(2), greaterThan(1)),
               mapEntry(lessThan(3), greaterThan(2)),
               mapEntry(lessThan(4), greaterThan(3))));
     }

     @Test(description = "проверка на то, " +
                "что что Map<> состоит только из " +
                "перечисленных записей. " +
                "Порядок их следования значения не имеет.")
     public void test3() {
       assertThat(m, mapOf(
               mapEntry(1, 2),
               mapEntry(3, 4),
               mapEntry(2, 3)));
     }

     @Test(description = "проверка на то, " +
                "что что Map<> состоит только из " +
                "перечисленных записей. " +
                "Порядок их следования значения не имеет." +
                "Записи описаны матчерами, которым они должны соответствовать")
     public void test4() {
       assertThat(m, mapOf(
               mapEntry(lessThan(2), greaterThan(1)),
               mapEntry(lessThan(4), greaterThan(3)),
               mapEntry(lessThan(3), greaterThan(2))));
     }


     @Test(description = "проверка на то, " +
                "что что Map<> включает " +
                "перечисленные записи в указанном порядке")
     public void test5() {
       assertThat(m, mapIncludesInOrder(
               mapEntry(2, 3),
               mapEntry(3, 4)));
     }

     @Test(description = "проверка на то, " +
                "что что Map<> включает " +
                "перечисленные записи в указанном порядке." +
                "Записи описаны матчерами, которым они должны соответствовать")
     public void test6() {
       assertThat(m, mapIncludesInOrder(
               mapEntry(lessThan(2), greaterThan(1)),
               mapEntry(lessThan(3), greaterThan(2))));
     }


     @Test(description = "проверка на то, " +
                "что что Map<> включает " +
                "перечисленные записи. " +
                "Порядок их следования значения не имеет")
     public void test7() {
       assertThat(m, mapIncludes(
               mapEntry(3, 4),
               mapEntry(1, 2)));
     }

     @Test(description = "проверка на то, " +
                "что что Map<> включает " +
                "перечисленные записи. " +
                "Порядок их следования значения не имеет. " +
                "Записи описаны матчерами, которым они должны соответствовать")
     public void test8() {
       assertThat(m, mapIncludes(
               mapEntry(lessThan(2), greaterThan(1)),
               mapEntry(lessThan(4), greaterThan(3))));
     }

     @Test(description = "проверка на то, " +
                "что что Map<> состоит записей, " +
                "каждая из которых соответствует указанным матчерам")
     public void test9() {
       //проверка каждой записи
       assertThat(m, eachEntry(
               instanceOf(Integer.class),
               instanceOf(Integer.class)
       ));

       //проверка каждого ключа
       assertThat(m, eachEntryKey(instanceOf(Integer.class)));

       //проверка каждого значения
       assertThat(m, eachEntryValue(instanceOf(Integer.class)));
     }

     @Test(description = "проверка на то, " +
                "что что Map<> включает запись")
     public void test10() {
       assertThat(m, mapHasEntry(1, 2)); //есть запись
       assertThat(m, mapHasEntryKey(1)); //есть ключ
       assertThat(m, mapHasEntryValue(2)); //есть значение
     }

     @Test(description = "проверка на то, " +
                "что что Map<> включает запись." +
                "Запись описана в виде матчеров")
     public void test11() {
       //есть запись
       assertThat(m, mapHasEntry(
               greaterThan(0), //матчер для ключа
               lessThan(3), greaterThan(0) //матчеры для значения
       ));

       assertThat(m,
               //есть ключ,
               mapHasEntryKey(greaterThan(0), lessThan(2))); // матчеры для ключа

       assertThat(m,
               //есть значение,
               mapHasEntryValue(greaterThan(0), lessThan(3))); // матчеры для значения
     }

     @Test(description = "Проверка на то, " +
                "что что Map<> включает записи указанное количество раз" +
                "Записи описаны матчерами")
     public void test12() {
       assertThat(m, mapHasEntries(2, //сколько раз должна встретиться запись,
               greaterThan(0), //матчер для ключа
               instanceOf(Integer.class), lessThan(4))); //матчеры для значения

       assertThat(m, mapHasEntries(
               //сколько раз должна встретиться запись
               greaterThan(1), //описано матчером
               greaterThan(0), //матчер для ключа
               instanceOf(Integer.class), lessThan(4))); //матчеры для значения

       //есть ключи,
       assertThat(m, mapHasEntryKeys(2, //сколько раз должен встретиться ключ,
               greaterThan(0), lessThan(3))); // матчеры для ключа

       //есть ключи,
       assertThat(m, mapHasEntryKeys(
               //сколько раз должен встретиться ключ
               greaterThan(1), //описано матчером
               greaterThan(0), lessThan(3))); // матчеры для ключа

       //есть значения,
       assertThat(m, mapHasEntryValues(2, //сколько раз должно встретиться значение,
               greaterThan(0), lessThan(4))); // матчеры для значения

       //есть значения,
       assertThat(m, mapHasEntryValues(
               //сколько раз должно встретиться значение
               greaterThan(1), //описано матчером
               greaterThan(0), lessThan(4))); // матчеры для значения

       //есть значения,
       assertThat(m, mapHasEntryValues(1, //сколько раз должно встретиться значение,
               2));
     }
   }