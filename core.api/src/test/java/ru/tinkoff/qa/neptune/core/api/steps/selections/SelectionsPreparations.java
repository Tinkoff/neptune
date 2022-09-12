package ru.tinkoff.qa.neptune.core.api.steps.selections;

import org.testng.annotations.DataProvider;

import java.util.List;

import static ru.tinkoff.qa.neptune.core.api.steps.selections.ItemsCountCondition.*;

public class SelectionsPreparations {

    static final List<Object> OBJECT_LIST = List.of(1,
        2,
        true,
        false,
        "ABCD",
        "EFG",
        List.of(2,
            true,
            false,
            "ABCD",
            "EFG"),
        5.2);
    static final Object[] OBJECT_ARRAY = OBJECT_LIST.toArray(new Object[]{});

    @DataProvider
    public static Object[][] positiveSizeConditions() {
        return new Object[][]{
            {isEqual(8)},
            {isGreater(1).andLesser(9)},
            {isLesser(9).andGreater(1)},
            {isGreater(1).andLesserOrEqual(9)},
            {isLesser(9).andGreaterOrEqual(1)},
            {isGreaterOrEqual(1).andLesser(9)},
            {isLesserOrEqual(9).andGreater(1)},
            {isGreaterOrEqual(1).andLesserOrEqual(9)},
            {isLesserOrEqual(9).andGreaterOrEqual(1)}
        };
    }

    @DataProvider
    public static Object[][] negativeSizeConditions() {
        return new Object[][]{
            {isEqual(10),
                "It is not possible to select resulted items because:\r\n" +
                    "Count [8] of got items doesnt match 'equal 10'"},

            {isGreater(15).andLesser(20),
                "It is not possible to select resulted items because:\r\n" +
                    "Count [8] of got items doesnt match 'Greater than 15; " +
                    "Lesser than 20'"},

            {isGreaterOrEqual(15).andLesserOrEqual(20),
                "It is not possible to select resulted items because:\r\n" +
                    "Count [8] of got items doesnt match 'Greater than or equal 15; " +
                    "Lesser than or equal 20'"},

            {isGreater(15).andLesserOrEqual(20),
                "It is not possible to select resulted items because:\r\n" +
                    "Count [8] of got items doesnt match 'Greater than 15; " +
                    "Lesser than or equal 20'"},

            {isGreaterOrEqual(15).andLesser(20),
                "It is not possible to select resulted items because:\r\n" +
                    "Count [8] of got items doesnt match 'Greater than or equal 15; " +
                    "Lesser than 20'"},

            {isGreater(15),
                "It is not possible to select resulted items because:\r\n" +
                    "Count [8] of got items doesnt match 'Greater than 15'"},

            {isGreaterOrEqual(15),
                "It is not possible to select resulted items because:\r\n" +
                    "Count [8] of got items doesnt match 'Greater than or equal 15'"},

            {isLesser(6).andGreater(2),
                "It is not possible to select resulted items because:\r\n" +
                    "Count [8] of got items doesnt match 'Greater than 2; " +
                    "Lesser than 6'"},

            {isLesserOrEqual(6).andGreaterOrEqual(2),
                "It is not possible to select resulted items because:\r\n" +
                    "Count [8] of got items doesnt match 'Greater than or equal 2; " +
                    "Lesser than or equal 6'"},

            {isLesser(6).andGreaterOrEqual(2),
                "It is not possible to select resulted items because:\r\n" +
                    "Count [8] of got items doesnt match 'Greater than or equal 2; " +
                    "Lesser than 6'"},

            {isLesserOrEqual(6).andGreater(2),
                "It is not possible to select resulted items because:\r\n" +
                    "Count [8] of got items doesnt match 'Greater than 2; " +
                    "Lesser than or equal 6'"},

            {isLesser(6),
                "It is not possible to select resulted items because:\r\n" +
                    "Count [8] of got items doesnt match 'Lesser than 6'"},

            {isLesserOrEqual(6),
                "It is not possible to select resulted items because:\r\n" +
                    "Count [8] of got items doesnt match 'Lesser than or equal 6'"},
        };
    }
}
