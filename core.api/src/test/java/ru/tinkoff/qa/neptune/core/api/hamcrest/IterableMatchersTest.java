package ru.tinkoff.qa.neptune.core.api.hamcrest;

import org.hamcrest.Matcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.not.NotMatcher.notOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.only.one.OnlyOneMatcher.onlyOne;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.*;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.*;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher.*;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher.*;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.*;


public class IterableMatchersTest {

    private static final List<String> TEST_LIST = of("A1", "A2", "A3", "A4", "A5");
    private static final Map<?, ?> TEST_MAP = new LinkedHashMap<>() {
        {
            put("A1", "B1");
            put("A2", "B2");
            put("A3", "B3");
            put("A4", "B4");
            put("A5", "B5");
        }
    };

    @DataProvider
    public static Object[][] data1() {
        return new Object[][]{
                {TEST_LIST, iterableInOrder("A1", "A2", "A3", "A4", "A5")},
                {TEST_LIST, iterableOf("A2", "A1", "A3", "A4", "A5")},
                {TEST_LIST, iterableIncludesInOrder("A1", "A3", "A5")},
                {TEST_LIST, iterableIncludes("A1", "A5")},
                {TEST_LIST, eachOfIterable(containsString("A"))},
                {TEST_LIST, iterableHasItems(greaterThan(4), containsString("A"))},
                {TEST_LIST, iterableHasItems(5, containsString("A"))},
                {TEST_LIST, iterableHasItem(containsString("A"), containsString("1"))},
                {TEST_LIST, iterableHasItem(notOf(containsString("B")))},
                {TEST_LIST, iterableHasItem("A1")},
                {TEST_LIST, iterableHasItems(1, "A1")},
                {TEST_LIST, iterableHasItems(greaterThanOrEqualTo(1), "A1")},

                {TEST_MAP, mapInOrder(mapEntry("A1", "B1"),
                        mapEntry("A2", "B2"),
                        mapEntry("A3", "B3"),
                        mapEntry("A4", "B4"),
                        mapEntry("A5", "B5"))},

                {TEST_MAP, mapInOrder(entryKey("A1"),
                        entryKey("A2"),
                        entryKey("A3"),
                        entryKey("A4"),
                        entryKey("A5"))},

                {TEST_MAP, mapInOrder(entryValue("B1"),
                        entryValue("B2"),
                        entryValue("B3"),
                        entryValue("B4"),
                        entryValue("B5"))},

                {TEST_MAP, mapOf(mapEntry("A2", "B2"),
                        mapEntry("A1", "B1"),
                        mapEntry("A3", "B3"),
                        mapEntry("A4", "B4"),
                        mapEntry("A5", "B5"))},

                {TEST_MAP, mapOf(entryKey("A2"),
                        entryKey("A1"),
                        entryKey("A3"),
                        entryKey("A4"),
                        entryKey("A5"))},

                {TEST_MAP, mapOf(entryValue("B2"),
                        entryValue("B1"),
                        entryValue("B3"),
                        entryValue("B4"),
                        entryValue("B5"))},

                {TEST_MAP, mapIncludesInOrder(mapEntry("A1", "B1"),
                        mapEntry("A3", "B3"),
                        mapEntry("A5", "B5"))},

                {TEST_MAP, mapIncludesInOrder(entryKey("A1"),
                        entryKey("A3"),
                        entryKey("A5"))},

                {TEST_MAP, mapIncludesInOrder(entryValue("B1"),
                        entryValue("B3"),
                        entryValue("B5"))},

                {TEST_MAP, mapIncludes(mapEntry("A3", "B3"),
                        mapEntry("A2", "B2"),
                        mapEntry("A5", "B5"))},

                {TEST_MAP, mapIncludes(entryKey("A3"),
                        entryKey("A2"),
                        entryKey("A5"))},

                {TEST_MAP, mapIncludes(entryValue("B3"),
                        entryValue("B2"),
                        entryValue("B5"))},

                {TEST_MAP, eachEntry(startsWith("A"), startsWith("B"))},

                {TEST_MAP, eachEntryKey(startsWith("A"))},

                {TEST_MAP, eachEntryValue(startsWith("B"))},

                {TEST_MAP, mapHasEntries(greaterThanOrEqualTo(4), startsWith("A"), startsWith("B"))},

                {TEST_MAP, mapHasEntries(5, startsWith("A"), startsWith("B"))},

                {TEST_MAP, mapHasEntry(startsWith("A"), startsWith("B"))},

                {TEST_MAP, mapHasEntry("A1", "B1")},

                {TEST_MAP, mapHasEntryKeys(greaterThanOrEqualTo(4), startsWith("A"))},

                {TEST_MAP, mapHasEntryKeys(5, startsWith("A"), notOf(containsString("B")))},

                {TEST_MAP, mapHasEntryKey(startsWith("A"), notOf(containsString("B")))},

                {TEST_MAP, mapHasEntryKey(startsWith("A"))},

                {TEST_MAP, mapHasEntryKey("A1")},

                {TEST_MAP, mapHasEntryValues(greaterThanOrEqualTo(4), startsWith("B"))},

                {TEST_MAP, mapHasEntryValues(2, onlyOne(is("B1"), is("B3")), notOf(startsWith("A")))},

                {TEST_MAP, mapHasEntryValues(2, onlyOne(is("B1"), is("B3")))},

                {TEST_MAP, mapHasEntryValues(1, "B1")},

                {TEST_MAP, mapHasEntryValue("B1")},

                {TEST_MAP, mapHasEntryValue(startsWith("B"))},

                {TEST_MAP, mapHasEntryValue(notOf(startsWith("A")), containsString("B"))},

        };
    }

