package it.kotik.smsdelivery.controller;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {
    private final String href;
    private final String errorMsg;

    public String getHref() {
        return href;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public ResponseDto(String href, String errorMsg) {
        this.href = href;
        this.errorMsg = errorMsg;
    }
}
