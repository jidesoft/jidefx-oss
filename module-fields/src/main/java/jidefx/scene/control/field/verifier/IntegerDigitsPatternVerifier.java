/*
 * Copyright (c) 2002-2015, JIDE Software Inc. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package jidefx.scene.control.field.verifier;

/**
 * A pattern verifier that verifies the digits that is the integer digits (before the floating point) of the whole
 * number.
 */
public abstract class IntegerDigitsPatternVerifier<T> extends NumberValuePatternVerifier<T> {
    public IntegerDigitsPatternVerifier() {
        this(0, Integer.MAX_VALUE, 1, 1);
    }

    public IntegerDigitsPatternVerifier(int min, int max) {
        this(min, max, 1, 1);
    }

    public IntegerDigitsPatternVerifier(int min, int max, double valueMultiplier) {
        this(min, max, 1, valueMultiplier);
    }

    public IntegerDigitsPatternVerifier(int min, int max, double adjustmentMultiplier, double valueMultiplier) {
        super(min, max, adjustmentMultiplier, valueMultiplier);
    }

    @Override
    protected Number getGroupValue(double targetValue) {
        super.getGroupValue(targetValue);
        return Math.round(targetValue);
    }

    @Override
    protected double getTargetValue(Number groupValue) {
        double value = getMultipliedValue(targetValue.doubleValue());
        return getDemultipliedValue(Math.round(value - Math.round(value)) + groupValue.intValue());
    }
}
