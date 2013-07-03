/*
 * @(#)FieldsResource.java 5/26/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field;

import java.util.Locale;
import java.util.ResourceBundle;

public class FieldsResource {
    static final String BASENAME = "jidefx.scene.control.field.fields"; //NON-NLS

    static final ResourceBundle RB = ResourceBundle.getBundle(BASENAME);

    public static ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle(BASENAME, locale);
    }
}
