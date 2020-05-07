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
import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

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

        if (stepUIIDs.size() == 0) {
            allureLifecycle.startStep(uuid, result);
        } else {
            allureLifecycle.startStep(stepUIIDs.getLast(), uuid, result);
        }
        stepUIIDs.addLast(uuid);
        results.put(uuid, null);
    }

    @Override
    public void fireThrownException(Throwable throwable) {
        if (stepUIIDs.size() == 0) {
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
        String data = new String(bos.toByteArray(), UTF_8);
        addAttachment("Thrown exception:", data);
    }

    @Override
    public void fireReturnedValue(Object returned) {
        if (stepUIIDs.size() == 0) {
            return;
        }

        var uuid = stepUIIDs.getLast();
        allureLifecycle.updateStep(uuid, s -> s.setStatus(PASSED)
                .getParameters()
                .add(new Parameter()
                        .setName("RETURNED VALUE")
                        .setValue(valueOf(returned))));
        results.put(uuid, PASSED);
    }

    @Override
    public void fireEventFinishing() {
        if (stepUIIDs.size() == 0) {
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
}
