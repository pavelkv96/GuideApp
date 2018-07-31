package com.grsu.database_library.annotations;

import com.grsu.database_library.annotations.dbNotNull.NotNull;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@dbNotNull
@Retention(RUNTIME)
@Target(FIELD)
public @interface dbInteger {

    enum Integer {
        INT,
        TINYINT,
        SMALLINT,
        MEDIUMINT,
        INTEGER,
        BIGINT
    }

    Integer value() default Integer.INTEGER;
    NotNull isNotNull() default NotNull.NULL;
}
