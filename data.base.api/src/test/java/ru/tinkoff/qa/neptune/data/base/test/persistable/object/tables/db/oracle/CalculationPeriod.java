package ru.tinkoff.qa.neptune.data.base.test.persistable.object.tables.db.oracle;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.text.DateFormat;
import java.util.Date;

import static javax.jdo.annotations.IdGeneratorStrategy.IDENTITY;

@PersistenceCapable(table = "CALCULATION_PERIOD")
public class CalculationPeriod extends PersistableObject {

    @PrimaryKey
    @Persistent(valueStrategy = IDENTITY)
    @Column(name = "CALCULATION_PERIOD_ID")
    private Integer calculationPeriodId;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "IS_CLOSED")
    private boolean isClosed;

    @Column(name = "LIMIT_EVALUATION")
    private int limitEvaluation;

    public int getCalculationPeriodId() {
        return calculationPeriodId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getStartDate(DateFormat format) {
        return format.format(getStartDate());
    }

    public CalculationPeriod setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getEndDate(DateFormat format) {
        return format.format(endDate);
    }

    public CalculationPeriod setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public CalculationPeriod setClosed(boolean closed) {
        isClosed = closed;
        return this;
    }

    public int getLimitEvaluation() {
        return limitEvaluation;
    }

    public CalculationPeriod setLimitEvaluation(int limitEvaluation) {
        this.limitEvaluation = limitEvaluation;
        return this;
    }
}
