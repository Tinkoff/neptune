package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(table = "HI_TASK_STATE")
public class HiTaskState extends PersistableObject {

    @PrimaryKey
    @Column(name = "HI_TASK_STATE_ID")
    private Integer hiTaskStateId;

    @Column(name = "TITLE")
    private String title;

    public Integer getHiTaskStateId() {
        return hiTaskStateId;
    }

    public String getTitle() {
        return title;
    }

    public void setHiTaskStateId(Integer hiTaskStateId) {
        this.hiTaskStateId = hiTaskStateId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
