package ru.tinkoff.qa.neptune.allure;

import ru.tinkoff.qa.neptune.core.api.event.firing.EventLogger;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static io.qameta.allure.Allure.addAttachment;
import static io.qameta.allure.Allure.getLifecycle;
import static io.qameta.allure.model.Status.BROKEN;
import static io.qameta.allure.model.Status.PASSED;
import static io.qameta.allure.util.ResultsUtils.getStatus;
import static io.qameta.allure.util.ResultsUtils.getStatusDetails;
import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.Arrays.stream;

public class AllureEventLogger implements EventLogger {

    private AllureLifecycle allureLifecycle = getLifecycle();
    private final LinkedList<String> stepUIIDs = new LinkedList<>();
    private final HashMap<String, Status> results = new HashMap<>();

    @Override
    public void fireTheEventStarting(String message) {
        var uuid = UUID.randomUUID().toString();
        var result = new StepResult().setName(message).setParameters(List.of());

        if (stepUIIDs.size() == 0) {
            allureLifecycle.startStep(uuid, result);
        }
        else {
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

        var lineSeparator = lineSeparator();
        var log = new StringBuilder();
        stream(throwable.getStackTrace()).forEach(st -> log.append(format("%s%s", st, lineSeparator)));
        addAttachment("Thrown exception:", log.toString());
    }

    @Override
    public void fireReturnedValue(Object returned) {
        if (stepUIIDs.size() == 0) {
            return;
        }

        fireTheEventStarting(format("Returned value: %s", returned));
        fireEventFinishing();

        var uuid = stepUIIDs.getLast();
        allureLifecycle.updateStep(uuid, s -> s.setStatus(PASSED));
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
