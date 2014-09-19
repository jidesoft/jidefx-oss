package jidefx.scene.control.validation;

import javafx.event.EventType;
import javafx.scene.text.Font;

/**
 * A class to provide all the FontAwesome icons for the validation results.
 */
public class ValidationFontAwesomeIcons {

    public static final String ICON_CORRECT = "\uf00c"; //NON-NLS
    public static final String ICON_INFO = "\uf129"; //NON-NLS
    public static final String ICON_ATTENTION = "\uf12a"; //NON-NLS
    public static final String ICON_QUESTION = "\uf128"; //NON-NLS
    public static final String ICON_ERROR = "\uf00d"; //NON-NLS
    private static ValidationFontAwesomeIcons INSTANCE = null;

    /**
     * Gets a new instance which will be used globally to get the validation icons.
     *
     * @return the instance of {@code ValidationIcons}.
     */
    public static ValidationFontAwesomeIcons getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ValidationFontAwesomeIcons();
            Font.loadFont(ValidationFontAwesomeIcons.class.getResource("/jidefx/scene/control/decoration/fontawesome.ttf").toExternalForm(), 12.0);
        }
        return INSTANCE;
    }

    /**
     * Sets a new instance which will be used globally to get the validation icons.
     *
     * @param instance a new instance of {@code ValidationIcons}.
     */
    public static void setInstance(ValidationFontAwesomeIcons instance) {
        INSTANCE = instance;
    }

    /**
     * By default, we use our own icons inside the JideFX but you can override this method to return another set of
     * icons and then call {@link #setInstance(ValidationFontAwesomeIcons)} to set your instance.
     *
     * @param type the event type
     * @return a full path to the icon in the class path format. For example, if you have an error.png icon under
     *         package com.mycompany.myicons, the full path would be "/com/mycompany/myicons/error.png". In the other
     *         word, the icon must be in the classpath in order for the icon to be used by {@code ValidationIcons}.
     */
    public String getValidationFontAwesomeIcon(EventType<ValidationEvent> type) {


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
