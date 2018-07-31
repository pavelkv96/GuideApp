package com.grsu.database_library.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
public @interface dbNotNull {

    enum NotNull {
        NULL("NULL"),
        NOT_NULL("NOT NULL");

        private String key;

        NotNull(String key) {
            this.key = key;
        }

        public String getValue() {
            return key;
        }
    }
}
