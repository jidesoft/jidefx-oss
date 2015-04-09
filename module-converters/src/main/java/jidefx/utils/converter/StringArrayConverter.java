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

public class StringArrayConverter extends DefaultObjectConverter<Object> {
    public static final ConverterContext CONTEXT = new ConverterContext("StringArray"); //NON-NLS
    private String _separator = ";";

    /**
     * Creates a StringArrayConverter using default constructor. The semicolon (";") will be used as the separator when
     * converting from string to array and vise versa.
     */
    public StringArrayConverter() {
    }

    /**
     * Creates a StringArrayConverter with a specified separator. Please make sure the separator is not used in the
     * character set used in each string element. For example, you want to use space as separator, then each string in
     * the string array must not use space.
     *
     * @param separator the separator used to separate string to an array.
     */
    public StringArrayConverter(String separator) {
        _separator = separator;
    }

    @Override
    public String toString(Object object, ConverterContext context) {
        if (object == null) {
            return null;
        }
        if (object.getClass().isArray()) {
            String[] array = (String[]) object;
            StringBuffer b = new StringBuffer();
            for (int i = 0; i < array.length; i++) {
                if (i > 0) b.append(_separator);
                b.append(array[i]);
            }
            return b.toString();
        }
        return null;
    }

    @Override
    public Object fromString(String string, ConverterContext context) {
        if (string.length() == 0) {
            return new String[0];
        }
        return string.split(_separator);
    }
}
