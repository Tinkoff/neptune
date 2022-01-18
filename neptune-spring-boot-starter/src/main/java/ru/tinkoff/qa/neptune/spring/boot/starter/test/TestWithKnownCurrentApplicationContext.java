package ru.tinkoff.qa.neptune.spring.boot.starter.test;

import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import ru.tinkoff.qa.neptune.spring.boot.starter.application.contexts.CurrentApplicationContextTestExecutionListener;

import static org.springframework.test.context.TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS;

/**
 * This is the marker which is useful for any test runner except JUnit5.
 * It allows applying {@link CurrentApplicationContextTestExecutionListener} with other
 * {@link TestExecutionListener}'s of implementor regardless its inheritance.
 */
@TestExecutionListeners(
        value = CurrentApplicationContextTestExecutionListener.class,
        mergeMode = MERGE_WITH_DEFAULTS)
public interface TestWithKnownCurrentApplicationContext {
}
