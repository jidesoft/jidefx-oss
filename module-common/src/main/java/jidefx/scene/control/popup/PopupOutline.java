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
