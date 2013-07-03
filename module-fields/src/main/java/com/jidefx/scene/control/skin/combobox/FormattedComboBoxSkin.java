/*
 * @(#)FormattedComboBoxSkin.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package com.jidefx.scene.control.skin.combobox;

import com.jidefx.scene.control.behavior.combobox.FormattedComboBoxBehavior;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import com.sun.javafx.scene.control.skin.Utils;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Skin;
import javafx.scene.control.Skinnable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import jidefx.scene.control.combobox.FormattedComboBox;
import jidefx.scene.control.field.FormattedTextField;
import jidefx.scene.control.field.popup.PopupContent;

public class FormattedComboBoxSkin<T> extends ComboBoxPopupControl<T> {

    /**
     * ************************************************************************ * Private fields * *
     * ************************************************************************
     */

    private final FormattedComboBox<T> comboBox;

    private FormattedTextField<T> textField;

    private PopupContent<T> _popupContent;

    /***************************************************************************
     *                                                                         *
     * Listeners                                                               *
     *                                                                         *
     **************************************************************************/


    /**
     * ************************************************************************ * Constructors * *
     * ************************************************************************
     */

    public FormattedComboBoxSkin(final FormattedComboBox<T> comboBox) {
        super(comboBox, new FormattedComboBoxBehavior<>(comboBox));
        this.comboBox = comboBox;

        // editable input node
        textField = getInputNode();

        // Fix for RT-29565. Without this the textField does not have a correct
        // pref width at startup, as it is not part of the scenegraph (and therefore
        // has no pref width until after the first measurements have been taken).
        if (textField != null) {
            getChildren().add(textField);
        }

        // move focus in to the textfield if the comboBox is editable
        comboBox.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean hasFocus) {
                if (comboBox.isEditable() && hasFocus) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            textField.requestFocus();
                        }
                    });
                }
            }
        });

        comboBox.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (textField == null) return;

                // When the user hits the enter or F4 keys, we respond before 
                // ever giving the event to the TextField.
                if (ke.getCode() == KeyCode.ENTER) {
                    setTextFromTextFieldIntoComboBoxValue();
                    /*
                    ** don't consume this if we're on an embedded
                    ** platform that supports 5-button navigation 
                    */
                    if (!Utils.isTwoLevelFocus()) {
                        ke.consume();
                    }
                }
                else if (ke.getCode() == KeyCode.F4 && KeyEvent.KEY_RELEASED.equals(ke.getEventType())) {
                    if (comboBox.isShowing()) comboBox.hide();
                    else comboBox.show();
                    ke.consume();
                }
                else if (ke.getCode() == KeyCode.F10 || ke.getCode() == KeyCode.ESCAPE) {
                    // RT-23275: The TextField fires F10 and ESCAPE key events
                    // up to the parent, which are then fired back at the 
                    // TextField, and this ends up in an infinite loop until
                    // the stack overflows. So, here we consume these two
                    // events and stop them from going any further.
                    ke.consume();
                }
            }
        });

        registerChangeListener(comboBox.promptTextProperty(), "PROMPT_TEXT"); //NON-NLS
        registerChangeListener(comboBox.popupContentFactoryProperty(), "POPUP_CONTENT_FACTORY"); //NON-NLS
        registerChangeListener(comboBox.valueProperty(), "VALUE"); //NON-NLS
        registerChangeListener(comboBox.editorProperty(), "EDITABLE"); //NON-NLS
    }


    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handleControlPropertyChanged(String p) {
        super.handleControlPropertyChanged(p);

        switch (p) {
            case "PROMPT_TEXT": //NON-NLS
                updateDisplayNode();
                break;
            case "POPUP_CONTENT_FACTORY": //NON-NLS
                updatePopupContentFactory();
                break;
            case "EDITABLE": //NON-NLS
                updateEditable();
                break;
            case "EDITOR": //NON-NLS
                getInputNode();
                break;
            case "VALUE": //NON-NLS
                updateValue();
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node getDisplayNode() {
        Node displayNode = getInputNode();
        updateDisplayNode();
        return displayNode;
    }

    @Override
    public Node getPopupContent() {
        if (_popupContent == null) {
            _popupContent = createPopupContent();
        }
        return (Node) _popupContent;
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return 50;
    }

    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return Integer.MAX_VALUE;
    }

    /**
     * ************************************************************************ Private methods
     * ************************************************************************
     */

    private void updateValue() {
        T newValue = comboBox.getValue();
        if (_popupContent != null) {
            //noinspection unchecked
            ((PopupContent<T>) getPopupContent()).setValue(newValue);
        }
    }

    private String initialTextFieldValue = null;

    private FormattedTextField<T> getInputNode() {
        if (textField != null) return textField;

        textField = comboBox.getEditor();
        textField.setFocusTraversable(true);
        textField.promptTextProperty().bindBidirectional(comboBox.promptTextProperty());

        // Fix for RT-21406: ComboBox do not show initial text value
        initialTextFieldValue = textField.getText();
        // End of fix (see updateDisplayNode below for the related code)

        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean hasFocus) {
                // RT-21454 starts here
                if (!hasFocus) {
                    setTextFromTextFieldIntoComboBoxValue();
                    pseudoClassStateChanged(CONTAINS_FOCUS_PSEUDOCLASS_STATE, false);
                }
                else {
                    pseudoClassStateChanged(CONTAINS_FOCUS_PSEUDOCLASS_STATE, true);
                }
            }
        });

        return textField;
    }

    private void updateDisplayNode() {
        T value = comboBox.getValue();
        if (initialTextFieldValue != null && !initialTextFieldValue.isEmpty()) {
            // Remainder of fix for RT-21406: ComboBox do not show initial text value
            textField.setText(initialTextFieldValue);
            initialTextFieldValue = null;
            // end of fix
        }
        else {
            comboBox.setValue(value);
        }
    }

    private void updateEditable() {
        textField.setEditable(comboBox.isEditable());
    }

    private void setTextFromTextFieldIntoComboBoxValue() {
        comboBox.setValue(textField.getValue());
    }

    private void updatePopupContentFactory() {
        _popupContent = createPopupContent();
    }

    private PopupContent<T> createPopupContent() {
        PopupContent<T> pane = comboBox.getPopupContentFactory().call(comboBox.getValue());
        pane.valueProperty().addListener(new ChangeListener<T>() {
            @Override
            public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
                comboBox.setValue(newValue);
            }
        });
        return pane;
    }

    // JideFX: Added so that we can customize the mouse handler

    @Override
    protected PopupControl getPopup() {
        if (popup == null) {
            createPopup();
        }
        return popup;
    }

    protected void createPopup() {
        popup = new PopupControl() {

            @Override
            public Styleable getStyleableParent() {
                return getSkinnable();
            }

            {
                setSkin(new Skin<Skinnable>() {
                    @Override
                    public Skinnable getSkinnable() {
                        return FormattedComboBoxSkin.this.getSkinnable();
                    }

                    @Override
                    public Node getNode() {
                        return getPopupContent();
                    }

                    @Override
                    public void dispose() {
                    }
                });
            }
        };
        popup.getStyleClass().add(COMBO_BOX_STYLE_CLASS);
        popup.setAutoHide(true);
        popup.setAutoFix(true);
        popup.setHideOnEscape(true);
        popup.setOnAutoHide(new EventHandler<Event>() {
            @Override
            public void handle(Event e) {
                getBehavior().onAutoHide();
            }
        });
        popup.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                // RT-18529: We listen to mouse input that is received by the popup
                // but that is not consumed, and assume that this is due to the mouse
                // clicking outside of the node, but in areas such as the
                // dropshadow.
                if (t.getTarget() == getPopupContent()) {   // JideFX: Added this if statement. Otherwise teh popup will be hidden when clicking on the labels.
                    getBehavior().onAutoHide();
                }
            }
        });

        // Fix for RT-21207
        InvalidationListener layoutPosListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable o) {
                reconfigurePopup();
            }
        };
        getSkinnable().layoutXProperty().addListener(layoutPosListener);
        getSkinnable().layoutYProperty().addListener(layoutPosListener);
        getSkinnable().widthProperty().addListener(layoutPosListener);
    }

    void reconfigurePopup() {
        if (!getPopup().isShowing()) return;

        Point2D p = getPrefPopupPosition();
        reconfigurePopup(p.getX(), p.getY(),
                getPopupContent().prefWidth(1), getPopupContent().prefHeight(1));
    }

    void reconfigurePopup(double x, double y, double minWidth, double minHeight) {
        if (!getPopup().isShowing()) return;

        if (x > -1) getPopup().setX(x);
        if (y > -1) getPopup().setY(y);
        if (minWidth > -1) getPopup().setMinWidth(minWidth);
        if (minHeight > -1) getPopup().setMinHeight(minHeight);
    }

    private Point2D getPrefPopupPosition() {
        double dx = 0;
        dx += (getSkinnable().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) ? -1 : 1;
        return com.sun.javafx.Utils.pointRelativeTo(getSkinnable(), getPopupContent(), HPos.RIGHT, VPos.BOTTOM, -getPopup().prefWidth(-1), 0, false);
    }

    // End of JideFX: Added so that we can customize the mouse handler


    /**
     * ************************************************************************ * Stylesheet Handling * *
     * ************************************************************************
     */

    private static PseudoClass CONTAINS_FOCUS_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("contains-focus"); //NON-NLS
}
