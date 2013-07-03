/*
 * @(#)SecurityUtils.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils;

import java.security.AccessControlException;

/**
 * A class that keeps all the security stuff so that an application can safely run in applet or WebStart environment.
 * Please refer to JIDE_Developer_Guide_for_WebStart_Applet.pdf in doc folder for more information.
 */
public class SecurityUtils {
    /**
     * Gets the system property.
     *
     * @param key          the property key
     * @param defaultValue the default value for the property.
     * @return the system property.
     */
    public static String getProperty(String key, String defaultValue) {
        try {
            return System.getProperty(key, defaultValue);
        }
        catch (AccessControlException e) {
            return defaultValue;
        }
    }
}

