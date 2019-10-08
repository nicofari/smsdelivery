package it.kotik.smsdelivery.domain.validator;

import org.apache.logging.log4j.util.Strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BodyValidator implements ConstraintValidator<BodyConstraint, String> {

    private static final String sms = " @£$¥èéùìòÇØøÅåΔ_ΦΓΛΩΠΨΣΘΞ^{}\\[~]|€ÆæßÉ!\"#¤%&'()*+,-./0123456789:;<=>?¡ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÑÜ§¿abcdefghijklmnopqrstuvwxyzäöñüà";

    @Override
    public void initialize(BodyConstraint constraintAnnotation) {}

    @Override
    public boolean isValid(String bodyValue, ConstraintValidatorContext constraintValidatorContext) {
        return Strings.isNotBlank(bodyValue) && isInRange(bodyValue);
    }

    private boolean isInRange(String value) {
        for (char c : value.toCharArray()) {
            if (sms.indexOf(c) < 0) {
                return false;
            }
        }
        return true;
    }
}
