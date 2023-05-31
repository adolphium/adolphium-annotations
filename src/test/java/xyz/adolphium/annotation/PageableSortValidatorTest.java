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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PageableSortValidatorTest {
    private final ValidPageableSortParam annotation = mock(ValidPageableSortParam.class);
    private final PageableSortValidator validator = new PageableSortValidator();

    private final static String[] allowedSortParams = new String[]{"firstName", "lastName", "secondName"};

    @BeforeEach
    void setUp() {
        when(annotation.field())
                .thenReturn(allowedSortParams);
        validator.initialize(annotation);
    }

    public static Stream<Arguments> allowedSortParams() {
        return Arrays.stream(allowedSortParams).map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("allowedSortParams")
    void shouldReturnTrue(String allowedSortParam) {
        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Order.asc(allowedSortParam)));
        assertTrue(validator.isValid(pageRequest, null));
    }

    @Test
    void shouldReturnFalse() {
        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Order.asc(UUID.randomUUID().toString())));
        assertTrue(validator.isValid(pageRequest, null));
    }
}