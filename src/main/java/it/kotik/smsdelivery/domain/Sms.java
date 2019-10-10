package it.kotik.smsdelivery.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import it.kotik.smsdelivery.domain.validator.BodyConstraint;
import it.kotik.smsdelivery.domain.validator.PhoneNumberConstraint;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sms {
    public Sms() {}

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

    public void confirm() {
        state = SmsState.CONFIRMED;
        confirmedDate = LocalDateTime.now();
    }

    @JsonIgnore
    public boolean isAccepted() {
        return state == SmsState.ACCEPTED;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime receptionDate = LocalDateTime.now();

    public LocalDateTime getReceptionDate() {
        return receptionDate;
    }

    private LocalDateTime sentDate;

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }

    private LocalDateTime confirmedDate;

    public LocalDateTime getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(LocalDateTime confirmedDate) {
        this.confirmedDate = confirmedDate;
    }
}
