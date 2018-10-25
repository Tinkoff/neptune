package ru.tinkoff.qa.neptune.data.base.api;

import com.google.gson.Gson;
import org.datanucleus.enhancement.Persistable;

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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        var otherClass = obj.getClass();
        if (!PersistableObject.class.isAssignableFrom(otherClass)) {
            return false;
        }

        var thisClass  = this.getClass();

        if (!thisClass.equals(otherClass)) {
            return false;
        }

        if (!Persistable.class.isAssignableFrom(thisClass) && !Persistable.class.isAssignableFrom(otherClass)) {
            return super.equals(obj);
        }

        if ((!Persistable.class.isAssignableFrom(thisClass) && Persistable.class.isAssignableFrom(otherClass)) ||
                (Persistable.class.isAssignableFrom(thisClass) && !Persistable.class.isAssignableFrom(otherClass))) {
            return false;
        }

        return ((Persistable) this).dnGetObjectId().equals(((Persistable) obj).dnGetObjectId());
    }
}
