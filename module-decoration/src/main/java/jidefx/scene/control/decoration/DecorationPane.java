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
package jidefx.scene.control.decoration;

import javafx.application.Platform;
import javafx.beans.DefaultProperty;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import jidefx.utils.DelegatingObservableList;

import java.util.Collection;

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
@DefaultProperty("content")
public class DecorationPane extends StackPane implements DecorationSupport {
    private Parent _content;
    private DecorationDelegate _decorationDelegate;

    public DecorationPane() {
        _decorationDelegate = new DecorationDelegate(this);
    }

    public DecorationPane(Parent content) {
        _decorationDelegate = new DecorationDelegate(this);
        setContent(content);
    }

    protected void initializeStyle() {
        getStyleClass().setAll(STYLE_CLASS_DECORATION_SUPPORT);
    }

    protected void initializeChildren() {
    }

    protected void registerListeners() {
    }

    protected void unregisterListeners() {
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

    /**
     * Sets the content of the {@code DecorationPane}.
     * <p><strong>WARNING: a single element can be added as a child to {@code DecorationPane}!</strong></p>
     *
     * @param content the content of the {@code DecorationPane}.
     */
    public void setContent(Parent content) {
        if (_content == content) {
            // there's nothing else to be done here
            return;
        }

        unregisterListeners();
        getChildren().clear();

        _content = content;

        if (_content != null) {
            getChildrenInternal().add(getContent());
            initializeChildren();
            initializeStyle();
            registerListeners();
        }
    }

    private boolean setContentIfParent(Node node) {
        if (node instanceof Parent) {
            setContent((Parent) node);
            return true;
        }
        return false;
    }

    protected ObservableList<Node> getChildrenInternal() {
        return super.getChildren();
    }

    @Override
    public final ObservableList<Node> getChildren() {
        return new SingleElementObservableList(super.getChildren());
    }

    @Override
    public void positionInArea(Node child, double areaX, double areaY, double areaWidth, double areaHeight, double areaBaselineOffset, HPos hAlignment, VPos vAlignment) {
        super.positionInArea(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, hAlignment, vAlignment);
    }

    /**
     * Used to enforce a single element inside a given {@code ObservableList} that's linked
     * to this {@code DecorationPane}. The single element is the content of the {@code DecorationPane}.
     */
    private class SingleElementObservableList extends DelegatingObservableList<Node> {
        public SingleElementObservableList(ObservableList<Node> delegate) {
            super(delegate);
        }

        @Override
        public boolean addAll(Node... elements) {
            if (elements != null && elements.length > 0) {
                setContentIfParent(elements[elements.length - 1]);
            }
            return false;
        }

        @Override
        public boolean setAll(Node... elements) {
            return addAll(elements);
        }

        @Override
        public boolean setAll(Collection<? extends Node> col) {
            return addAll(col);
        }

        @Override
        public boolean add(Node node) {
            return setContentIfParent(node);
        }

        @Override
        public boolean addAll(Collection<? extends Node> c) {
            if (c != null && c.size() > 0) {
                Node[] array = c.toArray(new Node[c.size()]);
                add(array[array.length - 1]);
            }
            return false;
        }

        @Override
        public boolean addAll(int index, Collection<? extends Node> c) {
            return addAll(c);
        }

        @Override
        public void add(int index, Node element) {
            setContentIfParent(element);
        }

        @Override
        public Node set(int index, Node element) {
            Node node = getContent();
            super.set(index, element);
            return node;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            if (!c.contains(getContent())) {
                clear();
                return true;
            }
            return false;
        }

        @Override
        public boolean retainAll(Node... elements) {
            if (elements != null) {
                for (Node node : elements) {
                    if (node == getContent()) {
                        return false;
                    }
                }
            }

            clear();
            return true;
        }
    }
}
