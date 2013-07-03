/*
 * @(#)Resource.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.searchable;

import java.util.Locale;
import java.util.ResourceBundle;

class Resource {
    static final String BASENAME = "jidefx.scene.control.searchable.searchable"; //NON-NLS

    static final ResourceBundle RB = ResourceBundle.getBundle(BASENAME);

    public static ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle(BASENAME, locale);
    }
}
