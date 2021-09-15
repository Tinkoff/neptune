package ru.tinkoff.qa.neptune.jupiter.integration;

import ru.tinkoff.qa.neptune.jupiter.integration.properties.RefreshEachTimeBefore;

import static java.util.Arrays.asList;
import static java.util.List.of;
import static ru.tinkoff.qa.neptune.jupiter.integration.properties.RefreshEachTimeBefore.*;

public class MultiThreadIntegrationTest extends Junit5IntegrationTest {

    public MultiThreadIntegrationTest() {
        super(Junit5ParalleledTest.class,
                new Object[][]{
                        {null, 6},
                        {of(ALL_STARTING),  1},
                        {of(EACH_STARTING), 6},
                        {of(TEST_STARTING), 6},
                        {asList(RefreshEachTimeBefore.values()), 7}},
                6,
                14);
    }
}
