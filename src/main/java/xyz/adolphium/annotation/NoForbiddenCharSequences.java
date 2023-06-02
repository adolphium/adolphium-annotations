package xyz.adolphium.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD})
@Documented
@Constraint(validatedBy = CharSequenceValidator.class)
public @interface NoForbiddenCharSequences {

    String message() default "Forbidden chars";


    String[] field() default {"<", ">", "%", "$","&"};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
