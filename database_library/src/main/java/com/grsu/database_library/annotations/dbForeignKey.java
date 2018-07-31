package com.grsu.database_library.annotations;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(FIELD)
public @interface dbForeignKey {

    enum ForeignKey {
        FOREIGN_KEY("FOREIGN KEY");


        private String key;

        ForeignKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    enum ChangeData {
        UPDATE_CASCADE("ON UPDATE CASCADE"),
        DELETE_CASCADE("ON DELETE CASCADE"),
        DELETE_NO_ACTION("ON DELETE NO ACTION"),
        UPDATE_NO_ACTION("ON UPDATE NO ACTION"),
        NONE(""),;


        private String name;

        ChangeData(String name) {
            this.name = name;
        }

        public String getValue() {
            return this.name;
        }
    }

    ForeignKey value() default ForeignKey.FOREIGN_KEY;

    Class<?> entity();

    String entityField();

    ChangeData onDelete() default ChangeData.NONE;

    ChangeData onUpdate() default ChangeData.NONE;

    //FOREIGN KEY(`@dbForeignKey field`) REFERENCES `entity()`(`entityField()`) onDelete() onUpdate()
}
