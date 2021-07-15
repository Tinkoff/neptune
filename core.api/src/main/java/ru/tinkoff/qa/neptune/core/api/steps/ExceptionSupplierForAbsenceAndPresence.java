package ru.tinkoff.qa.neptune.core.api.steps;

import static org.apache.commons.lang3.StringUtils.SPACE;

class ExceptionSupplierForAbsenceAndPresence extends ExceptionSupplier {

    ExceptionSupplierForAbsenceAndPresence(SequentialGetStepSupplier<?, ?, ?, ?, ?> stepThatCanBeFailed) {
        super(stepThatCanBeFailed);
    }

    @Override
    String getExceptionMessage(String messageStarting) {
        var stringBuilder = new StringBuilder(messageStarting)
                .append(SPACE)
                .append(((SequentialGetStepSupplier<?, ?, ?, ?, ?>) stepThatCanBeFailed.from).getDescription());
        stepThatCanBeFailed.getParameters().forEach((key, value) -> stringBuilder.append("\r\n")
                .append("- ")
                .append(key)
                .append(":")
                .append(value));
        return stringBuilder.toString();
    }
}
