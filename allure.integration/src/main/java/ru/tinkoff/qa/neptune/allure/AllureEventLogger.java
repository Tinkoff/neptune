package ru.tinkoff.qa.neptune.allure;

import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import ru.tinkoff.qa.neptune.core.api.event.firing.EventLogger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static io.qameta.allure.Allure.addAttachment;
import static io.qameta.allure.Allure.getLifecycle;
import static io.qameta.allure.model.Status.BROKEN;
import static io.qameta.allure.model.Status.PASSED;
import static io.qameta.allure.util.ResultsUtils.getStatus;
import static io.qameta.allure.util.ResultsUtils.getStatusDetails;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.core.api.utils.ToArrayUtil.stringValueOfObjectOrArray;

public class AllureEventLogger implements EventLogger {

    private final AllureLifecycle allureLifecycle = getLifecycle();
    private final LinkedList<String> stepUIIDs = new LinkedList<>();
    private final HashMap<String, Status> results = new HashMap<>();

    @Override
    public void fireTheEventStarting(String message, Map<String, String> parameters) {
        var uuid = randomUUID().toString();
        var result = new StepResult().setName(message)
                .setParameters(parameters
                        .entrySet()
                        .stream()
                        .map(e -> new Parameter()
                                .setName(e.getKey())
                                .setValue(e.getValue()))
                        .collect(toList()));

        if (getStepUIIDs().isEmpty()) {
            getAllureLifecycle().startStep(uuid, result);
        } else {
            getAllureLifecycle().startStep(getStepUIIDs().getLast(), uuid, result);
        }
        getStepUIIDs().addLast(uuid);
        getResults().put(uuid, null);
    }

    @Override
    public void fireThrownException(Throwable throwable) {
        if (getStepUIIDs().isEmpty()) {
            return;
        }

        var uuid = getStepUIIDs().getLast();
        getAllureLifecycle().updateStep(uuid, s -> s
                .setStatus(getStatus(throwable).orElse(BROKEN))
                .setStatusDetails(getStatusDetails(throwable).orElse(null)));
        getResults().put(uuid, getStatus(throwable).orElse(BROKEN));

        var bos = new ByteArrayOutputStream();
        var ps = new PrintStream(bos, true, UTF_8);
        throwable.printStackTrace(ps);
        String data = bos.toString(UTF_8);
        addAttachment("Thrown exception:", data);
    }

    @Override
    public void fireReturnedValue(String resultDescription, Object returned) {
        if (getStepUIIDs().isEmpty()) {
            return;
        }

        var uuid = getStepUIIDs().getLast();
        getAllureLifecycle().updateStep(uuid, s -> s.setStatus(PASSED)
                .getParameters()
                .add(new Parameter()
                        .setName(resultDescription)
                        .setValue(stringValueOfObjectOrArray(returned))));
        getResults().put(uuid, PASSED);
    }

    @Override
    public void fireEventFinishing() {
        if (getStepUIIDs().isEmpty()) {
            return;
        }

        var uuid = getStepUIIDs().getLast();
        getAllureLifecycle().updateStep(uuid, stepResult -> {
            if (getResults().get(uuid) == null) {
                stepResult.setStatus(PASSED);
            }
        });
        getAllureLifecycle().stopStep(uuid);
        getStepUIIDs().removeLast();
    }

    @Override
    public void addParameters(Map<String, String> parameters) {
        if (getStepUIIDs().isEmpty()) {
            return;
        }

        var uuid = getStepUIIDs().getLast();
        getAllureLifecycle().updateStep(uuid, stepResult -> {
            var params = stepResult.getParameters();
            params.addAll(parameters
                    .entrySet()
                    .stream()
                    .map(e -> new Parameter().setName(e.getKey()).setValue(e.getValue()))
                    .collect(toList()));
            stepResult.setParameters(params);
        });

    }

    AllureLifecycle getAllureLifecycle() {
        return allureLifecycle;
    }

    LinkedList<String> getStepUIIDs() {
        return stepUIIDs;
    }

    Map<String, Status> getResults() {
        return results;
    }
}
