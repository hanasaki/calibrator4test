/**
 * Copyright 2015 Craig A. Stockton
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
package com.craigstockton.validator4test;

import java.util.List;

public class Reconciler {

    private final static int RECONCILED = 0;
    private final static int EXPECTED_NULL = 1;
    private final static int ACTUAL_NULL = 2;
    private final static int DISCREPANCY = 3;
    private final Object actual;
    private final Object expected;

    private Reconciler(Object expected, Object actual) {
        this.expected = expected;
        this.actual = actual;
    }

    /**
     * Get an instance of a Boolean reconciler
     *
     * @return An instance of of a Boolean reconciler
     */
    static Reconciler getInstance(Boolean expected, Boolean actual) {
        return new Reconciler(expected, actual);
    }

    /**
     * Get an instance of a Integer reconciler
     *
     * @return An instance of of a Integer reconciler
     */
    static Reconciler getInstance(Integer expected, Integer actual) {
        return new Reconciler(expected, actual);
    }

    /**
     * Get an instance of a Long reconciler
     *
     * @return An instance of of a Long reconciler
     */
    static Reconciler getInstance(Long expected, Long actual) {
        return new Reconciler(expected, actual);
    }

    /**
     * Get an instance of a String reconciler
     *
     * @return An instance of of a String reconciler
     */
    static Reconciler getInstance(String expected, String actual) {
        return new Reconciler(expected, actual);
    }

    /**
     * Get an instance of a String-list reconciler
     *
     * @return An instance of of a String-list reconciler
     */
    static Reconciler getInstance(List<String> expected, List<String> actual) {
        return new Reconciler(expected, actual);
    }

    /**
     * @return Integer value indicating the reconciliation result (0 - Reconciled; 1 - Expected is null; 2 - Actual is
     * null; 3 - Discrepancy)
     */
    int reconcile() {
        if (expected == null && actual == null)
            return RECONCILED;
        if (expected == null)
            return EXPECTED_NULL;
        if (actual == null)
            return ACTUAL_NULL;
        return actual.equals(expected) ? RECONCILED : DISCREPANCY;
    }
}