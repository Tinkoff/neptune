package ru.tinkoff.qa.neptune.kafka.functions.poll;

import java.util.List;

public interface CollectsMessages {

    List<String> getMessages();
}
