package com.github.toy.constructor.testng.integration.properties;

import org.testng.annotations.*;

import java.lang.annotation.Annotation;
import java.util.function.Supplier;

public enum RefreshEachTimeBefore implements Supplier<Class<? extends Annotation>> {

    SUITE_STARTING {
        @Override
        public Class<? extends Annotation> get() {
            return BeforeSuite.class;
        }
    },

    ALL_TEST_STARTING {
        @Override
        public Class<? extends Annotation> get() {
            return BeforeTest.class;
        }
    },

    CLASS_STARTING {
        @Override
        public Class<? extends Annotation> get() {
            return BeforeClass.class;
        }
    },

    GROUP_STARTING {
        @Override
        public Class<? extends Annotation> get() {
            return BeforeGroups.class;
        }
    },

    BEFORE_METHOD_STARTING {
        @Override
        public Class<? extends Annotation> get() {
            return BeforeMethod.class;
        }
    },

    METHOD_STARTING {
        @Override
        public Class<? extends Annotation> get() {
            return Test.class;
        }
    };

    @Override
    public abstract Class<? extends Annotation> get();
}
