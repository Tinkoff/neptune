package ru.tinkoff.qa.neptune.selenium.functions.searching;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class FindElementsBuilder<R, S extends Iterable<R>> implements Function<SearchContext, S> {
    @StepParameter("Above")
    private List<Object> above = new ArrayList<>();
    @StepParameter("Below")
    private List<Object> below = new ArrayList<>();
    @StepParameter("Left of")
    private List<Object> toLeftOf = new ArrayList<>();
    @StepParameter("Right of")
    private List<Object> toRightOf = new ArrayList<>();
    @StepParameter("Near")
    private List<Object> near = new ArrayList<>();
    @StepParameter("Near with distance")
    private Map<Object, Integer> nearWithDistance = new HashMap<>();

    abstract void buildLocator(SearchContext searchContext);

    WebElement performFind(SearchContext searchContext, SearchSupplier<?> find) {
        var found = find.get().apply(searchContext);
        if (found instanceof WrapsElement) {
            return ((WrapsElement) found).getWrappedElement();
        }
        return (WebElement) found;
    }

    public FindElementsBuilder<R, S> above(Object above) {
        this.above.add(above);
        return this;
    }

    public FindElementsBuilder<R, S> below(Object below) {
        this.below.add(below);
        return this;
    }

    public FindElementsBuilder<R, S> toLeftOf(Object toLeftOf) {
        this.toLeftOf.add(toLeftOf);
        return this;
    }

    public FindElementsBuilder<R, S> toRightOf(Object toRightOf) {
        this.toRightOf.add(toRightOf);
        return this;
    }

    public FindElementsBuilder<R, S> near(Object near) {
        this.near.add(near);
        return this;
    }

    public FindElementsBuilder<R, S> near(Object near, Integer distance) {
        this.nearWithDistance.put(near, distance);
        return this;
    }


    public List<Object> getAbove() {
        return above;
    }

    public List<Object> getBelow() {
        return below;
    }

    public List<Object> getToLeftOf() {
        return toLeftOf;
    }

    public List<Object> getToRightOf() {
        return toRightOf;
    }

    public List<Object> getNear() {
        return near;
    }

    public Map<Object, Integer> getNearWithDistance() {
        return nearWithDistance;
    }
}
