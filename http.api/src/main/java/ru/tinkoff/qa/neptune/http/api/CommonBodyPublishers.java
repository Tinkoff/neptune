package ru.tinkoff.qa.neptune.http.api;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.function.Supplier;

import static java.net.http.HttpRequest.BodyPublishers.*;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Creates instances if {@link java.net.http.HttpRequest.BodyPublisher} for more comfort usage
 */
public final class CommonBodyPublishers {

    private CommonBodyPublishers() {
        super();
    }

    /**
     * Returns a request body publisher whose body is the given {@code
     * String}, converted using the {@link StandardCharsets#UTF_8 UTF_8}
     * character set.
     *
     * @param body the String containing the body
     * @return a BodyPublisher
     */
    public static HttpRequest.BodyPublisher stringBody(String body) {
        return ofString(body, UTF_8);
    }

    /**
     * Returns a request body publisher whose body is the given {@code
     * String}, converted using the given character set.
     *
     * @param s the String containing the body
     * @param charset the character set to convert the string to bytes
     * @return a BodyPublisher
     */
    public static HttpRequest.BodyPublisher stringBody(String s, Charset charset) {
        return ofString(s, charset);
    }

    /**
     * A request body publisher that reads its data from an {@link
     * InputStream}. A {@link Supplier} of {@code InputStream} is used in
     * case the request needs to be repeated, as the content is not buffered.
     * The {@code Supplier} may return {@code null} on subsequent attempts,
     * in which case the request fails.
     *
     * @param streamSupplier a Supplier of open InputStreams
     * @return a BodyPublisher
     */
    public static HttpRequest.BodyPublisher streamBody(Supplier<? extends InputStream> streamSupplier) {
        return ofInputStream(streamSupplier);
    }

    /**
     * Returns a request body publisher whose body is the given byte array.
     *
     * @param buf the byte array containing the body
     * @return a BodyPublisher
     */
    public static HttpRequest.BodyPublisher byteArrayBody(byte[] buf) {
        return ofByteArray(buf);
    }

    /**
     * Returns a request body publisher whose body is the content of the
     * given byte array of {@code length} bytes starting from the specified
     * {@code offset}.
     *
     * @param buf the byte array containing the body
     * @param offset the offset of the first byte
     * @param length the number of bytes to use
     * @return a BodyPublisher
     * @throws IndexOutOfBoundsException if the sub-range is defined to be
     *                                   out of bounds
     */
    public static HttpRequest.BodyPublisher byteArrayBody(byte[] buf, int offset, int length) {
        return ofByteArray(buf, offset, length);
    }

    /**
     * A request body publisher that takes data from the contents of a File.
     *
     * <p> Security manager permission checks are performed in this factory
     * method, when the {@code BodyPublisher} is created. Care must be taken
     * that the {@code BodyPublisher} is not shared with untrusted code.
     *
     * @param path the path to the file containing the body
     * @return a BodyPublisher
     * @throws java.io.FileNotFoundException if the path is not found
     * @throws SecurityException if a security manager has been installed
     *          and it denies {@link SecurityManager#checkRead(String)
     *          read access} to the given file
     */
    public static HttpRequest.BodyPublisher fileBody(Path path) throws FileNotFoundException {
        return ofFile(path);
    }

    /**
     * A request body publisher that takes data from an {@code Iterable}
     * of byte arrays. An {@link Iterable} is provided which supplies
     * {@link Iterator} instances. Each attempt to send the request results
     * in one invocation of the {@code Iterable}.
     *
     * @param iter an Iterable of byte arrays
     * @return a BodyPublisher
     */
    public static HttpRequest.BodyPublisher byteArraysBody(Iterable<byte[]> iter) {
        return ofByteArrays(iter);
    }

    /**
     * A request body publisher which sends no request body.
     *
     * @return a BodyPublisher which completes immediately and sends
     *         no request body.
     */
    public static HttpRequest.BodyPublisher empty() {
        return noBody();
    }
}
