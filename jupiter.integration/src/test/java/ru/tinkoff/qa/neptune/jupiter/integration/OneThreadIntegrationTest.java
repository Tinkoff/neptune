package ru.tinkoff.qa.neptune.jupiter.integration;

import ru.tinkoff.qa.neptune.jupiter.integration.properties.RefreshEachTimeBefore;

import static java.util.Arrays.asList;
import static java.util.List.of;
import static ru.tinkoff.qa.neptune.jupiter.integration.properties.RefreshEachTimeBefore.*;

public class OneThreadIntegrationTest extends Junit5IntegrationTest {

    public OneThreadIntegrationTest() {
        super(Junit5StubTest.class,
                new Object[][]{
                        {null, 2},
                        {of(ALL_STARTING),  1},
                        {of(EACH_STARTING), 2},
                        {of(TEST_STARTING), 2},
                        {asList(RefreshEachTimeBefore.values()), 2}},
                1,
                10);
    }
}
