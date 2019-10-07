package it.kotik.smsdelivery.domain;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Sms {

    @NotBlank(message = "UserId is required")
    public String userId;

    @NotBlank(message = "Source number is required")
    @PhoneNumberConstraint
    public String sourceNumber;

    @NotBlank(message = "Destination number is required")
    @PhoneNumberConstraint
    public String destNumber;

    @NotBlank(message = "Body is required")
    @BodyConstraint
    public String body;
}
