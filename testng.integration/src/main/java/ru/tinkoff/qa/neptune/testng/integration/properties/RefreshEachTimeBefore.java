package ru.tinkoff.qa.neptune.testng.integration.properties;

import org.testng.annotations.*;
import ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable;

import java.lang.annotation.Annotation;
import java.util.function.Supplier;

/**
 * This enum is designed to define the strategy of the invoking of {@link ContextRefreshable#refreshContext()}
 * by engines of TestNg. It is supposed to be invoked before methods annotated by {@link BeforeSuite} and/or {@link BeforeTest}
 * and/or {@link BeforeClass} and/or {@link BeforeGroups} and/or {@link BeforeMethod} and/or {@link Test}.
 */
public enum RefreshEachTimeBefore implements Supplier<Class<? extends Annotation>> {

    /**
     * This element is used to define the strategy to invoke the
     * {@link ContextRefreshable#refreshContext()} each time before
     * the first method annotated by {@link BeforeSuite} is run.
     */
    SUITE_STARTING {
        @Override
        public Class<? extends Annotation> get() {
            return BeforeSuite.class;
        }
    },

    /**
     * This element is used to define the strategy to invoke the
     * {@link ContextRefreshable#refreshContext()} each time before
     * the first annotated by {@link BeforeTest} is run.
     *
     * <p></p>
     * Rule:
     * <p></p>
     * {@link ContextRefreshable#refreshContext()} is invoked when:
     * <p></p>
     * 1. {@link ContextRefreshable#refreshContext()} has not been invoked before any
     * {@link BeforeSuite}/{@link BeforeTest}-method.
     */
    TEST_STARTING {
        @Override
        public Class<? extends Annotation> get() {
            return BeforeTest.class;
        }
    },

    /**
     * This element is used to define the strategy to invoke the
     * {@link ContextRefreshable#refreshContext()} each time before
     * the first {@link BeforeClass} - method of a class is run.
     *
     * <p></p>
     * Rule:
     * <p></p>
     * {@link ContextRefreshable#refreshContext()} is invoked when:
     * <p></p>
     * 1. {@link ContextRefreshable#refreshContext()} has not been invoked before some
     * {@link BeforeSuite}/{@link BeforeTest}/{@link BeforeClass}-method
     * and any {@link Test}-method has not been invoked yet.
     * <p></p>
     * 2. {@link ContextRefreshable#refreshContext()} has not been invoked before some
     * {@link BeforeClass}-method and any {@link Test}-method has not been invoked yet.
     */
    CLASS_STARTING {
        @Override
        public Class<? extends Annotation> get() {
            return BeforeClass.class;
        }
    },

    /**
     * This element is used to define the strategy to invoke the
     * {@link ContextRefreshable#refreshContext()} each time before
     * the first {@link BeforeGroups} - method of a group is run.
     */
    GROUP_STARTING {
        @Override
        public Class<? extends Annotation> get() {
            return BeforeGroups.class;
        }
    },

    /**
     * This element is used to define the strategy to invoke the
     * {@link ContextRefreshable#refreshContext()} each time before
     * the first method annotated by {@link BeforeMethod} is run before any test.
     *
     * <p></p>
     * Rule:
     * <p></p>
     * {@link ContextRefreshable#refreshContext()} is invoked when:
     * <p></p>
     * 1. {@link ContextRefreshable#refreshContext()} has not been invoked before some 
     * {@link BeforeSuite}/{@link BeforeTest}/{@link BeforeClass}/{@link BeforeGroups}/
     * {@link BeforeMethod}-method and any {@link Test}-method has not been invoked yet.
     * <p></p>
     * 2. {@link ContextRefreshable#refreshContext()} has not been invoked before some 
     * {@link BeforeMethod}-method and any {@link Test}-method has not been invoked yet.
     */
    BEFORE_METHOD_STARTING {
        @Override
        public Class<? extends Annotation> get() {
            return BeforeMethod.class;
        }
    },

    /**
     * This element is used to define the strategy to invoke the
     * {@link ContextRefreshable#refreshContext()} each time before
     * any method annotated by {@link Test} is run.
     *
     * <p></p>
     * Rule:
     * <p></p>
     * {@link ContextRefreshable#refreshContext()} is invoked when:
     * <p></p>
     * 1. {@link ContextRefreshable#refreshContext()} has not been invoked before some 
     * {@link BeforeSuite}/{@link BeforeTest}/{@link BeforeClass}/{@link BeforeGroups}/
     * {@link BeforeMethod}-method and any {@link Test}-method has not been invoked yet.
     * <p></p>
     * 2. {@link ContextRefreshable#refreshContext()} has not been invoked before some 
     * {@link BeforeMethod}-method and any {@link Test}-method has not been invoked yet.
     */
    METHOD_STARTING {
        @Override
        public Class<? extends Annotation> get() {
            return Test.class;
        }
    };

    @Override
    public abstract Class<? extends Annotation> get();
}
