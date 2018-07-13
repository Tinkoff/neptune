package com.github.toy.constructor.testng.integration.properties;

import com.github.toy.constructor.core.api.cleaning.Refreshable;
import org.testng.annotations.*;

import java.lang.annotation.Annotation;
import java.util.function.Supplier;

/**
 * This enum is designed to define the strategy of the invoking of {@link Refreshable#refresh()}
 * by engines of TestNg. It is supposed to be invoked before methods annotated by {@link BeforeSuite} and/or {@link BeforeTest}
 * and/or {@link BeforeClass} and/or {@link BeforeGroups} and/or {@link BeforeMethod} and/or {@link Test}.
 */
public enum RefreshEachTimeBefore implements Supplier<Class<? extends Annotation>> {

    /**
     * This element is used to define the strategy to invoke the
     * {@link Refreshable#refresh()} each time before
     * any method annotated by {@link BeforeSuite} is run.
     */
    SUITE_STARTING {
        @Override
        public Class<? extends Annotation> get() {
            return BeforeSuite.class;
        }
    },

    /**
     * This element is used to define the strategy to invoke the
     * {@link Refreshable#refresh()} each time before
     * any method annotated by {@link BeforeTest} is run.
     */
    ALL_TEST_STARTING {
        @Override
        public Class<? extends Annotation> get() {
            return BeforeTest.class;
        }
    },

    /**
     * This element is used to define the strategy to invoke the
     * {@link Refreshable#refresh()} each time before
     * any method annotated by {@link BeforeClass} is run.
     */
    CLASS_STARTING {
        @Override
        public Class<? extends Annotation> get() {
            return BeforeClass.class;
        }
    },

    /**
     * This element is used to define the strategy to invoke the
     * {@link Refreshable#refresh()} each time before
     * any method annotated by {@link BeforeGroups} is run.
     */
    GROUP_STARTING {
        @Override
        public Class<? extends Annotation> get() {
            return BeforeGroups.class;
        }
    },

    /**
     * This element is used to define the strategy to invoke the
     * {@link Refreshable#refresh()} each time before
     * any method annotated by {@link BeforeMethod} is run.
     */
    BEFORE_METHOD_STARTING {
        @Override
        public Class<? extends Annotation> get() {
            return BeforeMethod.class;
        }
    },

    /**
     * This element is used to define the strategy to invoke the
     * {@link Refreshable#refresh()} each time before
     * any method annotated by {@link Test} is run.
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
