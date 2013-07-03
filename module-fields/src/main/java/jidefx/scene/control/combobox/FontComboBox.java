/*
 * @(#)FontComboBox.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.combobox;

import javafx.scene.text.Font;
import jidefx.scene.control.field.FontField;
import jidefx.scene.control.field.FormattedTextField;

/**
 * A {@code FormattedComboBox} for {@link Font}.
 */
public class FontComboBox extends FormattedComboBox<Font> {
    private static final String STYLE_CLASS_DEFAULT = "font-combo-box"; //NON-NLS

    /**
     * Creates a {@code FontComboBox} with the value set to Font.getDefault().
     */
    public FontComboBox() {
        this(Font.getDefault());
    }

    /**
     * Creates a {@code FontComboBox} with the specified value.
     *
     * @param font the initial value
     */
    public FontComboBox(Font font) {
        super(font);
    }

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().add(STYLE_CLASS_DEFAULT);
    }

    @Override
    protected FormattedTextField<Font> createFormattedTextField() {
        return new FontField();
    }

    @Override
    protected void initializeComboBox() {
        super.initializeComboBox();
        setPopupContentFactory(((FontField) getEditor()).getPopupContentFactory());
    }
}
