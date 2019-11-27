package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

import static javax.jdo.annotations.IdGeneratorStrategy.IDENTITY;

@PersistenceCapable(table = "OPERATOR_SCHEDULE")
public class OperatorSchedule extends PersistableObject {

    public OperatorSchedule(//Integer operatorScheduleId,
                            User user,
                            Date intervalStart,
                            Date intervalEnd,
                            boolean isActive,
                            Date editingDate,
                            User editor) {
        //this.operatorScheduleId = operatorScheduleId;
        this.user = user;
        this.editor = editor;
        this.intervalStart = intervalStart;
        this.intervalEnd = intervalEnd;
        this.isActive = isActive;
        this.editingDate = editingDate;
    }

    @PrimaryKey
    @Persistent(valueStrategy = IDENTITY)
    @Column(name = "OPERATOR_SCHEDULE_ID")
    private Integer operatorScheduleId;

    @Column(name = "USER_ID")
    private User user;

    @Column(name = "EDITOR_ID")
    private User editor;

    @Column(name = "INTERVAL_START")
    private Date intervalStart;

    @Column(name = "INTERVAL_END")
    private Date intervalEnd;

    @Column(name = "IS_ACTIVE")
    private boolean isActive;

    @Column(name = "EDITING_DATE")
    private Date editingDate;

    public Date getEditingDate() {
        return editingDate;
    }

    public void setEditingDate(Date editingDate) {
        this.editingDate = editingDate;
    }

    public Integer getOperatorScheduleId() {
        return operatorScheduleId;
    }

    public void setOperatorScheduleId(Integer operatorScheduleId) {
        this.operatorScheduleId = operatorScheduleId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getEditor() {
        return editor;
    }

    public void setEditor(User editor) {
        this.editor = editor;
    }

    public Date getIntervalStart() {
        return intervalStart;
    }

    public void setIntervalStart(Date intervalStart) {
        this.intervalStart = intervalStart;
    }

    public Date getIntervalEnd() {
        return intervalEnd;
    }

    public void setIntervalEnd(Date intervalEnd) {
        this.intervalEnd = intervalEnd;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
