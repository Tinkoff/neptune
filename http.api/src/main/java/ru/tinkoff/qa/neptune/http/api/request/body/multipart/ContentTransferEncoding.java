package ru.tinkoff.qa.neptune.http.api.request.body.multipart;

import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body.multipart.MultiPartBody;

/**
 * Types of content transfer encodings
 */
public enum ContentTransferEncoding {
    /**
     * Is used as the default value of {@link MultiPartBody#contentTransferEncoding()}
     */
    NOT_DEFINED(""),
    /**
     * is for {@code 7bit}
     */
    BIT7("7bit"),
    /**
     * is for {@code quoted-printable}
     */
    QUOTED_PRINTABLE("quoted-printable"),
    /**
     * is for {@code base64}
     */
    BASE64("base64"),
    /**
     * is for {@code 8bit}
     */
    BIT8("8bit"),
    /**
     * is for {@code binary}
     */
    BINARY("binary"),
    /**
     * is for {@code x-token}
     */
    X_TOKEN("x-token");

    private final String name;

    ContentTransferEncoding(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
