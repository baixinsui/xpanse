/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.modules.models.service.register.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.xpanse.modules.models.common.exceptions.ResponseInvalidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test of TerraformScriptFormatInvalidException.
 */
class TerraformScriptFormatInvalidExceptionTest {

    private List<String> errorReasons1;
    private List<String> errorReasons2;
    private List<String> errorReasons3;

    @BeforeEach
    void setUp() {
        errorReasons1 = new ArrayList<>(Arrays.asList("Reason 1", "Reason 2"));
        errorReasons2 = new ArrayList<>(Arrays.asList("Reason 1", "Reason 2"));
        errorReasons3 = new ArrayList<>(Arrays.asList("Reason 1", "Reason 3"));
    }

    @Test
    void testConstructorAndGetErrorReasons() {
        ResponseInvalidException exception = new ResponseInvalidException(errorReasons1);
        assertEquals(errorReasons1, exception.getErrorReasons());
    }

    @Test
    void testEqualsAndHashCode() {
        ResponseInvalidException exception1 = new ResponseInvalidException(errorReasons1);
        ResponseInvalidException exception2 = new ResponseInvalidException(errorReasons2);
        ResponseInvalidException exception3 = new ResponseInvalidException(errorReasons3);

        assertEquals(exception1, exception1);
        assertNotEquals(exception1, exception2);
        assertNotEquals(exception1, exception3);

        assertEquals(exception1.hashCode(), exception1.hashCode());
        assertNotEquals(exception1.hashCode(), exception2.hashCode());
        assertNotEquals(exception1.hashCode(), exception3.hashCode());
    }

    @Test
    void testToString() {
        ResponseInvalidException exception = new ResponseInvalidException(errorReasons1);

        String expectedToString =
                "ResponseInvalidException(errorReasons=" + errorReasons1 + ")";
        assertEquals(expectedToString, exception.toString());
    }

}