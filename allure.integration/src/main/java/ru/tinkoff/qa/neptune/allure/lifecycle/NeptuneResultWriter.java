package ru.tinkoff.qa.neptune.allure.lifecycle;

import io.qameta.allure.AllureResultsWriter;
import io.qameta.allure.FileSystemResultsWriter;
import io.qameta.allure.model.TestResult;
import io.qameta.allure.model.TestResultContainer;

import java.io.InputStream;
import java.util.LinkedList;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.tinkoff.qa.neptune.allure.lifecycle.ItemsToNotBeReported.*;

public class NeptuneResultWriter implements AllureResultsWriter {

    private AllureResultsWriter delegateTo;

    public NeptuneResultWriter(FileSystemResultsWriter delegateTo) {
        setResultWriter(delegateTo);
    }

    /**
     * This method was added for testing purposes
     *
     * @param resultWriter an instance of {@link AllureResultsWriter}
     *                     that does all the work actually
     */
    public void setResultWriter(AllureResultsWriter resultWriter) {
        checkNotNull(resultWriter);
        this.delegateTo = resultWriter;
    }

    @Override
    public void write(final TestResult testResult) {
        if (!isExcludedFromReport(testResult)) {
            delegateTo.write(testResult);
        }
    }

    @Override
    public void write(final TestResultContainer testResultContainer) {
        if (isExcludedFromReport(testResultContainer)) {
            return;
        }

        var excludedUUIDs = getExcludedUUIDs();

        var children = new LinkedList<>(testResultContainer.getChildren());
        children.removeIf(excludedUUIDs::contains);
        testResultContainer.setChildren(children);

        var befores = new LinkedList<>(testResultContainer.getBefores());
        var afters = new LinkedList<>(testResultContainer.getAfters());

        befores.removeIf(ItemsToNotBeReported::isExcludedFromReport);
        afters.removeIf(ItemsToNotBeReported::isExcludedFromReport);

        testResultContainer.setBefores(befores);
        testResultContainer.setAfters(afters);

        if (children.isEmpty()) {
            excludeTestResultContainer(testResultContainer);
            return;
        }

        delegateTo.write(testResultContainer);
    }

    @Override
    public void write(String source, InputStream attachment) {
        delegateTo.write(source, attachment);
    }
}
