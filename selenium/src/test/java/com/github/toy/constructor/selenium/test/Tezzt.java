package com.github.toy.constructor.selenium.test;

import com.github.toy.constructor.core.api.proxy.ConstructorParameters;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.api.widget.drafts.Button;
import com.github.toy.constructor.selenium.api.widget.drafts.Link;
import com.github.toy.constructor.selenium.functions.searching.SequentialSearchSupplier;
import org.openqa.selenium.By;

import java.util.List;

import static com.github.toy.constructor.core.api.proxy.ConstructorParameters.params;
import static com.github.toy.constructor.core.api.proxy.Substitution.getSubstituted;
import static com.github.toy.constructor.selenium.functions.searching.MultipleSearchSupplier.links;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.button;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.link;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.webElement;
import static com.github.toy.constructor.selenium.functions.searching.SequentialMultipleSearchSupplier.elements;
import static com.github.toy.constructor.selenium.functions.searching.SequentialSearchSupplier.element;
import static org.openqa.selenium.By.xpath;

public class Tezzt {

    public void tezzt() throws Exception {
        SeleniumSteps selenium = getSubstituted(SeleniumSteps.class, params());
        Button button = selenium.find(element(button())
                .from(link())
                .from(webElement(xpath(""))));
        List<Link> links = selenium.find(elements(links())
                .from(button)
                .from(webElement(xpath(""))));
    }
}