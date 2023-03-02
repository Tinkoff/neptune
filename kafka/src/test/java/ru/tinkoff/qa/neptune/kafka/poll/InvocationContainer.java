package ru.tinkoff.qa.neptune.kafka.poll;

import java.util.Date;

class InvocationContainer {

    private Date pollingInvokedAt;

    private Date stepInvokedAt;

    Date getPollingInvokedAt() {
        return pollingInvokedAt;
    }

    InvocationContainer setPollingInvokedAt(Date pollingInvokedAt) {
        this.pollingInvokedAt = pollingInvokedAt;
        return this;
    }

    Date getStepInvokedAt() {
        return stepInvokedAt;
    }

    InvocationContainer setStepInvokedAt(Date stepInvokedAt) {
        this.stepInvokedAt = stepInvokedAt;
        return this;
    }
}
