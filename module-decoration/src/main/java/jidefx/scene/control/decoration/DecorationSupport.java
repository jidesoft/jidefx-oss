/*
 * @(#)DecorationSupport.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.decoration;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;

/**
 * {@code DecorationSupport} is an interface that indicates a Region can be a host for decorations created by {@link
 * Decorator}. {@link DecorationPane} is a host for decorations and it will cover majorities of the use cases for
 * decorations. However, in cases of TableColumnHeader, TableCell, ListCell, TreeCell, etc. that are inside a {@code
 * VirtualFlow}, using {@code DecorationPane} directly is not practical. That's why we abstracted this interface so that
 * those regions can implement it to provide its own dedicated support for decorations.
 * <p/>
 * Both methods below are on the {@link javafx.scene.layout.Region} but as protected so we can't call them directly from
 * outside. So by exposing them on this interface, we can call them from outside so that we can add decoration nodes and
 * position them at the location we wanted.
 * <p/>
 * When you implement this interface, all your need to do is to add the following implementation of both methods.
 * <pre>
 * {@code
 * public ObservableList&lt;Node&gt; getChildren() {
 *    return super.getChildren();
 * }
 *
 * public void positionInArea(Node child, double areaX, double areaY, double areaWidth, double areaHeight, double
 * areaBaselineOffset, HPos halignment, VPos valignment) {
 *     super.positionInArea(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, halignment, valignment);
 * }
 * }
 * </pre>
 *
 * @see DecorationDelegate more details.
 */
public interface DecorationSupport {
    public static final String STYLE_CLASS_DECORATION_SUPPORT = "decoration-support"; //NON-NLS

    /**
     * Gets the children of the region. {@code Parent}'s getChildren method is protected so the implementation of this
     * method could simply call super.getChildren().
     *
     * @return the children.
     */
    ObservableList<Node> getChildren();

    /**
     * This is a protected method on {@code Region}. The implementation of this method could simply call
     * super.positionInArea(...) with the exact same parameters.
     *
     * @param child              the child being positioned within this region
     * @param areaX              the horizontal offset of the layout area relative to this region
     * @param areaY              the vertical offset of the layout area relative to this region
     * @param areaWidth          the width of the layout area
     * @param areaHeight         the height of the layout area
     * @param areaBaselineOffset the baseline offset to be used if VPos is BASELINE
     * @param halignment         the horizontal alignment for the child within the area
     * @param valignment         the vertical alignment for the child within the area
     */
    void positionInArea(Node child, double areaX, double areaY, double areaWidth, double areaHeight,
                        double areaBaselineOffset, HPos halignment, VPos valignment);
}