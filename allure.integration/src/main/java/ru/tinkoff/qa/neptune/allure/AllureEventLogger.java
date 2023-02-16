package ru.tinkoff.qa.neptune.allure;

import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.StepResult;
import ru.tinkoff.qa.neptune.core.api.event.firing.EventLogger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import static io.qameta.allure.Allure.addAttachment;
import static io.qameta.allure.Allure.getLifecycle;
import static io.qameta.allure.model.Parameter.Mode.DEFAULT;
import static io.qameta.allure.model.Parameter.Mode.MASKED;
import static io.qameta.allure.model.Status.BROKEN;
import static io.qameta.allure.model.Status.PASSED;
import static io.qameta.allure.util.ResultsUtils.getStatus;
import static io.qameta.allure.util.ResultsUtils.getStatusDetails;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.allure.lifecycle.ItemsToNotBeReported.toReport;
import static ru.tinkoff.qa.neptune.allure.properties.MaskedParametersProperty.MASKED_PARAMETERS;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.checkByStringContainingOrRegExp;
import static ru.tinkoff.qa.neptune.core.api.utils.ToArrayUtil.stringValueOfObjectOrArray;

public class AllureEventLogger implements EventLogger {

    private StepEventLogger delegate;

    public AllureEventLogger() {
        setDelegate(new StepEventLogger());
    }

    @Override
    public void fireTheEventStarting(String message, Map<String, String> parameters) {

        if (!toReport()) {
            return;
        }

        delegate.fireTheEventStarting(message, parameters);
    }

    @Override
    public void fireThrownException(Throwable throwable) {
        if (!toReport()) {
            return;
        }

        delegate.fireThrownException(throwable);
    }

    @Override
    public void fireReturnedValue(String resultDescription, Object returned) {
        if (!toReport()) {
            return;
        }

        delegate.fireReturnedValue(resultDescription, returned);
    }

    @Override
    public void fireEventFinishing() {
        if (!toReport()) {
            return;
        }

        delegate.fireEventFinishing();
    }

    @Override
    public void addParameters(Map<String, String> parameters) {
        if (!toReport()) {
            return;
        }

        delegate.addParameters(parameters);
    }

    static Parameter createParameter(String name, String value) {
        var mode = MASKED_PARAMETERS
            .get()
            .stream()
            .filter(s -> checkByStringContainingOrRegExp(s).test(name))
            .findAny()
            .map(s -> MASKED)
            .orElse(DEFAULT);

        return new Parameter()
            .setName(name)
            .setValue(value)
            .setExcluded(false)
            .setMode(mode);
    }

    void setDelegate(StepEventLogger delegate) {
        this.delegate = delegate;
    }

    static class StepEventLogger implements EventLogger {
        private final AllureLifecycle allureLifecycle = getLifecycle();
        private final static ThreadLocal<Boolean> IS_PASSED = new ThreadLocal<>();

        @Override
        public void fireTheEventStarting(String message, Map<String, String> parameters) {

            var uuid = randomUUID().toString();
            var result = new StepResult().setName(message)
                .setParameters(parameters
                    .entrySet()
                    .stream()
                    .map(e -> createParameter(e.getKey(), e.getValue()))
                    .collect(toList()));

            var caseOrStep = getAllureLifecycle().getCurrentTestCaseOrStep().orElse(null);
            if (nonNull(caseOrStep)) {
                getAllureLifecycle().startStep(caseOrStep, uuid, result);
            } else {
                getAllureLifecycle().startStep(uuid, result);
            }
        }

        @Override
        public void fireThrownException(Throwable throwable) {

            var caseOrStep = getAllureLifecycle().getCurrentTestCaseOrStep().orElse(null);
            if (isNull(caseOrStep)) {
                return;
            }

            getAllureLifecycle().updateStep(caseOrStep, s -> s
                .setStatus(getStatus(throwable).orElse(BROKEN))
                .setStatusDetails(getStatusDetails(throwable).orElse(null)));

            var bos = new ByteArrayOutputStream();
            var ps = new PrintStream(bos, true, UTF_8);
            throwable.printStackTrace(ps);
            String data = bos.toString(UTF_8);
            addAttachment("Thrown exception:", data);
            IS_PASSED.set(false);
        }

        @Override
        public void fireReturnedValue(String resultDescription, Object returned) {

            var caseOrStep = getAllureLifecycle().getCurrentTestCaseOrStep().orElse(null);
            if (isNull(caseOrStep)) {
                return;
            }

            getAllureLifecycle().updateStep(caseOrStep, s -> s.setStatus(PASSED)
                .getParameters()
                .add(createParameter(resultDescription, stringValueOfObjectOrArray(returned))));

            IS_PASSED.set(true);
        }

        @Override
        public void fireEventFinishing() {

            var caseOrStep = getAllureLifecycle().getCurrentTestCaseOrStep().orElse(null);
            if (isNull(caseOrStep)) {
                return;
            }

            if (isNull(IS_PASSED.get())) {

                getAllureLifecycle().updateStep(caseOrStep, stepResult -> {
                    stepResult.setStatus(PASSED);
                });
            }
            IS_PASSED.remove();
            getAllureLifecycle().stopStep(caseOrStep);
        }

        @Override
        public void addParameters(Map<String, String> parameters) {

            var caseOrStep = getAllureLifecycle().getCurrentTestCaseOrStep().orElse(null);
            if (isNull(caseOrStep)) {
                return;
            }

            getAllureLifecycle().updateStep(caseOrStep, stepResult -> {
                var params = stepResult.getParameters();
                params.addAll(parameters
                    .entrySet()
                    .stream()
                    .map(e -> createParameter(e.getKey(), e.getValue()))
                    .collect(toList()));
                stepResult.setParameters(params);
            });

        }

        AllureLifecycle getAllureLifecycle() {
            return allureLifecycle;
        }
    }
}
