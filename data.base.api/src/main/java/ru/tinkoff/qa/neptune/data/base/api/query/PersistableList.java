package ru.tinkoff.qa.neptune.data.base.api.query;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.ArrayList;
import java.util.List;

public class PersistableList<T extends PersistableObject> extends ArrayList<T> {

    PersistableList() {
        super();
    }

    PersistableList(List<T> list) {
        super(list);
    }
}
