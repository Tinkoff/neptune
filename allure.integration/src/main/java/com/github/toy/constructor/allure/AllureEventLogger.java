package com.github.toy.constructor.allure;

import com.github.toy.constructor.core.api.event.firing.EventLogger;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import static io.qameta.allure.Allure.getLifecycle;
import static io.qameta.allure.model.Status.BROKEN;
import static io.qameta.allure.model.Status.PASSED;
import static io.qameta.allure.util.ResultsUtils.getStatus;
import static io.qameta.allure.util.ResultsUtils.getStatusDetails;
import static java.lang.String.format;

public class AllureEventLogger implements EventLogger {

    private AllureLifecycle allureLifecycle = getLifecycle();
    private final LinkedList<String> stepUIIDs = new LinkedList<>();
    private final HashMap<String, Status> results = new HashMap<>();

    @Override
    public void fireTheEventStarting(String message) {
        String uuid = UUID.randomUUID().toString();
        StepResult result = new StepResult()
                .withName(message)
                .withParameters();

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

        String uuid = stepUIIDs.getLast();
        allureLifecycle.updateStep(uuid, s -> s
                .withStatus(getStatus(throwable).orElse(BROKEN))
                .withStatusDetails(getStatusDetails(throwable).orElse(null)));
        results.put(uuid, getStatus(throwable).orElse(BROKEN));
    }

    @Override
    public void fireReturnedValue(Object returned) {
        if (stepUIIDs.size() == 0) {
            return;
        }

        fireTheEventStarting(format("Returned value: %s", returned));
        fireEventFinishing();

        String uuid = stepUIIDs.getLast();
        allureLifecycle.updateStep(uuid, s -> s.withStatus(PASSED));
        results.put(uuid, PASSED);
    }

    @Override
    public void fireEventFinishing() {
        if (stepUIIDs.size() == 0) {
            return;
        }

        String uuid = stepUIIDs.getLast();
        allureLifecycle.updateStep(uuid, stepResult -> {
            if (results.get(uuid) == null) {
                stepResult.withStatus(PASSED);
            }
        });
        allureLifecycle.stopStep(uuid);
        stepUIIDs.removeLast();
    }
}
