/*
 * @(#)ColorComboBox.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.combobox;

import javafx.scene.paint.Color;
import jidefx.scene.control.field.ColorField;
import jidefx.scene.control.field.FormattedTextField;

/**
 * A {@code FormattedComboBox} for {@link Color}.
 */
public class ColorComboBox extends FormattedComboBox<Color> {
    private static final String STYLE_CLASS_DEFAULT = "color-combo-box"; //NON-NLS

    public ColorComboBox() {
        this(Color.WHITE);
    }

    public ColorComboBox(Color color) {
        super(color);
    }

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().add(STYLE_CLASS_DEFAULT);
    }

    @Override
    protected FormattedTextField<Color> createFormattedTextField() {
        return new ColorField();
    }

    @Override
    protected void initializeComboBox() {
        super.initializeComboBox();
        setPopupContentFactory(((ColorField) getEditor()).getPopupContentFactory());
    }
}
