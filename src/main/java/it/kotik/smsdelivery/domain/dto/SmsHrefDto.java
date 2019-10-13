package it.kotik.smsdelivery.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmsHrefDto {
    private final String href;

    public String getHref() {
        return href;
    }

    public SmsHrefDto(String smsId) {
        this.href = "/public/api/v1/sms/" + smsId;
    }
}
