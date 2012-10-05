package org.cjimera;

import org.junit.Test;

import java.io.InputStream;

import static ixcode.platform.io.IoClasspath.inputStreamFromClasspathEntry;
import static org.cjimera.ParseRepresentation.parse;
import static org.fest.assertions.Assertions.assertThat;

public class ParseRepresentationTest {

    private ParseRepresentation parseRepresentation;


    @Test
    public void single_field_form() {

        SimpleObject result = processFormData("single_parameter_form_data.txt", SimpleObject.class);

        assertThat(result).isNotNull();
        assertThat(result.firstName).isEqualTo("Johnny");
        assertThat(result.lastName).isNull();

    }

    @Test
    public void multiple_field_form() {

        SimpleObject result = processFormData("multiple_parameter_form_data.txt", SimpleObject.class);

        assertThat(result).isNotNull();
        assertThat(result.firstName).isEqualTo("Johnny");
        assertThat(result.lastName).isEqualTo("Billy");
        assertThat(result.age).isEqualTo(666);

    }


    private static <T> T processFormData(String entity, Class<T> type) {
        InputStream in = inputStreamFromClasspathEntry(ParseRepresentationTest.class, entity);

        return parse().inputStream(in)
                .from().applicationFormUrlEncoded()
                .to(type);
    }


    private static class SimpleObject {

        public final String firstName;
        public final String lastName;
        public final Long age;

        private SimpleObject(String firstName) {
            this(firstName, null, null);
        }

        private SimpleObject(String firstName, String lastName, Long age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }
    }

}