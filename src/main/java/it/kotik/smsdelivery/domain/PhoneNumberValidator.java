package it.kotik.smsdelivery.domain;

import org.apache.logging.log4j.util.Strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberConstraint, String> {

    @Override
    public void initialize(PhoneNumberConstraint constraintAnnotation) {}

    @Override
    public boolean isValid(String fieldValue, ConstraintValidatorContext constraintValidatorContext) {
        return Strings.isNotBlank(fieldValue) && fieldValue.matches("^\\+(?:[0-9] ?){6,14}[0-9]$");
    }

}
