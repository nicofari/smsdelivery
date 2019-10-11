package it.kotik.smsdelivery.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import it.kotik.smsdelivery.domain.validator.BodyConstraint;
import it.kotik.smsdelivery.domain.validator.PhoneNumberConstraint;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sms {
    public Sms() {
    }

    public Sms(@NotBlank(message = "UserId is required") String userId, String sourceNumber, String destNumber, String body) {
        this.userId = userId;
        this.sourceNumber = sourceNumber;
        this.destNumber = destNumber;
        this.body = body;
    }

    @Id
    @GeneratedValue
    private UUID id;

    @JsonIgnore
    public String getIdAsString() {
        return id.toString();
    }

    @NotBlank(message = "UserId is required")
    private String userId;

    public String getUserId() {
        return userId;
    }

    @PhoneNumberConstraint
    private String sourceNumber;

    public String getSourceNumber() {
        return sourceNumber;
    }

    @PhoneNumberConstraint
    private String destNumber;

    public String getDestNumber() {
        return destNumber;
    }

    @BodyConstraint
    private String body;

    public String getBody() {
        return body;
    }

    private SmsState state = SmsState.ACCEPTED;

    public SmsState getState() {
        return state;
    }

    public void setState(SmsState state) {
        this.state = state;
    }

    @JsonIgnore
    public boolean isDeletable() {
        return SmsState.deletableStates().contains(state);
    }

    public final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public void confirm() {
        state = SmsState.CONFIRMED;
        confirmedDate = LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }

    @JsonIgnore
    public boolean isAccepted() {
        return state == SmsState.ACCEPTED;
    }

    private String receptionDate = LocalDateTime.now().format(DATE_TIME_FORMATTER);

    public String getReceptionDate() {
        return receptionDate;
    }

    private String sentDate;

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    private String confirmedDate;

    public String getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(String confirmedDate) {
        this.confirmedDate = confirmedDate;
    }
}
