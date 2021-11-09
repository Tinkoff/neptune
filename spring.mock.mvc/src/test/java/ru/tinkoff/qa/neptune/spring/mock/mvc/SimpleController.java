package ru.tinkoff.qa.neptune.spring.mock.mvc;

import org.springframework.http.ResponseEntity;

public class SimpleController {

    public ResponseEntity<Void> handle(Object...params) {
        return null;
    }
}
