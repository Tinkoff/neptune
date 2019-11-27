package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import static javax.jdo.annotations.IdGeneratorStrategy.IDENTITY;

@PersistenceCapable(table = "SKILL")
public class Skill extends PersistableObject {

    @PrimaryKey
    @Persistent(valueStrategy = IDENTITY)
    @Column(name = "SKILL_ID")
    private Integer skillId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "HI_TASK_TYPE_ID")
    private TaskType taskType;

    @Column(name = "IS_DELETED")
    private boolean isDeleted;

    @Column(name = "IS_IGNORED_IN_EVALUATION")
    private boolean isIgnoredInEvaluation;

    @Column(name = "CANT_WORK_NOT_IN_PLAN")
    private boolean cantWorkNotInPlan;

    @Column(name = "PDS_TELEPHONY_QUEUE_ID")
    private Integer pdsTelephonyQueueId;

    @Column(name = "INCOMING_CALLS_QUEUE_ID")
    private Integer incomingCallsQueueId;

    public Skill(String title, TaskType taskType, boolean isDeleted,
                 boolean isIgnoredInEvaluation, boolean cantWorkNotInPlan) {
        this.title = title;
        this.taskType = taskType;
        this.isDeleted = isDeleted;
        this.isIgnoredInEvaluation = isIgnoredInEvaluation;
        this.cantWorkNotInPlan = cantWorkNotInPlan;
    }

    public Integer getSkillId() {
        return skillId;
    }

    public Skill setSkillId(Integer skillId) {
        this.skillId = skillId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Skill setTitle(String title) {
        this.title = title;
        return this;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public Skill setTaskType(TaskType taskType) {
        this.taskType = taskType;
        return this;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public Skill setDeleted(boolean deleted) {
        isDeleted = deleted;
        return this;
    }

    public boolean isIgnoredInEvaluation() {
        return isIgnoredInEvaluation;
    }

    public Skill setIgnoredInEvaluation(boolean ignoredInEvaluation) {
        isIgnoredInEvaluation = ignoredInEvaluation;
        return this;
    }

    public boolean isCantWorkNotInPlan() {
        return cantWorkNotInPlan;
    }

    public Skill setCantWorkNotInPlan(boolean cantWorkNotInPlan) {
        this.cantWorkNotInPlan = cantWorkNotInPlan;
        return this;
    }

    public Integer getPdsTelephonyQueueId() {
        return pdsTelephonyQueueId;
    }

    public Skill setPdsTelephonyQueueId(Integer pdsTelephonyQueueId) {
        this.pdsTelephonyQueueId = pdsTelephonyQueueId;
        return this;
    }

    public Integer getIncomingCallsQueueId() {
        return incomingCallsQueueId;
    }

    public Skill setIncomingCallsQueueId(Integer incomingCallsQueueId) {
        this.incomingCallsQueueId = incomingCallsQueueId;
        return this;
    }
}
