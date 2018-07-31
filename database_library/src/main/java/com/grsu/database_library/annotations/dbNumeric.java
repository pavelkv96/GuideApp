package com.grsu.database_library.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@dbNotNull
@Retention(RUNTIME)
@Target(FIELD)
public @interface dbNumeric {
    enum Numeric {
        NUMERIC,
        BOOLEAN,
        DATE,
        DATETIME
    }

    Numeric value() default Numeric.NUMERIC;

    dbNotNull.NotNull isNotNull() default dbNotNull.NotNull.NULL;
}
