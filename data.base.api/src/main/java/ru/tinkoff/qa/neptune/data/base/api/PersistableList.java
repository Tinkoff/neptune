package ru.tinkoff.qa.neptune.data.base.api;

import java.util.ArrayList;
import java.util.Collection;

public class PersistableList<T> extends ArrayList<T> {

    public PersistableList() {
        super();
    }

    public PersistableList(Collection<T> list) {
        super(list);
    }
}
