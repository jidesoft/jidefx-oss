/*
 * @(#)PercentConverterTest.java 5/26/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class PercentConverterTest {

    private PercentConverter _converter;

    @Before
    public void setUp() throws Exception {
        _converter = new PercentConverter();

    }

    @Test
    public void testFromString() throws Exception {
        Assert.assertEquals(0.5, _converter.fromString("50%"));
        Assert.assertEquals(0.5, _converter.fromString("50"));
        Assert.assertEquals(0.005, _converter.fromString("0.5"));

    }

    @Test
    public void testToString() throws Exception {
        Assert.assertEquals("50%", _converter.toString(0.5));
    }
}
