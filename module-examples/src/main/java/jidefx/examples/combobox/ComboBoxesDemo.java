/*
 * @(#)ComboBoxesDemo.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.examples.combobox;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import jidefx.examples.demo.AbstractFxDemo;
import jidefx.scene.control.combobox.*;
import jidefx.scene.control.decoration.DecorationPane;
import jidefx.scene.control.decoration.DecorationUtils;
import jidefx.scene.control.decoration.Decorator;
import jidefx.scene.control.field.*;
import jidefx.scene.control.popup.TooltipEx;
import jidefx.utils.FXUtils;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

import java.util.Calendar;

public class ComboBoxesDemo extends AbstractFxDemo {
    public ComboBoxesDemo() {
    }

    public String getName() {
        return "FormattedComboBox/PopupField Demo";
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static final String PREFIX_COMBO_BOX_FORM = "ComboBoxForm";

    public Region getDemoPanel() {
        Parent pane1 = createComboBoxFields();
        Parent pane2 = createPopupFields();
        HBox.setHgrow(pane1, Priority.ALWAYS);
        HBox.setHgrow(pane2, Priority.ALWAYS);
        HBox pane = new HBox(6, pane1, new Separator(Orientation.VERTICAL), pane2);
        return new DecorationPane(pane);
    }

    public static Parent createComboBoxFields() {

        MigPane pane = new MigPane(new LC().minWidth("450px").minHeight("600px").insets("20 10 10 10"), new AC().index(0).align("right").gap("20px").index(1).fill().grow().gap("6px").fill(), new AC().gap("6px"));

        Label title = new Label("ComboBoxes");
        title.setStyle("-fx-font-size:1.4em;");
        pane.add(title, new CC().span(2).alignX("left").wrap());

        final CheckBox editableCheckBox = new CheckBox("Editable");
        editableCheckBox.setSelected(false);
        editableCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                FXUtils.setRecursively(pane, new FXUtils.Handler() {
                    @Override
                    public boolean condition(Object c) {
                        return c instanceof ComboBoxBase;
                    }

                    @Override
                    public void action(Object c) {
                        ((ComboBoxBase) c).setEditable(newValue);
                    }
                });
            }
        });

        final CheckBox disableCheckBox = new CheckBox("Disable");
        disableCheckBox.setSelected(false);
        disableCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                FXUtils.setRecursively(pane, new FXUtils.Handler() {
                    @Override
                    public boolean condition(Object c) {
                        return c instanceof ComboBoxBase;
                    }

                    @Override
                    public void action(Object c) {
                        ((ComboBoxBase) c).setDisable(newValue);
                    }
                });
            }
        });

        HBox hBox = new HBox(10, editableCheckBox, disableCheckBox);

        pane.add(hBox, new CC().spanX(2).alignX("right").gapBottom("10px").wrap());

        Label dateLabel = new Label("java.util.Date:");
        dateLabel.setId(PREFIX_COMBO_BOX_FORM + "dateLabel");
        DateComboBox dateComboBox = new DateComboBox();
        dateComboBox.setId(PREFIX_COMBO_BOX_FORM + "dateField");
        pane.add(dateLabel);
        pane.add(dateComboBox, new CC().wrap());

        addDecoratorForFormattedComboBox(dateLabel, dateComboBox);

        Label calendarLabel = new Label("java.util.Calendar:");
        calendarLabel.setId(PREFIX_COMBO_BOX_FORM + "calendarLabel");
        CalendarComboBox calendarComboBox = new CalendarComboBox();
        calendarComboBox.setId(PREFIX_COMBO_BOX_FORM + "calendarField");
        pane.add(calendarLabel);
        pane.add(calendarComboBox, new CC().wrap());

        calendarComboBox.setOnShowing(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                System.out.println("showing");
            }
        });
        calendarComboBox.setOnHidden(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                System.out.println("hidden");
            }
        });

        addDecoratorForFormattedComboBox(calendarLabel, calendarComboBox);

        Label localDateLabel = new Label("java.time.LocalDate:");
        localDateLabel.setId(PREFIX_COMBO_BOX_FORM + "localDateLabel");
        LocalDateComboBox localDateComboBox = new LocalDateComboBox();
        localDateComboBox.setId(PREFIX_COMBO_BOX_FORM + "localDateField");
        pane.add(localDateLabel);
        pane.add(localDateComboBox, new CC().wrap());

        addDecoratorForFormattedComboBox(localDateLabel, localDateComboBox);

        Label localDateTimeLabel = new Label("java.time.LocalDateTime:");
        localDateTimeLabel.setId(PREFIX_COMBO_BOX_FORM + "localDateTimeLabel");
        LocalDateTimeComboBox localDateTimeComboBox = new LocalDateTimeComboBox();
        localDateTimeComboBox.setId(PREFIX_COMBO_BOX_FORM + "localDateTimeField");
        pane.add(localDateTimeLabel);
        pane.add(localDateTimeComboBox, new CC().wrap());

        addDecoratorForFormattedComboBox(localDateTimeLabel, localDateTimeComboBox);

        Label colorLabel = new Label("javafx.scene.paint.Color:");
        colorLabel.setId(PREFIX_COMBO_BOX_FORM + "colorLabel");
        ColorComboBox colorComboBox = new ColorComboBox();
        colorComboBox.setId(PREFIX_COMBO_BOX_FORM + "colorField");
        pane.add(colorLabel);
        pane.add(colorComboBox, new CC().wrap());

        addDecoratorForFormattedComboBox(colorLabel, colorComboBox);

        Label integerLabel = new Label("Integer:");
        integerLabel.setId(PREFIX_COMBO_BOX_FORM + "integerLabel");
        IntegerComboBox integerComboBox = new IntegerComboBox(-50, 50, 0);
        integerComboBox.setId(PREFIX_COMBO_BOX_FORM + "integerField");
        pane.add(integerLabel);
        pane.add(integerComboBox, new CC().wrap());

        addDecoratorForFormattedComboBox(integerLabel, integerComboBox);

        Label fontLabel = new Label("javafx.scene.text.Font:");
        fontLabel.setId(PREFIX_COMBO_BOX_FORM + "fontLabel");
        FontComboBox fontComboBox = new FontComboBox();
        fontComboBox.setId(PREFIX_COMBO_BOX_FORM + "fontField");
        pane.add(fontLabel);
        pane.add(fontComboBox, new CC().wrap());

        addDecoratorForFormattedComboBox(fontLabel, fontComboBox);

        Label point2DLabel = new Label("javafx.geometry.Point2D:");
        point2DLabel.setId(PREFIX_COMBO_BOX_FORM + "point2DLabel");
        Point2DComboBox point2DComboBox = new Point2DComboBox();
        point2DComboBox.setId(PREFIX_COMBO_BOX_FORM + "point2DField");
        point2DComboBox.setValue(new Point2D(10, 20));
        pane.add(point2DLabel);
        pane.add(point2DComboBox, new CC().wrap());

        addDecoratorForFormattedComboBox(point2DLabel, point2DComboBox);

        Label point3DLabel = new Label("javafx.geometry.Point3D:");
        point3DLabel.setId(PREFIX_COMBO_BOX_FORM + "point3DLabel");
        Point3DComboBox point3DComboBox = new Point3DComboBox();
        point3DComboBox.setId(PREFIX_COMBO_BOX_FORM + "point3DField");
        point3DComboBox.setValue(new Point3D(10, 20, 30));
        pane.add(point3DLabel);
        pane.add(point3DComboBox, new CC().wrap());

        addDecoratorForFormattedComboBox(point3DLabel, point3DComboBox);

        Label insetsLabel = new Label("javafx.geometry.Insets:");
        insetsLabel.setId(PREFIX_COMBO_BOX_FORM + "insetsLabel");
        InsetsComboBox insetsComboBox = new InsetsComboBox();
        insetsComboBox.setId(PREFIX_COMBO_BOX_FORM + "insetsField");
        pane.add(insetsLabel);
        pane.add(insetsComboBox, new CC().wrap());

        addDecoratorForFormattedComboBox(insetsLabel, insetsComboBox);

        Label dimension2DLabel = new Label("javafx.geometry.Dimension2D:");
        dimension2DLabel.setId(PREFIX_COMBO_BOX_FORM + "dimension2DLabel");
        Dimension2DComboBox dimension2DComboBox = new Dimension2DComboBox();
        dimension2DComboBox.setId(PREFIX_COMBO_BOX_FORM + "dimension2DField");
        dimension2DComboBox.setValue(new Dimension2D(10, 20));
        pane.add(dimension2DLabel);
        pane.add(dimension2DComboBox, new CC().wrap());

        addDecoratorForFormattedComboBox(dimension2DLabel, dimension2DComboBox);

        Label rectangle2DLabel = new Label("javafx.geometry.Rectangle2D:");
        rectangle2DLabel.setId(PREFIX_COMBO_BOX_FORM + "rectangle2DLabel");
        Rectangle2DComboBox rectangle2DComboBox = new Rectangle2DComboBox();
        rectangle2DComboBox.setId(PREFIX_COMBO_BOX_FORM + "rectangle2DField");
        rectangle2DComboBox.setValue(new Rectangle2D(10, 20, 30, 40));
        pane.add(rectangle2DLabel);
        pane.add(rectangle2DComboBox, new CC().wrap());

        addDecoratorForFormattedComboBox(rectangle2DLabel, rectangle2DComboBox);

        Label boundingBoxLabel = new Label("javafx.geometry.BoundingBox:");
        boundingBoxLabel.setId(PREFIX_COMBO_BOX_FORM + "boundingBoxLabel");
        BoundingBoxComboBox boundingBoxComboBox = new BoundingBoxComboBox();
        boundingBoxComboBox.setId(PREFIX_COMBO_BOX_FORM + "boundingBoxField");
        boundingBoxComboBox.setValue(new BoundingBox(10, 20, 30, 40, 50, 60));
        pane.add(boundingBoxLabel);
        pane.add(boundingBoxComboBox, new CC().wrap());

        addDecoratorForFormattedComboBox(boundingBoxLabel, boundingBoxComboBox);

        return pane;
    }

    public static Parent createPopupFields() {
        MigPane pane = new MigPane(new LC().minWidth("450px").minHeight("450px").insets("20 10 10 10"), new AC().index(0).align("right").gap("20px").index(1).fill().grow().gap("6px").fill(), new AC().gap("6px"));

        Label title = new Label("PopupFields");
        title.setStyle("-fx-font-size:1.4em;");
        pane.add(title, new CC().span(2).alignX("left").wrap());

        final CheckBox showSpinnersCheckBox = new CheckBox("Show Spinners");
        showSpinnersCheckBox.setSelected(false);
        showSpinnersCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                FXUtils.setRecursively(pane, new FXUtils.Handler() {
                    @Override
                    public boolean condition(Object c) {
                        return c instanceof FormattedTextField;
                    }

                    @Override
                    public void action(Object c) {
                        if (c instanceof PopupField) {
                            ((FormattedTextField) c).setSpinnersVisible(newValue);
                            ((PopupField) c).setPopupButtonVisible(!newValue);
                        }
                        else {
                            ((FormattedTextField) c).setSpinnersVisible(newValue);
                        }
                    }
                });
            }
        });

        final CheckBox editableCheckBox = new CheckBox("Editable");
        editableCheckBox.setSelected(true);
        editableCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                FXUtils.setRecursively(pane, new FXUtils.Handler() {
                    @Override
                    public boolean condition(Object c) {
                        return c instanceof FormattedTextField;
                    }

                    @Override
                    public void action(Object c) {
                        ((FormattedTextField) c).setEditable(newValue);
                    }
                });
            }
        });

        final CheckBox disableCheckBox = new CheckBox("Disable");
        disableCheckBox.setSelected(false);
        disableCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                FXUtils.setRecursively(pane, new FXUtils.Handler() {
                    @Override
                    public boolean condition(Object c) {
                        return c instanceof FormattedTextField;
                    }

                    @Override
                    public void action(Object c) {
                        ((FormattedTextField) c).setDisable(newValue);
                    }
                });
            }
        });

        HBox hBox = new HBox(10, showSpinnersCheckBox, editableCheckBox, disableCheckBox);

        pane.add(hBox, new CC().spanX(2).alignX("right").gapBottom("10px").wrap());

        Label dateLabel = new Label("java.util.Date:");
        dateLabel.setId(PREFIX_COMBO_BOX_FORM + "dateLabel");
        PopupField dateField = DateField.createDateField();
        dateField.setId(PREFIX_COMBO_BOX_FORM + "dateField");
        pane.add(dateLabel);
        pane.add(dateField, new CC().wrap());

        addDecoratorForPopupField(dateLabel, dateField);

        Label calendarLabel = new Label("java.util.Calendar:");
        calendarLabel.setId(PREFIX_COMBO_BOX_FORM + "calendarLabel");
        PopupField calendarField = CalendarField.createCalendarField(Calendar.getInstance());
        calendarField.setId(PREFIX_COMBO_BOX_FORM + "calendarField");
        pane.add(calendarLabel);
        pane.add(calendarField, new CC().wrap());

        addDecoratorForPopupField(calendarLabel, calendarField);

        Label localDateLabel = new Label("java.time.LocalDate:");
        localDateLabel.setId(PREFIX_COMBO_BOX_FORM + "localDateLabel");
        PopupField localDateField = LocalDateField.createLocalDateField();
        localDateField.setId(PREFIX_COMBO_BOX_FORM + "localDateField");
        pane.add(localDateLabel);
        pane.add(localDateField, new CC().wrap());

        addDecoratorForPopupField(localDateLabel, localDateField);

        Label localDateTimeLabel = new Label("java.time.LocalDateTime:");
        localDateTimeLabel.setId(PREFIX_COMBO_BOX_FORM + "localDateTimeLabel");
        PopupField localDateTimeField = LocalDateTimeField.createLocalDateTimeField();
        localDateTimeField.setId(PREFIX_COMBO_BOX_FORM + "localTimeField");
        pane.add(localDateTimeLabel);
        pane.add(localDateTimeField, new CC().wrap());

        addDecoratorForPopupField(localDateTimeLabel, localDateTimeField);

        Label colorLabel = new Label("javafx.scene.paint.Color:");
        colorLabel.setId(PREFIX_COMBO_BOX_FORM + "colorLabel");
        ColorField colorField = new ColorField(ColorField.ColorFormat.RGB);
        colorField.setValue(Color.WHITE);
        colorField.setId(PREFIX_COMBO_BOX_FORM + "colorField");
        pane.add(colorLabel);
        pane.add(colorField, new CC().wrap());

        addDecoratorForPopupField(colorLabel, colorField);


        Label integerLabel = new Label("Integer:");
        integerLabel.setId(PREFIX_COMBO_BOX_FORM + "integerLabel");
        IntegerField integerField = new IntegerField(-50, 50, 0);
        integerField.setId(PREFIX_COMBO_BOX_FORM + "integerField");
        pane.add(integerLabel);
        pane.add(integerField, new CC().wrap());

        addDecoratorForPopupField(integerLabel, integerField);

        Label fontLabel = new Label("javafx.scene.text.Font:");
        fontLabel.setId(PREFIX_COMBO_BOX_FORM + "fontLabel");
        FontField fontField = new FontField();
        fontField.setValue(Font.getDefault());
        fontField.setId(PREFIX_COMBO_BOX_FORM + "fontField");
        pane.add(fontLabel);
        pane.add(fontField, new CC().wrap());

        addDecoratorForPopupField(fontLabel, fontField);

        Label point2DLabel = new Label("javafx.geometry.Point2D:");
        point2DLabel.setId(PREFIX_COMBO_BOX_FORM + "point2DLabel");
        Point2DField point2DField = new Point2DField();
        point2DField.setValue(new Point2D(10, 20));
        point2DField.setId(PREFIX_COMBO_BOX_FORM + "point2DField");
        pane.add(point2DLabel);
        pane.add(point2DField, new CC().wrap());

        addDecoratorForPopupField(point2DLabel, point2DField);

        Label point3DLabel = new Label("javafx.geometry.Point3D:");
        point3DLabel.setId(PREFIX_COMBO_BOX_FORM + "point3DLabel");
        Point3DField point3DField = new Point3DField();
        point3DField.setValue(new Point3D(10, 20, 30));
        point3DField.setId(PREFIX_COMBO_BOX_FORM + "point3DField");
        pane.add(point3DLabel);
        pane.add(point3DField, new CC().wrap());

        addDecoratorForPopupField(point3DLabel, point3DField);

        Label insetsLabel = new Label("javafx.geometry.Insets:");
        insetsLabel.setId(PREFIX_COMBO_BOX_FORM + "insetsLabel");
        InsetsField insetsField = new InsetsField();
        insetsField.setValue(new Insets(15, 25, 35, 45));
        insetsField.setId(PREFIX_COMBO_BOX_FORM + "insetsField");
        pane.add(insetsLabel);
        pane.add(insetsField, new CC().wrap());

        addDecoratorForPopupField(insetsLabel, insetsField);

        Label dimension2DLabel = new Label("javafx.geometry.Dimension2D:");
        dimension2DLabel.setId(PREFIX_COMBO_BOX_FORM + "dimension2DLabel");
        Dimension2DField dimension2DField = new Dimension2DField();
        dimension2DField.setValue(new Dimension2D(10, 20));
        dimension2DField.setId(PREFIX_COMBO_BOX_FORM + "dimension2DField");
        pane.add(dimension2DLabel);
        pane.add(dimension2DField, new CC().wrap());

        addDecoratorForPopupField(dimension2DLabel, dimension2DField);

        Label rectangle2DLabel = new Label("javafx.geometry.Rectangle2D:");
        rectangle2DLabel.setId(PREFIX_COMBO_BOX_FORM + "rectangle2DLabel");
        Rectangle2DField rectangle2DField = new Rectangle2DField();
        rectangle2DField.setValue(new Rectangle2D(10, 20, 30, 40));
        rectangle2DField.setId(PREFIX_COMBO_BOX_FORM + "rectangle2DField");
        pane.add(rectangle2DLabel);
        pane.add(rectangle2DField, new CC().wrap());

        addDecoratorForPopupField(rectangle2DLabel, rectangle2DField);

        Label boundingBoxLabel = new Label("javafx.geometry.BoundingBox:");
        boundingBoxLabel.setId(PREFIX_COMBO_BOX_FORM + "boundingBoxLabel");
        BoundingBoxField boundingBoxField = new BoundingBoxField();
        boundingBoxField.setValue(new BoundingBox(10, 20, 30, 40, 50, 60));
        boundingBoxField.setId(PREFIX_COMBO_BOX_FORM + "boundingBoxField");
        pane.add(boundingBoxLabel);
        pane.add(boundingBoxField, new CC().wrap());

        addDecoratorForPopupField(boundingBoxLabel, boundingBoxField);

        return pane;
    }

    private static void addDecoratorForFormattedComboBox(Label label, FormattedComboBox<?> field) {
        ImageView tip = new ImageView(new Image("/jidefx/examples/fields/formatted.png"));
        TooltipEx tooltip = new TooltipEx("Pattern:" + field.getEditor().getPattern());
        tooltip.setPos(Pos.BOTTOM_CENTER);
        TooltipEx.install(tip, tooltip);
        DecorationUtils.install(label, new Decorator<Node>(tip, Pos.CENTER_RIGHT, new Point2D(80, 0)));

        field.getEditor().installAdjustmentMouseHandler(label, 1);
    }

    private static void addDecoratorForPopupField(Label label, PopupField<?> field) {
        field.setPopupButtonVisible(true);

        ImageView tip = new ImageView(new Image("/jidefx/examples/fields/formatted.png"));
        TooltipEx tooltip = new TooltipEx("Pattern:" + field.getPattern());
        tooltip.setPos(Pos.BOTTOM_CENTER);
        TooltipEx.install(tip, tooltip);
        DecorationUtils.install(label, new Decorator<Node>(tip, Pos.CENTER_RIGHT, new Point2D(80, 0)));

        field.installAdjustmentMouseHandler(label, 1);
    }

    @Override
    public String getDemoFolder() {
        return "src/combobox";
    }
}