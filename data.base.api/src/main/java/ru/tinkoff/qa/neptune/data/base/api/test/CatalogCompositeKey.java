package ru.tinkoff.qa.neptune.data.base.api.test;

import org.datanucleus.identity.IntId;
import ru.tinkoff.qa.neptune.data.base.api.CompositeKey;

public class CatalogCompositeKey extends CompositeKey {
    public Integer yearOfPublishing;
    public IntId book;
    public IntId publisher;

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return super.toString();
    }
}
