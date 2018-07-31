package com.grsu.database_library;

import com.grsu.database_library.annotations.dbBlob;
import com.grsu.database_library.annotations.dbForeignKey;
import com.grsu.database_library.annotations.dbInteger;
import com.grsu.database_library.annotations.dbNumeric;
import com.grsu.database_library.annotations.dbPrimaryKey;
import com.grsu.database_library.annotations.dbReal;
import com.grsu.database_library.annotations.dbTable;
import com.grsu.database_library.annotations.dbText;
import com.grsu.database_library.utils.DbUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SqlConnector {

    private static final String TABLE_TEMPLATE = "CREATE TABLE IF NOT EXISTS";
    private static final String REFERENCES = "REFERENCES";
    private static String TABLE_KEYS = null;

    public static List<String> createTables(final Class<?>[] tableClassArray)
            throws IllegalAccessException {

        List<String> queries = new ArrayList<>();

        for (Class<?> tableClass : tableClassArray) {
            StringBuilder stringBuilder = new StringBuilder();

            final dbTable dbTableAnnotation = tableClass.getAnnotation(dbTable.class);

            for (final Field field : tableClass.getFields()) {
                stringBuilder = getFieldString(field, stringBuilder);
            }

            queries.add(String.format("\n%s '%s' (\n%s,\n%s);\n", TABLE_TEMPLATE,
                    dbTableAnnotation.value().getSimpleName(), stringBuilder, TABLE_KEYS));
            TABLE_KEYS = null;
        }
        return queries;
    }

    private static StringBuilder getFieldString(Field field, StringBuilder stringBuilder)
            throws IllegalAccessException {
        String fieldName = (String) field.get(null);

        for (final Annotation fieldAnnotation : field.getAnnotations()) {
            String fieldType = getFieldType(fieldAnnotation);
            stringBuilder = gets(fieldAnnotation, fieldName, fieldType, stringBuilder);
        }

        return stringBuilder;
    }

    private static String getFieldType(Annotation fieldAnnotation) {

        final Class<?> fieldAnnotationType = fieldAnnotation.annotationType();
        if (fieldAnnotationType.equals(dbText.class)) {
            dbText dbText = (dbText) fieldAnnotation;
            return dbText.value().toString() + " " + dbText.isNotNull().getValue();
        }

        if (fieldAnnotationType.equals(dbReal.class)) {
            dbReal dbReal = (dbReal) fieldAnnotation;
            return dbReal.value().toString() + " " + dbReal.isNotNull().getValue();
        }

        if (fieldAnnotationType.equals(dbInteger.class)) {
            dbInteger dbInteger = (dbInteger) fieldAnnotation;
            return dbInteger.value().toString() + " " + dbInteger.isNotNull().getValue();
        }

        if (fieldAnnotationType.equals(dbBlob.class)) {
            dbBlob dbBlob = (dbBlob) fieldAnnotation;
            return dbBlob.value().toString() + " " + dbBlob.isNotNull().getValue();
        }
        if (fieldAnnotationType.equals(dbNumeric.class)) {
            dbNumeric dbNumeric = (dbNumeric) fieldAnnotation;
            return dbNumeric.value().toString() + " " + dbNumeric.isNotNull().getValue();
        }

        return null;
    }

    private static StringBuilder gets(Annotation fieldAnnotation, String fieldName,
            String fieldType, StringBuilder stringBuilder) {
        if (fieldAnnotation != null) {

            if (fieldAnnotation instanceof dbPrimaryKey) {
                TABLE_KEYS = getPrimaryKeyString(fieldAnnotation, fieldName);
            }

            if (fieldAnnotation instanceof dbForeignKey) {
                TABLE_KEYS = getForeignKeyString(fieldAnnotation, fieldName);
            }

            if (!(fieldAnnotation instanceof dbForeignKey)
                    && !(fieldAnnotation instanceof dbPrimaryKey)) {
                stringBuilder = getStringBuilder(stringBuilder, fieldName, fieldType);
            }

        }

        return stringBuilder;
    }

    private static StringBuilder getStringBuilder(StringBuilder stringBuilder, String fieldName,
            String fieldType) {
        if (stringBuilder.length() != 0) {
            return stringBuilder.append(",\n").append(DbUtils.set(fieldName)).append(" ")
                    .append(fieldType);
        }

        return stringBuilder.append(DbUtils.set(fieldName)).append(" ").append(fieldType);
    }

    private static String getPrimaryKey(dbPrimaryKey primaryKey, String fieldName) {
        return primaryKey.key().getValue() + DbUtils.set1(fieldName);
    }

    private static String getPrimaryKeyString(Annotation fieldAnnotation, String fieldName) {

        dbPrimaryKey primaryKey = ((dbPrimaryKey) fieldAnnotation);
        if (TABLE_KEYS != null) {
            return TABLE_KEYS + ",\n" + getPrimaryKey(primaryKey, fieldName);
        }

        return getPrimaryKey(primaryKey, fieldName);
    }

    private static String getForeignKey(dbForeignKey foreignKey, String fieldName) {
        return foreignKey.value().getKey() + DbUtils.set1(fieldName) + " " + REFERENCES + " "
                + DbUtils.set(foreignKey.entity().getSimpleName()) + DbUtils
                .set1(foreignKey.entityField()) + " " + foreignKey.onDelete().getValue() + " "
                + foreignKey.onUpdate().getValue();
    }

    private static String getForeignKeyString(Annotation fieldAnnotation, String fieldName) {
        dbForeignKey foreignKey = ((dbForeignKey) fieldAnnotation);
        if (TABLE_KEYS != null) {
            return TABLE_KEYS + ",\n" + getForeignKey(foreignKey, fieldName);
        }

        return getForeignKey(foreignKey, fieldName);
    }

}
