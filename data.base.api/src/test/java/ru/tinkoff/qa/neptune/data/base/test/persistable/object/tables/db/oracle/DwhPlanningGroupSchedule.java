package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

@PersistenceCapable(table = "DWH_PLANNING_GROUP_SCHEDULE")
public class DwhPlanningGroupSchedule extends PersistableObject {

    public DwhPlanningGroupSchedule(PlanningGroupShedule planningGroupShedule,
                                    String operatorLogin,
                                    Date intervalStart,
                                    Date intervalEnd,
                                    boolean isWished,
                                    Date editingDate,
                                    String editorLogin,
                                    String status,
                                    String planningGroupName) {
        this.planningGroupShedule = planningGroupShedule;
        this.operatorLogin = operatorLogin;
        this.intervalStart = intervalStart;
        this.intervalEnd = intervalEnd;
        this.isWished = isWished;
        this.editingDate = editingDate;
        this.editorLogin = editorLogin;
        this.status = status;
        this.planningGroupName = planningGroupName;
    }

    @PrimaryKey
    @Column(name = "PLANNING_GROUP_SCHEDULE_ID")
    private PlanningGroupShedule planningGroupShedule;

    @Column(name = "OPERATOR_LOGIN")
    private String operatorLogin;

    @Column(name = "INTERVAL_START")
    private Date intervalStart;

    @Column(name = "INTERVAL_END")
    private Date intervalEnd;

    @Column(name = "IS_WISHED")
    private boolean isWished;

    @Column(name = "EDITING_DATE")
    private Date editingDate;

    @Column(name = "EDITOR_LOGIN")
    private String editorLogin;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "PLANNING_GROUP")
    private String planningGroupName;

    public PlanningGroupShedule getPlanningGroupShedule() {
        return planningGroupShedule;
    }

    public String getOperatorLogin() {
        return operatorLogin;
    }

    public void setOperatorLogin(String operatorLogin) {
        this.operatorLogin = operatorLogin;
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

    public boolean isWished() {
        return isWished;
    }

    public void setWished(boolean wished) {
        isWished = wished;
    }

    public Date getEditingDate() {
        return editingDate;
    }

    public void setEditingDate(Date editingDate) {
        this.editingDate = editingDate;
    }

    public String getEditorLogin() {
        return editorLogin;
    }

    public void setEditorLogin(String editorLogin) {
        this.editorLogin = editorLogin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlanningGroupName() {
        return planningGroupName;
    }

    public void setPlanningGroupName(String planningGroupName) {
        this.planningGroupName = planningGroupName;
    }
}