    @DataProvider
    public static Object[][] data2() {
        return new Object[][]{
                {TEST_LIST, iterableInOrder("A2", "A1", "A3", "A4", "A5"),
                        "Tested object\n" +
                                "Expected: in following order: \"A2\", \"A1\", \"A3\", \"A4\", \"A5\"\n" +
                                "     but: item [0]: A1. was \"A1\"\r\n" +
                                "item [1]: A2. was \"A2\""},
                {TEST_LIST, iterableInOrder("A1", "A2", "A3", "A4", "A5", "A6"),
                        "Tested object\n" +
                                "Expected: in following order: \"A1\", \"A2\", \"A3\", \"A4\", \"A5\", \"A6\"\n" +
                                "     but: 5 items instead of 6"},
                {TEST_LIST, iterableInOrder("A1", "A2", "A3", "A4", "A6"),
                        "Tested object\n" +
                                "Expected: in following order: \"A1\", \"A2\", \"A3\", \"A4\", \"A6\"\n" +
                                "     but: item [4]: A5. was \"A5\""},
                {TEST_LIST, iterableOf("A2", "A1", "A3", "A4", "A6"),
                        "Tested object\n" +
                                "Expected: in any order: \"A2\", \"A1\", \"A3\", \"A4\", \"A6\"\n" +
                                "     but: Not present item: \"A6\""},
                {TEST_LIST, iterableIncludesInOrder("A3", "A1", "A5"),
                        "Tested object\n" +
                                "Expected: includes in following order: \"A3\", \"A1\", \"A5\"\n" +
                                "     but: The item ['\"A1\"'] doesn't go after : [A3; index: 2; criteria: '\"A3\"']"},
                {TEST_LIST, iterableIncludesInOrder("A1", "A5", "A6"),
                        "Tested object\n" +
                                "Expected: includes in following order: \"A1\", \"A5\", \"A6\"\n" +
                                "     but: The item ['\"A6\"'] doesn't go after : [A5; index: 4; criteria: '\"A5\"']"},
                {TEST_LIST, iterableIncludes("A1", "A6"), "Tested object\n" +
                        "Expected: includes in any order: \"A1\", \"A6\"\n" +
                        "     but: Not present item: \"A6\""},
                {TEST_LIST, iterableIncludes("A1", "A1", "A1", "A1", "A1", "A1", "A1", "A1"),
                        "Tested object\n" +
                                "Expected: includes in any order: \"A1\", \"A1\", \"A1\", \"A1\", \"A1\", \"A1\", \"A1\", \"A1\"\n" +
                                "     but: 5 items instead of 8"},
                {TEST_LIST, eachOfIterable(containsString("B")), "Tested object\n" +
                        "Expected: each item: a string containing \"B\"\n" +
                        "     but: 0: A1. was \"A1\"\r\n" +
                        "1: A2. was \"A2\"\r\n" +
                        "2: A3. was \"A3\"\r\n" +
                        "3: A4. was \"A4\"\r\n" +
                        "4: A5. was \"A5\""},
                {TEST_LIST, iterableHasItems(greaterThan(5), containsString("A")), "Tested object\n" +
                        "Expected: has item(s): a string containing \"A\". Expected count: a value greater than <5>\n" +
                        "     but: Count: 5. <5> was equal to <5>"},
                {TEST_LIST, iterableHasItems(4, containsString("A")), "Tested object\n" +
                        "Expected: has item(s): a string containing \"A\". Expected count: <4>\n" +
                        "     but: Count: 5. was <5>"},
                {TEST_LIST, iterableHasItem(notOf(containsString("A")), containsString("1")),
                        "Tested object\n" +
                                "Expected: has item(s): not a string containing \"A\", a string containing \"1\". Expected count: a value equal to or greater than <1>\n" +
                                "     but: Not present item: not a string containing \"A\", a string containing \"1\""},
                {TEST_LIST, iterableHasItem(notOf(containsString("A"))), "Tested object\n" +
                        "Expected: has item(s): not a string containing \"A\". Expected count: a value equal to or greater than <1>\n" +
                        "     but: Not present item: not a string containing \"A\""},
                {TEST_LIST, iterableHasItem("A6"), "Tested object\n" +
                        "Expected: has item(s): \"A6\". Expected count: a value equal to or greater than <1>\n" +
                        "     but: Not present item: \"A6\""},
                {TEST_LIST, iterableHasItems(2, "A1"), "Tested object\n" +
                        "Expected: has item(s): \"A1\". Expected count: <2>\n" +
                        "     but: Count: 1. was <1>"},
                {TEST_LIST, iterableHasItems(greaterThanOrEqualTo(2), "A1"),
                        "Tested object\n" +
                                "Expected: has item(s): \"A1\". Expected count: a value equal to or greater than <2>\n" +
                                "     but: Count: 1. <1> was less than <2>"},


                {TEST_MAP, mapInOrder(mapEntry("A2", "B2"),
                        mapEntry("A1", "B1"),
                        mapEntry("A3", "B3"),
                        mapEntry("A4", "B4"),
                        mapEntry("A5", "B5")), "Tested object\n" +
                        "Expected: in following order: Key: \"A2\" Value: \"B2\", Key: \"A1\" Value: \"B1\", Key: \"A3\" Value: \"B3\", Key: \"A4\" Value: \"B4\", Key: \"A5\" Value: \"B5\"\n" +
                        "     but: item [0]: A1=B1. Key: A1. was \"A1\"\r\n" +
                        "Value: B1. was \"B1\"\r\n" +
                        "item [1]: A2=B2. Key: A2. was \"A2\"\r\n" +
                        "Value: B2. was \"B2\""},

                {TEST_MAP, mapInOrder(mapEntry("A1", "B2"),
                        mapEntry("A1", "B2"),
                        mapEntry("A3", "B3"),
                        mapEntry("A4", "B4"),
                        mapEntry("A5", "B5")), "Tested object\n" +
                        "Expected: in following order: Key: \"A1\" Value: \"B2\", Key: \"A1\" Value: \"B2\", Key: \"A3\" Value: \"B3\", Key: \"A4\" Value: \"B4\", Key: \"A5\" Value: \"B5\"\n" +
                        "     but: item [0]: A1=B1. Value: B1. was \"B1\"\r\n" +
                        "item [1]: A2=B2. Key: A2. was \"A2\""},

                {TEST_MAP, mapInOrder(mapEntry("A1", "B1"),
                        mapEntry("A2", "B2"),
                        mapEntry("A3", "B3"),
                        mapEntry("A4", "B4")), "Tested object\n" +
                        "Expected: in following order: Key: \"A1\" Value: \"B1\", Key: \"A2\" Value: \"B2\", Key: \"A3\" Value: \"B3\", Key: \"A4\" Value: \"B4\"\n" +
                        "     but: 5 items instead of 4"},

                {TEST_MAP, mapInOrder(entryKey("A2"),
                        entryKey("A1"),
                        entryKey("A3"),
                        entryKey("A4"),
                        entryKey("A5")), "Tested object\n" +
                        "Expected: in following order: Key: \"A2\" Value: ANYTHING, Key: \"A1\" Value: ANYTHING, Key: \"A3\" Value: ANYTHING, Key: \"A4\" Value: ANYTHING, Key: \"A5\" Value: ANYTHING\n" +
                        "     but: item [0]: A1=B1. Key: A1. was \"A1\"\r\n" +
                        "item [1]: A2=B2. Key: A2. was \"A2\""},

                {TEST_MAP, mapInOrder(entryValue("B2"),
                        entryValue("B1"),
                        entryValue("B3"),
                        entryValue("B4"),
                        entryValue("B5")), "Tested object\n" +
                        "Expected: in following order: Key: ANYTHING Value: \"B2\", Key: ANYTHING Value: \"B1\", Key: ANYTHING Value: \"B3\", Key: ANYTHING Value: \"B4\", Key: ANYTHING Value: \"B5\"\n" +
                        "     but: item [0]: A1=B1. Value: B1. was \"B1\"\r\n" +
                        "item [1]: A2=B2. Value: B2. was \"B2\""},

                {TEST_MAP, mapInOrder(entryValue("B1"),
                        entryValue("B2"),
                        entryValue("B3"),
                        entryValue("B4"),
                        entryValue("B6")),
                        "Tested object\n" +
                                "Expected: in following order: Key: ANYTHING Value: \"B1\", Key: ANYTHING Value: \"B2\", Key: ANYTHING Value: \"B3\", Key: ANYTHING Value: \"B4\", Key: ANYTHING Value: \"B6\"\n" +
                                "     but: item [4]: A5=B5. Value: B5. was \"B5\""},

                {TEST_MAP, mapOf(mapEntry("A2", "B2"),
                        mapEntry("A1", "B1"),
                        mapEntry("A3", "B3"),
                        mapEntry("A6", "B4"),
                        mapEntry("A5", "B5")), "Tested object\n" +
                        "Expected: in any order: Key: \"A2\" Value: \"B2\", Key: \"A1\" Value: \"B1\", Key: \"A3\" Value: \"B3\", Key: \"A6\" Value: \"B4\", Key: \"A5\" Value: \"B5\"\n" +
                        "     but: Not present item: Key: \"A6\" Value: \"B4\""},

                {TEST_MAP, mapOf(entryKey("A2"),
                        entryKey("A1"),
                        entryKey("A3"),
                        entryKey("A6"),
                        entryKey("A5")), "Tested object\n" +
                        "Expected: in any order: Key: \"A2\" Value: ANYTHING, Key: \"A1\" Value: ANYTHING, Key: \"A3\" Value: ANYTHING, Key: \"A6\" Value: ANYTHING, Key: \"A5\" Value: ANYTHING\n" +
                        "     but: Not present item: Key: \"A6\" Value: ANYTHING"},

                {TEST_MAP, mapOf(entryValue("B2"),
                        entryValue("B1"),
                        entryValue("B6"),
                        entryValue("B4"),
                        entryValue("B5")), "Tested object\n" +
                        "Expected: in any order: Key: ANYTHING Value: \"B2\", Key: ANYTHING Value: \"B1\", Key: ANYTHING Value: \"B6\", Key: ANYTHING Value: \"B4\", Key: ANYTHING Value: \"B5\"\n" +
                        "     but: Not present item: Key: ANYTHING Value: \"B6\""},

                {TEST_MAP, mapIncludesInOrder(mapEntry("A6", "B6"),
                        mapEntry("A3", "B3"),
                        mapEntry("A5", "B5")), "Tested object\n" +
                        "Expected: includes in following order: Key: \"A6\" Value: \"B6\", Key: \"A3\" Value: \"B3\", Key: \"A5\" Value: \"B5\"\n" +
                        "     but: Not present item: Key: \"A6\" Value: \"B6\""},

                {TEST_MAP, mapIncludesInOrder(mapEntry("A1", "B1"),
                        mapEntry("A5", "B5"),
                        mapEntry("A3", "B3")), "Tested object\n" +
                        "Expected: includes in following order: Key: \"A1\" Value: \"B1\", Key: \"A5\" Value: \"B5\", Key: \"A3\" Value: \"B3\"\n" +
                        "     but: The item ['Key: \"A3\" Value: \"B3\"'] doesn't go after : [A5=B5; index: 4; criteria: 'Key: \"A5\" Value: \"B5\"']"},


                {TEST_MAP, eachEntry(startsWith("B"), startsWith("A")),
                        "Tested object\n" +
                                "Expected: each entry: Key: a string starting with \"B\" Value: a string starting with \"A\"\n" +
                                "     but: 0: A1=B1. Key: A1. was \"A1\"\r\n" +
                                "Value: B1. was \"B1\"\r\n" +
                                "1: A2=B2. Key: A2. was \"A2\"\r\n" +
                                "Value: B2. was \"B2\"\r\n" +
                                "2: A3=B3. Key: A3. was \"A3\"\r\n" +
                                "Value: B3. was \"B3\"\r\n" +
                                "3: A4=B4. Key: A4. was \"A4\"\r\n" +
                                "Value: B4. was \"B4\"\r\n" +
                                "4: A5=B5. Key: A5. was \"A5\"\r\n" +
                                "Value: B5. was \"B5\""},

                {TEST_MAP, eachEntryKey(startsWith("B")), "Tested object\n" +
                        "Expected: each entry: Key: a string starting with \"B\" Value: ANYTHING\n" +
                        "     but: 0: A1=B1. Key: A1. was \"A1\"\r\n" +
                        "1: A2=B2. Key: A2. was \"A2\"\r\n" +
                        "2: A3=B3. Key: A3. was \"A3\"\r\n" +
                        "3: A4=B4. Key: A4. was \"A4\"\r\n" +
                        "4: A5=B5. Key: A5. was \"A5\""},

                {TEST_MAP, eachEntryValue(startsWith("A")),
                        "Tested object\n" +
                                "Expected: each entry: Key: ANYTHING Value: a string starting with \"A\"\n" +
                                "     but: 0: A1=B1. Value: B1. was \"B1\"\r\n" +
                                "1: A2=B2. Value: B2. was \"B2\"\r\n" +
                                "2: A3=B3. Value: B3. was \"B3\"\r\n" +
                                "3: A4=B4. Value: B4. was \"B4\"\r\n" +
                                "4: A5=B5. Value: B5. was \"B5\""},

                {TEST_MAP, mapHasEntries(greaterThan(5), startsWith("A"), startsWith("B")),
                        "Tested object\n" +
                                "Expected: has entry(es): Key: a string starting with \"A\" Value: a string starting with \"B\". Expected count: a value greater than <5>\n" +
                                "     but: Count: 5. <5> was equal to <5>"},

                {TEST_MAP, mapHasEntries(6, startsWith("A"), startsWith("B")),
                        "Tested object\n" +
                                "Expected: has entry(es): Key: a string starting with \"A\" Value: a string starting with \"B\". Expected count: <6>\n" +
                                "     but: Count: 5. was <5>"},


                {TEST_MAP, mapHasEntry(startsWith("B"), startsWith("A")), "Tested object\n" +
                        "Expected: has entry(es): Key: a string starting with \"B\" Value: a string starting with \"A\". Expected count: a value equal to or greater than <1>\n" +
                        "     but: Not present item: Key: a string starting with \"B\" Value: a string starting with \"A\""},

                {TEST_MAP, mapHasEntry("A6", "B6"),
                        "Tested object\n" +
                                "Expected: has entry(es): Key: \"A6\" Value: \"B6\". Expected count: <1>\n" +
                                "     but: Not present item: Key: \"A6\" Value: \"B6\""},

                {TEST_MAP, mapHasEntry("A1", "B6"), "Tested object\n" +
                        "Expected: has entry(es): Key: \"A1\" Value: \"B6\". Expected count: <1>\n" +
                        "     but: Not present item: Key: \"A1\" Value: \"B6\""},

                {TEST_MAP, mapHasEntry("A6", "B1"),
                        "Tested object\n" +
                                "Expected: has entry(es): Key: \"A6\" Value: \"B1\". Expected count: <1>\n" +
                                "     but: Not present item: Key: \"A6\" Value: \"B1\""},
        };
    }

    @Test(dataProvider = "data1")
    public void test1(Object object, Matcher<Object> matcher) {
        assertThat("Tested object", object, matcher);
    }

    @Test(dataProvider = "data2")
    public void test2(Object o, Matcher<Object> matcher, String errorText) {
        try {
            assertThat("Tested object", o, matcher);
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(errorText));
            return;
        }

        fail("Exception was expected");
    }
}
