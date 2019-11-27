package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.*;

@PersistenceCapable(table = "QUEUE")
public class Queue extends PersistableObject {

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    @Column(name = "QUEUE_ID")
    private Integer queueId;

    @Column(name = "QUEUE_CODE")
    private String queueCode;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "HI_TASK_TYPE_ID")
    private TaskType hiTaskTypeId;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @Column(name = "QUEUE_GROUP_ID")
    private QueueGroup queueGroupId;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IS_PRIVATE_QUEUE")
    private Boolean isPrivateQueue;

    @Column(name = "PRIVATE_QUEUE_PREFIX")
    private String prefix;

    @Column(name = "PRIVATE_QUEUE_POSTFIX")
    private String postfix;

    public String getDescription() {
        return description;
    }

    public Queue setDescription(String description) {
        this.description = description;
        return this;
    }

    public QueueGroup getQueueGroupId() {
        return queueGroupId;
    }

    public Queue setQueueGroupId(QueueGroup queueGroupId) {
        this.queueGroupId = queueGroupId;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public Queue setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getPostfix() {
        return postfix;
    }

    public Queue setPostfix(String postfix) {
        this.postfix = postfix;
        return this;
    }

    public int getQueueId() {
        return queueId;
    }

    public Queue setQueueId(int queueId) {
        this.queueId = queueId;
        return this;
    }

    public String getQueueCode() {
        return queueCode;
    }

    public Queue setQueueCode(String queueCode) {
        this.queueCode = queueCode;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Queue setTitle(String title) {
        this.title = title;
        return this;
    }

    public TaskType getHiTaskTypeId() {
        return hiTaskTypeId;
    }

    public Queue setHiTaskTypeId(TaskType hiTaskTypeId) {
        this.hiTaskTypeId = hiTaskTypeId;
        return this;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public Queue setDeleted(boolean deleted) {
        isDeleted = deleted;
        return this;
    }

    public Boolean getPrivateQueue() {
        return isPrivateQueue;
    }

    public Queue setPrivateQueue(Boolean privateQueue) {
        isPrivateQueue = privateQueue;
        return this;
    }
}
