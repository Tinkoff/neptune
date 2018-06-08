package com.github.toy.constructor.selenium.functions.searching;

import com.github.toy.constructor.selenium.api.widget.Widget;

import java.util.Comparator;

import static com.github.toy.constructor.selenium.api.widget.Priority.Reader.getPriority;

class WidgetPriorityComparator implements Comparator<Class<? extends Widget>> {

    static WidgetPriorityComparator widgetPriorityComparator() {
        return new WidgetPriorityComparator();
    }

    @Override
    public int compare(Class<? extends Widget> o1, Class<? extends Widget> o2) {
        int priority1 = getPriority(o1);
        int priority2 = getPriority(o2);
        if (priority1 > priority2) {
            return 1;
        }
        else if (priority1 == priority2) {
            return 0;
        }
        else {
            return -1;
        }
    }
}
