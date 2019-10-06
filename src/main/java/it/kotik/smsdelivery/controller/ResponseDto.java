package it.kotik.smsdelivery.controller;

public class ResponseDto {
    private final String link;
    private final String errorMsg;

    public String getLink() {
        return link;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public ResponseDto(String link, String errorMsg) {
        this.link = link;
        this.errorMsg = errorMsg;
    }
}
