package hr.java.spring.foodstore.foodstore.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target( { ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PriceValidator.class)
public @interface PriceConstraint {
    String message() default "Invalid price: selling price must be greater than initial price";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
