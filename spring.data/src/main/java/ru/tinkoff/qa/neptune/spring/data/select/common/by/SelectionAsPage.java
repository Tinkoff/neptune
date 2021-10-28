package ru.tinkoff.qa.neptune.spring.data.select.common.by;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.spring.data.SpringDataFunction;

import java.util.ArrayList;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("As page")
public final class SelectionAsPage<R, ID, T extends PagingAndSortingRepository<R, ID>> extends SpringDataFunction<T, Iterable<R>> {

    private Pageable pageable;

    public SelectionAsPage() {
        super(PagingAndSortingRepository.class);
    }

    @Override
    public String toString() {
        return translate(this);
    }

    @Override
    public Iterable<R> apply(T t) {
        return new ArrayList<>(t.findAll(pageable).getContent());
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }
}
