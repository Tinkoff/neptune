package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(table = "USER_TO_PLANNING_GROUP")
public class UserToPlanningGroup extends PersistableObject {

    @PrimaryKey
    @Column(name = "USER_ID")
    private User user;

    @Column(name = "SKILL_AUTOCHANGE_ENABLED")
    private boolean skillAutochangeEnabled;

    @Column(name = "PLANNING_GROUP_ID")
    private PlanningGroup planningGroupId;

    @Column(name = "SCORE")
    private Integer score;

    public UserToPlanningGroup(User user, boolean skillAutochangeEnabled, PlanningGroup planningGroupId, Integer score) {
        this.user = user;
        this.skillAutochangeEnabled = skillAutochangeEnabled;
        this.planningGroupId = planningGroupId;
        this.score = score;
    }

    public PlanningGroup getPlanningGroupId() {
        return planningGroupId;
    }

    public void setPlanningGroupId(PlanningGroup planningGroupId) {
        this.planningGroupId = planningGroupId;
    }

    public Integer getScore() {
        return score;
    }

    public UserToPlanningGroup(User user, PlanningGroup planningGroupId, Integer score, boolean skillAutochangeEnabled) {
        this.user = user;
        this.skillAutochangeEnabled = skillAutochangeEnabled;
        this.planningGroupId = planningGroupId;
        this.score = score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setSkillAutochangeEnabled(boolean skillAutochangeEnabled) {
        this.skillAutochangeEnabled = skillAutochangeEnabled;
    }

    public boolean getSkillAutochangeEnabled() {
        return skillAutochangeEnabled;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
