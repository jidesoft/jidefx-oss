/*
 * @(#)DecorationDemo.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.examples.decoration;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import jidefx.animation.AnimationType;
import jidefx.animation.AnimationUtils;
import jidefx.examples.demo.AbstractFxDemo;
import jidefx.examples.demo.DemoData;
import jidefx.scene.control.decoration.DecorationPane;
import jidefx.scene.control.decoration.DecorationUtils;
import jidefx.scene.control.decoration.Decorator;
import jidefx.scene.control.decoration.MutableDecorator;
import jidefx.scene.control.field.NumberField;
import jidefx.utils.FXUtils;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

import java.util.function.Supplier;

@SuppressWarnings({"FieldCanBeLocal", "Convert2Lambda"})
public class DecorationDemo extends AbstractFxDemo {

    private ComboBox<Pos> _optionPos;
    private ObjectProperty<Insets> _optionPadding;
    private ObjectProperty<Point2D> _optionOffset;
    private CheckBox _optionPercent;

    private NumberField _offsetX;
    private NumberField _offsetY;
//    private NumberField _marginRight;
//    private NumberField _marginLeft;
//    private NumberField _marginTop;
//    private NumberField _marginBottom;
    private NumberField _paddingTop;
    private NumberField _paddingLeft;
    private NumberField _paddingRight;
    private NumberField _paddingBottom;

    private TextField _demoNode;
    private Label _demoDecoration;
    private CheckBox _showPaddingCheckBox;
    private MigPane _paddingPane;
    private Label _paddingContent;
    private String _paneStyle = null;
    private ComboBox<AnimationType> _animationTypes;
    private CheckBox _paddingComputeCheckBox;
    private Insets _rawPadding;

    @Override
    public String getName() {
        return "Decoration Demo";
    }

    @Override
    public String getDescription() {
        return "This is a demo of Decorator components. Decorator can display additional nodes in an existing node " +
                "\n" +
                "Demoed classes:\n" +
                "jidefx.scene.control.decoration.Decorator\n" +
                "jidefx.scene.control.decoration.DecoratorUtils\n" +
                "jidefx.scene.control.decoration.DecorationPane\n";
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public Region getDemoPanel() {
        initializeChildren();

        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add(TabPane.STYLE_CLASS_FLOATING);

        Tab options = new Tab("Decoration Options");
        options.setClosable(false);
        options.setContent(createDecorationOptions());

        Tab form = new Tab("Form Decoration");
        form.setClosable(false);
        form.setContent(createSignUpForm());

        tabPane.getTabs().addAll(options, form);

        return tabPane;
    }

    private Pane createDecorationOptions() {
        VBox fieldPane = new VBox();

        _showPaddingCheckBox = new CheckBox("Show padding");
        _showPaddingCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                showPadding();
            }
        });

        fieldPane.setPrefSize(150, 300);
        fieldPane.setAlignment(Pos.CENTER);
        fieldPane.setPadding(new Insets(30));
        _demoNode = new TextField("12345678901234567890");
        _demoNode.selectAll();
        _demoNode.setId("optionField");
        _demoNode.setPrefWidth(120);
        Label separator = new Label("");
        separator.setPrefHeight(30);
        fieldPane.getChildren().addAll(_showPaddingCheckBox, separator, _demoNode);

        _demoDecoration = new Label("", new ImageView(new Image("/jidefx/scene/control/decoration/overlay_info.png")));
        MutableDecorator<Label> optionDecorator = new MutableDecorator<>(_demoDecoration, _optionPos.getSelectionModel().getSelectedItem(), _optionOffset.get());
        DecorationUtils.install(_demoNode, optionDecorator);

        ///////////////////////////////////////
        // Option Pane
        MigPane optionPane = new MigPane(new LC().width("360px").height("300px").insets("20 10 10 10"), new AC().index(0).align("right").gap("10px").index(1).grow(), new AC().gap("8px"));

        Label requiredPosLabel = new Label("Pos: ");
        optionPane.add(requiredPosLabel);
        optionPane.add(_optionPos, new CC().span(3).wrap());
        optionDecorator.posProperty().bind(_optionPos.getSelectionModel().selectedItemProperty());

        // percent
        Label percentLabel = new Label("Percent: ");
        optionPane.add(percentLabel);
        optionPane.add(_optionPercent, new CC().span(3).wrap());
        optionDecorator.valueInPercentProperty().bind(_optionPercent.selectedProperty());

        _optionPercent.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    _offsetX.setNumberType(NumberField.NumberType.Percent);
                    _offsetY.setNumberType(NumberField.NumberType.Percent);
                    _paddingTop.setNumberType(NumberField.NumberType.Percent);
                    _paddingRight.setNumberType(NumberField.NumberType.Percent);
                    _paddingBottom.setNumberType(NumberField.NumberType.Percent);
                    _paddingLeft.setNumberType(NumberField.NumberType.Percent);

                    _offsetX.setValue(_offsetX.getValue().doubleValue() / _demoDecoration.getWidth());
                    _offsetY.setValue(_offsetY.getValue().doubleValue() / _demoDecoration.getHeight());
                    _paddingTop.setValue(_paddingTop.getValue().doubleValue() / _demoDecoration.getHeight());
                    _paddingRight.setValue(_paddingRight.getValue().doubleValue() / _demoDecoration.getWidth());
                    _paddingBottom.setValue(_paddingBottom.getValue().doubleValue() / _demoDecoration.getHeight());
                    _paddingLeft.setValue(_paddingLeft.getValue().doubleValue() / _demoDecoration.getWidth());
                }
                else {
                    _offsetX.setNumberType(NumberField.NumberType.Integer);
                    _offsetY.setNumberType(NumberField.NumberType.Integer);
                    _paddingTop.setNumberType(NumberField.NumberType.Integer);
                    _paddingRight.setNumberType(NumberField.NumberType.Integer);
                    _paddingBottom.setNumberType(NumberField.NumberType.Integer);
                    _paddingLeft.setNumberType(NumberField.NumberType.Integer);

                    _offsetX.setValue(_offsetX.getValue().doubleValue() * _demoDecoration.getWidth());
                    _offsetY.setValue(_offsetY.getValue().doubleValue() * _demoDecoration.getHeight());
                    _paddingTop.setValue(_paddingTop.getValue().doubleValue() * _demoDecoration.getHeight());
                    _paddingRight.setValue(_paddingRight.getValue().doubleValue() * _demoDecoration.getWidth());
                    _paddingBottom.setValue(_paddingBottom.getValue().doubleValue() * _demoDecoration.getHeight());
                    _paddingLeft.setValue(_paddingLeft.getValue().doubleValue() * _demoDecoration.getWidth());
                }
            }
        });

        Label percentHelp = new Label("", new ImageView(new Image("/jidefx/scene/control/decoration/overlay_question.png")));
        Tooltip percentTooltip = new Tooltip(
                "If this is checked, the padding and offset value will be treated \n" +
                "as a percentage of the width or height of the decoration node. \n" +
                "This is preferred when you don't know the size of the decoration node.");
        percentHelp.setTooltip(percentTooltip);
        Decorator<Label> percentDecorator = new Decorator<>(percentHelp, Pos.TOP_RIGHT);
        DecorationUtils.install(percentLabel, percentDecorator);

        int width = 80;
        String sWidth = width + "px";
        // Offset
        Label offsetLabel = new Label("Offset: ");
        optionPane.add(offsetLabel);
        _offsetX = new NumberField(NumberField.NumberType.Integer);
        _offsetX.setValue(0);
        _offsetX.setPrefWidth(width);
        optionPane.add(_offsetX, new CC().width(sWidth));
        DecorationUtils.install(_offsetX, new Decorator<>(new Label("X:"), Pos.CENTER_LEFT, new Point2D(90, 0), new Insets(0, 0, 0, 100)));
        _offsetY = new NumberField(NumberField.NumberType.Integer);
        _offsetY.setValue(0);
        _offsetY.setPrefWidth(width);
        optionPane.add(_offsetY, new CC().width(sWidth).wrap());
        DecorationUtils.install(_offsetY, new Decorator<>(new Label("Y:"), Pos.CENTER_LEFT, new Point2D(90, 0), new Insets(0, 0, 0, 100)));

        ChangeListener<Number> offsetParameterChangeListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (_optionPercent.isSelected()) {
                    _optionOffset.set(new Point2D(_offsetX.getValue().doubleValue() * 100, _offsetY.getValue().doubleValue() * 100));
                }
                else {
                    _optionOffset.set(new Point2D(_offsetX.getValue().doubleValue(), _offsetY.getValue().doubleValue()));
                }
            }
        };
        _offsetX.valueProperty().addListener(offsetParameterChangeListener);
        _offsetY.valueProperty().addListener(offsetParameterChangeListener);
        optionDecorator.offsetProperty().bind(_optionOffset);

//        // Margin
//        Label marginLabel = new Label("Margin: ");
//        optionPane.add(marginLabel);
//        optionPane.add(new Label());
//        _marginTop = new NumberField(NumberField.NumberType.Integer);
//        _marginTop.setValue(0);
//        _marginTop.setPrefWidth(width);
//        optionPane.add(_marginTop, new CC().width(sWidth).wrap());
//
//        optionPane.add(new Label());
//        _marginLeft = new NumberField(NumberField.NumberType.Integer);
//        _marginLeft.setValue(0);
//        _marginLeft.setPrefWidth(width);
//        optionPane.add(_marginLeft, new CC().width(sWidth));
//        Label marginBox = new Label("NODE");
//        marginBox.setPrefWidth(width);
//        marginBox.setAlignment(Pos.CENTER);
//        marginBox.setStyle(
//                "    -fx-padding: 4,0,4,0;\n" +
//                        "    -fx-border-color: -fx-focus-color;\n" +
//                        "    -fx-border-style: solid;\n" +
//                        "    -fx-border-width: 1px;\n");
//        optionPane.add(marginBox, new CC().width("" + width + "px"));
//        _marginRight = new NumberField(NumberField.NumberType.Integer);
//        _marginRight.setValue(0);
//        _marginRight.setPrefWidth(width);
//        optionPane.add(_marginRight, new CC().width(sWidth).wrap());
//
//        optionPane.add(new Label());
//        optionPane.add(new Label());
//        _marginBottom = new NumberField(NumberField.NumberType.Integer);
//        _marginBottom.setValue(0);
//        _marginBottom.setPrefWidth(width);
//        optionPane.add(_marginBottom, new CC().width(sWidth).wrap());

        // Padding
        Label paddingLabel = new Label("Padding: ");
        _paddingComputeCheckBox = new CheckBox("Compute ");
        VBox paddingBox = new VBox(paddingLabel, _paddingComputeCheckBox);
        paddingBox.setAlignment(Pos.CENTER_RIGHT);

        Label paddingModeHelp = new Label("", new ImageView(new Image("/jidefx/scene/control/decoration/overlay_question.png")));
        Tooltip paddingModeTooltip = new Tooltip(
                "If this is checked, the padding value will be computed based on position and offset.\n");
        paddingModeHelp.setTooltip(paddingModeTooltip);
        Decorator<Label> paddingModeDecorator = new Decorator<>(paddingModeHelp, Pos.TOP_LEFT);
        DecorationUtils.install(_paddingComputeCheckBox, paddingModeDecorator);

        optionPane.add(paddingBox);

        _paddingPane = new MigPane(new LC().width("250px").height("100px").insets("5 5 5 5"), new AC().gap("8px"), new AC().gap("5px"));
        _paddingPane.setStyle(
                "    -fx-border-color: -fx-focus-color;\n" +
                        "    -fx-border-style: solid;\n" +
                        "    -fx-border-width: 1px;\n");

        _paddingPane.add(new Label(" "), new CC().width(sWidth));
        _paddingTop = new NumberField(NumberField.NumberType.Integer);
        _paddingTop.setValue(0);
        _paddingTop.setPrefWidth(width);
        _paddingPane.add(_paddingTop, new CC().width(sWidth).wrap());

        _paddingLeft = new NumberField(NumberField.NumberType.Integer);
        _paddingLeft.setValue(0);
        _paddingLeft.setPrefWidth(width);
        _paddingPane.add(_paddingLeft, new CC().width(sWidth));
        _paddingContent = new Label("CONTENT");
        _paddingContent.setPrefWidth(width);
        _paddingContent.setAlignment(Pos.CENTER);
        _paddingContent.setStyle(
                "    -fx-background-color: white;\n" +
                        "    -fx-padding: 4,0,4,0;\n" +
                        "    -fx-border-color: -fx-focus-color;\n" +
                        "    -fx-border-style: segments(0.166667em, 0.166667em);\n" +
                        "    -fx-border-width: 1px;\n");
        _paddingPane.add(_paddingContent, new CC().width(sWidth));
        _paddingRight = new NumberField(NumberField.NumberType.Integer);
        _paddingRight.setValue(0);
        _paddingRight.setPrefWidth(width);
        _paddingPane.add(_paddingRight, new CC().width(sWidth).wrap());

        _paddingPane.add(new Label());
        _paddingBottom = new NumberField(NumberField.NumberType.Integer);
        _paddingBottom.setValue(0);
        _paddingBottom.setPrefWidth(width);
        _paddingPane.add(_paddingBottom, new CC().width(sWidth).wrap());

        optionPane.add(_paddingPane, new CC().span(3).wrap());
        Label waterMark = new Label("NODE");
        waterMark.setStyle(
                "-fx-opacity: 0.05;\n" +
                "-fx-font-size: 70;\n"
        );
        waterMark.setMouseTransparent(true);
        DecorationUtils.install(_paddingPane, new Decorator<>(waterMark, Pos.CENTER));


        // animation
        Label animationLabel = new Label("Animation:");
        optionPane.add(animationLabel);

        _animationTypes.getItems().addAll(AnimationType.values());
        _animationTypes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AnimationType>() {
            @Override
            public void changed(ObservableValue<? extends AnimationType> observable, AnimationType oldValue, AnimationType newValue) {
                optionDecorator.setTransition(AnimationUtils.createTransition(optionDecorator.getNode(), newValue));
            }
        });
        optionPane.add(_animationTypes, new CC().span(3).wrap());

        TitledPane optionTitledPane = new TitledPane("Options", new DecorationPane(optionPane));
        optionTitledPane.setCollapsible(true);
        TitledPane previewTitledPane = new TitledPane("Preview", new DecorationPane(fieldPane));
        previewTitledPane.setCollapsible(true);
        HBox hBox = new HBox(20, optionTitledPane, previewTitledPane);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(10));
        hBox.setStyle("-fx-background-color: white;");

        InvalidationListener paddingPropertyInvalidationListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                showPadding();
            }
        };
        ChangeListener<Number> paddingParameterChangeListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                buildOptionPadding();
                showPadding();
            }
        };
        _paddingComputeCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    optionDecorator.paddingProperty().unbind();

                    _paddingTop.valueProperty().removeListener(paddingParameterChangeListener);
                    _paddingRight.valueProperty().removeListener(paddingParameterChangeListener);
                    _paddingBottom.valueProperty().removeListener(paddingParameterChangeListener);
                    _paddingLeft.valueProperty().removeListener(paddingParameterChangeListener);

                    _demoNode.paddingProperty().addListener(paddingPropertyInvalidationListener);

                    optionDecorator.setPadding(null);

                    _paddingTop.setDisable(true);
                    _paddingRight.setDisable(true);
                    _paddingBottom.setDisable(true);
                    _paddingLeft.setDisable(true);
                }
                else {
                    _paddingTop.setDisable(false);
                    _paddingRight.setDisable(false);
                    _paddingBottom.setDisable(false);
                    _paddingLeft.setDisable(false);

                    _paddingTop.valueProperty().addListener(paddingParameterChangeListener);
                    _paddingRight.valueProperty().addListener(paddingParameterChangeListener);
                    _paddingBottom.valueProperty().addListener(paddingParameterChangeListener);
                    _paddingLeft.valueProperty().addListener(paddingParameterChangeListener);

                    _demoNode.paddingProperty().removeListener(paddingPropertyInvalidationListener);

                    optionDecorator.paddingProperty().bind(_optionPadding);
                }
            }
        });
        _paddingComputeCheckBox.setSelected(true);
        showPadding();

        return new DecorationPane(hBox);
    }

    private void buildOptionPadding() {
        if (!_paddingComputeCheckBox.isSelected()) {
            if (_optionPercent.isSelected()) {
                _optionPadding.set(new Insets(
                        _paddingTop.getValue().doubleValue() * 100,
                        _paddingRight.getValue().doubleValue() * 100,
                        _paddingBottom.getValue().doubleValue() * 100,
                        _paddingLeft.getValue().doubleValue() * 100));
            }
            else {
                _optionPadding.set(new Insets(
                        _paddingTop.getValue().doubleValue(),
                        _paddingRight.getValue().doubleValue(),
                        _paddingBottom.getValue().doubleValue(),
                        _paddingLeft.getValue().doubleValue()));
            }
        }
    }

    private void showPadding() {
        String s;
        if (_rawPadding == null || _rawPadding.equals(Insets.EMPTY)) {
            _rawPadding = _demoNode.getPadding();
        }

        if (_paddingComputeCheckBox.isSelected()) {
            _optionPadding.set(new Insets(
                    _demoNode.getPadding().getTop() - _rawPadding.getTop(),
                    _demoNode.getPadding().getRight() - _rawPadding.getRight(),
                    _demoNode.getPadding().getBottom() - _rawPadding.getBottom(),
                    _demoNode.getPadding().getLeft() - _rawPadding.getLeft()));

            if (_optionPercent.isSelected()) {
                _paddingTop.setValue(_optionPadding.get().getTop() / _demoDecoration.getHeight() * 100);
                _paddingRight.setValue(_optionPadding.get().getRight() / _demoDecoration.getWidth() * 100);
                _paddingBottom.setValue(_optionPadding.get().getBottom() / _demoDecoration.getHeight() * 100);
                _paddingLeft.setValue(_optionPadding.get().getLeft() / _demoDecoration.getWidth() * 100);
            }
            else {
                _paddingTop.setValue(_optionPadding.get().getTop());
                _paddingRight.setValue(_optionPadding.get().getRight());
                _paddingBottom.setValue(_optionPadding.get().getBottom());
                _paddingLeft.setValue(_optionPadding.get().getLeft());
            }
        }

        if (_showPaddingCheckBox.isSelected()) {
            Insets padding = _demoNode.getPadding();
            if (!padding.equals(Insets.EMPTY)) {
                s = " " + padding.getTop()
                        + " " + padding.getRight()
                        + " " + padding.getBottom()
                        + " " + padding.getLeft() + ";\n";
                String demoFieldStyle = "-fx-padding: " + s;
                demoFieldStyle +=
                        "-fx-background-radius: 2, 2;\n" +
                                "-fx-background-color: gray, white;\n" +
                                "-fx-background-insets: 0," + s;
                _demoNode.setStyle(demoFieldStyle);
            }
        }
        else {
            String style = _demoNode.getStyle();
            _demoNode.setStyle(style.replaceAll("-fx-background.*;", ""));
        }

        if (_paneStyle == null) {
            _paneStyle = _paddingPane.getStyle();
        }
        if (_showPaddingCheckBox.isSelected()) {
            _paddingPane.setStyle(_paneStyle + "-fx-background-color:gray;");
        }
        else {
            _paddingPane.setStyle(_paneStyle);
        }
    }

    private void initializeChildren() {
        ObservableList<Pos> poses = FXCollections.observableArrayList();
        poses.addAll(Pos.values());

        _optionPos = new ComboBox<>();
        _optionPos.setItems(poses);
        _optionPos.getSelectionModel().select(Pos.TOP_RIGHT);

        _optionPadding = new SimpleObjectProperty<>(new Insets(0));
        _optionOffset = new SimpleObjectProperty<>(new Point2D(0, 0));

        _optionPercent = new CheckBox();
        _optionPercent.setSelected(false);

        _animationTypes = new ComboBox<>();
    }

    private Pane createSignUpForm() {
        Region pane = DemoData.createSignUpForm();

        CheckBox checkBox = new CheckBox("Add decorations");
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    installDecorations(pane);
                }
                else {
                    uninstallDecorations(pane);
                }
            }
        });
        checkBox.setSelected(true);
        ((MigPane) pane).add(checkBox, new CC().gapTop("20px").span(2).alignX("center").wrap());

        return new DecorationPane(pane);

    }

    private void uninstallDecorations(Region pane) {
        FXUtils.setRecursively(pane, new FXUtils.Handler<Node>() {
            @Override
            public boolean condition(Node c) {
                return DecorationUtils.hasDecorators(c);
            }

            @Override
            public void action(Node c) {
                DecorationUtils.uninstall(c);
            }
        });
    }

    private void installDecorations(Region pane) {
        Supplier<Decorator> requiredFactory = new Supplier<Decorator>() {
            @Override
            public Decorator get() {
                return new Decorator<>(new ImageView(new Image("/jidefx/scene/control/decoration/overlay_required.png")));
            }
        };

        String prefix = DemoData.PREFIX_SIGNUP_FORM;

        // add the required icon to email and confirm email fields
        Node emailLabel = pane.lookup("#" + prefix + "emailLabel");
        DecorationUtils.install(emailLabel, requiredFactory.get());
        Node confirmEmailLabel = pane.lookup("#" + prefix + "confirmEmailLabel");
        DecorationUtils.install(confirmEmailLabel, requiredFactory.get());

        // add the secure and help icon to password fields
        Supplier<Decorator> secureFactory = new Supplier<Decorator>() {
            @Override
            public Decorator get() {
                return new Decorator<>(new ImageView(new Image("/jidefx/scene/control/decoration/overlay_lock.png")), Pos.CENTER_RIGHT, new Point2D(-100, 0), new Insets(0, 120, 0, 0));
            }
        };

        Supplier<Decorator> helpFactory = new Supplier<Decorator>() {
            @Override
            public Decorator get() {
                Label label = new Label("", new ImageView(new Image("/jidefx/scene/control/decoration/overlay_question.png")));
                Tooltip tooltip = new Tooltip("At least 6 length and includes:\n1 digital, 1 upper character, 1 special character");
                tooltip.setAutoHide(true);
                label.setTooltip(tooltip);
                return new Decorator<>(label, Pos.CENTER_RIGHT, new Point2D(100, 0), new Insets(0));
            }
        };

        Node passwordField = pane.lookup("#" + prefix + "passwordField");
        DecorationUtils.install(passwordField, secureFactory.get());
        DecorationUtils.install(passwordField, helpFactory.get());

        Node confirmPasswordField = pane.lookup("#" + prefix + "confirmPasswordField");
        DecorationUtils.install(confirmPasswordField, secureFactory.get());
        DecorationUtils.install(confirmPasswordField, helpFactory.get());

        Button signUpButton = (Button) pane.lookup("#" + prefix + "signUpButton");
        signUpButton.setOnAction(new EventHandler<ActionEvent>() {

            private Button _committedButton;

            @Override
            public void handle(ActionEvent event) {
                final Pos pos = Pos.CENTER;

                ProgressIndicator progressIndicator = new ProgressIndicator();
                Tooltip tooltip = new Tooltip("Please wait a moment while submitting");
                progressIndicator.setTooltip(tooltip);
                final Decorator<ProgressIndicator> progressDecorator = new Decorator<>(progressIndicator, pos, new Point2D(0, -100));
                DecorationUtils.install(pane, progressDecorator);
                pane.requestLayout();

                Timeline timeline = new Timeline();
                timeline.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(3),
                                new EventHandler<ActionEvent>() {
                                    public void handle(ActionEvent t) {
                                        DecorationUtils.uninstall(pane, progressDecorator);

                                        _committedButton = new Button("Submitted Successfully!");
                                        _committedButton.setStyle(
                                                "-fx-background-color: linear-gradient(#ff5400, #be1d00);\n" +
                                                        "-fx-background-radius: 10;\n" +
                                                        "-fx-background-insets: 0;\n" +
                                                        "-fx-padding: 8;\n" +
                                                        "-fx-text-fill: white;-fx-font-size: 24px;");
                                        Transition transition = AnimationUtils.createTransition(_committedButton, AnimationType.BOUNCE_IN);
                                        transition.setOnFinished(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent event) {
                                                _committedButton.setText("Click Me!");
                                            }
                                        });
                                        Decorator<Button> committedDecorator =
                                                new Decorator<>(_committedButton, pos, new Point2D(0, -100), new Insets(0), true, transition);
                                        _committedButton.setOnAction(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent event) {
                                                DecorationUtils.uninstall(pane, committedDecorator);
                                                pane.setDisable(false);
                                                pane.requestLayout();
                                            }
                                        });
                                        DecorationUtils.install(pane, committedDecorator);
                                        pane.setDisable(true);
                                    }
                                }
                        )
                );
                timeline.play();
            }
        });

        // add a large help icon to the sign-up button
        DecorationUtils.install(signUpButton, new Decorator<>(new ImageView(new Image("/jidefx/examples/decoration/help.png")), Pos.CENTER_RIGHT, new Point2D(100, 0), new Insets(0)));
    }
}
