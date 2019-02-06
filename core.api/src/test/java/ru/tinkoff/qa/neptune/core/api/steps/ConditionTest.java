package ru.tinkoff.qa.neptune.core.api.steps;

import org.testng.annotations.Test;

import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.steps.ConditionConcatenation.*;
import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.condition;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

public class ConditionTest {

    private static Predicate<Number> IS_POSITIVE = number -> number.intValue() > 0;
    private static Predicate<Number> IS_NOT_FRACTION = number -> (number.floatValue() - number.intValue()) == 0;
    private static Predicate<Number> GREATER_THAN_TEN = number -> number.intValue() > 10;
    private static Predicate<Number> LOWER_THAN_TWENTY = number -> number.intValue() < 20;
    private static Predicate<Number> GREATER_THAN_FIFTEEN = number -> number.intValue() > 15;


    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp =
            "Description should not be empty")
    public void negativeTestOfEmptyDescription() {
        condition("", IS_POSITIVE);
        fail("The exception throwing was expected");
    }

    @Test
    public void checkPredicateDescriptionAnd() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Predicate<Number> describedIsNotFraction = condition("Number should not have decimal value", IS_NOT_FRACTION);

        assertThat("String value of the predicate",
                describedIsPositive.and(describedIsNotFraction).toString(),
                is("Number should be positive, Number should not have decimal value"));
    }

    @Test
    public void checkPredicateNotDescribedAnd() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Predicate<Number> describedIsNotFraction = IS_NOT_FRACTION;

        assertThat("String value of the predicate",
                describedIsPositive.and(describedIsNotFraction).toString(),
                is("Number should be positive, <not described condition>"));
    }

    @Test
    public void checkPredicateDescriptionAndReportTurnedOff() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Predicate<Number> describedIsNotFraction = condition("Number should not have decimal value", IS_NOT_FRACTION);
        ((TurnsRetortingOff<?>) describedIsNotFraction).turnReportingOff();

        assertThat("String value of the predicate",
                describedIsPositive.and(describedIsNotFraction).toString(),
                is("Number should be positive"));
    }

    @Test
    public void checkPredicateDescriptionOr() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Predicate<Number> describedIsNotFraction = condition("Number should not have decimal value", IS_NOT_FRACTION);

        assertThat("String value of the predicate",
                describedIsPositive.or(describedIsNotFraction).toString(),
                is("(Number should be positive) or (Number should not have decimal value)"));
    }

    @Test
    public void checkPredicateNotDescribedOr() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Predicate<Number> describedIsNotFraction = IS_NOT_FRACTION;

        assertThat("String value of the predicate",
                describedIsPositive.or(describedIsNotFraction).toString(),
                is("(Number should be positive) or (<not described condition>)"));
    }

    @Test
    public void checkPredicateDescriptionOrTurnedOff() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Predicate<Number> describedIsNotFraction = condition("Number should not have decimal value", IS_NOT_FRACTION);
        ((TurnsRetortingOff<?>) describedIsNotFraction).turnReportingOff();

        assertThat("String value of the predicate",
                describedIsPositive.or(describedIsNotFraction).toString(),
                is("Number should be positive"));
    }

    @Test
    public void checkPredicateDescriptionXor() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Predicate<Number> describedIsNotFraction = condition("Number should not have decimal value", IS_NOT_FRACTION);

        assertThat("String value of the predicate",
                ((Condition<Number>) describedIsPositive).xor(describedIsNotFraction).toString(),
                is("(Number should be positive) xor (Number should not have decimal value)"));
    }

    @Test
    public void checkPredicateNotDescribedXor() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Predicate<Number> describedIsNotFraction = IS_NOT_FRACTION;

        assertThat("String value of the predicate",
                ((Condition<Number>) describedIsPositive).xor(describedIsNotFraction).toString(),
                is("(Number should be positive) xor (<not described condition>)"));
    }

    @Test
    public void checkPredicateDescriptionXorTurnedOff() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Predicate<Number> describedIsNotFraction = condition("Number should not have decimal value", IS_NOT_FRACTION);
        ((TurnsRetortingOff<?>) describedIsNotFraction).turnReportingOff();

        assertThat("String value of the predicate",
                ((Condition<Number>) describedIsPositive).xor(describedIsNotFraction).toString(),
                is("Number should be positive"));
    }

    @Test
    public void checkPredicateDescriptionNot() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        assertThat("String value of the predicate",
                describedIsPositive.negate().toString(),
                is("not [Number should be positive]"));
    }

    @Test
    public void checkPredicateDescriptionComplex() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Predicate<Number> describedIsNotFraction = condition("Number should not have decimal value", IS_NOT_FRACTION);

        Predicate<Number> describedGreaterThan10 = condition("Number should be greater than 10", GREATER_THAN_TEN);
        Predicate<Number> describedGreaterThan15 = condition("Number should be greater than 15", GREATER_THAN_FIFTEEN);
        Predicate<Number> describedLowerThan20 = condition("Number should be lower than 20", LOWER_THAN_TWENTY);

        assertThat("String value of the predicate",
                describedIsPositive.and(describedIsNotFraction)
                        .and(describedGreaterThan10
                        .or(describedGreaterThan15))
                        .or(describedLowerThan20.negate()).toString(),
                is("(Number should be positive, Number should not have decimal value, " +
                        "(Number should be greater than 10) or (Number should be greater than 15)) " +
                        "or (not [Number should be lower than 20])"));
    }

    @Test
    public void checkPredicateConcatenationAndBothDescribed() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Predicate<Number> describedIsNotFraction = condition("Number should not have decimal value", IS_NOT_FRACTION);

        assertThat("String value of the predicate",
                AND.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("Number should be positive, Number should not have decimal value"));
    }

    @Test
    public void checkPredicateConcatenationAndFirstDescribed() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Predicate<Number> describedIsNotFraction = IS_NOT_FRACTION;

        assertThat("String value of the predicate",
                AND.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("Number should be positive, <not described condition>"));
    }

    @Test
    public void checkPredicateConcatenationAndSecondDescribed() {
        Predicate<Number> describedIsPositive = IS_POSITIVE;
        Predicate<Number> describedIsNotFraction = condition("Number should not have decimal value", IS_NOT_FRACTION);

        assertThat("String value of the predicate",
                AND.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("Number should not have decimal value, <not described condition>"));
    }

    @Test
    public void checkPredicateConcatenationAndBothLoggable() {
        Predicate<Number> describedIsPositive = new Predicate<>() {
            @Override
            public boolean test(Number number) {
                return IS_POSITIVE.test(number);
            }

            @Override
            public String toString() {
                return "Number should be positive";
            }
        };

        Predicate<Number> describedIsNotFraction = new Predicate<>() {
            @Override
            public boolean test(Number number) {
                return IS_NOT_FRACTION.test(number);
            }

            @Override
            public String toString() {
                return "Number should not have decimal value";
            }
        };

        assertThat("String value of the predicate",
                AND.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("Number should be positive, Number should not have decimal value"));
    }

    @Test
    public void checkPredicateConcatenationAndFirstLoggable() {
        Predicate<Number> describedIsPositive = new Predicate<>() {
            @Override
            public boolean test(Number number) {
                return IS_POSITIVE.test(number);
            }

            @Override
            public String toString() {
                return "Number should be positive";
            }
        };

        Predicate<Number> describedIsNotFraction = IS_NOT_FRACTION;
        assertThat("String value of the predicate",
                AND.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("Number should be positive, <not described condition>"));
    }

    @Test
    public void checkPredicateConcatenationAndSecondLoggable() {
        Predicate<Number> describedIsPositive = IS_POSITIVE;

        Predicate<Number> describedIsNotFraction = new Predicate<>() {
            @Override
            public boolean test(Number number) {
                return IS_NOT_FRACTION.test(number);
            }

            @Override
            public String toString() {
                return "Number should not have decimal value";
            }
        };

        assertThat("String value of the predicate",
                AND.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("Number should not have decimal value, <not described condition>"));
    }

    @Test
    public void checkPredicateConcatenationAndNotDescribed() {
        Predicate<Number> describedIsPositive = IS_POSITIVE;
        Predicate<Number> describedIsNotFraction = IS_NOT_FRACTION;

        assertThat("String value of the predicate",
                AND.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("<not described condition>, <not described condition>"));
    }

    @Test
    public void checkPredicateConcatenationAndFirstReportingTurnedOff() {
        Condition.DescribedCondition<Number> describedIsPositive = (Condition.DescribedCondition<Number>)
                condition("Number should be positive", IS_POSITIVE);
        describedIsPositive.turnReportingOff();

        Predicate<Number> describedIsNotFraction = condition("Number should not have decimal value", IS_NOT_FRACTION);

        assertThat("String value of the predicate",
                AND.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("Number should not have decimal value"));
    }

    @Test
    public void checkPredicateConcatenationAndSecondReportingTurnedOff() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Condition.DescribedCondition<Number> describedIsNotFraction = (Condition.DescribedCondition<Number>)
                condition("Number should not have decimal value", IS_NOT_FRACTION);
        describedIsNotFraction.turnReportingOff();

        assertThat("String value of the predicate",
                AND.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("Number should be positive"));
    }

    @Test
    public void checkPredicateConcatenationAndBothReportingTurnedOff() {
        Condition.DescribedCondition<Number> describedIsPositive = (Condition.DescribedCondition<Number>)
                condition("Number should be positive", IS_POSITIVE);
        Condition.DescribedCondition<Number> describedIsNotFraction = (Condition.DescribedCondition<Number>)
                condition("Number should not have decimal value", IS_NOT_FRACTION);

        describedIsPositive.turnReportingOff();
        describedIsNotFraction.turnReportingOff();

        assertThat("Is condition loggable",
                isLoggable(AND.concat(describedIsPositive, describedIsNotFraction)),
                is(false));
    }


    @Test
    public void checkPredicateConcatenationOrBothDescribed() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Predicate<Number> describedIsNotFraction = condition("Number should not have decimal value", IS_NOT_FRACTION);

        assertThat("String value of the predicate",
                OR.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("(Number should be positive) or (Number should not have decimal value)"));
    }

    @Test
    public void checkPredicateConcatenationOrFirstDescribed() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Predicate<Number> describedIsNotFraction = IS_NOT_FRACTION;

        assertThat("String value of the predicate",
                OR.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("(Number should be positive) or (<not described condition>)"));
    }

    @Test
    public void checkPredicateConcatenationOrSecondDescribed() {
        Predicate<Number> describedIsPositive = IS_POSITIVE;
        Predicate<Number> describedIsNotFraction = condition("Number should not have decimal value", IS_NOT_FRACTION);

        assertThat("String value of the predicate",
                OR.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("(Number should not have decimal value) or (<not described condition>)"));
    }

    @Test
    public void checkPredicateConcatenationOrBothLoggable() {
        Predicate<Number> describedIsPositive = new Predicate<>() {
            @Override
            public boolean test(Number number) {
                return IS_POSITIVE.test(number);
            }

            @Override
            public String toString() {
                return "Number should be positive";
            }
        };

        Predicate<Number> describedIsNotFraction = new Predicate<>() {
            @Override
            public boolean test(Number number) {
                return IS_NOT_FRACTION.test(number);
            }

            @Override
            public String toString() {
                return "Number should not have decimal value";
            }
        };

        assertThat("String value of the predicate",
                OR.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("(Number should be positive) or (Number should not have decimal value)"));
    }

    @Test
    public void checkPredicateConcatenationOrFirstLoggable() {
        Predicate<Number> describedIsPositive = new Predicate<>() {
            @Override
            public boolean test(Number number) {
                return IS_POSITIVE.test(number);
            }

            @Override
            public String toString() {
                return "Number should be positive";
            }
        };

        Predicate<Number> describedIsNotFraction = IS_NOT_FRACTION;
        assertThat("String value of the predicate",
                OR.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("(Number should be positive) or (<not described condition>)"));
    }

    @Test
    public void checkPredicateConcatenationOrSecondLoggable() {
        Predicate<Number> describedIsPositive = IS_POSITIVE;

        Predicate<Number> describedIsNotFraction = new Predicate<>() {
            @Override
            public boolean test(Number number) {
                return IS_NOT_FRACTION.test(number);
            }

            @Override
            public String toString() {
                return "Number should not have decimal value";
            }
        };

        assertThat("String value of the predicate",
                OR.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("(Number should not have decimal value) or (<not described condition>)"));
    }

    @Test
    public void checkPredicateConcatenationOrNotDescribed() {
        Predicate<Number> describedIsPositive = IS_POSITIVE;
        Predicate<Number> describedIsNotFraction = IS_NOT_FRACTION;

        assertThat("String value of the predicate",
                OR.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("(<not described condition>) or (<not described condition>)"));
    }


    @Test
    public void checkPredicateConcatenationOrFirstReportingTurnedOff() {
        Condition.DescribedCondition<Number> describedIsPositive = (Condition.DescribedCondition<Number>)
                condition("Number should be positive", IS_POSITIVE);
        describedIsPositive.turnReportingOff();

        Predicate<Number> describedIsNotFraction = condition("Number should not have decimal value", IS_NOT_FRACTION);

        assertThat("String value of the predicate",
                OR.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("Number should not have decimal value"));
    }

    @Test
    public void checkPredicateConcatenationOrSecondReportingTurnedOff() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Condition.DescribedCondition<Number> describedIsNotFraction = (Condition.DescribedCondition<Number>)
                condition("Number should not have decimal value", IS_NOT_FRACTION);
        describedIsNotFraction.turnReportingOff();

        assertThat("String value of the predicate",
                OR.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("Number should be positive"));
    }

    @Test
    public void checkPredicateConcatenationOrBothReportingTurnedOff() {
        Condition.DescribedCondition<Number> describedIsPositive = (Condition.DescribedCondition<Number>)
                condition("Number should be positive", IS_POSITIVE);
        Condition.DescribedCondition<Number> describedIsNotFraction = (Condition.DescribedCondition<Number>)
                condition("Number should not have decimal value", IS_NOT_FRACTION);

        describedIsPositive.turnReportingOff();
        describedIsNotFraction.turnReportingOff();

        assertThat("Is condition loggable",
                isLoggable(OR.concat(describedIsPositive, describedIsNotFraction)),
                is(false));
    }

    @Test
    public void checkPredicateConcatenationXorBothDescribed() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Predicate<Number> describedIsNotFraction = condition("Number should not have decimal value", IS_NOT_FRACTION);

        assertThat("String value of the predicate",
                XOR.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("(Number should be positive) xor (Number should not have decimal value)"));
    }

    @Test
    public void checkPredicateConcatenationXorFirstDescribed() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Predicate<Number> describedIsNotFraction = IS_NOT_FRACTION;

        assertThat("String value of the predicate",
                XOR.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("(Number should be positive) xor (<not described condition>)"));
    }

    @Test
    public void checkPredicateConcatenationXorSecondDescribed() {
        Predicate<Number> describedIsPositive = IS_POSITIVE;
        Predicate<Number> describedIsNotFraction = condition("Number should not have decimal value", IS_NOT_FRACTION);

        assertThat("String value of the predicate",
                XOR.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("(Number should not have decimal value) xor (<not described condition>)"));
    }

    @Test
    public void checkPredicateConcatenationXorBothLoggable() {
        Predicate<Number> describedIsPositive = new Predicate<>() {
            @Override
            public boolean test(Number number) {
                return IS_POSITIVE.test(number);
            }

            @Override
            public String toString() {
                return "Number should be positive";
            }
        };

        Predicate<Number> describedIsNotFraction = new Predicate<>() {
            @Override
            public boolean test(Number number) {
                return IS_NOT_FRACTION.test(number);
            }

            @Override
            public String toString() {
                return "Number should not have decimal value";
            }
        };

        assertThat("String value of the predicate",
                XOR.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("(Number should be positive) xor (Number should not have decimal value)"));
    }

    @Test
    public void checkPredicateConcatenationXorFirstLoggable() {
        Predicate<Number> describedIsPositive = new Predicate<>() {
            @Override
            public boolean test(Number number) {
                return IS_POSITIVE.test(number);
            }

            @Override
            public String toString() {
                return "Number should be positive";
            }
        };

        Predicate<Number> describedIsNotFraction = IS_NOT_FRACTION;
        assertThat("String value of the predicate",
                XOR.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("(Number should be positive) xor (<not described condition>)"));
    }

    @Test
    public void checkPredicateConcatenationXorSecondLoggable() {
        Predicate<Number> describedIsPositive = IS_POSITIVE;

        Predicate<Number> describedIsNotFraction = new Predicate<>() {
            @Override
            public boolean test(Number number) {
                return IS_NOT_FRACTION.test(number);
            }

            @Override
            public String toString() {
                return "Number should not have decimal value";
            }
        };

        assertThat("String value of the predicate",
                XOR.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("(Number should not have decimal value) xor (<not described condition>)"));
    }

    @Test
    public void checkPredicateConcatenationXorNotDescribed() {
        Predicate<Number> describedIsPositive = IS_POSITIVE;
        Predicate<Number> describedIsNotFraction = IS_NOT_FRACTION;

        assertThat("String value of the predicate",
                XOR.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("(<not described condition>) xor (<not described condition>)"));
    }

    @Test
    public void checkPredicateConcatenationXorFirstReportingTurnedOff() {
        Condition.DescribedCondition<Number> describedIsPositive = (Condition.DescribedCondition<Number>)
                condition("Number should be positive", IS_POSITIVE);
        describedIsPositive.turnReportingOff();

        Predicate<Number> describedIsNotFraction = condition("Number should not have decimal value", IS_NOT_FRACTION);

        assertThat("String value of the predicate",
                XOR.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("Number should not have decimal value"));
    }

    @Test
    public void checkPredicateConcatenationXorSecondReportingTurnedOff() {
        Predicate<Number> describedIsPositive = condition("Number should be positive", IS_POSITIVE);
        Condition.DescribedCondition<Number> describedIsNotFraction = (Condition.DescribedCondition<Number>)
                condition("Number should not have decimal value", IS_NOT_FRACTION);
        describedIsNotFraction.turnReportingOff();

        assertThat("String value of the predicate",
                XOR.concat(describedIsPositive, describedIsNotFraction).toString(),
                is("Number should be positive"));
    }

    @Test
    public void checkPredicateConcatenationXorBothReportingTurnedOff() {
        Condition.DescribedCondition<Number> describedIsPositive = (Condition.DescribedCondition<Number>)
                condition("Number should be positive", IS_POSITIVE);
        Condition.DescribedCondition<Number> describedIsNotFraction = (Condition.DescribedCondition<Number>)
                condition("Number should not have decimal value", IS_NOT_FRACTION);

        describedIsPositive.turnReportingOff();
        describedIsNotFraction.turnReportingOff();

        assertThat("Is condition loggable",
                isLoggable(XOR.concat(describedIsPositive, describedIsNotFraction)),
                is(false));
    }
}
