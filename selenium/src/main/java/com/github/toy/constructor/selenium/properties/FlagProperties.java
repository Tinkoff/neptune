package com.github.toy.constructor.selenium.properties;

import com.github.toy.constructor.core.api.PropertySupplier;

public enum FlagProperties implements PropertySupplier<Boolean> {
    FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION("find.only.visible.elements.when.no.condition");

    private final String propertyName;

    FlagProperties(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public Boolean get() {
        return returnOptional()
                .map(Boolean::parseBoolean).orElse(false);
    }
}
