package it.kotik.smsdelivery.domain;

import it.kotik.smsdelivery.domain.validator.BodyConstraint;
import it.kotik.smsdelivery.domain.validator.PhoneNumberConstraint;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
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

}
