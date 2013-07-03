/*
 * @(#)ValuesComboBox.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.combobox;

/**
 * An abstract {@code FormattedComboBox} for any data types that can be represented as a value array.
 */
public abstract class ValuesComboBox<T> extends FormattedComboBox<T> {
    private static final String STYLE_CLASS_DEFAULT = "values-combo-box"; //NON-NLS

    public ValuesComboBox() {
    }

    public ValuesComboBox(T value) {
        super(value);
    }

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().add(STYLE_CLASS_DEFAULT);
    }
}
