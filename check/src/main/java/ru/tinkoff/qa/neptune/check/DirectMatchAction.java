package ru.tinkoff.qa.neptune.check;

import org.hamcrest.Matcher;

import static org.hamcrest.MatcherAssert.assertThat;

class DirectMatchAction<T> extends MatchAction<T, T> {

    DirectMatchAction(Matcher<? super T> criteria) {
        super("object", criteria);
    }

    @Override
    public void accept(T t) {
        assertThat(t, new InnerMatcher<>(description, criteria));
    }
}
