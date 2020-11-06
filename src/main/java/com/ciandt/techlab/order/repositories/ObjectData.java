package com.ciandt.techlab.order.repositories;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class ObjectData<U> {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    protected abstract U toEntity();

    LocalDateTime parseLocalDateTime(final String localDateTime) {
        if (localDateTime != null) {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
            return LocalDateTime.parse(localDateTime, formatter);
        }
        return null;
    }
}
