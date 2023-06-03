/**
 * Copyright 2023 Patrick Täsler
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
