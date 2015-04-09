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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import jidefx.scene.control.decoration.DecorationUtils;
import jidefx.scene.control.decoration.Decorator;
import jidefx.scene.control.field.popup.ColorPopupContent;
import jidefx.scene.control.field.popup.PopupContent;
import jidefx.scene.control.field.verifier.HexRangePatternVerifier;
import jidefx.scene.control.field.verifier.IntegerRangePatternVerifier;
import jidefx.utils.converter.javafx.HexColorConverter;
import jidefx.utils.converter.javafx.RgbColorConverter;

/**
 * A {@code PopupField} for {@link Color}.
 */
@SuppressWarnings("UnusedDeclaration")
public class ColorField extends PopupField<Color> {
    public enum ColorFormat {RGB, RGBA, HEX_RGB, HEX_RGBA}

    private ObjectProperty<ColorFormat> _colorFormatProperty;
    private Decorator<Node> _colorRectDecorator;
    private BooleanProperty _colorRectVisibleProperty;

    public ColorField() {
        this(ColorFormat.RGB);
    }

    public ColorField(ColorFormat colorFormat) {
        setColorFormat(colorFormat);
    }

    private static final String STYLE_CLASS_DEFAULT = "color-field"; //NON-NLS

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().addAll(STYLE_CLASS_DEFAULT);
    }

    public ObjectProperty<ColorFormat> colorFormatProperty() {
        if (_colorFormatProperty == null) {
            _colorFormatProperty = new SimpleObjectProperty<ColorFormat>(this, "colorFormat") { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    switch (get()) {
                        case RGB:
                            setStringConverter(new RgbColorConverter().toStringConverter());
                            getPatternVerifiers().put("r", new IntegerRangePatternVerifier(0, 255)); //NON-NLS
                            getPatternVerifiers().put("g", new IntegerRangePatternVerifier(0, 255)); //NON-NLS
                            getPatternVerifiers().put("b", new IntegerRangePatternVerifier(0, 255)); //NON-NLS
                            setPattern("r, g, b"); //NON-NLS
                            break;
                        case RGBA:
                            setStringConverter(new RgbColorConverter(true).toStringConverter());
                            getPatternVerifiers().put("r", new IntegerRangePatternVerifier(0, 255)); //NON-NLS
                            getPatternVerifiers().put("g", new IntegerRangePatternVerifier(0, 255)); //NON-NLS
                            getPatternVerifiers().put("b", new IntegerRangePatternVerifier(0, 255)); //NON-NLS
                            getPatternVerifiers().put("a", new IntegerRangePatternVerifier(0, 255));
                            setPattern("r, g, b, a"); //NON-NLS
                            break;
                        case HEX_RGB:
                            setStringConverter(new HexColorConverter().toStringConverter());
                            getPatternVerifiers().put("rr", new HexRangePatternVerifier(0, 255, true)); //NON-NLS
                            getPatternVerifiers().put("gg", new HexRangePatternVerifier(0, 255, true)); //NON-NLS
                            getPatternVerifiers().put("bb", new HexRangePatternVerifier(0, 255, true)); //NON-NLS
                            setPattern("#rrggbb"); //NON-NLS
                            break;
                        case HEX_RGBA:
                            setStringConverter(new HexColorConverter(true).toStringConverter());
                            getPatternVerifiers().put("aa", new HexRangePatternVerifier(0, 255, true)); //NON-NLS
                            getPatternVerifiers().put("rr", new HexRangePatternVerifier(0, 255, true)); //NON-NLS
                            getPatternVerifiers().put("gg", new HexRangePatternVerifier(0, 255, true)); //NON-NLS
                            getPatternVerifiers().put("bb", new HexRangePatternVerifier(0, 255, true)); //NON-NLS
                            setPattern("#aarrggbb"); //NON-NLS
                            break;
                    }
                }
            };
        }
        return _colorFormatProperty;
    }

    public ColorFormat getColorFormat() {
        return colorFormatProperty().get();
    }

    public void setColorFormat(ColorFormat format) {
        colorFormatProperty().set(format);
    }


    @Override
    protected void initializeTextField() {
        super.initializeTextField();
        showColorRect();
        setPopupContentFactory(new Callback<Color, PopupContent<Color>>() {
            @Override
            public PopupContent<Color> call(Color param) {
                ColorPopupContent content = new ColorPopupContent();
                content.setValue(getValue());
                return content;
            }
        });
    }

    public BooleanProperty colorRectVisibleProperty() {
        if (_colorRectVisibleProperty == null) {
            _colorRectVisibleProperty = new SimpleBooleanProperty(this, "colorRectVisible") { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    boolean visible = get();
                    if (visible) {
                        showColorRect();
                    }
                    else {
                        hideColorRect();
                    }
                }
            };
        }
        return _colorRectVisibleProperty;
    }

    /**
     * Checks if the popup button is visible. The popup button will show a popup to choose a value for the field.
     *
     * @return true or false.
     */
    public boolean isColorRectVisible() {
        return colorRectVisibleProperty().get();
    }

    /**
     * Shows or hides the popup button.
     *
     * @param colorRectVisible true or false.
     */
    public void setColorRectVisible(boolean colorRectVisible) {
        colorRectVisibleProperty().set(colorRectVisible);
    }

    private void showColorRect() {
        if (_colorRectDecorator == null) {
            Rectangle rectangle = new Rectangle(16, 12);
            rectangle.fillProperty().bind(valueProperty());
            rectangle.setStroke(Color.GRAY);
            rectangle.disableProperty().bind(disabledProperty());
            _colorRectDecorator = new Decorator<Node>(rectangle, Pos.CENTER_LEFT, new Point2D(90, 0), new Insets(0, 0, 0, 120));
        }
        DecorationUtils.install(this, _colorRectDecorator);
    }

    private void hideColorRect() {
        if (_colorRectDecorator != null) {
            DecorationUtils.uninstall(this, _colorRectDecorator);
        }
    }

}
