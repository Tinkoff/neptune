package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import static javax.jdo.annotations.IdGeneratorStrategy.IDENTITY;

@PersistenceCapable(table = "PLANNING_GROUP")
public class PlanningGroup extends PersistableObject {

    public PlanningGroup(String title, boolean isDeleted, Skill extraSkill, Skill mainSkill, Skill defaultSkill,
                         boolean skillAutochangeEnabled, int schedulesAprovingType) {
        this.title = title;
        this.skillAutochangeEnabled = skillAutochangeEnabled;
        this.isDeleted = isDeleted;
        this.extraSkill = extraSkill;
        this.mainSkill = mainSkill;
        this.defaultSkill = defaultSkill;
        this.schedulesAprovingType = schedulesAprovingType;
    }

    @PrimaryKey
    @Persistent(valueStrategy = IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "SKILL_AUTOCHANGE_ENABLED")
    private boolean skillAutochangeEnabled;

    @Column(name = "IS_DELETED")
    private boolean isDeleted;

    @Column(name = "EXTRA_SKILL_ID")
    private Skill extraSkill;

    @Column(name = "MAIN_SKILL_ID")
    private Skill mainSkill;

    @Column(name = "DEFAULT_SKILL_ID")
    private Skill defaultSkill;

    @Column(name = "SCHEDULES_APPROVING_TYPE")
    private int schedulesAprovingType;

    public Skill getExtraSkill() {
        return extraSkill;
    }

    public void setExtraSkill(Skill extraSkill) {
        this.extraSkill = extraSkill;
    }

    public Skill getMainSkill() {
        return mainSkill;
    }

    public void setMainSkill(Skill mainSkill) {
        this.mainSkill = mainSkill;
    }

    public Skill getDefaultSkill() {
        return defaultSkill;
    }

    public void setDefaultSkill(Skill defaultSkill) {
        this.defaultSkill = defaultSkill;
    }

    public int getSchedulesAprovingType() {
        return schedulesAprovingType;
    }

    public void setSchedulesAprovingType(int schedulesAprovingType) {
        this.schedulesAprovingType = schedulesAprovingType;
    }

    public String getTitle() {
        return title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSkillAutochangeEnabled() {
        return skillAutochangeEnabled;
    }

    public void setSkillAutochangeEnabled(boolean skillAutochangeEnabled) {
        this.skillAutochangeEnabled = skillAutochangeEnabled;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
