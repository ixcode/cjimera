package org.cjimera;

import java.io.InputStream;

public class ParseRepresentation {

    public static ParseRepresentationBuilder parse() {
        return new ParseRepresentationBuilder();
    }


    public static class ParseRepresentationBuilder {

        public ParseRepresentationBuilder() {

        }

        public ParseRepresentationBuilder inputStream(InputStream in) {
            return this;
        }

        public SourceTypeBuilder from() {
            return new SourceTypeBuilder(this);
        }

        public <T> T to(Class<T> type) {
            return null;
        }

    }

    public static class SourceTypeBuilder {

        private ParseRepresentationBuilder parent;

        public SourceTypeBuilder(ParseRepresentationBuilder parent) {
            this.parent = parent;
        }

        public ParseRepresentationBuilder applicationFormUrlEncoded() {
            return parent;
        }

    }


}