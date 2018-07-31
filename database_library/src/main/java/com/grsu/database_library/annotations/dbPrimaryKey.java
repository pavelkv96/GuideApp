package com.grsu.database_library.annotations;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(FIELD)
public @interface dbPrimaryKey {
    enum Key{
        PRIMARY_KEY("PRIMARY KEY"),
        UNIQUE("UNIQUE")
        ;

        private String name;

        Key(String name) {
            this.name = name;
        }

        public String getValue() {
            return name;
        }
    }

    Key key() default Key.PRIMARY_KEY;
}
