package org.cjimera.wwwformurlencoded;

import org.junit.Test;

import static java.lang.Integer.parseInt;
import static org.fest.assertions.Assertions.assertThat;

public class OgnlKeyParserTest {

    private OgnlKeyParser ognlKeyParser = new OgnlKeyParser();

    @Test
    public void parse_simple_key() {

        OgnlKeyParser.OgnlKey key = ognlKeyParser.parse("someProperty");

        assertThat(key.isArray).isFalse();
        assertThat(key.name).isEqualTo("someProperty");
        assertThat(key.arrayIndex).isEqualTo(-1);

    }

    @Test
    public void parse_indexed_key() {

        OgnlKeyParser.OgnlKey key = ognlKeyParser.parse("someIndexedProperty[22]");


        assertThat(key.isArray).isTrue();
        assertThat(key.name).isEqualTo("someIndexedProperty");
        assertThat(key.arrayIndex).isEqualTo(22);

    }


}