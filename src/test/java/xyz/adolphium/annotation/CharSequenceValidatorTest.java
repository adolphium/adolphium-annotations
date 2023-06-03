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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CharSequenceValidatorTest {
    private final NoForbiddenCharSequences annotation = mock(NoForbiddenCharSequences.class);
    private final CharSequenceValidator validator = new CharSequenceValidator();
    private final static String[] defaultForbiddenStrings = new String[]{"%", "$", "&", "<", ">"};
    private final static String[] customForbiddenStrings = new String[]{"a","lelelele","!"};

    static class TestObject {
        TestObject(String testString, Object testObject) {
            this.testString = testString;
            this.testObject = testObject;
        }

        private final String testString;
        private final Object testObject;
    }

    static class TestObjectWithOverriddenAnnotation {
        TestObjectWithOverriddenAnnotation(String testString, Object testObject) {
            this.testString = testString;
            this.testObject = testObject;
        }

        @NoForbiddenCharSequences(field = "a")
        private final String testString;
        private final Object testObject;
    }

    public static Stream<Arguments> objectsReturningNotValid() {
        return Stream.concat(Arrays.stream(defaultForbiddenStrings),
                        Arrays.stream(defaultForbiddenStrings)
                                .map(x -> new TestObject(x, new Object())))
                .map(Arguments::of);
    }


    public static Stream<Arguments> objectsReturningValid() {
        var willIgnoreOverriddenAnnotation = Arrays.stream(customForbiddenStrings)
                .map(x -> new TestObjectWithOverriddenAnnotation(x, new Object()));
        return Stream.concat(Stream.of("test", "test1", "xf2"),
                        Stream.concat(willIgnoreOverriddenAnnotation,
                                Stream.of("only", "allowed", "Strings", "")
                                        .map(x -> new TestObject(x, new Object()))))
                .map(Arguments::of);
    }

    @BeforeEach
    void setUp() {
        when(annotation.field())
                .thenReturn(defaultForbiddenStrings);
        validator.initialize(annotation);
    }

    @ParameterizedTest
    @MethodSource("objectsReturningNotValid")
    void shouldReturnFalse(Object forbiddenObject) {
        assertFalse(validator.isValid(forbiddenObject, null));
    }

    @ParameterizedTest
    @MethodSource("objectsReturningValid")
    void shouldReturnTrue(Object allowedObject) {
        assertTrue(validator.isValid(allowedObject, null));

    }

    @Test
    void shouldReturnValidForNullValue() {
        assertTrue(validator.isValid(null, null));
    }
}