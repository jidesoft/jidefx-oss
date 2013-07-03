/*
 * @(#)ValidationIcons.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.validation;

import javafx.event.EventType;

@SuppressWarnings("UnusedDeclaration")
public class ValidationIcons {
    public static final String ICON_CORRECT = "/jidefx/scene/control/decoration/overlay_correct.png"; //NON-NLS
    public static final String ICON_INFO = "/jidefx/scene/control/decoration/overlay_info.png"; //NON-NLS
    public static final String ICON_ATTENTION = "/jidefx/scene/control/decoration/overlay_warning.png"; //NON-NLS
    public static final String ICON_QUESTION = "/jidefx/scene/control/decoration/overlay_question.png"; //NON-NLS
    public static final String ICON_ERROR = "/jidefx/scene/control/decoration/overlay_error.png"; //NON-NLS

    public static String getValidationResultIcon(EventType<ValidationEvent> type) {
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
