package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(table = "SKILL_QUEUE_GROUP_LINK", objectIdClass = SkillQueueGroupLinkKey.class )
public class SkillQueueGroupLink extends PersistableObject {

    public SkillQueueGroupLink(QueueGroup queueGroupId, Skill skillId, int weight) {
        this.queueGroupId = queueGroupId;
        this.skillId = skillId;
        this.weight = weight;
    }

    @PrimaryKey
    @Column(name = "QUEUE_GROUP_ID")
    private QueueGroup queueGroupId;

    @PrimaryKey
    @Column(name = "SKILL_ID")
    private Skill skillId;

    @Column(name = "WEIGHT")
    private int weight;

    public QueueGroup getQueueGroupId() {
        return queueGroupId;
    }

    public void setQueueGroupId(QueueGroup queueGroupId) {
        this.queueGroupId = queueGroupId;
    }

    public Skill getSkillId() {
        return skillId;
    }

    public void setSkillId(Skill skillId) {
        this.skillId = skillId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
