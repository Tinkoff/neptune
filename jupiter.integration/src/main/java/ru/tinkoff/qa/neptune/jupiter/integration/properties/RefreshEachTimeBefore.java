package ru.tinkoff.qa.neptune.jupiter.integration.properties;

import org.junit.jupiter.api.*;
import ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable;

/**
 * This enum is designed to define the strategy of the invoking of {@link ContextRefreshable#refreshContext()}
 * by engines of TestNg. It is supposed to be invoked before methods annotated by {@link BeforeAll} and/or {@link BeforeEach}
 * and/or {@link Test}/{@link TestFactory}/{@link TestTemplate}.
 */
public enum RefreshEachTimeBefore {

    /**
     * This element is used to define the strategy to invoke the
     * {@link ContextRefreshable#refreshContext()} each time before
     * the first {@link BeforeAll} - method of a class is run.
     *
     * <p></p>
     * Rule:
     * <p></p>
     * {@link ContextRefreshable#refreshContext()} is invoked when:
     * <p></p>
     * 1. {@link ContextRefreshable#refreshContext()} has not been invoked before any
     * {@link BeforeAll}/{@link BeforeEach}/{@link Test} or {@link TestFactory} or {@link TestTemplate}-method.
     */
    ALL_STARTING,

    /**
     * This element is used to define the strategy to invoke the
     * {@link ContextRefreshable#refreshContext()} each time before
     * the first method annotated by {@link BeforeEach} is run before any test.
     *
     * <p></p>
     * Rule:
     * <p></p>
     * {@link ContextRefreshable#refreshContext()} is invoked when:
     * <p></p>
     * 1. {@link ContextRefreshable#refreshContext()} has not been invoked before some
     * {@link BeforeAll}/{@link BeforeEach}-method and any {@link Test} or {@link TestFactory} or
     * {@link TestTemplate}method has not been invoked yet.
     * <p></p>
     * 2. {@link ContextRefreshable#refreshContext()} has not been invoked before some
     * {@link BeforeEach}-method and any {@link Test} or {@link TestFactory} or {@link TestTemplate}-method
     * has not been invoked yet.
     */
    EACH_STARTING,

    /**
     * This element is used to define the strategy to invoke the
     * {@link ContextRefreshable#refreshContext()} each time before
     * any method annotated by {@link Test} or {@link TestFactory} or {@link TestTemplate} is run.
     *
     * <p></p>
     * Rule:
     * <p></p>
     * {@link ContextRefreshable#refreshContext()} is invoked when:
     * <p></p>
     * 1. {@link ContextRefreshable#refreshContext()} has not been invoked before some
     * {@link BeforeAll}/{@link BeforeEach}-method and any {@link Test} or {@link TestFactory} or
     * {@link TestTemplate}-method has not been invoked yet.
     * <p></p>
     * 2. {@link ContextRefreshable#refreshContext()} has not been invoked before some
     * {@link BeforeEach}-method and any {@link Test} or {@link TestFactory} or {@link TestTemplate}-method
     * has not been invoked yet.
     */
    TEST_STARTING
}
