package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Matcher;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.hamcrest.AllMatchersParameterValueGetter;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.ElementNotFoundMismatch;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.Duration.ofMillis;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.iterableHasItem;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier.turnReportingOff;

@Description("has child elements {search} {elementsMatcher}")
public final class HasChildElementsMatcher<T extends SearchContext> extends NeptuneFeatureMatcher<SearchContext> {

    @DescriptionFragment(value = "elementsMatcher", makeReadableBy = AllMatchersParameterValueGetter.class)
    final Matcher<?>[] elementsMatcher;
    @DescriptionFragment("search")
    private final MultipleSearchSupplier<T> search;
    private final Matcher<Iterable<T>> iterableMatcher;

    private HasChildElementsMatcher(MultipleSearchSupplier<T> search, Matcher<Iterable<T>> iterableMatcher) {
        super(true, WebElement.class, Widget.class);
        checkArgument(nonNull(search), "The way to find child elements should be defined");
        elementsMatcher = ofNullable(iterableMatcher).map(i -> new Matcher[]{i}).orElseGet(() -> new Matcher[]{});
        this.iterableMatcher = iterableMatcher;
        this.search = turnReportingOff(search.clone().timeOut(ofMillis(0)));
    }

    /**
     * Creates a matcher that checks list of child page elements.
     *
     * @param search          is the way to find desired child elements.
     * @param elementsMatcher criteria for child elements
     * @param <T>             is the type of child elements
     * @return a matcher
     */
    public static <T extends SearchContext> Matcher<SearchContext> hasChildElements(MultipleSearchSupplier<T> search,
                                                                                    Matcher<Iterable<T>> elementsMatcher) {
        return new HasChildElementsMatcher<>(search, elementsMatcher);
    }

    /**
     * Creates a matcher that checks list of child page elements.
     *
     * @param search is the way to find desired child elements.
     * @param <T>    is the type of child elements
     * @return a matcher
     */
    public static <T extends SearchContext> Matcher<SearchContext> hasChildElements(MultipleSearchSupplier<T> search) {
        return hasChildElements(search, (Matcher<Iterable<T>>) null);
    }

    /**
     * Creates a matcher that checks list of child page elements. It is expected that child elements are in same order
     * as {@code elementsMatchers} defined and each one child element meets the corresponding criteria.
     *
     * @param search           is the way to find desired child elements.
     * @param elementsMatchers describe every one distinct child element
     * @param <T>              is the type of child elements
     * @return a matcher
     */
    @SafeVarargs
    public static <T extends SearchContext> Matcher<SearchContext> hasChildElements(MultipleSearchSupplier<T> search,
                                                                                    Matcher<T>... elementsMatchers) {
        return hasChildElements(search, iterableInOrder(elementsMatchers));
    }

    /**
     * Creates a matcher that checks list of child page elements. It is expected that child elements include one that
     * meets defined criteria.
     *
     * @param search          is the way to find desired child elements.
     * @param elementsMatcher description of a child element
     * @param <T>             is the type of child elements
     * @return a matcher
     */
    public static <T extends SearchContext> Matcher<SearchContext> hasChildElements(MultipleSearchSupplier<T> search,
                                                                                    BaseMatcher<T> elementsMatcher) {
        return hasChildElements(search, iterableHasItem(elementsMatcher));
    }

    @Override
    protected boolean featureMatches(SearchContext toMatch) {
        var found = search.get().apply(toMatch);
        if (found.isEmpty()) {
            appendMismatchDescription(new ElementNotFoundMismatch(search));
            return false;
        }

        if (iterableMatcher != null) {
            if (!iterableMatcher.matches(found)) {
                appendMismatchDescription(iterableMatcher, found);
                return false;
            }
        }
        return true;
    }
}
