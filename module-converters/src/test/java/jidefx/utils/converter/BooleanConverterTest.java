/*
 * @(#)BooleanConverterTest.java 5/26/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
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
