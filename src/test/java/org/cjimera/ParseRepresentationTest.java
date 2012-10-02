package org.cjimera;

import org.fest.assertions.Assertions;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;

import static org.cjimera.ParseRepresentation.parse;
import static org.fest.assertions.Assertions.assertThat;


public class ParseRepresentationTest {

    @Test
    @Ignore("not yet implemented")
    public void can_parse_a_simple_form() {

        InputStream in = null;

        SimpleObject result = parse().inputStream(in)
                                     .from().applicationFormUrlEncoded()
                                     .to(SimpleObject.class);

        assertThat(result).isNotNull();
    }

    private static class SimpleObject {

        public final String firstName;
        public final String lastName;

        private SimpleObject(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

}