/*
 * @(#)JideTooltipSkin.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package com.jidefx.scene.control.skin;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import jidefx.scene.control.popup.TooltipEx;


/**
 * Copied from {@code TooltipSkin} to support tooltip position. Nothing is chnaged except it takes a ToolTipEx
 * instance.
 */
public class TooltipExSkin implements Skin<TooltipEx> {
    private Label tipLabel;

    private TooltipEx tooltip;

    public TooltipExSkin(TooltipEx t) {
        this.tooltip = t;
        tipLabel = new Label();
        tipLabel.contentDisplayProperty().bind(t.contentDisplayProperty());
        tipLabel.fontProperty().bind(t.fontProperty());
        tipLabel.graphicProperty().bind(t.graphicProperty());
        tipLabel.textAlignmentProperty().bind(t.textAlignmentProperty());
        tipLabel.textOverrunProperty().bind(t.textOverrunProperty());
        tipLabel.textProperty().bind(t.textProperty());
        tipLabel.wrapTextProperty().bind(t.wrapTextProperty());
        tipLabel.minWidthProperty().bind(t.minWidthProperty());
        tipLabel.prefWidthProperty().bind(t.prefWidthProperty());
        tipLabel.maxWidthProperty().bind(t.maxWidthProperty());
        tipLabel.minHeightProperty().bind(t.minHeightProperty());
        tipLabel.prefHeightProperty().bind(t.prefHeightProperty());
        tipLabel.maxHeightProperty().bind(t.maxHeightProperty());

        // RT-7512 - skin needs to have styleClass of the control
        // TODO - This needs to be bound together, not just set! Probably should
        // do the same for id and style as well.
        tipLabel.getStyleClass().setAll(t.getStyleClass());
        tipLabel.setStyle(t.getStyle());
        tipLabel.setId(t.getId());
    }

    @Override
    public TooltipEx getSkinnable() {
        return tooltip;
    }

    @Override
    public Node getNode() {
        return tipLabel;
    }

    @Override
    public void dispose() {
        tooltip = null;
        tipLabel = null;
    }
}
