/*
 * @(#)NestedDecorationDemo.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.examples.decoration;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import jidefx.scene.control.decoration.DecorationPane;
import jidefx.scene.control.decoration.DecorationUtils;
import jidefx.scene.control.decoration.Decorator;

public class NestedDecorationDemo extends DecorationDemo {
    @Override
    public String getName() {
        return super.getName() + " with another decoration layer above";
    }

    @Override
    public String getDescription() {
        return "This is a demo of tow level decoration layers. ";
    }

    @Override
    public Region getDemoPanel() {
        Region pane = super.getDemoPanel();

        String decoratorStyle = "-fx-text-fill: red;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 0 0 10";
        Label label = new Label("This is displayed on the second layer.");
        label.setStyle(decoratorStyle);
        Decorator decorator = new Decorator(label, Pos.TOP_CENTER);
        DecorationUtils.install(pane, decorator);

        DecorationPane decorationPane = new DecorationPane(pane);

        return decorationPane;
    }
}
