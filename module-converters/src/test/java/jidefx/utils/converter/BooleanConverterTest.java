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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BooleanConverterTest {

    private BooleanConverter _converter;

    @Before
    public void setUp() throws Exception {
        _converter = new BooleanConverter();
    }

    @Test
    public void testToString() throws Exception {
        Assert.assertEquals("True", _converter.toString(true));
        Assert.assertEquals("False", _converter.toString(false));
    }

    @Test
    public void testFromString() throws Exception {
        Assert.assertEquals(Boolean.TRUE, _converter.fromString("true")); //NON-NLS
        Assert.assertEquals(Boolean.TRUE, _converter.fromString("True")); //NON-NLS

        Assert.assertEquals(Boolean.FALSE, _converter.fromString("false")); //NON-NLS
        Assert.assertEquals(Boolean.FALSE, _converter.fromString("False")); //NON-NLS
    }
}
