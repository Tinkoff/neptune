package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.steps.conditions.ResultSelection;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetConditionalHelper.*;

@SuppressWarnings("unchecked")
public interface FunctionFactory<T, S, R, P> {

    Function<T, R> createFunction(Function<T, S> originalFunction,
                                  Predicate<P> predicate,
                                  Duration wait,
                                  Duration sleep,
                                  Supplier<? extends RuntimeException> exceptionSupplier,
                                  Collection<Class<? extends Throwable>> toIgnore);

    Function<T, R> createFunction(Function<T, S> originalFunction,
                                  Predicate<P> predicate,
                                  Duration wait,
                                  Duration sleep,
                                  Collection<Class<? extends Throwable>> toIgnore);

    Function<T, R> createFunction(Function<T, S> originalFunction,
                                  Predicate<P> predicate,
                                  Duration wait,
                                  Supplier<? extends RuntimeException> exceptionSupplier,
                                  Collection<Class<? extends Throwable>> toIgnore);

    Function<T, R> createFunction(Function<T, S> originalFunction,
                                  Predicate<P> predicate,
                                  Duration wait,
                                  Collection<Class<? extends Throwable>> toIgnore);

    Function<T, R> createFunction(Function<T, S> originalFunction,
                                  Predicate<P> predicate,
                                  Supplier<? extends RuntimeException> exceptionSupplier,
                                  Collection<Class<? extends Throwable>> toIgnore);

    Function<T, R> createFunction(Function<T, S> originalFunction,
                                  Predicate<P> predicate,
                                  Collection<Class<? extends Throwable>> toIgnore);

    Function<T, R> createFunction(Function<T, S> originalFunction,
                                  Duration wait,
                                  Duration sleep,
                                  Supplier<? extends RuntimeException> exceptionSupplier,
                                  Collection<Class<? extends Throwable>> toIgnore);

    Function<T, R> createFunction(Function<T, S> originalFunction,
                                  Duration wait,
                                  Duration sleep,
                                  Collection<Class<? extends Throwable>> toIgnore);

    Function<T, R> createFunction(Function<T, S> originalFunction,
                                  Duration wait,
                                  Supplier<? extends RuntimeException> exceptionSupplier,
                                  Collection<Class<? extends Throwable>> toIgnore);

    Function<T, R> createFunction(Function<T, S> originalFunction,
                                  Duration wait,
                                  Collection<Class<? extends Throwable>> toIgnore);

    Function<T, R> createFunction(Function<T, S> originalFunction,
                                  Supplier<? extends RuntimeException> exceptionSupplier,
                                  Collection<Class<? extends Throwable>> toIgnore);

    Function<T, R> createFunction(Function<T, S> originalFunction,
                                  Collection<Class<? extends Throwable>> toIgnore);

    class ObjectFunctionFactory<T, R> implements FunctionFactory<T, R, R, R> {

