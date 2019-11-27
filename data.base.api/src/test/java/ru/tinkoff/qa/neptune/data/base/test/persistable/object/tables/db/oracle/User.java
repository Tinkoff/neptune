package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.*;
import java.text.DateFormat;
import java.util.Date;

@PersistenceCapable(table = "USERS")
public class User extends PersistableObject {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
    @Column(name = "USER_ID")
    private int userId;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "MOBILE_PHONE")
    private String mobilePhone;

    @Column(name = "EMAIL")
    private String eMail;

    @Column(name = "IS_ENABLED")
    private boolean isEnabled;

    @Column(name = "CRYPTO_HASH")
    private String cryptoHash;

    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;

    @Column(name = "SKYPE")
    private String skype;

    @Column(name = "PERSONNEL_NUMBER")
    private String personalNumber;

    @Column(name = "EXTERNAL_REQUISITES")
    private String externalRequisites;

    @Column(name = "AGREEMENT_NUMBER")
    private String agreementNumver;

    @Column(name = "REFRESH_TOKEN_DATE")
    private Date refreshTokenDate;

    @Column(name = "BIRTH_DAY")
    private Date birthDay;

    @Column(name = "IS_FIRED_CHANGE_DATE")
    private Date isFiredChangeDate;

    @Column(name = "AGREEMENT_DATE")
    private Date agreementDate;

    @Column(name = "USER_CODE")
    private String userCode;

    @Column(name = "USE_SMS_CODE_IN_LAN")
    private boolean useSmsCodeInLan;

    @Column(name = "USE_SMS_CODE_IN_WAN")
    private boolean useSmsCodeInWan;

    @Column(name = "IS_WAN_ACCESS_ENABLED")
    private boolean isWanAccessEnabled;

    @Column(name = "ACTIVE_SKILL_ID")
    private Skill activeSkill;

    @Column(name = "IS_FIRED")
    private boolean isFired;

    @Column(name = "USE_QI_WORGROUP_RESTRICTION")
    private boolean useQiWorkgroupRestriction;


    @Column(name = "IS_ABSENT")
    private boolean absent;

    public boolean isAbsent() {
        return absent;
    }

    public void setAbsent(boolean absent) {
        this.absent = absent;
    }

    public Date getIsFiredChangeDate() {
        return isFiredChangeDate;
    }

    public void setIsFiredChangeDate(Date isFiredChangeDate) {
        this.isFiredChangeDate = isFiredChangeDate;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getExternalRequisites() {
        return externalRequisites;
    }

    public void setExternalRequisites(String externalRequisites) {
        this.externalRequisites = externalRequisites;
    }

    public String getAgreementNumver() {
        return agreementNumver;
    }

    public void setAgreementNumver(String agreementNumver) {
        this.agreementNumver = agreementNumver;
    }

    public boolean isUseQiWorkgroupRestriction() {
        return useQiWorkgroupRestriction;
    }

    public void setUseQiWorkgroupRestriction(boolean useQiWorkgroupRestriction) {
        this.useQiWorkgroupRestriction = useQiWorkgroupRestriction;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public Date getCreationDate() {
        return creationDate;
    }

    public String getCreationDate(DateFormat dateFormat) {
        return dateFormat.format(getCreationDate());
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String getIsEnabled(){
        if(isEnabled){
            return "Активен";
        }
        return "Неактивен";
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getCryptoHash() {
        return cryptoHash;
    }

    public void setCryptoHash(String cryptoHash) {
        this.cryptoHash = cryptoHash;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Date getRefreshTokenDate() {
        return refreshTokenDate;
    }

    public void setRefreshTokenDate(Date refreshTokenDate) {
        this.refreshTokenDate = refreshTokenDate;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public String getBirthDay(DateFormat format) {
        return format.format(getBirthDay());
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Date getAgreementDate() {
        return agreementDate;
    }

    public String getAgreementDate(DateFormat format) {
        return format.format(getAgreementDate());
    }

    public void setAgreementDate(Date agreementDate) {
        this.agreementDate = agreementDate;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public boolean isUseSmsCodeInLan() {
        return useSmsCodeInLan;
    }

    public String getUseSmsCodeInLan() {
        if(!useSmsCodeInLan){
            return "* Разрешен вход в приложение вне банковской сети;\n";
        }
        return "* Запрещен вход в приложение вне банковской сети;\n";
    }

    public void setUseSmsCodeInLan(boolean useSmsCodeInLan) {
        this.useSmsCodeInLan = useSmsCodeInLan;
    }

    public boolean isUseSmsCodeInWan() {
        return useSmsCodeInWan;
    }

    public String getUseSmsCodeInWan() {
        if(useSmsCodeInLan){
            return "* Отправляем смс-код из банковской сети;\n";
        }
        return "* Не отправляем смс-код из банковской сети;\n";
    }

    public void setUseSmsCodeInWan(boolean useSmsCodeInWan) {
        this.useSmsCodeInWan = useSmsCodeInWan;
    }

    public boolean isWanAccessEnabled() {
        return isWanAccessEnabled;
    }

    public String getIsWamAccessEnabled() {
        if(!isWanAccessEnabled){
            return "* Отправляем смс-код вне банковской сети;";
        }
        return "* Не отправляем смс-код вне банковской сети;";
    }

    public void setWanAccessEnabled(boolean wanAccessEnabled) {
        isWanAccessEnabled = wanAccessEnabled;
    }

    public Skill getActiveSkill() {
        return activeSkill;
    }

    public void setActiveSkill(Skill activeSkill) {
        this.activeSkill = activeSkill;
    }

    public boolean isFired() {
        return isFired;
    }

    public String getIsFired(){
        if(!isFired){
            return "Работает";
        }
        return "Уволен";
    }

    public void setFired(boolean fired) {
        isFired = fired;
    }
}
