package com.grsu.database_library.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.grsu.database_library.annotations.dbNotNull.NotNull;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@dbNotNull
@Retention(RUNTIME)
@Target(FIELD)
public @interface dbText {
    enum Text {
        CHARACTER,
        VARCHAR,
        NCHAR,
        NVARCHAR,
        TEXT,
        CLOB,
    }

    Text value() default Text.TEXT;
    NotNull isNotNull() default NotNull.NULL;
}
