package ru.tinkoff.qa.neptune.core.api.hooks;

import org.testng.annotations.Test;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.contains;
import static ru.tinkoff.qa.neptune.core.api.hooks.ExecutionHook.getHooks;

public class HooksTest {

    @Test
    public void test() {
        assertThat(
                getHooks().stream().map(ExecutionHook::getClass).collect(toList()),
                anyOf(contains(Hook2.class, Hook3.class, Hook5.class, Hook4.class, Hook1.class),
                        contains(Hook3.class, Hook2.class, Hook5.class, Hook4.class, Hook1.class))
        );
    }
}
