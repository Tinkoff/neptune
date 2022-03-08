package ru.tinkoff.qa.neptune.allure;

import io.qameta.allure.AllureResultsWriteException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.fireEventStarting;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.iterableHasItems;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.pojo.PojoGetterReturnsMatcher.getterReturns;

public class AttachmentTest extends AbstractAllurePreparations {

    private String rootStepUUID;

    @BeforeClass
    public void prepareStep() {
        fireEventStarting("Some step", Map.of());
        rootStepUUID = lifeCycle.getCurrentTestCaseOrStep().get();
    }

    @Test
    public void textAttach() {
        new AllureStringInjector().inject(new StringBuilder("ABCD"), "String attach");
        var step = storage.getStep(rootStepUUID).get();
        assertThat(step.getAttachments(),
                iterableHasItems(1,
                        getterReturns("getName", "String attach"),
                        getterReturns("getSource", not(nullValue())),
                        getterReturns("getType", "text/plain")));
    }

    @Test
    public void imageAttach() throws Exception {
        var source = Paths.get("src/test/resources/picture.jpeg");
        BufferedImage bi = ImageIO.read(source.toFile());

        new AllureImageInjector().inject(bi, "Image attach");
        var step = storage.getStep(rootStepUUID).get();


        assertThat(step.getAttachments(),
                iterableHasItems(1,
                        getterReturns("getName", "Image attach"),
                        getterReturns("getSource", not(nullValue())),
                        getterReturns("getType", "image/*")));
    }

    @Test
    public void fileAttach() {
        var source = Paths.get("src/test/resources/test.json").toFile();
        new AllureFileInjector().inject(source, "File attach");

        var step = storage.getStep(rootStepUUID).get();

        assertThat(step.getAttachments(),
                iterableHasItems(1,
                        getterReturns("getName", "File attach"),
                        getterReturns("getSource", not(nullValue())),
                        getterReturns("getType", "application/json")));
    }

    @Test(expectedExceptions = AllureResultsWriteException.class)
    public void fileAttach2() {
        var source = Paths.get("src/test/resources/not_existent").toFile();
        new AllureFileInjector().inject(source, "File attach");

        fail("Exception was expected");
    }
}