        @Override
        public Function<T, R> createFunction(Function<T, R> originalFunction,
                                             Predicate<R> predicate,
                                             Duration wait,
                                             Duration sleep,
                                             Supplier<? extends RuntimeException> exceptionSupplier,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getSingle(originalFunction, predicate, wait, sleep, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R> originalFunction,
                                             Predicate<R> predicate,
                                             Duration wait,
                                             Duration sleep,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getSingle(originalFunction, predicate, wait, sleep, null, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R> originalFunction,
                                             Predicate<R> predicate,
                                             Duration wait,
                                             Supplier<? extends RuntimeException> exceptionSupplier,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getSingle(originalFunction, predicate, wait, null, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R> originalFunction,
                                             Predicate<R> predicate,
                                             Duration wait,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getSingle(originalFunction, predicate, wait, null, null, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R> originalFunction,
                                             Predicate<R> predicate,
                                             Supplier<? extends RuntimeException> exceptionSupplier,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getSingle(originalFunction, predicate, null, null, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R> originalFunction,
                                             Predicate<R> predicate,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getSingle(originalFunction, predicate, null, null, null, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R> originalFunction,
                                             Duration wait,
                                             Duration sleep,
                                             Supplier<? extends RuntimeException> exceptionSupplier,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getSingle(originalFunction, (Predicate<? super R>) AS_IS, wait, sleep, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R> originalFunction,
                                             Duration wait,
                                             Duration sleep,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getSingle(originalFunction, (Predicate<? super R>) AS_IS, wait, sleep, null, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R> originalFunction,
                                             Duration wait,
                                             Supplier<? extends RuntimeException> exceptionSupplier,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getSingle(originalFunction, (Predicate<? super R>) AS_IS, wait, null, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R> originalFunction,
                                             Duration wait,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getSingle(originalFunction, (Predicate<? super R>) AS_IS, wait, null, null, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R> originalFunction,
                                             Supplier<? extends RuntimeException> exceptionSupplier,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getSingle(originalFunction, (Predicate<? super R>) AS_IS, null, null, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R> originalFunction,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getSingle(originalFunction, (Predicate<? super R>) AS_IS, null, null, null, toIgnore);
        }
    }

    abstract class IterableFunctionFactory<T, S, R, P> implements FunctionFactory<T, S, R, P> {

        ResultSelection<R, R> resultSelection;
        boolean ignoreSelectionParameters;

        void ignoreSelectionParameters() {
            this.ignoreSelectionParameters = true;
        }

        ResultSelection<R, R> getResultSelection() {
            return resultSelection;
        }

        void setResultSelection(ResultSelection<R, R> resultSelection) {
            this.resultSelection = resultSelection;
        }

        ResultSelection<R, R> returnResultSelection() {
            if (!ignoreSelectionParameters) {
                return resultSelection;
            }
            return null;
        }
    }

    class ListFunctionFactory<T, R, S extends Iterable<R>> extends IterableFunctionFactory<T, S, List<R>, R> {

        @Override
        public Function<T, List<R>> createFunction(Function<T, S> originalFunction,
                                                   Predicate<R> predicate,
                                                   Duration wait,
                                                   Duration sleep,
                                                   Supplier<? extends RuntimeException> exceptionSupplier,
                                                   Collection<Class<? extends Throwable>> toIgnore) {
            return getList(originalFunction, predicate, returnResultSelection(), wait, sleep, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, List<R>> createFunction(Function<T, S> originalFunction,
                                                   Predicate<R> predicate,
                                                   Duration wait,
                                                   Duration sleep,
                                                   Collection<Class<? extends Throwable>> toIgnore) {
            return getList(originalFunction, predicate, returnResultSelection(), wait, sleep, null, toIgnore);
        }

        @Override
        public Function<T, List<R>> createFunction(Function<T, S> originalFunction,
                                                   Predicate<R> predicate,
                                                   Duration wait,
                                                   Supplier<? extends RuntimeException> exceptionSupplier,
                                                   Collection<Class<? extends Throwable>> toIgnore) {
            return getList(originalFunction, predicate, returnResultSelection(), wait, null, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, List<R>> createFunction(Function<T, S> originalFunction,
                                                   Predicate<R> predicate,
                                                   Duration wait,
                                                   Collection<Class<? extends Throwable>> toIgnore) {
            return getList(originalFunction, predicate, returnResultSelection(), wait, null, null, toIgnore);
        }

        @Override
        public Function<T, List<R>> createFunction(Function<T, S> originalFunction,
                                                   Predicate<R> predicate,
                                                   Supplier<? extends RuntimeException> exceptionSupplier,
                                                   Collection<Class<? extends Throwable>> toIgnore) {
            return getList(originalFunction, predicate, returnResultSelection(), null, null, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, List<R>> createFunction(Function<T, S> originalFunction,
                                                   Predicate<R> predicate,
                                                   Collection<Class<? extends Throwable>> toIgnore) {
            return getList(originalFunction, predicate, returnResultSelection(), null, null, null, toIgnore);
        }

        @Override
        public Function<T, List<R>> createFunction(Function<T, S> originalFunction,
                                                   Duration wait,
                                                   Duration sleep,
                                                   Supplier<? extends RuntimeException> exceptionSupplier,
                                                   Collection<Class<? extends Throwable>> toIgnore) {
            return getList(originalFunction, (Predicate<? super R>) AS_IS, returnResultSelection(), wait, sleep, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, List<R>> createFunction(Function<T, S> originalFunction,
                                                   Duration wait,
                                                   Duration sleep,
                                                   Collection<Class<? extends Throwable>> toIgnore) {
            return getList(originalFunction, (Predicate<? super R>) AS_IS, returnResultSelection(), wait, sleep, null, toIgnore);
        }

        @Override
        public Function<T, List<R>> createFunction(Function<T, S> originalFunction,
                                                   Duration wait,
                                                   Supplier<? extends RuntimeException> exceptionSupplier,
                                                   Collection<Class<? extends Throwable>> toIgnore) {
            return getList(originalFunction, (Predicate<? super R>) AS_IS, returnResultSelection(), wait, null, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, List<R>> createFunction(Function<T, S> originalFunction,
                                                   Duration wait, Collection<Class<? extends Throwable>> toIgnore) {
            return getList(originalFunction, (Predicate<? super R>) AS_IS, returnResultSelection(), wait, null, null, toIgnore);
        }

        @Override
        public Function<T, List<R>> createFunction(Function<T, S> originalFunction,
                                                   Supplier<? extends RuntimeException> exceptionSupplier,
                                                   Collection<Class<? extends Throwable>> toIgnore) {
            return getList(originalFunction, (Predicate<? super R>) AS_IS, returnResultSelection(), null, null, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, List<R>> createFunction(Function<T, S> originalFunction,
                                                   Collection<Class<? extends Throwable>> toIgnore) {
            return getList(originalFunction, (Predicate<? super R>) AS_IS, returnResultSelection(), null, null, null, toIgnore);
        }
    }

    class ArrayFunctionFactory<T, R> extends IterableFunctionFactory<T, R[], R[], R> {

        @Override
        public Function<T, R[]> createFunction(Function<T, R[]> originalFunction,
                                               Predicate<R> predicate,
                                               Duration wait,
                                               Duration sleep,
                                               Supplier<? extends RuntimeException> exceptionSupplier,
                                               Collection<Class<? extends Throwable>> toIgnore) {
            return getArray(originalFunction, predicate, returnResultSelection(), wait, sleep, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R[]> createFunction(Function<T, R[]> originalFunction,
                                               Predicate<R> predicate,
                                               Duration wait,
                                               Duration sleep,
                                               Collection<Class<? extends Throwable>> toIgnore) {
            return getArray(originalFunction, predicate, returnResultSelection(), wait, sleep, null, toIgnore);
        }

        @Override
        public Function<T, R[]> createFunction(Function<T, R[]> originalFunction,
                                               Predicate<R> predicate,
                                               Duration wait,
                                               Supplier<? extends RuntimeException> exceptionSupplier,
                                               Collection<Class<? extends Throwable>> toIgnore) {
            return getArray(originalFunction, predicate, returnResultSelection(), wait, null, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R[]> createFunction(Function<T, R[]> originalFunction,
                                               Predicate<R> predicate,
                                               Duration wait,
                                               Collection<Class<? extends Throwable>> toIgnore) {
            return getArray(originalFunction, predicate, returnResultSelection(), wait, null, null, toIgnore);
        }

        @Override
        public Function<T, R[]> createFunction(Function<T, R[]> originalFunction,
                                               Predicate<R> predicate,
                                               Supplier<? extends RuntimeException> exceptionSupplier,
                                               Collection<Class<? extends Throwable>> toIgnore) {
            return getArray(originalFunction, predicate, returnResultSelection(), null, null, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R[]> createFunction(Function<T, R[]> originalFunction,
                                               Predicate<R> predicate,
                                               Collection<Class<? extends Throwable>> toIgnore) {
            return getArray(originalFunction, predicate, returnResultSelection(), null, null, null, toIgnore);
        }

        @Override
        public Function<T, R[]> createFunction(Function<T, R[]> originalFunction,
                                               Duration wait,
                                               Duration sleep,
                                               Supplier<? extends RuntimeException> exceptionSupplier,
                                               Collection<Class<? extends Throwable>> toIgnore) {
            return getArray(originalFunction, (Predicate<? super R>) AS_IS, returnResultSelection(), wait, sleep, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R[]> createFunction(Function<T, R[]> originalFunction,
                                               Duration wait,
                                               Duration sleep,
                                               Collection<Class<? extends Throwable>> toIgnore) {
            return getArray(originalFunction, (Predicate<? super R>) AS_IS, returnResultSelection(), wait, sleep, null, toIgnore);
        }

        @Override
        public Function<T, R[]> createFunction(Function<T, R[]> originalFunction,
                                               Duration wait,
                                               Supplier<? extends RuntimeException> exceptionSupplier,
                                               Collection<Class<? extends Throwable>> toIgnore) {
            return getArray(originalFunction, (Predicate<? super R>) AS_IS, returnResultSelection(), wait, null, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R[]> createFunction(Function<T, R[]> originalFunction,
                                               Duration wait,
                                               Collection<Class<? extends Throwable>> toIgnore) {
            return getArray(originalFunction, (Predicate<? super R>) AS_IS, returnResultSelection(), wait, null, null, toIgnore);
        }

        @Override
        public Function<T, R[]> createFunction(Function<T, R[]> originalFunction,
                                               Supplier<? extends RuntimeException> exceptionSupplier,
                                               Collection<Class<? extends Throwable>> toIgnore) {
            return getArray(originalFunction, (Predicate<? super R>) AS_IS, returnResultSelection(), null, null, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R[]> createFunction(Function<T, R[]> originalFunction,
                                               Collection<Class<? extends Throwable>> toIgnore) {
            return getArray(originalFunction, (Predicate<? super R>) AS_IS, returnResultSelection(), null, null, null, toIgnore);
        }
    }

    abstract class ItemFunctionFactory<T, S, R, P> implements FunctionFactory<T, S, R, P> {
        ResultSelection<S, R> resultSelection;
        boolean ignoreSelectionParameters;

        void ignoreSelectionParameters() {
            this.ignoreSelectionParameters = true;
        }

        ResultSelection<S, R> getResultSelection() {
            return resultSelection;
        }

        void setResultSelection(ResultSelection<S, R> resultSelection) {
            this.resultSelection = resultSelection;
        }

        ResultSelection<S, R> returnResultSelection() {
            if (!ignoreSelectionParameters) {
                return resultSelection;
            }
            return null;
        }
    }

    class IterableItemFunctionFactory<T, R, S extends Iterable<R>> extends ItemFunctionFactory<T, S, R, R> {

        @Override
        public Function<T, R> createFunction(Function<T, S> originalFunction,
                                             Predicate<R> predicate,
                                             Duration wait,
                                             Duration sleep,
                                             Supplier<? extends RuntimeException> exceptionSupplier,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getFromIterable(originalFunction, predicate, (ResultSelection<Iterable<R>, R>) returnResultSelection(), wait, sleep, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, S> originalFunction,
                                             Predicate<R> predicate,
                                             Duration wait,
                                             Duration sleep,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getFromIterable(originalFunction, predicate, (ResultSelection<Iterable<R>, R>) returnResultSelection(), wait, sleep, null, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, S> originalFunction,
                                             Predicate<R> predicate,
                                             Duration wait,
                                             Supplier<? extends RuntimeException> exceptionSupplier,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getFromIterable(originalFunction, predicate, (ResultSelection<Iterable<R>, R>) returnResultSelection(), wait, null, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, S> originalFunction,
                                             Predicate<R> predicate,
                                             Duration wait,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getFromIterable(originalFunction, predicate, (ResultSelection<Iterable<R>, R>) returnResultSelection(), wait, null, null, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, S> originalFunction,
                                             Predicate<R> predicate,
                                             Supplier<? extends RuntimeException> exceptionSupplier,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getFromIterable(originalFunction, predicate, (ResultSelection<Iterable<R>, R>) returnResultSelection(), null, null, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, S> originalFunction,
                                             Predicate<R> predicate,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getFromIterable(originalFunction, predicate, (ResultSelection<Iterable<R>, R>) returnResultSelection(), null, null, null, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, S> originalFunction,
                                             Duration wait,
                                             Duration sleep,
                                             Supplier<? extends RuntimeException> exceptionSupplier,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getFromIterable(originalFunction, (Predicate<? super R>) AS_IS, (ResultSelection<Iterable<R>, R>) returnResultSelection(), wait, sleep, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, S> originalFunction,
                                             Duration wait,
                                             Duration sleep,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getFromIterable(originalFunction, (Predicate<? super R>) AS_IS, (ResultSelection<Iterable<R>, R>) returnResultSelection(), wait, sleep, null, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, S> originalFunction,
                                             Duration wait, Supplier<? extends RuntimeException> exceptionSupplier,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getFromIterable(originalFunction, (Predicate<? super R>) AS_IS, (ResultSelection<Iterable<R>, R>) returnResultSelection(), wait, null, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, S> originalFunction,
                                             Duration wait,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getFromIterable(originalFunction, (Predicate<? super R>) AS_IS, (ResultSelection<Iterable<R>, R>) returnResultSelection(), wait, null, null, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, S> originalFunction,
                                             Supplier<? extends RuntimeException> exceptionSupplier,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getFromIterable(originalFunction, (Predicate<? super R>) AS_IS, (ResultSelection<Iterable<R>, R>) returnResultSelection(), null, null, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, S> originalFunction,
                                             Collection<Class<? extends Throwable>> toIgnore) {
            return getFromIterable(originalFunction, (Predicate<? super R>) AS_IS, (ResultSelection<Iterable<R>, R>) returnResultSelection(), null, null, null, toIgnore);
        }
    }

    class ArrayItemFunctionFactory<T, R> extends ItemFunctionFactory<T, R[], R, R> {

        @Override
        public Function<T, R> createFunction(Function<T, R[]> originalFunction, Predicate<R> predicate, Duration wait, Duration sleep, Supplier<? extends RuntimeException> exceptionSupplier, Collection<Class<? extends Throwable>> toIgnore) {
            return getFromArray(originalFunction, predicate, returnResultSelection(), wait, sleep, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R[]> originalFunction, Predicate<R> predicate, Duration wait, Duration sleep, Collection<Class<? extends Throwable>> toIgnore) {
            return getFromArray(originalFunction, predicate, returnResultSelection(), wait, sleep, null, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R[]> originalFunction, Predicate<R> predicate, Duration wait, Supplier<? extends RuntimeException> exceptionSupplier, Collection<Class<? extends Throwable>> toIgnore) {
            return getFromArray(originalFunction, predicate, returnResultSelection(), wait, null, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R[]> originalFunction, Predicate<R> predicate, Duration wait, Collection<Class<? extends Throwable>> toIgnore) {
            return getFromArray(originalFunction, predicate, returnResultSelection(), wait, null, null, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R[]> originalFunction, Predicate<R> predicate, Supplier<? extends RuntimeException> exceptionSupplier, Collection<Class<? extends Throwable>> toIgnore) {
            return getFromArray(originalFunction, predicate, returnResultSelection(), null, null, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R[]> originalFunction, Predicate<R> predicate, Collection<Class<? extends Throwable>> toIgnore) {
            return getFromArray(originalFunction, predicate, returnResultSelection(), null, null, null, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R[]> originalFunction, Duration wait, Duration sleep, Supplier<? extends RuntimeException> exceptionSupplier, Collection<Class<? extends Throwable>> toIgnore) {
            return getFromArray(originalFunction, (Predicate<? super R>) AS_IS, returnResultSelection(), wait, sleep, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R[]> originalFunction, Duration wait, Duration sleep, Collection<Class<? extends Throwable>> toIgnore) {
            return getFromArray(originalFunction, (Predicate<? super R>) AS_IS, returnResultSelection(), wait, sleep, null, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R[]> originalFunction, Duration wait, Supplier<? extends RuntimeException> exceptionSupplier, Collection<Class<? extends Throwable>> toIgnore) {
            return getFromArray(originalFunction, (Predicate<? super R>) AS_IS, returnResultSelection(), wait, null, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R[]> originalFunction, Duration wait, Collection<Class<? extends Throwable>> toIgnore) {
            return getFromArray(originalFunction, (Predicate<? super R>) AS_IS, returnResultSelection(), wait, null, null, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R[]> originalFunction, Supplier<? extends RuntimeException> exceptionSupplier, Collection<Class<? extends Throwable>> toIgnore) {
            return getFromArray(originalFunction, (Predicate<? super R>) AS_IS, returnResultSelection(), null, null, exceptionSupplier, toIgnore);
        }

        @Override
        public Function<T, R> createFunction(Function<T, R[]> originalFunction, Collection<Class<? extends Throwable>> toIgnore) {
            return getFromArray(originalFunction, (Predicate<? super R>) AS_IS, returnResultSelection(), null, null, null, toIgnore);
        }
    }
}
