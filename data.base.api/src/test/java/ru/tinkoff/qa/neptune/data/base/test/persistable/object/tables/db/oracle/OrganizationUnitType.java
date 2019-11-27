package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.*;

@PersistenceCapable(table = "ORGANIZATION_UNIT_TYPE")
public class OrganizationUnitType extends PersistableObject {

    @PrimaryKey
    @Column(name = "UNIT_TYPE_ID")
    private Integer unitTypeId;

    @Column(name = "TITLE_ENG")
    private String titleEng;

    @Column(name = "TITLE_RUS")
    private String titleRus;

    public int getUnitTypeId() {
        return unitTypeId;
    }

    public OrganizationUnitType setUnitTypeId(int unitTypeId) {
        this.unitTypeId = unitTypeId;
        return this;
    }

    public String getTitleEng() {
        return titleEng;
    }

    public String getTitleRus() {
        return titleRus;
    }

    public OrganizationUnitType setTitleRus(String titleRus) {
        this.titleRus = titleRus;
        return this;
    }

    public OrganizationUnitType setTitleEng(String titleEng) {
        this.titleEng = titleEng;
        return this;
    }
}
