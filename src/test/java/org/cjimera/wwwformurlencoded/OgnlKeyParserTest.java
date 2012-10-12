package org.cjimera.wwwformurlencoded;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        OgnlKeyParser.OgnlKey key = null;
        String input = "someIndexedProperty[22]";

        Pattern p = Pattern.compile("(.*)\\[([0-9]*)\\]");
        Matcher matcher = p.matcher(input);
        if (matcher.matches()) {
             key = new OgnlKeyParser.OgnlKey(matcher.group(1), true, parseInt(matcher.group(2)));
        }

        assertThat(key.isArray).isTrue();
        assertThat(key.name).isEqualTo("someIndexedProperty");
        assertThat(key.arrayIndex).isEqualTo(22);

    }


}