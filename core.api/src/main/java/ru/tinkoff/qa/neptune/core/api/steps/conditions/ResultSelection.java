package ru.tinkoff.qa.neptune.core.api.steps.conditions;


import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;

/**
 * This class describes selection rule to return the result
 *
 * @param <T> is the type of the evaluation argument
 * @param <R> is the type of the evaluation result
 */
public abstract class ResultSelection<T, R> implements StepParameterPojo {

    private T toSelectFrom;
    private boolean isValidated;
    private boolean isChecked;

    protected R evaluate(T toSelectFrom) {
        if (!isValidated) {
            validateParameters();
            isValidated = true;
        }

        this.toSelectFrom = toSelectFrom;
        try {
            return evaluateResultSelection(toSelectFrom);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        } finally {
            isChecked = true;
        }
    }

    /**
     * Performs the evaluation of the final result to be returned.
     *
     * @param toSelectFrom is the argument of result evaluation
     * @return evaluated result. When it is impossible to evaluate that value
     * then {@code null} should be returned
     */
    protected abstract R evaluateResultSelection(T toSelectFrom);

    /**
     * Returns a message of thrown {@link ru.tinkoff.qa.neptune.core.api.steps.NotPresentException} when
     * it is impossible to evaluate the result
     *
     * @return message of {@link ru.tinkoff.qa.neptune.core.api.steps.NotPresentException}
     * to be thrown
     */
    protected abstract String mismatchMessage();

    /**
     * Validation of selection parameters. When parameters are not
     * valid then {@link IllegalArgumentException} should be thrown
     */
    protected abstract void validateParameters() throws IllegalArgumentException;

    /**
     * Returns value of result evaluation argument that is caught last time.
     *
     * @return value of result evaluation argument that is caught last time
     */
    public final T getLastSelectionArgumentValue() {
        return toSelectFrom;
    }

    final void clear() {
        toSelectFrom = null;
        isChecked = false;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setNotValidated() {
        isValidated = false;
    }
}
