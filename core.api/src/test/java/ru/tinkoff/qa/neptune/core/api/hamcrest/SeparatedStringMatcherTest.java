package ru.tinkoff.qa.neptune.core.api.hamcrest;

import org.hamcrest.Matcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.hamcrest.text.StringContainsWithSeparator;

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
import static ru.tinkoff.qa.neptune.core.api.hamcrest.text.StringContainsWithSeparator.withSeparator;

public class SeparatedStringMatcherTest {

    private static final String TEST_STRING = "A1,A2,A3,A4,A5";

    @DataProvider
    public static Object[][] data1() {
        return new Object[][]{
                {withSeparator(",", "A1", "A2", "A3", "A4", "A5")},
                {StringContainsWithSeparator.withSeparator(",", "A1")},
                {StringContainsWithSeparator.withSeparator(",", arrayOf("A1", "A3", "A2", "A4", "A5"))},
                {StringContainsWithSeparator.withSeparator(",", arrayInOrder(containsString("1"),
                        containsString("2"),
                        containsString("3"),
                        containsString("4"),
                        containsString("5")))},
                {StringContainsWithSeparator.withSeparator(",", arrayIncludes("A1", "A5"))},
                {StringContainsWithSeparator.withSeparator(",", arrayIncludesInOrder("A1", "A3", "A5"))},
                {StringContainsWithSeparator.withSeparator(",", eachOfArray(containsString("A")))},
                {StringContainsWithSeparator.withSeparator(",", arrayHasItems(greaterThan(4), containsString("A")))},
                {StringContainsWithSeparator.withSeparator(",", arrayHasItems(5, containsString("A")))},
                {StringContainsWithSeparator.withSeparator(",", arrayHasItem(containsString("A"), containsString("1")))},
                {StringContainsWithSeparator.withSeparator(",", arrayHasItem(notOf(containsString("B"))))}
        };
    }

    @DataProvider
    public static Object[][] data2() {
        return new Object[][]{
                {withSeparator(",", "A1", "A2", "A3", "A4", "A6"),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: in following order: \"A1\", \"A2\", \"A3\", \"A4\", \"A6\"\n" +
                                "     but: item [4]: A5. was \"A5\""},

                {StringContainsWithSeparator.withSeparator(",", "A6"),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: has item(s): \"A6\". Expected count: a value equal to or greater than <1>\n" +
                                "     but: Not present item: \"A6\""},

                {StringContainsWithSeparator.withSeparator(",", arrayOf("A1", "A3", "A2", "A4")),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: in any order: \"A1\", \"A3\", \"A2\", \"A4\"\n" +
                                "     but: 5 items instead of 4"},

                {StringContainsWithSeparator.withSeparator(",", arrayOf("A1", "A3", "A2", "A4", "A6")),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: in any order: \"A1\", \"A3\", \"A2\", \"A4\", \"A6\"\n" +
                                "     but: Not present item: \"A6\""},

                {StringContainsWithSeparator.withSeparator(",", arrayInOrder(containsString("1"),
                        containsString("2"),
                        containsString("3"),
                        containsString("4"),
                        containsString("6"))),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: in following order: a string containing \"1\", a string containing \"2\", a string containing \"3\", a string containing \"4\", a string containing \"6\"\n" +
                                "     but: item [4]: A5. was \"A5\""},

                {StringContainsWithSeparator.withSeparator(",", arrayIncludes("A1", "A6")),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: includes in any order: \"A1\", \"A6\"\n" +
                                "     but: Not present item: \"A6\""},

                {StringContainsWithSeparator.withSeparator(",", arrayIncludesInOrder("A6", "A2", "A3")),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: includes in following order: \"A6\", \"A2\", \"A3\"\n" +
                                "     but: Not present item: \"A6\""},

                {StringContainsWithSeparator.withSeparator(",", arrayIncludesInOrder("A1", "A5", "A3")),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: includes in following order: \"A1\", \"A5\", \"A3\"\n" +
                                "     but: The item ['\"A3\"'] doesn't go after : [A5; index: 4; criteria: '\"A5\"']"},

                {StringContainsWithSeparator.withSeparator(",", arrayIncludesInOrder("A1", "A3", "A3")),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: includes in following order: \"A1\", \"A3\", \"A3\"\n" +
                                "     but: The item ['\"A3\"'] doesn't go after : [A3; index: 2; criteria: '\"A3\"']"},

                {StringContainsWithSeparator.withSeparator(",", eachOfArray(containsString("B"))),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: each item: a string containing \"B\"\n" +
                                "     but: 0: A1. was \"A1\"\r\n" +
                                "1: A2. was \"A2\"\r\n" +
                                "2: A3. was \"A3\"\r\n" +
                                "3: A4. was \"A4\"\r\n" +
                                "4: A5. was \"A5\""},

                {StringContainsWithSeparator.withSeparator(",", arrayHasItems(greaterThan(6), containsString("A"))),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: has item(s): a string containing \"A\". Expected count: a value greater than <6>\n" +
                                "     but: Count: 5. <5> was less than <6>"},

                {StringContainsWithSeparator.withSeparator(",", arrayHasItems(6, containsString("A"))),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: has item(s): a string containing \"A\". Expected count: <6>\n" +
                                "     but: Count: 5. was <5>"},

                {StringContainsWithSeparator.withSeparator(",", arrayHasItem(containsString("A"), containsString("6"))),
                        "Checked string\n" +
                                "Expected: string has substring(s) separated by <','>: has item(s): a string containing \"A\", " +
                                "a string containing \"6\". Expected count: a value equal to or greater than <1>\n" +
                                "     but: Not present item: a string containing \"A\", a string containing \"6\""},
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
