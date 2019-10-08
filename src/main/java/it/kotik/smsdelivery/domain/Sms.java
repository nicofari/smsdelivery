package it.kotik.smsdelivery.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
public class Sms {

    @Id
    @GeneratedValue
    private UUID id;

    public String getIdAsString() {
        return id.toString();
    }

    @NotBlank(message = "UserId is required")
    public String userId;

    @PhoneNumberConstraint
    public String sourceNumber;

    @PhoneNumberConstraint
    public String destNumber;

    @BodyConstraint
    public String body;
}
