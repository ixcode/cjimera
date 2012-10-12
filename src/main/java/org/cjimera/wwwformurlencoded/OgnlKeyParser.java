package org.cjimera.wwwformurlencoded;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class OgnlKeyParser {


    public OgnlKey parse(String input) {
        Pattern p = Pattern.compile("(.*)\\[([0-9]*)\\]");
        Matcher matcher = p.matcher(input);
        if (matcher.matches()) {
            return new OgnlKey(matcher.group(1), true, parseInt(matcher.group(2)));
        }

        return new OgnlKey(input, false, -1);
    }

    public static class OgnlKey {

        public final String name;
        public final boolean isArray;
        public final int arrayIndex;

        public OgnlKey(String name, boolean isArray, int arrayIndex) {
            this.isArray = isArray;
            this.name = name;
            this.arrayIndex = arrayIndex;
        }
    }


}