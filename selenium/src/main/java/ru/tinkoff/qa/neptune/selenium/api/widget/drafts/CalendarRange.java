package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Defines date ranges
 */
public final class CalendarRange {

    private final CalendarDay from;
    private final CalendarDay to;

    private CalendarRange(CalendarDay from, CalendarDay to) {
        checkNotNull(from);
        checkNotNull(to);
        this.from = from;
        this.to = to;
    }

    public static CalendarRange calendarRange(CalendarDay from, CalendarDay to) {
        return new CalendarRange(from, to);
    }

    @Override
    public String toString() {
        return "Calendar Date Range{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CalendarRange that = (CalendarRange) o;

        return new EqualsBuilder()
                .append(from, that.from)
                .append(to, that.to).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(from)
                .append(to)
                .toHashCode();
    }

    public CalendarDay getFrom() {
        return from;
    }

    public CalendarDay getTo() {
        return to;
    }
}
