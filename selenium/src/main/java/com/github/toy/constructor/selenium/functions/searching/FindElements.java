package com.github.toy.constructor.selenium.functions.searching;

import org.openqa.selenium.SearchContext;

import java.util.List;
import java.util.function.Function;

class FindElements<T extends SearchContext> implements Function<SearchContext, List<T>> {
    @Override
    public List<T> apply(SearchContext searchContext) {
        //TODO TO BE IMPLEMENTED
        return null;
    }
}
