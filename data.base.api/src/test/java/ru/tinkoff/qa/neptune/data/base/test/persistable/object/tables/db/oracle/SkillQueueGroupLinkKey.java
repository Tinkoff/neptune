package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle;

import org.datanucleus.identity.IntId;
import ru.tinkoff.qa.neptune.data.base.api.CompositeKey;

public class SkillQueueGroupLinkKey extends CompositeKey {
    public IntId queueGroupId;
    public IntId skillId;

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
