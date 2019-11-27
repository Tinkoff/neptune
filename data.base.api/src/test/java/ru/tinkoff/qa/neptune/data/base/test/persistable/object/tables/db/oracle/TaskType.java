package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.*;

@PersistenceCapable(table = "HI_TASK_TYPE")
public class TaskType extends PersistableObject {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
    @Column(name = "HI_TASK_TYPE_ID")
    private Integer taskTypeId;

    @Column(name = "HI_TASK_TYPE_CODE")
    private String taskCode;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @Column(name = "DEFAULT_SKILL_ID")
    private Skill defaultSkill;

    @Column(name = "OFFICE_UNIT_ID")
    private OrganizationUnit officeUnit;

    public int getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(int taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Skill getDefaultSkill() {
        return defaultSkill;
    }

    public void setDefaultSkill(Skill defaultSkill) {
        this.defaultSkill = defaultSkill;
    }

    public OrganizationUnit getOfficeUnit() {
        return officeUnit;
    }

    public void setOfficeUnit(OrganizationUnit officeUnit) {
        this.officeUnit = officeUnit;
    }
}
