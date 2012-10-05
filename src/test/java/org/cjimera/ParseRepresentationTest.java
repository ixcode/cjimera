package org.cjimera;

import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;

import static ixcode.platform.io.IoClasspath.inputStreamFromClasspathEntry;
import static org.cjimera.ParseRepresentation.parse;
import static org.fest.assertions.Assertions.assertThat;

public class ParseRepresentationTest {

    private ParseRepresentation parseRepresentation;


    @Test
    @Ignore("not yet implemented")
    public void parse_application_form() {

        InputStream in = inputStreamFromClasspathEntry(this, "single_parameter_form_data.txt");

        SimpleObject result = parse()
                .inputStream(in)
                .from().applicationFormUrlEncoded()
                .to(SimpleObject.class);

        assertThat(result).isNotNull();
        assertThat(result.firstName).isEqualTo("Johnny");
        assertThat(result.lastName).isNull();

    }


    private static class SimpleObject {

        public final String firstName;
        public final String lastName;

        private SimpleObject(String firstName) {
            this(firstName, null);
        }

        private SimpleObject(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

}