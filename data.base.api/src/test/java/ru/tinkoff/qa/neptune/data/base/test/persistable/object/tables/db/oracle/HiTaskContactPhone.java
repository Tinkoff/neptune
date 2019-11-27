package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.*;

@PersistenceCapable(table = "HI_TASK_CONTACT_PHONE")
public class HiTaskContactPhone extends PersistableObject {

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    @Column(name = "HI_TASK_CONTACT_PHONE_ID")
    private String contactPhoneId;

    @Column(name = "HI_TASK_ID")
    private HiTask hiTask;

    @Column(name = "PHONE_TYPE")
    private String phoneType;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "PHONE_STATUS")
    private Integer phoneStatus;

    public String getContactPhoneId() {
        return contactPhoneId;
    }


    public String getPhoneType() {
        return phoneType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public HiTaskContactPhone setPhoneType(String phoneType) {
        this.phoneType = phoneType;
        return this;
    }

    public HiTaskContactPhone setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public Integer getPhoneStatus() {
        return phoneStatus;
    }

    public HiTaskContactPhone setPhoneStatus(Integer phoneStatus) {
        this.phoneStatus = phoneStatus;
        return this;
    }

    public HiTask getHiTask() {
        return hiTask;
    }

    public void setHiTask(HiTask hiTask) {
        this.hiTask = hiTask;
    }
}
