/*
 * @(#)Rectangle2DComboBox.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.combobox;

import javafx.geometry.Rectangle2D;
import jidefx.scene.control.field.FormattedTextField;
import jidefx.scene.control.field.Rectangle2DField;

/**
 * A {@code FormattedComboBox} for {@link Rectangle2D}.
 */
public class Rectangle2DComboBox extends ValuesComboBox<Rectangle2D> {
    public Rectangle2DComboBox() {
        this(new Rectangle2D(0, 0, 0, 0));
    }

    public Rectangle2DComboBox(Rectangle2D value) {
        super(value);
    }


    @Override
    protected FormattedTextField<Rectangle2D> createFormattedTextField() {
        return new Rectangle2DField();
    }

    @Override
    protected void initializeComboBox() {
        super.initializeComboBox();
        setPopupContentFactory(((Rectangle2DField) getEditor()).getPopupContentFactory());
    }
}
