package ru.tinkoff.qa.neptune.check;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.check.parameter.value.getters.CriteriaCollectionValueGetter;
import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import java.util.Collection;

@Description("Only one of the listed criteria was expected to be matched. Checks of following criteria were positive:\r\n {matchers}")
final class MatchesMoreThanOneCriteria extends MismatchDescriber {

    @DescriptionFragment(value = "matchers", makeReadableBy = CriteriaCollectionValueGetter.class)
    final Collection<Matcher<?>> matchers;

    MatchesMoreThanOneCriteria(Collection<Matcher<?>> matchers) {
        this.matchers = matchers;
    }
}
