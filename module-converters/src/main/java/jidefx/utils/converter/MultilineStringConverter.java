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
package jidefx.utils.converter;

/**
 * An ObjectConverter for multi-line string. It basically escape "\r" and "\n" in the toString method so that a label
 * can display the whole string in one line instead of automatically wrapping the lines, and un-escape them in the
 * fromString method.
 */
public class MultilineStringConverter extends DefaultObjectConverter<String> {
    public static final ConverterContext CONTEXT = new ConverterContext("MultilineString"); //NON-NLS

    @Override
    public String toString(String object, ConverterContext context) {
        if (object != null) {
            return object.replaceAll("\\\\n", "\\\\\\\\n").replaceAll("\\\\r", "\\\\\\\\r").replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r"); //NON-NLS
        }
        else {
            return "";
        }
    }

    @Override
    public String fromString(String string, ConverterContext context) {
        if (string != null) {
            return string.replaceAll("\\\\\\\\n", "\\\\n").replaceAll("\\\\\\\\r", "\\\\r"); //NON-NLS
        }
        else {
            return null;
        }
    }

}