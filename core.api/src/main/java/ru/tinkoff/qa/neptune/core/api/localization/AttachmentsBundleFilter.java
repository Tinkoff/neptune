package ru.tinkoff.qa.neptune.core.api.localization;

import io.github.classgraph.ClassGraph;
import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

final class AttachmentsBundleFilter extends DefaultAbstractBundleFiller {

    AttachmentsBundleFilter(LocalizationBundlePartition p) {
        super(p, getAttachments(), "ATTACHMENTS");
    }

    private static List<Class<?>> getAttachments() {
        return new ClassGraph()
                .enableClassInfo()
                .ignoreClassVisibility()
                .scan()
                .getSubclasses(Captor.class.getName())
                .loadClasses(Captor.class)
                .stream()
                .map(cls -> (Class<?>) cls)
                .sorted(comparing(Class::getName))
                .collect(toList());
    }
}
