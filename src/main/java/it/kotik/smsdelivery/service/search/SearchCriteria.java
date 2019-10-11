package it.kotik.smsdelivery.service.search;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SearchCriteria {
    private final String key;
    private final String operator;
    private final Object value;

    public String getKey() {
        return key;
    }

    public String getOperator() {
        return operator;
    }

    public Object getValue() {
        return isDate() ? asDate() : value;
    }

    private boolean isDate() {
        return key.endsWith("Date");
    }

    public LocalDateTime asDate() {
        if (value == null) {
            return null;
        }
        String dateString = value.toString();
        DateTimeFormatter formatter;
        if (dateString.length() == 10) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(value.toString(), formatter).atStartOfDay();
        } else {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return LocalDateTime.parse(value.toString(), formatter);
        }
    }

    public SearchCriteria(String key, String operator, Object value) {
        this.key = key;
        this.operator = operator;
        this.value = value;
    }
}
