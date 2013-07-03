package jidefx.scene.control.field;

/**
 * An enum for SpinnerStyles. It can be set using {@link FormattedTextField#setSpinnerStyle(SpinnerStyle)}.
 *
 * @see FormattedTextField
 */
public enum SpinnerStyle {
    /**
     * The spinner buttons are inside the field, on the right hand side and vertically positioned. This is the default
     * style.
     */
    INSIDE_RIGHT_VERTICAL,
    /**
     * The spinner buttons are inside the field, on the left hand side and vertically positioned.
     */
    INSIDE_LEFT_VERTICAL,

    /**
     * The spinner buttons are inside the field, on the right hand side and horizontally positioned.
     */
    INSIDE_RIGHT_HORIZONTAL,

    /**
     * The spinner buttons are inside the field, on the left hand side and horizontally positioned.
     */
    INSIDE_LEFT_HORIZONTAL,

    /**
     * The spinner buttons are inside the field, on both sides and horizontally positioned.
     */
    INSIDE_CENTER_HORIZONTAL,

    /**
     * The spinner buttons are outside the field, on the right hand side and vertically positioned. Better use it by
     * adding {@link FormattedTextField#asSpinner()} to a parent.
     */
    OUTSIDE_RIGHT_VERTICAL,

    /**
     * The spinner buttons are outside the field, on the left hand side and vertically positioned. Better use it by
     * adding {@link FormattedTextField#asSpinner()} to a parent.
     */
    OUTSIDE_LEFT_VERTICAL,
    /**
     * The spinner buttons are outside the field, on the horizontal center and vertically positioned. Better use it by
     * adding {@link FormattedTextField#asSpinner()} to a parent.
     */
    OUTSIDE_CENTER_VERTICAL,
    /**
     * The spinner buttons are outside the field, on the right hand side and horizontally positioned. Better use it by
     * adding {@link FormattedTextField#asSpinner()} to a parent.
     */
    OUTSIDE_RIGHT_HORIZONTAL,
    /**
     * The spinner buttons are outside the field, on the left hand side and horizontally positioned. Better use it by
     * adding {@link FormattedTextField#asSpinner()} to a parent.
     */
    OUTSIDE_LEFT_HORIZONTAL,
    /**
     * The spinner buttons are outside the field, on both sides and horizontally positioned. Better use it by
     * adding {@link FormattedTextField#asSpinner()} to a parent.
     */
    OUTSIDE_CENTER_HORIZONTAL,
}
