package ru.tinkoff.qa.neptune.data.base.api.captors;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import static org.apache.commons.lang3.ArrayUtils.contains;
import static ru.tinkoff.qa.neptune.data.base.api.PersistableObject.DATA_NUCLEOUS_ENHANCED_FIELDS;

public class DataNucleousEnhancedExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return contains(DATA_NUCLEOUS_ENHANCED_FIELDS, f.getName());
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
