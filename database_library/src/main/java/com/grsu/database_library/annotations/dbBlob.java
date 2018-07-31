package com.grsu.database_library.annotations;

import com.grsu.database_library.annotations.dbNotNull.NotNull;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@dbNotNull
@Retention(RUNTIME)
@Target(FIELD)
public @interface dbBlob {

    enum Blob{
        BLOB
    }

    Blob value() default Blob.BLOB;
    NotNull isNotNull() default NotNull.NULL;
}
