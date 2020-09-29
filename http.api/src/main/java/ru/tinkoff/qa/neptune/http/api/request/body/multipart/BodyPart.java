package ru.tinkoff.qa.neptune.http.api.request.body.multipart;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.probeContentType;
import static java.nio.file.Files.readAllBytes;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ArrayUtils.addAll;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BodyPart {

    private final String name;
    private final String fileName;
    private final byte[] bytes;
    private String contentType;
    private String contentTransferEncoding;

    private BodyPart(String name, String fileName, byte[] bytes) {
        this.name = name;
        this.fileName = fileName;
        this.bytes = bytes;
    }

    /**
     * Defines a part of a request body as an array of bytes. By default it sets {@code Content-Type} to
     * {@code application/octet-stream}. It is possible to change content type to another by invocation of
     * {@link #setContentType(String)}
     *
     * @param rawBytes is byte array content
     * @param name     is a name of a part
     * @param fileName is filename of a part
     * @return an instance of {@link BodyPart}
     */
    public static BodyPart bodyPart(byte[] rawBytes, String name, String fileName) {
        checkArgument(nonNull(rawBytes), "Byte array should be defined");
        return new BodyPart(name, fileName, rawBytes) {
            @Override
            String getContentDescription() {
                return "Byte array of length " + rawBytes.length;
            }
        }.setContentType("application/octet-stream");
    }

    /**
     * Defines a part of a request body as an array of bytes. By default it sets {@code Content-Type} to
     * {@code application/octet-stream}. It is possible to change content type to another by invocation of
     * {@link #setContentType(String)}
     *
     * @param rawBytes is byte array content
     * @param name     is a name of a part
     * @return an instance of {@link BodyPart}
     */
    public static BodyPart bodyPart(byte[] rawBytes, String name) {
        return bodyPart(rawBytes, name, null);
    }

    /**
     * Defines a part of a request body as an array of bytes. By default it sets {@code Content-Type} to
     * {@code application/octet-stream}. It is possible to change content type to another by invocation of
     * {@link #setContentType(String)}
     *
     * @param rawBytes is byte array content
     * @return an instance of {@link BodyPart}
     */
    public static BodyPart bodyPart(byte[] rawBytes) {
        return bodyPart(rawBytes, (String) null, null);
    }

    /**
     * Defines a part of a request body as an input stream. By default it sets {@code Content-Type} to
     * {@code application/octet-stream}. It is possible to change content type to another by invocation of
     * {@link #setContentType(String)}
     *
     * @param stream   is an input stream content
     * @param name     is a name of a part
     * @param fileName is filename of a part
     * @return an instance of {@link BodyPart}
     */
    public static BodyPart bodyPart(InputStream stream, String name, String fileName) {
        checkArgument(nonNull(stream), "Input stream should be defined");
        try (stream) {
            return bodyPart(stream.readAllBytes(), name, fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Defines a part of a request body as an input stream. By default it sets {@code Content-Type} to
     * {@code application/octet-stream}. It is possible to change content type to another by invocation of
     * {@link #setContentType(String)}
     *
     * @param stream is an input stream content
     * @param name   is a name of a part
     * @return an instance of {@link BodyPart}
     */
    public static BodyPart bodyPart(InputStream stream, String name) {
        return bodyPart(stream, name, null);
    }

    /**
     * Defines a part of a request body as an input stream. By default it sets {@code Content-Type} to
     * {@code application/octet-stream}. It is possible to change content type to another by invocation of
     * {@link #setContentType(String)}
     *
     * @param stream is an input stream content
     * @return an instance of {@link BodyPart}
     */
    public static BodyPart bodyPart(InputStream stream) {
        return bodyPart(stream, (String) null, null);
    }


    /**
     * Defines a part of a request body as a file.
     *
     * @param file                 is a file content
     * @param name                 is a name of a part
     * @param fileName             is filename of a part
     * @param calculateContentType enables calculation of content type. It sets {@code Content-Type} to
     *                             {@code application/octet-stream} when {@code calculateContentType}
     *                             is {@code false} or it is not possible to get mime. It is possible to
     *                             change content type to another by invocation of {@link #setContentType(String)}
     * @return an instance of {@link BodyPart}
     */
    public static BodyPart bodyPart(File file, String name, String fileName, boolean calculateContentType) {
        checkArgument(nonNull(file), "File should be defined");
        checkArgument(file.exists(), "File should exist. " + file.getAbsolutePath() + " doesn't exist");
        checkArgument(!file.isDirectory(), "File should not be directory");
        String contentType;
        if (calculateContentType) {
            try {
                contentType = ofNullable(probeContentType(file.toPath())).orElse("application/octet-stream");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            contentType = "application/octet-stream";
        }

        try {
            return new BodyPart(name, fileName, readAllBytes(file.toPath())) {
                @Override
                String getContentDescription() {
                    return "File " + file.getAbsolutePath() + " of size " + file.length() + "(bytes)";
                }
            }.setContentType(contentType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Defines a part of a request body as a file.
     *
     * @param file                 is a file content
     * @param name                 is a name of a part
     * @param setFileName          defines to set filename to the name of the given file ({@link File#getName()}) or keep it
     *                             empty.
     * @param calculateContentType enables calculation of content type. It sets {@code Content-Type} to
     *                             {@code application/octet-stream} when {@code calculateContentType}
     *                             is {@code false} or it is not possible to get mime. It is possible to
     *                             change content type to another by invocation of {@link #setContentType(String)}
     * @return an instance of {@link BodyPart}
     */
    public static BodyPart bodyPart(File file, String name, boolean setFileName, boolean calculateContentType) {
        checkArgument(nonNull(file), "File should be defined");
        return bodyPart(file, name, setFileName ? file.getName() : null, calculateContentType);
    }

    /**
     * Defines a part of a request body as a file.
     *
     * @param file                 is a file content
     * @param calculateContentType enables calculation of content type. It sets {@code Content-Type} to
     *                             {@code application/octet-stream} when {@code calculateContentType}
     *                             is {@code false} or it is not possible to get mime. It is possible to
     *                             change content type to another by invocation of {@link #setContentType(String)}
     * @return an instance of {@link BodyPart}
     */
    public static BodyPart bodyPart(File file, boolean calculateContentType) {
        checkArgument(nonNull(file), "File should be defined");
        return bodyPart(file, null, null, calculateContentType);
    }


    /**
     * Defines a part of a request body as a file.
     *
     * @param file                 is a file content
     * @param name                 is a name of a part
     * @param fileName             is filename of a part
     * @param calculateContentType enables calculation of content type. It sets {@code Content-Type} to
     *                             {@code application/octet-stream} when {@code calculateContentType}
     *                             is {@code false} or it is not possible to get mime. It is possible to
     *                             change content type to another by invocation of {@link #setContentType(String)}
     * @return an instance of {@link BodyPart}
     */
    public static BodyPart bodyPart(Path file, String name, String fileName, boolean calculateContentType) {
        return bodyPart(file.toFile(), name, fileName, calculateContentType);
    }

    /**
     * Defines a part of a request body as a file.
     *
     * @param file                 is a file content
     * @param name                 is a name of a part
     * @param setFileName          defines to set filename to the name of the given file ({@link File#getName()}) or keep it
     *                             empty.
     * @param calculateContentType enables calculation of content type. It sets {@code Content-Type} to
     *                             {@code application/octet-stream} when {@code calculateContentType}
     *                             is {@code false} or it is not possible to get mime. It is possible to
     *                             change content type to another by invocation of {@link #setContentType(String)}
     * @return an instance of {@link BodyPart}
     */
    public static BodyPart bodyPart(Path file, String name, boolean setFileName, boolean calculateContentType) {
        return bodyPart(file.toFile(), name, setFileName, calculateContentType);
    }

    /**
     * Defines a part of a request body as a file.
     *
     * @param file                 is a file content
     * @param calculateContentType enables calculation of content type. It sets {@code Content-Type} to
     *                             {@code application/octet-stream} when {@code calculateContentType}
     *                             is {@code false} or it is not possible to get mime. It is possible to
     *                             change content type to another by invocation of {@link #setContentType(String)}
     * @return an instance of {@link BodyPart}
     */
    public static BodyPart bodyPart(Path file, boolean calculateContentType) {
        return bodyPart(file.toFile(), calculateContentType);
    }


    /**
     * Defines a part of a request body as a string. It needs to define {@code Content-Type} if it is necessary.
     * It is possible to do by invocation of {@link #setContentType(String)}.
     *
     * @param content  is string content
     * @param name     is a name of a part
     * @param fileName is filename of a part
     * @return an instance of {@link BodyPart}
     */
    public static BodyPart bodyPart(String content, String name, String fileName) {
        checkArgument(nonNull(content), "String should be defined");
        return new BodyPart(name, fileName, content.getBytes()) {
            @Override
            String getContentDescription() {
                return content;
            }
        };
    }

    /**
     * Defines a part of a request body as a string. It needs to define {@code Content-Type} if it is necessary.
     * It is possible to do by invocation of {@link #setContentType(String)}.
     *
     * @param content is string content
     * @param name    is a name of a part
     * @return an instance of {@link BodyPart}
     */
    public static BodyPart bodyPart(String content, String name) {
        return bodyPart(content, name, null);
    }

    /**
     * Defines a part of a request body as a string. It needs to define {@code Content-Type} if it is necessary.
     * It is possible to do by invocation of {@link #setContentType(String)}.
     *
     * @param content is string content
     * @return an instance of {@link BodyPart}
     */
    public static BodyPart bodyPart(String content) {
        return bodyPart(content, (String) null, null);
    }


    /**
     * Defines a part of a request body as a string by an object serialized by {@link ObjectMapper}.
     * It needs to define {@code Content-Type} if it is necessary.
     * It is possible to do by invocation of {@link #setContentType(String)}.
     *
     * @param o        is an object to be serialized
     * @param mapper   is an {@link ObjectMapper} that performs serialization
     * @param name     is a name of a part
     * @param fileName is filename of a part
     * @return an instance of {@link BodyPart}
     */
    public static BodyPart bodyPart(Object o, ObjectMapper mapper, String name, String fileName) {
        try {
            return bodyPart(mapper.writeValueAsString(o), name, fileName);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Defines a part of a request body as a string by an object serialized by {@link ObjectMapper}.
     * It needs to define {@code Content-Type} if it is necessary.
     * It is possible to do by invocation of {@link #setContentType(String)}.
     *
     * @param o      is an object to be serialized
     * @param mapper is an {@link ObjectMapper} that performs serialization
     * @param name   is a name of a part
     * @return an instance of {@link BodyPart}
     */
    public static BodyPart bodyPart(Object o, ObjectMapper mapper, String name) {
        try {
            return bodyPart(mapper.writeValueAsString(o), name, null);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Defines a part of a request body as a string by an object serialized by {@link ObjectMapper}.
     * It needs to define {@code Content-Type} if it is necessary.
     * It is possible to do by invocation of {@link #setContentType(String)}.
     *
     * @param o      is an object to be serialized
     * @param mapper is an {@link ObjectMapper} that performs serialization
     * @return an instance of {@link BodyPart}
     */
    public static BodyPart bodyPart(Object o, ObjectMapper mapper) {
        try {
            return bodyPart(mapper.writeValueAsString(o), (String) null, null);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Defines a part of a request body as a string by an object serialized by {@link ObjectMapper}. This mapper
     * is available by one of items of the enum {@link DefaultMapper}.
     * It needs to define {@code Content-Type} if it is necessary.
     * It is possible to do by invocation of {@link #setContentType(String)}.
     *
     * @param o        is an object to be serialized
     * @param mapper   is an {@link ObjectMapper} that performs serialization. This mapper
     *                 is available by one of items of the enum {@link DefaultMapper}.
     * @param name     is a name of a part
     * @param fileName is filename of a part
     * @return an instance of {@link BodyPart}
     * @see DefaultMapper#getMapper()
     */
    public static BodyPart bodyPart(Object o, DefaultMapper mapper, String name, String fileName) {
        return bodyPart(o, mapper.getMapper(), name, fileName);
    }

    /**
     * Defines a part of a request body as a string by an object serialized by {@link ObjectMapper}. This mapper
     * is available by one of items of the enum {@link DefaultMapper}.
     * It needs to define {@code Content-Type} if it is necessary.
     * It is possible to do by invocation of {@link #setContentType(String)}.
     *
     * @param o      is an object to be serialized
     * @param mapper is an {@link ObjectMapper} that performs serialization. This mapper
     *               is available by one of items of the enum {@link DefaultMapper}.
     * @param name   is a name of a part
     * @return an instance of {@link BodyPart}
     * @see DefaultMapper#getMapper()
     */
    public static BodyPart bodyPart(Object o, DefaultMapper mapper, String name) {
        return bodyPart(o, mapper.getMapper(), name, null);
    }

    /**
     * Defines a part of a request body as a string by an object serialized by {@link ObjectMapper}. This mapper
     * is available by one of items of the enum {@link DefaultMapper}.
     * It needs to define {@code Content-Type} if it is necessary.
     * It is possible to do by invocation of {@link #setContentType(String)}.
     *
     * @param o      is an object to be serialized
     * @param mapper is an {@link ObjectMapper} that performs serialization. This mapper
     *               is available by one of items of the enum {@link DefaultMapper}.
     * @return an instance of {@link BodyPart}
     * @see DefaultMapper#getMapper()
     */
    public static BodyPart bodyPart(Object o, DefaultMapper mapper) {
        return bodyPart(o, mapper.getMapper(), null, null);
    }


    /**
     * Defines a part of a request body as a string by string representation of an object.
     * It needs to define {@code Content-Type} if it is necessary.
     * It is possible to do by invocation of {@link #setContentType(String)}.
     *
     * @param o        is an object to be serialized
     * @param name     is a name of a part
     * @param fileName is filename of a part
     * @return an instance of {@link BodyPart}
     * @see Object#toString()
     */
    public static BodyPart bodyPart(Object o, String name, String fileName) {
        return bodyPart(ofNullable(o).map(Object::toString).orElse(null), name, fileName);
    }

    /**
     * Defines a part of a request body as a string by string representation of an object.
     * It needs to define {@code Content-Type} if it is necessary.
     * It is possible to do by invocation of {@link #setContentType(String)}.
     *
     * @param o    is an object to be serialized
     * @param name is a name of a part
     * @return an instance of {@link BodyPart}
     * @see Object#toString()
     */
    public static BodyPart bodyPart(Object o, String name) {
        return bodyPart(ofNullable(o).map(Object::toString).orElse(null), name, null);
    }

    /**
     * Defines a part of a request body as a string by string representation of an object.
     * It needs to define {@code Content-Type} if it is necessary.
     * It is possible to do by invocation of {@link #setContentType(String)}.
     *
     * @param o is an object to be serialized
     * @return an instance of {@link BodyPart}
     * @see Object#toString()
     */
    public static BodyPart bodyPart(Object o) {
        return bodyPart(ofNullable(o).map(Object::toString).orElse(null), (String) null, null);
    }


    /**
     * Defines content type of a body part
     *
     * @param contentType content type of a body part
     * @return self-reference
     */
    public BodyPart setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * Defines content transfer encoding of a body part
     *
     * @param contentTransfer content transfer encoding of a body part
     * @return self-reference
     */
    public BodyPart setContentTransferEncoding(ContentTransferEncoding contentTransfer) {
        this.contentTransferEncoding = contentTransfer.toString();
        return this;
    }

    private String getPartInfo() {
        var contentInfo = "Content-Disposition: form-data";
        if (isNotBlank(name)) {
            contentInfo = contentInfo + ";name=\"" + name + "\"";
        }

        if (isNotBlank(fileName)) {
            contentInfo = contentInfo + ";filename=\"" + fileName + "\"";
        }


        contentInfo = contentInfo + "\r\n";

        if (isNotBlank(contentType)) {
            contentInfo = contentInfo + "Content-Type: " + contentType + "\r\n";
        }

        if (isNotBlank(contentTransferEncoding)) {
            contentInfo = contentInfo + "Content-Transfer-Encoding: " + contentTransferEncoding + "\r\n";
        }

        return contentInfo;
    }

    public String toString() {
        return getPartInfo() +
                "Content: "
                + getContentDescription()
                + "\r\n";
    }

    abstract String getContentDescription();

    /**
     * Returns byte content of a part.
     *
     * @return byte content of a part
     */
    public List<byte[]> content(String boundary) {
        var list = new ArrayList<byte[]>();
        list.add((boundary + "\r\n" + getPartInfo() + "\r\n").getBytes(UTF_8));
        list.add(addAll(bytes, "\r\n".getBytes(UTF_8)));
        return list;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContentTransferEncoding() {
        return contentTransferEncoding;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
