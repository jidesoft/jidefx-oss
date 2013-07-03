/*
 * @(#)PopupOutline.java 6/4/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.popup;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.shape.Path;

/**
 * {@code PopupOutline} is a special path that works along with {@link ShapedPopup}. You can write your own {@code
 * PopupOutline} to get different shaped popup windows.
 */
abstract public class PopupOutline extends Path {
    /**
     * Sets the width property of the outline.
     *
     * @return the width property.
     */
    abstract DoubleProperty widthProperty();

    /**
     * Gets the height property of the outline.
     *
     * @return the height property.
     */
    abstract DoubleProperty heightProperty();

    /**
     * Gets the origin point. The origin point is the point that points to the specified position of the owner node as
     * in {@link ShapedPopup#showPopup(javafx.scene.Node, javafx.geometry.Pos)}.
     *
     * @return the origin point
     */
    abstract Point2D getOriginPoint();

    /**
     * Gets the content padding. It is padding between the outline and the content of the actual popup window.
     *
     * @return the content padding.
     */
    abstract Insets getContentPadding();
}
