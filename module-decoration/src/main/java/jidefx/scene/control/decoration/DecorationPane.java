/*
 * @(#)DecorationPane.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.decoration;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

/**
 * {@code DecorationPane} is the default implementation for the DecorationSupport. DecorationPane which will search
 * for all the decorators installed on its children (more precisely, descendants) and placed them at the position as
 * specified in the Decorator interface. A typical use case for a DecorationPane is
 * <pre>{@code
 * // create nodes and add it to a pane
 * Pane pane = new Xxxx ();
 * pane.getChildren().addAll(?);
 * return new DecorationPane(pane);  // instead of return pane, you wrap it into a DecorationPane
 * }</pre>
 * With the code above, if you ever call {@link DecorationUtils} install methods to any of the children nodes in the
 * pane above, the decorator will be displayed as defined.
 */
@SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef"})
public class DecorationPane extends StackPane implements DecorationSupport {
    private Parent _content;
    private DecorationDelegate _decorationDelegate;

    public DecorationPane(Parent content) {
        _content = content;
        _decorationDelegate = new DecorationDelegate(this);

        getChildren().add(getContent());
        initializeChildren();
        initializeStyle();
        registerListeners();
    }

    protected void initializeStyle() {
        getStyleClass().setAll(STYLE_CLASS_DECORATION_SUPPORT);
    }

    protected void initializeChildren() {
    }

    protected void registerListeners() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void layoutChildren() {
        if (getContent().getParent() != this) {
            throw new IllegalStateException("The content is not added to the Decoration. If you override initializeChildren method, please make sure you call super.initializeChildren first.");
        }
        _decorationDelegate.prepareDecorations();
        super.layoutChildren();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                _decorationDelegate.layoutDecorations();
            }
        });
    }

    @Override
    protected double computeMinWidth(double height) {
        return getContent().minWidth(height) + getPadding().getLeft() + getPadding().getRight();
    }

    @Override
    protected double computeMinHeight(double width) {
        return getContent().minHeight(width) + getPadding().getTop() + getPadding().getBottom();
    }

    @Override
    protected double computeMaxWidth(double height) {
        return getContent().maxWidth(height) + getPadding().getLeft() + getPadding().getRight();
    }

    @Override
    protected double computeMaxHeight(double width) {
        return getContent().maxHeight(width) + getPadding().getTop() + getPadding().getBottom();
    }

    @Override
    protected double computePrefWidth(double height) {
        return getContent().prefWidth(height) + getPadding().getLeft() + getPadding().getRight();
    }

    @Override
    protected double computePrefHeight(double width) {
        return getContent().prefHeight(width) + getPadding().getTop() + getPadding().getBottom();
    }

    /**
     * Gets the content of the {@code DecorationPane}.
     *
     * @return the content of the {@code DecorationPane}.
     */
    public Parent getContent() {
        return _content;
    }

    @Override
    public void positionInArea(Node child, double areaX, double areaY, double areaWidth, double areaHeight, double areaBaselineOffset, HPos hAlignment, VPos vAlignment) {
        super.positionInArea(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, hAlignment, vAlignment);
    }
}
