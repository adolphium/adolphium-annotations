package xyz.adolphium.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class CharSequenceValidator implements ConstraintValidator<NoForbiddenCharSequences, Object> {

    private Collection<String> forbiddenCharSequences;

    @Override
    public void initialize(NoForbiddenCharSequences constraintAnnotation) {
        this.forbiddenCharSequences = Arrays.asList(constraintAnnotation.field());
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (Objects.isNull(object)) {
            return true;
        }
        if (object instanceof String string) {
            return !containsForbiddenChars(string);
        }
        for (var declaredField : object.getClass().getDeclaredFields()) {
            if (fieldHasOverridingAnnotation(declaredField)) {
                continue;
            }
            declaredField.setAccessible(true); // not used to update the field; the declaredField's value is anyway a copy
            Object o;
            try {
                o = declaredField.get(object);
            } catch (IllegalAccessException e) {
                throw new AssertionError();
            }
            if (o instanceof String string1 && containsForbiddenChars(string1)) {
                return false;
            }
        }
        return true;
    }

    private boolean fieldHasOverridingAnnotation(Field declaredField) {
        return Objects.nonNull(declaredField.getAnnotation(NoForbiddenCharSequences.class));
    }

    private boolean containsForbiddenChars(String string) {
        for (var forbiddenCharSequence : forbiddenCharSequences) {
            if (string.contains(forbiddenCharSequence)) {
                return true;
            }
        }
        return false;
    }
}
