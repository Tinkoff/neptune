package ru.tinkoff.qa.neptune.selenium.functions.searching;

import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

import java.util.Comparator;

import static ru.tinkoff.qa.neptune.selenium.api.widget.Priority.Reader.getPriority;

class WidgetPriorityComparator implements Comparator<Class<? extends Widget>> {

    static WidgetPriorityComparator widgetPriorityComparator() {
        return new WidgetPriorityComparator();
    }

    @Override
    public int compare(Class<? extends Widget> o1, Class<? extends Widget> o2) {
        int priority1 = getPriority(o1);
        int priority2 = getPriority(o2);
        return Integer.compare(priority1, priority2);
    }
}
