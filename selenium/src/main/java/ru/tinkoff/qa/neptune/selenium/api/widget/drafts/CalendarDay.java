package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Defines calendar day in user format
 */
public final class CalendarDay {

    private String seconds;

    private String minutes;

    private String hours;

    private String day;

    private String month;

    private String year;

    @Override
    public String toString() {
        return "Calendar day{" +
                "seconds='" + seconds + '\'' +
                ", minutes='" + minutes + '\'' +
                ", hours='" + hours + '\'' +
                ", day='" + day + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                '}';
    }

    private CalendarDay() {
        super();
    }

    public static CalendarDay calendarDay() {
        return new CalendarDay();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CalendarDay that = (CalendarDay) o;

        return new EqualsBuilder()
                .append(seconds, that.seconds)
                .append(minutes, that.minutes)
                .append(hours, that.hours)
                .append(day, that.day)
                .append(month, that.month)
                .append(year, that.year)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(seconds)
                .append(minutes)
                .append(hours)
                .append(day)
                .append(month)
                .append(year).toHashCode();
    }

    /**
     * Returns seconds value
     *
     * @return calendar seconds
     */
    public String getSeconds() {
        return seconds;
    }

    /**
     * Sets seconds value
     *
     * @param seconds calendar seconds
     * @return self-reference
     */
    public CalendarDay setSeconds(String seconds) {
        this.seconds = seconds;
        return this;
    }

    /**
     * Returns minutes value
     *
     * @return calendar minutes
     */
    public String getMinutes() {
        return minutes;
    }

    /**
     * Sets minutes value
     *
     * @param minutes calendar minutes
     * @return self-reference
     */
    public CalendarDay setMinutes(String minutes) {
        this.minutes = minutes;
        return this;
    }

    /**
     * Returns hours value
     *
     * @return calendar hours
     */
    public String getHours() {
        return hours;
    }

    /**
     * Sets hours value
     *
     * @param hours calendar hours
     * @return self-reference
     */
    public CalendarDay setHours(String hours) {
        this.hours = hours;
        return this;
    }

    /**
     * Returns day value
     *
     * @return calendar day of a month
     */
    public String getDay() {
        return day;
    }

    /**
     * Sets day value
     *
     * @param day calendar day of a month
     * @return self-reference
     */
    public CalendarDay setDay(String day) {
        this.day = day;
        return this;
    }

    /**
     * Returns month value
     *
     * @return calendar month of a year
     */
    public String getMonth() {
        return month;
    }

    /**
     * Sets month value
     *
     * @param month calendar month
     * @return self-reference
     */
    public CalendarDay setMonth(String month) {
        this.month = month;
        return this;
    }

    /**
     * Returns year value
     *
     * @return calendar year
     */
    public String getYear() {
        return year;
    }

    /**
     * Sets year value
     *
     * @param year calendar year
     * @return self-reference
     */
    public CalendarDay setYear(String year) {
        this.year = year;
        return this;
    }
}
