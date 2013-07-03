/*
 * @(#)Point2DComboBox.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.combobox;

import javafx.geometry.Point2D;
import jidefx.scene.control.field.FormattedTextField;
import jidefx.scene.control.field.Point2DField;

/**
 * A {@code FormattedComboBox} for {@link Point2D}.
 */
public class Point2DComboBox extends ValuesComboBox<Point2D> {
    public Point2DComboBox() {
        this(new Point2D(0, 0));
    }

    public Point2DComboBox(Point2D value) {
        super(value);
    }

    @Override
    protected FormattedTextField<Point2D> createFormattedTextField() {
        return new Point2DField();
    }

    @Override
    protected void initializeComboBox() {
        super.initializeComboBox();
        setPopupContentFactory(((Point2DField) getEditor()).getPopupContentFactory());
    }
}
