package com.lantopia.libjava.json;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author Mark McKenna &lt;mark.denis.mckenna@gmail.com&gt;
 * @version 0.1
 * @since 03/08/2014
 * <p/>
 * Facilitator for some basic JSON operations
 */
public class Json {
    private Json() {}

    public static String empty() { return ""; }

    public static String emptyObject() { return "{}"; }

    public static String emptyArray() { return "[]"; }

    /**
     * URLencodes the given string using UTF8 encoding (since the JSON spec requires it).  All Java implementations
     * should support this encoding, so {@link java.io.UnsupportedEncodingException} should be impossible.
     *
     * @param jsonString The JSON string to encode
     * @return The URLEncoded equivalent of the JSON string
     */
    public static String urlEncode(final String jsonString) {
        try {
            return URLEncoder.encode(jsonString, Charsets.UTF_8.name());
        } catch (final UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    /**
     * URLdecodes the given string using UTF8 encoding (since the JSON spec requires it).  All Java implementations
     * should support this encoding, so {@link java.io.UnsupportedEncodingException} should be impossible.
     *
     * @param urlEncodedJsonString The JSON string to encode
     * @return The bare equivalent of the URLDecoded JSON string
     */
    public static String urlDecode(final String urlEncodedJsonString) {
        try {
            return URLDecoder.decode(urlEncodedJsonString, Charsets.UTF_8.name());
        } catch (final UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    public static Optional<String> urlEncode(final Optional<String> jsonString) {
        if (jsonString.isPresent()) return Optional.of(urlDecode(jsonString.get()));
        else return jsonString;
    }

    public static Optional<String> urlDecode(final Optional<String> urlEncodedJsonString) {
        if (urlEncodedJsonString.isPresent()) return Optional.of(urlDecode(urlEncodedJsonString.get()));
        else return urlEncodedJsonString;
    }
}
