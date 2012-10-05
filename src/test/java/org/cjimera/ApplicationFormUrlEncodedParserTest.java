package org.cjimera;

import org.junit.Test;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import static org.fest.assertions.Assertions.assertThat;

/**
 * If you only see %A3 the browser is not sending back UTF-8!!
 */
public class ApplicationFormUrlEncodedParserTest {

    @Test
    public void decode_pound() throws Exception {
        String decodedString = URLDecoder.decode("%C2%A3", "UTF-8");
        assertThat(decodedString).isEqualTo("\u00A3");
    }

    @Test
    public void what_the_hell_is_going_on_with_urlencoding() throws Exception {
        String utf8String = "This is a pound character: \u00A3";


        System.out.println("Default charset " + Charset.defaultCharset().name());
        String encodedString = URLEncoder.encode(utf8String, "UTF-8");

        assertThat(encodedString).isEqualTo("This+is+a+pound+character%3A+%C2%A3");

        String decodedString = URLDecoder.decode(encodedString, "UTF-8");

        assertThat(decodedString).isEqualTo("This is a pound character: \u00A3");

    }
}