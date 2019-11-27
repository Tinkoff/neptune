package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.*;
import java.util.Date;

@PersistenceCapable(table = "PLANNING_GROUP_SCHEDULE")
public class PlanningGroupShedule extends PersistableObject {

    public PlanningGroupShedule(User user,
                                Date intervalStart,
                                Date intervalEnd,
                                boolean isWished,
                                Date editingDate,
                                User editor,
                                int statusId) {
        this.user = user;
        this.editor = editor;
        this.intervalStart = intervalStart;
        this.intervalEnd = intervalEnd;
        this.editingDate = editingDate;
        this.isWished = isWished;
        this.statusId = statusId;
    }

    @Column(name = "USER_ID")
    private User user;

    @Column(name = "EDITOR_ID")
    private User editor;

    @Column(name = "INTERVAL_START")
    private Date intervalStart;

    @Column(name = "INTERVAL_END")
    private Date intervalEnd;

    @Column(name = "EDITING_DATE")
    private Date editingDate;

    @Column(name = "IS_WISHED")
    private boolean isWished;

    @PrimaryKey
    @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
    @Column(name = "PLANNING_GROUP_SCHEDULE_ID")
    private Integer planningGroupScheduleId;

    @Column(name = "STATUS_ID")
    private int statusId;

    public Date getIntervalStart() {
        return intervalStart;
    }

    public void setIntervalStart(Date intervalStart) {
        this.intervalStart = intervalStart;
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

    public Date getIntervalEnd() {
        return intervalEnd;
    }

    public void setIntervalEnd(Date intervalEnd) {
        this.intervalEnd = intervalEnd;
    }

    public Date getEditingDate() {
        return editingDate;
    }

    public void setEditingDate(Date editingDate) {
        this.editingDate = editingDate;
    }

    public boolean isWished() {
        return isWished;
    }

    public void setWished(boolean wished) {
        isWished = wished;
    }

    public Integer getPlanningGroupScheduleId() {
        return planningGroupScheduleId;
    }

    public void setPlanningGroupScheduleId(Integer planningGroupScheduleId) {
        this.planningGroupScheduleId = planningGroupScheduleId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
}
