package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.*;

@PersistenceCapable(table = "QUEUE_GROUP")
public class QueueGroup extends PersistableObject {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Column(name = "QUEUE_GROUP_ID")
    private Integer queueGroupId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "HI_TASK_TYPE_ID")
    private TaskType TaskType;

    @Column(name = "IS_DELETED")
    private boolean isDeleted;

    @Column(name = "AON_SELECT_STRATEGY")
    private Integer aonSelectStrategy;

    @Column(name = "DISPLAY_PHONE_NUMBER")
    private String displayPhoneNumber;

    public QueueGroup(String title, TaskType taskType, boolean isDeleted) {
        this.title = title;
        this.TaskType = taskType;
        this.isDeleted = isDeleted;
    }

    public QueueGroup(String title, TaskType taskType, boolean isDeleted, Integer aonSelectStrategy, String displayPhoneNumber) {
        this.title = title;
        this.TaskType = taskType;
        this.isDeleted = isDeleted;
        this.aonSelectStrategy = aonSelectStrategy;
        this.displayPhoneNumber = displayPhoneNumber;
    }

    public Integer getQueueGroupId() {
        return queueGroupId;
    }

    public void setQueueGroupId(int queueGroupId) {
        this.queueGroupId = queueGroupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TaskType getHiTaskTypeId() {
        return TaskType;
    }

    public void setHiTaskTypeId(TaskType TaskType) {
        this.TaskType = TaskType;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getAonSelectStrategy() {
        return aonSelectStrategy;
    }

    public void setAonSelectStrategy(Integer aonSelectStrategy) {
        this.aonSelectStrategy = aonSelectStrategy;
    }

    public String getDisplayPhoneNumber() {
        return displayPhoneNumber;
    }

    public void setDisplayPhoneNumber(String displayPhoneNumber) {
        this.displayPhoneNumber = displayPhoneNumber;
    }
}
