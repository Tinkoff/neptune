package ru.tinkoff.qa.neptune.core.api.hamcrest;

import org.hamcrest.Matcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.not.NotMatcher.notOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.arrayInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.arrayOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher.eachOfArray;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher.arrayIncludes;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher.arrayIncludesInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.arrayHasItem;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.arrayHasItems;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.text.StringContainsWithSeparator.subStringSeparatedBy;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.text.StringContainsWithSeparator.subStringsSeparatedBy;

public class SeparatedStringMatcherTest {

    private static final String TEST_STRING = "A1,A2,A3,A4,A5";

    @DataProvider
    public static Object[][] data1() {
        return new Object[][]{
                {subStringsSeparatedBy(",", "A1", "A2", "A3", "A4", "A5")},
                {subStringSeparatedBy(",", "A1")},
                {subStringsSeparatedBy(",", arrayOf("A1", "A3", "A2", "A4", "A5"))},
                {subStringsSeparatedBy(",", arrayInOrder(containsString("1"),
                        containsString("2"),
                        containsString("3"),
                        containsString("4"),
                        containsString("5")))},
                {subStringsSeparatedBy(",", arrayIncludes("A1", "A5"))},
                {subStringsSeparatedBy(",", arrayIncludesInOrder("A1", "A3", "A5"))},
                {subStringsSeparatedBy(",", eachOfArray(containsString("A")))},
                {subStringsSeparatedBy(",", arrayHasItems(greaterThan(4), containsString("A")))},
                {subStringsSeparatedBy(",", arrayHasItems(5, containsString("A")))},
                {subStringsSeparatedBy(",", arrayHasItem(containsString("A"), containsString("1")))},
                {subStringsSeparatedBy(",", arrayHasItem(notOf(containsString("B"))))}
        };
    }

    @DataProvider
    public static Object[][] data2() {
        return new Object[][]{
                {subStringsSeparatedBy(",", "A1", "A2", "A3", "A4", "A6"),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: in following order: \"A1\", \"A2\", \"A3\", \"A4\", \"A6\"\n" +
                                "     but: Index: 4. Item: A5. was \"A5\""},

                {subStringSeparatedBy(",", "A6"),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: has item(s): \"A6\". Expected count: a value equal to or greater than <1>\n" +
                                "     but: No such item: \"A6\""},

                {subStringsSeparatedBy(",", arrayOf("A1", "A3", "A2", "A4")),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: in any order: \"A1\", \"A3\", \"A2\", \"A4\"\n" +
                                "     but: 5 items instead of 4"},

                {subStringsSeparatedBy(",", arrayOf("A1", "A3", "A2", "A4", "A6")),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: in any order: \"A1\", \"A3\", \"A2\", \"A4\", \"A6\"\n" +
                                "     but: No such item: \"A6\""},

                {subStringsSeparatedBy(",", arrayInOrder(containsString("1"),
                        containsString("2"),
                        containsString("3"),
                        containsString("4"),
                        containsString("6"))),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: in following order: a string containing \"1\", a string containing \"2\", a string containing \"3\", a string containing \"4\", a string containing \"6\"\n" +
                                "     but: Index: 4. Item: A5. was \"A5\""},

                {subStringsSeparatedBy(",", arrayIncludes("A1", "A6")),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: includes in any order: \"A1\", \"A6\"\n" +
                                "     but: No such item: \"A6\""},

                {subStringsSeparatedBy(",", arrayIncludesInOrder("A6", "A2", "A3")),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: includes in following order: \"A6\", \"A2\", \"A3\"\n" +
                                "     but: No such item: \"A6\""},

                {subStringsSeparatedBy(",", arrayIncludesInOrder("A1", "A5", "A3")),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: includes in following order: \"A1\", \"A5\", \"A3\"\n" +
                                "     but: The item ['\"A3\"'] doesn't go after : [A5; index: 4; criteria: '\"A5\"']"},

                {subStringsSeparatedBy(",", arrayIncludesInOrder("A1", "A3", "A3")),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: includes in following order: \"A1\", \"A3\", \"A3\"\n" +
                                "     but: The item ['\"A3\"'] doesn't go after : [A3; index: 2; criteria: '\"A3\"']"},

                {subStringsSeparatedBy(",", eachOfArray(containsString("B"))),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: each item: a string containing \"B\"\n" +
                                "     but: 0: A1. was \"A1\"\r\n" +
                                "1: A2. was \"A2\"\r\n" +
                                "2: A3. was \"A3\"\r\n" +
                                "3: A4. was \"A4\"\r\n" +
                                "4: A5. was \"A5\""},

                {subStringsSeparatedBy(",", arrayHasItems(greaterThan(6), containsString("A"))),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: has item(s): a string containing \"A\". Expected count: a value greater than <6>\n" +
                                "     but: Has 5 such items"},

                {subStringsSeparatedBy(",", arrayHasItems(6, containsString("A"))),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: has item(s): a string containing \"A\". Expected count: <6>\n" +
                                "     but: Has 5 such items"},

                {subStringsSeparatedBy(",", arrayHasItem(containsString("A"), containsString("6"))),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: has item(s): a string containing \"A\", " +
                                "a string containing \"6\". Expected count: a value equal to or greater than <1>\n" +
                                "     but: No such item: a string containing \"A\", a string containing \"6\""},
        };
    }

    @Test(dataProvider = "data1")
    public void test1(Matcher<String> m) {
        assertThat("Checked string", TEST_STRING, m);
    }

    @Test(dataProvider = "data2")
    public void test2(Matcher<String> m, String message) {
        try {
            assertThat("Checked string", TEST_STRING, m);
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(message));
            return;
        }

        fail("Exception was expected");
    }
}
