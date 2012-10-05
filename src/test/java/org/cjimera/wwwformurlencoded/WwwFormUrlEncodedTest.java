package org.cjimera.wwwformurlencoded;

import org.cjimera.Parser;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static ixcode.platform.io.IoClasspath.inputStreamFromClasspathEntry;
import static org.cjimera.Parser.parse;
import static org.fest.assertions.Assertions.assertThat;

public class WwwFormUrlEncodedTest {

    private Parser parser;


    @Test
    public void single_field_form() {

        SimpleObject result = processFormData("single_parameter_form_data.txt", SimpleObject.class);

        assertThat(result.firstName).isEqualTo("Johnny");
        assertThat(result.lastName).isNull();

    }

    @Test
    public void multiple_field_form() {

        SimpleObject result = processFormData("multiple_parameter_form_data.txt", SimpleObject.class);

        assertThat(result.firstName).isEqualTo("Johnny");
        assertThat(result.lastName).isEqualTo("Billy");
        assertThat(result.age).isEqualTo(666);

    }

    @Test
    public void url_encoded_value() {

        SimpleObject result = processFormData("urlencoded_form_data.txt", SimpleObject.class);

        assertThat(result.firstName).isEqualTo("Johnny \u00A3 mcfoo");

    }

    @Test
    public void multiple_values_with_the_same_name() {
        ObjectWithListProperty result = processFormData("multiple_values_with_same_name_form_data.txt", ObjectWithListProperty.class);

        assertThat(result.name).isEqualTo("Johnny");
        assertThat(result.favouriteBooks.size()).isEqualTo(2);
        assertThat(result.favouriteBooks.get(0)).isEqualTo("UBIK");
        assertThat(result.favouriteBooks.get(1)).isEqualTo("IROBOT");

    }


    private static <T> T processFormData(String entity, Class<T> type) {
        InputStream in = inputStreamFromClasspathEntry(WwwFormUrlEncodedTest.class, entity);

        return parse().inputStream(in)
                .from().wwwFormUrlEncoded()
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

    private static class ObjectWithListProperty {

        public final String name;
        public final List<String> favouriteBooks;

        private ObjectWithListProperty(String name, List<String> favouriteBooks) {
            this.name = name;
            this.favouriteBooks = favouriteBooks;
        }
    }

}