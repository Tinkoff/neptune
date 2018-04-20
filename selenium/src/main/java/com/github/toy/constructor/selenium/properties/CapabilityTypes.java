package com.github.toy.constructor.selenium.properties;

import org.openqa.selenium.Capabilities;

import java.util.function.Supplier;

public enum CapabilityTypes implements Supplier<Capabilities> {
    GENERAL {
        @Override
        public Capabilities get() {
            return super.get();
        }
    },

    CHROME {
        @Override
        public Capabilities get() {
            //TODO to be implemented
            return super.get();
        }
    },

    EDGE {
        @Override
        public Capabilities get() {
            //TODO to be implemented
            return super.get();
        }
    },

    FIREFOX {
        @Override
        public Capabilities get() {
            //TODO to be implemented
            return super.get();
        }
    },

    IE {
        @Override
        public Capabilities get() {
            //TODO to be implemented
            return super.get();
        }
    },

    OPERA {
        @Override
        public Capabilities get() {
            //TODO to be implemented
            return super.get();
        }
    },

    SAFARI {
        @Override
        public Capabilities get() {
            //TODO to be implemented
            return super.get();
        }
    },

    PHANTOM_JS {
        @Override
        public Capabilities get() {
            return super.get();
        }
    };

    @Override
    public Capabilities get() {
        //TODO to be implemented
        return null;
    }
}
