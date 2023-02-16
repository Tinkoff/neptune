package ru.tinkoff.qa.neptune.mockito;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.mockito.Mock;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static org.mockito.MockitoAnnotations.openMocks;
import static ru.tinkoff.qa.neptune.mockito.TestEventLogger.*;

@TestInstance(PER_CLASS)
@Execution(CONCURRENT)
public class MockitoPreparations {

    @Mock(name = "Test instance")
    SomeClass someClass;

    @AfterEach
    void afterMethod() {
        stepNames.get().clear();
        thrown.remove();
        isFinished.remove();
    }

    @BeforeAll
    void prepare() {
        openMocks(this);
    }
}
