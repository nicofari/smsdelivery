package it.kotik.smsdelivery.domain;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BodyValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BodyConstraint {
    String message() default "Invalid body";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
