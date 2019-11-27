package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.*;

@PersistenceCapable(table = "ORGANIZATION_UNIT")
public class OrganizationUnit extends PersistableObject {

    public OrganizationUnit(String title,
                            OrganizationUnitType unitType,
                            int rank,
                            boolean isDeleted,
                            boolean isEvaluationEnabled,
                            String unitCode) {
        this.title = title;
        this.unitType = unitType;
        this.rank = rank;
        this.unitCode = unitCode;
        this.isDeleted = isDeleted;
        this.isEvaluationEnabled = isEvaluationEnabled;
    }

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Column(name = "UNIT_ID")
    private Integer unitId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "UNIT_TYPE_ID")
    private OrganizationUnitType unitType;

    @Column(name = "RANK")
    private int rank;

    @Column(name = "UNIT_CODE")
    private String unitCode;

    @Column(name = "IS_DELETED")
    private boolean isDeleted;

    @Column(name = "IS_EVALUATION_ENABLED")
    private boolean isEvaluationEnabled;


    public Integer getUnitId() {
        return unitId;
    }

    public OrganizationUnit setUnitId(Integer unitId) {
        this.unitId = unitId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public OrganizationUnit setTitle(String title) {
        this.title = title;
        return this;
    }


    public OrganizationUnitType getUnitType() {
        return unitType;
    }

    public OrganizationUnit setUnitType(OrganizationUnitType unitType) {
        this.unitType = unitType;
        return this;
    }

    public int getRank() {
        return rank;
    }

    public OrganizationUnit setRank(int rank) {
        this.rank = rank;
        return this;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public OrganizationUnit setDeleted(boolean deleted) {
        isDeleted = deleted;
        return this;
    }

    public boolean isEvaluationEnabled() {
        return isEvaluationEnabled;
    }

    public OrganizationUnit setEvaluationEnabled(boolean evaluationEnabled) {
        isEvaluationEnabled = evaluationEnabled;
        return this;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }
}
