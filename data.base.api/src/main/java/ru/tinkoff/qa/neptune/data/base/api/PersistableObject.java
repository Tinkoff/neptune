package ru.tinkoff.qa.neptune.data.base.api;

import com.google.gson.Gson;

/**
 * This abstract class is designed to mark persistable classes.
 */
public abstract class PersistableObject extends OrmObject implements Cloneable {

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
