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

import java.util.List;

/**
 * A special verifier that restricts the input string to be in a String array. The order of the string in the array will
 * be used to implement the getNextValue and getPreviousValue methods.
 */
public abstract class StringValuesPatternVerifier<T> extends ValuesPatternVerifier<T, String> {
    public StringValuesPatternVerifier() {
    }

    public StringValuesPatternVerifier(String[] values) {
        super(values);
    }

    @Override
    public String format(String value) {
        return value;
    }

    @Override
    public String parse(String text) {
        List<String> values = getValues();
        for (String value : values) {
            if (value.startsWith(text)) {
                return value;
            }
        }
        return text;
    }

    @Override
    protected boolean matches(String current, String value) {
        return value.startsWith(current);
    }
}
