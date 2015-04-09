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
package jidefx.scene.control.field;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import jidefx.scene.control.decoration.DecorationPane;
import jidefx.scene.control.decoration.DecorationUtils;
import jidefx.scene.control.decoration.Decorator;
import jidefx.utils.PredefinedShapes;

/**
 * {@code LabeledTextField} includes a TextField and two Buttons. The first button is the label button which can be used
 * as a place to click to show a context menu, or a hint to tell user what this TextField is for. The second button is a
 * clear button. When there is text in the TextField, clicking this button clears the text.
 */
@SuppressWarnings({"Convert2Lambda", "UnusedDeclaration"})
public class LabeledTextField extends DecorationPane {
    private static final String STYLE_CLASS_DEFAULT = "labeled-text-field"; //NON-NLS
    private static final String STYLE_CLASS_LABEL_BUTTON = "label-button"; //NON-NLS
    private static final String STYLE_CLASS_CLEAR_BUTTON = "clear-button"; //NON-NLS
    private static final String STYLE_CLASS_MESSAGE = "message"; //NON-NLS
    private TextField _textField;
    private Button _clearButton;
    private Button _labelButton;
    private Tooltip _tooltip;
    private Timeline _tooltipHideTimeline = null;

    private Node _clearIcon;
    private Node _magnifyIcon;

    public LabeledTextField() {
        super(new TextField());
    }

    @Override
    protected void initializeChildren() {
        super.initializeChildren();
        _textField = (TextField) getContent();

        _clearButton = new Button();
        _clearButton.getStyleClass().addAll(STYLE_CLASS_CLEAR_BUTTON, "no-background-button"); //NON-NLS
        _clearButton.setVisible(false);
        _clearButton.setGraphic(PredefinedShapes.getInstance().createClearIcon(12));
        _clearButton.setPrefSize(12, 12);
        _clearButton.setMinSize(12, 12);
        _clearButton.setMaxSize(12, 12);

        _labelButton = new Button();
        _labelButton.getStyleClass().addAll(STYLE_CLASS_LABEL_BUTTON, "no-background-button"); //NON-NLS
        _labelButton.setVisible(true);
        _labelButton.setPrefSize(12, 12);
        _labelButton.setMinSize(12, 12);
        _labelButton.setMaxSize(12, 12);

        DecorationUtils.install(_textField, new Decorator<>(_clearButton, Pos.CENTER_RIGHT, new Point2D(-100, 0)));
        DecorationUtils.install(_textField, new Decorator<>(_labelButton, Pos.CENTER_LEFT, new Point2D(80, 0)));

        _tooltip = new Tooltip();
        _tooltip.setAutoHide(true);
        _tooltip.setOpacity(0.8);
    }

    @Override
    protected void registerListeners() {
        super.registerListeners();
        final TextField textField = getTextField();
        final Button clearButton = getClearButton();
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                textField.setText("");
                textField.requestFocus();
            }
        });
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                clearButton.setVisible(!textField.getText().isEmpty());
            }
        });
    }

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().add(STYLE_CLASS_DEFAULT);
        getTooltip().getStyleClass().addAll(STYLE_CLASS_MESSAGE); //NON-NLS
    }


    public TextField getTextField() {
        return _textField;
    }

    public Button getClearButton() {
        return _clearButton;
    }

    public Button getLabelButton() {
        return _labelButton;
    }

    private Tooltip getTooltip() {
        return _tooltip;
    }

    public StringProperty promptTextProperty() {
        return getTextField().promptTextProperty();
    }

    public void setPromptText(String promptText) {
        getTextField().setPromptText(promptText);
    }

    public String getPromptText() {
        return getTextField().getPromptText();
    }

    public StringProperty textProperty() {
        return getTextField().textProperty();
    }

    public void setText(String text) {
        getTextField().setText(text);
    }

    public void clearText() {
        getTextField().clear();
    }

    public String getText() {
        return getTextField().getText();
    }

    public StringProperty labelProperty() {
        return getLabelButton().textProperty();
    }

    public void setLabel(String text) {
        getLabelButton().setText(text);
    }

    public String getLabel() {
        return getLabelButton().getText();
    }

    public ObjectProperty<Node> graphicsProperty() {
        return getLabelButton().graphicProperty();
    }

    public void setGraphics(Node graphics) {
        getLabelButton().setGraphic(graphics);
    }

    public Node getGraphics() {
        return getLabelButton().getGraphic();
    }

    public BooleanProperty editableProperty() {
        return getTextField().editableProperty();
    }

    public void setEditable(boolean editable) {
        getTextField().setEditable(editable);
    }

    public boolean isEditable() {
        return getTextField().isEditable();
    }

    protected void showMessage(String message) {
        getTooltip().setText(message);
        if (_tooltipHideTimeline != null) _tooltipHideTimeline.stop();
        if (message != null) {
            TextField textField = getTextField();
            Point2D toolTipPos = textField.localToScene(0, textField.getLayoutBounds().getHeight() + 2);
            double x = toolTipPos.getX() + textField.getScene().getX() + textField.getScene().getWindow().getX();
            double y = toolTipPos.getY() + textField.getScene().getY() + textField.getScene().getWindow().getY();
            getTooltip().show(textField, x, y);
            _tooltipHideTimeline = new Timeline();
            _tooltipHideTimeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(2),
                            new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent t) {
                                    _tooltip.hide();
                                    _tooltip.setText(null);
                                }
                            }
                    )
            );
            _tooltipHideTimeline.play();
        }
        else {
            clearMessage();
        }
    }

    public void clearMessage() {
        getTooltip().hide();
        getTooltip().setText("");
    }

    @Override
    protected double computePrefHeight(double width) {
        return getTextField().prefHeight(width);
    }

    @Override
    protected double computeMinHeight(double width) {
        return getTextField().prefHeight(width);
    }

    @Override
    protected double computeMaxHeight(double width) {
        return getTextField().maxHeight(width);
    }

    public Node getClearIcon() {
        return _clearIcon;
    }

    private void setClearIcon(Node clearIcon) {
        _clearIcon = clearIcon;
    }

    public Node getMagnifyIcon() {
        return _magnifyIcon;
    }

    private void setMagnifyIcon(Node magnifyIcon) {
        _magnifyIcon = magnifyIcon;
    }
}
