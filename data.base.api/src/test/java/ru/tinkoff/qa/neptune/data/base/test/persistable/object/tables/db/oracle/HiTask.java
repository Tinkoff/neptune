package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

@PersistenceCapable(table = "HI_TASK")
public class HiTask extends PersistableObject {

    public HiTask() {}

    @PrimaryKey
    @Column(name = "HI_TASK_ID")
    private String hiTaskId;

    @Column(name = "HI_TASK_TYPE_ID")
    private TaskType hiTaskTypeId;

    @Column(name = "HI_TASK_STATE_ID")
    private HiTaskState hiTaskState;

    @Column(name = "TASK_ID")
    private String taskId;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "TASK_CREATE_DATE")
    private Date taskCreateDate;

    @Column(name = "EXPIRATION_DATE")
    private Date expirationDate;

    @Column(name = "USER_ID")
    private User user;

    @Column(name = "QUEUE_GROUP_ID")
    private QueueGroup queueGroup;

    @Column(name = "FORM_CODE")
    private String formCode;

    @Column(name = "PRICE")
    private Float price;

    @Column(name = "FINISH_DATE")
    private Date finishDate;

    @Column(name = "CANCELED_COMMENT")
    private String canceledComment;

    @Column(name = "IS_POSTPONED")
    private Boolean isPostponed;

    @Column(name = "POSTPONED_DATE")
    private Date postponedDate;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @Column(name = "EXPORT_DATE")
    private Date exportDate;

    @Column(name = "PROCESSING_STEP")
    private String processingStep;

    @Column(name = "QUEUE_ID")
    private Queue queueId;

    @Column(name = "DIALOG_SCRIPT_ID")
    private Integer dialogScriptId;

    @Column(name = "SCORE")
    private Double score;

    @Column(name = "IS_PDS")
    private Boolean isPds;

    @Column(name = "CALL_PROCESSING_METHOD")
    private Integer callProcessingMethod;

    @Column(name = "START_PROCESSING_DATE")
    private Date startProcessingDate;

    @Column(name = "CURRENT_PART_IDX")
    private Integer currentPartIdx;

    @Column(name = "WEBIM_SESSION_ID")
    private String webimSessionId;

    @Column(name = "IS_CONTINUED")
    private Boolean isContinued;

    @Column(name = "COMMENT")
    private String comment;

    public String getHiTaskId() {
        return hiTaskId;
    }

    public TaskType getHiTaskTypeId() {
        return hiTaskTypeId;
    }

    public HiTaskState getHiTaskState() {
        return hiTaskState;
    }

    public String getTaskId() {
        return taskId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getTaskCreateDate() {
        return taskCreateDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public User getUser() {
        return user;
    }

    public QueueGroup getQueueGroup() {
        return queueGroup;
    }

    public String getFormCode() {
        return formCode;
    }

    public Float getPrice() {
        return price;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public String getCanceledComment() {
        return canceledComment;
    }

    public Boolean getPostponed() {
        return isPostponed;
    }

    public Date getPostponedDate() {
        return postponedDate;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public Date getExportDate() {
        return exportDate;
    }

    public String getProcessingStep() {
        return processingStep;
    }

    public Queue getQueueId() {
        return queueId;
    }

    public Integer getDialogScriptId() {
        return dialogScriptId;
    }

    public Double getScore() {
        return score;
    }

    public Boolean getPds() {
        return isPds;
    }

    public Integer getCallProcessingMethod() {
        return callProcessingMethod;
    }

    public Date getStartProcessingDate() {
        return startProcessingDate;
    }

    public Integer getCurrentPartIdx() {
        return currentPartIdx;
    }

    public String getWebimSessionId() {
        return webimSessionId;
    }

    public Boolean getContinued() {
        return isContinued;
    }

    public HiTask setHiTaskId(String hiTaskId) {
        this.hiTaskId = hiTaskId;
        return this;
    }

    public HiTask setHiTaskTypeId(TaskType hiTaskTypeId) {
        this.hiTaskTypeId = hiTaskTypeId;
        return this;
    }

    public HiTask setHiTaskState(HiTaskState hiTaskState) {
        this.hiTaskState = hiTaskState;
        return this;
    }

    public HiTask setTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    public HiTask setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    public HiTask setTaskCreateDate(Date taskCreateDate) {
        this.taskCreateDate = taskCreateDate;
        return this;
    }

    public HiTask setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public HiTask setUser(User user) {
        this.user = user;
        return this;
    }

    public HiTask setQueueGroup(QueueGroup queueGroup) {
        this.queueGroup = queueGroup;
        return this;
    }

    public HiTask setFormCode(String formCode) {
        this.formCode = formCode;
        return this;
    }

    public HiTask setPrice(Float price) {
        this.price = price;
        return this;
    }

    public HiTask setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
        return this;
    }

    public HiTask setCanceledComment(String canceledComment) {
        this.canceledComment = canceledComment;
        return this;
    }

    public HiTask setPostponed(Boolean postponed) {
        isPostponed = postponed;
        return this;
    }

    public HiTask setPostponedDate(Date postponedDate) {
        this.postponedDate = postponedDate;
        return this;
    }

    public HiTask setDeleted(Boolean deleted) {
        isDeleted = deleted;
        return this;
    }

    public HiTask setExportDate(Date exportDate) {
        this.exportDate = exportDate;
        return this;
    }

    public HiTask setProcessingStep(String processingStep) {
        this.processingStep = processingStep;
        return this;
    }

    public HiTask setQueueId(Queue queueId) {
        this.queueId = queueId;
        return this;
    }

    public HiTask setDialogScriptId(Integer dialogScriptId) {
        this.dialogScriptId = dialogScriptId;
        return this;
    }

    public HiTask setScore(Double score) {
        this.score = score;
        return this;
    }

    public HiTask setPds(Boolean pds) {
        isPds = pds;
        return this;
    }

    public HiTask setCallProcessingMethod(Integer callProcessingMethod) {
        this.callProcessingMethod = callProcessingMethod;
        return this;
    }

    public HiTask setStartProcessingDate(Date startProcessingDate) {
        this.startProcessingDate = startProcessingDate;
        return this;
    }

    public HiTask setCurrentPartIdx(Integer currentPartIdx) {
        this.currentPartIdx = currentPartIdx;
        return this;
    }

    public HiTask setWebimSessionId(String webimSessionId) {
        this.webimSessionId = webimSessionId;
        return this;
    }

    public HiTask setContinued(Boolean continued) {
        isContinued = continued;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public HiTask setComment(String comment) {
        this.comment = comment;
        return this;
    }
}
