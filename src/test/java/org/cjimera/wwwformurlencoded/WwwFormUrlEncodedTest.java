package org.cjimera.wwwformurlencoded;

import org.cjimera.Parser;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static ixcode.platform.io.IoClasspath.inputStreamFromClasspathEntry;
import static java.lang.String.format;
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

    @Test
    public void object_graph() {
        ObjectWithGraph result = processFormData("object_graph_form_data.txt", ObjectWithGraph.class);

        assertThat(result.name).isEqualTo("Billy Bob");

        assertThat(result.address.firstLine).isEqualTo("1 the parade");
        assertThat(result.address.town).isEqualTo("Some Town");
        assertThat(result.address.postcode).isEqualTo(new Postcode("ST34 5ER"));

        assertThat(result.phoneNumber.areaCode).isEqualTo("+44");
        assertThat(result.phoneNumber.number).isEqualTo("335 3456");


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

    private static class ObjectWithGraph {

        public final String name;
        public final Address address;
        public final PhoneNumber phoneNumber;

        private ObjectWithGraph(String name, Address address, PhoneNumber phoneNumber) {
            this.name = name;
            this.address = address;
            this.phoneNumber = phoneNumber;
        }
    }

    private static class Address {

        public final String firstLine;
        public final String town;
        public final Postcode postcode;

        private Address(String firstLine, String town, Postcode postcode) {
            this.firstLine = firstLine;
            this.town = town;
            this.postcode = postcode;
        }
    }

    private static class Postcode {

        public final String code;

        private Postcode(String code) {
            this.code = code;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Postcode postcode = (Postcode) o;

            if (code != null ? !code.equals(postcode.code) : postcode.code != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return code != null ? code.hashCode() : 0;
        }

        @Override
        public String toString() {
            return code;
        }
    }

    private static class PhoneNumber {
        public final String areaCode;
        public final String number;

        private PhoneNumber(String areaCode, String number) {
            this.areaCode = areaCode;
            this.number = number;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PhoneNumber that = (PhoneNumber) o;

            if (areaCode != null ? !areaCode.equals(that.areaCode) : that.areaCode != null) return false;
            if (number != null ? !number.equals(that.number) : that.number != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = areaCode != null ? areaCode.hashCode() : 0;
            result = 31 * result + (number != null ? number.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return format("(%s) %s", areaCode, number);
        }
    }

}