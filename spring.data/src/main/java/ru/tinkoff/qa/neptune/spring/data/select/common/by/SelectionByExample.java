package ru.tinkoff.qa.neptune.spring.data.select.common.by;

import com.google.common.collect.Lists;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.spring.data.SpringDataFunction;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@SuppressWarnings("unchecked")
@Description("By example. Probe: '{probe}'. Matcher: {matcher}")
public abstract class SelectionByExample<R, ID, T extends Repository<R, ID>, RESULT> extends SpringDataFunction<T, RESULT> {

    @DescriptionFragment(value = "probe", makeReadableBy = ProbeParameterValueGetter.class)
    private final R probe;

    @DescriptionFragment("matcher")
    private final ExampleMatcher matcher;

    private SelectionByExample(R probe, ExampleMatcher matcher) {
        super(QueryByExampleExecutor.class, ReactiveQueryByExampleExecutor.class);
        checkNotNull(probe);
        checkNotNull(matcher);
        this.probe = probe;
        this.matcher = matcher;
    }

    public static <R, ID, T extends Repository<R, ID>> SelectionByExample<R, ID, T, R> getSingleByExample(R probe, ExampleMatcher matcher) {
        return new SelectASingleByExample<>(probe, matcher);
    }

    public static <R, ID, T extends Repository<R, ID>> SelectionByExample<R, ID, T, Iterable<R>> getIterableByExample(R probe,
                                                                                                                      ExampleMatcher matcher,
                                                                                                                      Sort sort) {
        return new SelectIterableByExample<>(probe, matcher, sort);
    }

    protected final Example<R> getExample() {
        return Example.of(probe, matcher);
    }

    @Override
    public String toString() {
        return translate(this);
    }

    private static final class SelectASingleByExample<R, ID, T
            extends Repository<R, ID>> extends SelectionByExample<R, ID, T, R> {

        private SelectASingleByExample(R probe, ExampleMatcher matcher) {
            super(probe, matcher);
        }

        @Override
        public R apply(T t) {
            if (t instanceof QueryByExampleExecutor) {
                return ((QueryByExampleExecutor<R>) t).findOne(getExample()).orElse(null);
            }

            if (t instanceof ReactiveQueryByExampleExecutor) {
                return ((ReactiveQueryByExampleExecutor<R>) t).findOne(getExample()).block();
            }

            throw unsupportedRepository(t);
        }
    }

    private static final class SelectIterableByExample<R, ID, T
            extends Repository<R, ID>> extends SelectionByExample<R, ID, T, Iterable<R>> {

        private final Sort sort;

        private SelectIterableByExample(R probe, ExampleMatcher matcher, Sort sort) {
            super(probe, matcher);
            this.sort = sort;
        }

        @Override
        public Iterable<R> apply(T t) {
            if (t instanceof QueryByExampleExecutor) {
                var repo = (QueryByExampleExecutor<R>) t;
                return isNull(sort) ? newArrayList(repo.findAll(getExample())) : newArrayList(repo.findAll(getExample(), sort));
            }

            if (t instanceof ReactiveQueryByExampleExecutor) {
                var repo = (ReactiveQueryByExampleExecutor<R>) t;
                return isNull(sort) ? ofNullable(repo.findAll(getExample()).collectList().block())
                        .map(Lists::newArrayList)
                        .orElse(null) :
                        ofNullable(repo.findAll(getExample(), sort).collectList().block())
                                .map(Lists::newArrayList)
                                .orElse(null);
            }

            throw unsupportedRepository(t);
        }

        @Override
        public String toString() {
            return isNull(sort) ? super.toString() : super.toString() + " " + sort;
        }
    }
}
