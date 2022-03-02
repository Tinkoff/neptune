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
import java.util.UUID;

import static io.qameta.allure.Allure.addAttachment;
import static io.qameta.allure.Allure.getLifecycle;
import static io.qameta.allure.model.Status.BROKEN;
import static io.qameta.allure.model.Status.PASSED;
import static io.qameta.allure.util.ResultsUtils.getStatus;
import static io.qameta.allure.util.ResultsUtils.getStatusDetails;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.core.api.utils.ToArrayUtil.stringValueOfObjectOrArray;

public class AllureEventLogger implements EventLogger {

    private final AllureLifecycle allureLifecycle = getLifecycle();
    private final LinkedList<String> stepUIIDs = new LinkedList<>();
    private final HashMap<String, Status> results = new HashMap<>();

    @Override
    public void fireTheEventStarting(String message, Map<String, String> parameters) {
        var uuid = UUID.randomUUID().toString();
        var result = new StepResult().setName(message)
                .setParameters(parameters
                        .entrySet()
                        .stream()
                        .map(e -> new Parameter()
                                .setName(e.getKey())
                                .setValue(e.getValue()))
                        .collect(toList()));

        if (stepUIIDs.isEmpty()) {
            allureLifecycle.startStep(uuid, result);
        } else {
            allureLifecycle.startStep(stepUIIDs.getLast(), uuid, result);
        }
        stepUIIDs.addLast(uuid);
        results.put(uuid, null);
    }

    @Override
    public void fireThrownException(Throwable throwable) {
        if (stepUIIDs.isEmpty()) {
            return;
        }

        var uuid = stepUIIDs.getLast();
        allureLifecycle.updateStep(uuid, s -> s
                .setStatus(getStatus(throwable).orElse(BROKEN))
                .setStatusDetails(getStatusDetails(throwable).orElse(null)));
        results.put(uuid, getStatus(throwable).orElse(BROKEN));

        var bos = new ByteArrayOutputStream();
        var ps = new PrintStream(bos, true, UTF_8);
        throwable.printStackTrace(ps);
        String data = bos.toString(UTF_8);
        addAttachment("Thrown exception:", data);
    }

    @Override
    public void fireReturnedValue(String resultDescription, Object returned) {
        if (stepUIIDs.isEmpty()) {
            return;
        }

        var uuid = stepUIIDs.getLast();
        allureLifecycle.updateStep(uuid, s -> s.setStatus(PASSED)
                .getParameters()
                .add(new Parameter()
                        .setName(resultDescription)
                        .setValue(stringValueOfObjectOrArray(returned))));
        results.put(uuid, PASSED);
    }

    @Override
    public void fireEventFinishing() {
        if (stepUIIDs.isEmpty()) {
            return;
        }

        var uuid = stepUIIDs.getLast();
        allureLifecycle.updateStep(uuid, stepResult -> {
            if (results.get(uuid) == null) {
                stepResult.setStatus(PASSED);
            }
        });
        allureLifecycle.stopStep(uuid);
        stepUIIDs.removeLast();
    }

    @Override
    public void addParameters(Map<String, String> parameters) {
        if (stepUIIDs.isEmpty()) {
            return;
        }

        var uuid = stepUIIDs.getLast();
        allureLifecycle.updateStep(uuid, stepResult -> {
            var params = stepResult.getParameters();
            params.addAll(parameters
                    .entrySet()
                    .stream()
                    .map(e -> new Parameter().setName(e.getKey()).setValue(e.getValue()))
                    .collect(toList()));
            stepResult.setParameters(params);
        });

    }
}
