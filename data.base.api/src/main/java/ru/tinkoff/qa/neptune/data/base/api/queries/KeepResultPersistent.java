package ru.tinkoff.qa.neptune.data.base.api.queries;

public class KeepResultPersistent {

    private boolean toKeepOnPersistent;

    KeepResultPersistent() {
        super();
    }

    public boolean toKeepOnPersistent() {
        return toKeepOnPersistent;
    }

    public void setToKeepOnPersistent(boolean toKeepOnPersistent) {
        this.toKeepOnPersistent = toKeepOnPersistent;
    }
}
