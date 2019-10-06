package it.kotik.smsdelivery.domain;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Sms {

    @NotBlank(message = "UserId is required")
    public String userId;

    @NotBlank(message = "Source number is required")
    public String sourceNumber;

    @NotBlank(message = "Destination number is required")
    public String destNumber;

    @NotBlank(message = "Body is required")
    public String body;
}
