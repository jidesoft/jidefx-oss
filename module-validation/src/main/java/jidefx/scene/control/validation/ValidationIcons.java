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
package jidefx.scene.control.validation;

import javafx.event.EventType;

/**
 * A class to provide all the icons for the validation results.
 */
@SuppressWarnings("UnusedDeclaration")
public class ValidationIcons {
    public static final String ICON_CORRECT = "/jidefx/scene/control/decoration/overlay_correct.png"; //NON-NLS
    public static final String ICON_INFO = "/jidefx/scene/control/decoration/overlay_info.png"; //NON-NLS
    public static final String ICON_ATTENTION = "/jidefx/scene/control/decoration/overlay_warning.png"; //NON-NLS
    public static final String ICON_QUESTION = "/jidefx/scene/control/decoration/overlay_question.png"; //NON-NLS
    public static final String ICON_ERROR = "/jidefx/scene/control/decoration/overlay_error.png"; //NON-NLS
    public static final String ICON_REQUIRED = "/jidefx/scene/control/decoration/required-indicator.png"; //NON-NLS
    private static ValidationIcons INSTANCE = null;

    /**
     * Gets a new instance which will be used globally to get the validation icons.
     *
     * @return the instance of {@code ValidationIcons}.
     */
    public static ValidationIcons getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ValidationIcons();
        }
        return INSTANCE;
    }

    /**
     * Sets a new instance which will be used globally to get the validation icons.
     *
     * @param instance a new instance of {@code ValidationIcons}.
     */
    public static void setInstance(ValidationIcons instance) {
        INSTANCE = instance;
    }

    /**
     * By default, we use our own icons inside the JideFX but you can override this method to return another set of
     * icons and then call {@link #setInstance(ValidationIcons)} to set your instance.
     *
     * @param type the event type
     * @return a full path to the icon in the class path format. For example, if you have an error.png icon under
     *         package com.mycompany.myicons, the full path would be "/com/mycompany/myicons/error.png". In the other
     *         word, the icon must be in the classpath in order for the icon to be used by {@code ValidationIcons}.
     */
    public String getValidationResultIcon(EventType<ValidationEvent> type) {
        if (ValidationEvent.VALIDATION_ERROR.equals(type)) {
            return ICON_ERROR;
        }
        else if (ValidationEvent.VALIDATION_WARNING.equals(type)) {
            return ICON_ATTENTION;
        }
        else if (ValidationEvent.VALIDATION_INFO.equals(type)) {
            return ICON_INFO;
        }
        else {
            return ICON_CORRECT;
        }
    }
}
