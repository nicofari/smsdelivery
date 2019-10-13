package it.kotik.smsdelivery.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.kotik.smsdelivery.domain.Sms;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmsPageDto {
    private List<Sms> content;
    private String prev;
    private String next;

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<Sms> getContent() {
        return content;
    }

    public void setContent(List<Sms> content) {
        this.content = content;
    }
}
