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
package jidefx.utils;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonBase;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

/**
 * {@code AutoRepeatButtonUtils} is a utility class which can make a button automatically trigger action events
 * continuously. To enable this feature on any button, just call AutoRepeatButtonUtils.install(button) or
 * AutoRepeatButtonUtils.install(button, initialDelay, interval).
 */
public class AutoRepeatButtonUtils {
    public final static String AUTO_REPEAT = "AutoRepeat"; //NON-NLS
    public final static String CLIENT_PROPERTY_AUTO_REPEAT = "AutoRepeat.AutoRepeatButtonUtils"; //NON-NLS
    public final static Duration DEFAULT_DELAY = Duration.millis(100);
    public final static Duration DEFAULT_INITIAL_DELAY = Duration.millis(500);

    private Timeline _timer = null;
    private ButtonBase _button;
    private EventHandler<MouseEvent> _mouseEventEventHandler;

    /**
     * Enable auto-repeat feature on the button. By default, it will delay for 500 ms and then repeat every 50 ms.
     *
     * @param button the button.
     */
    public static void install(ButtonBase button) {
        uninstall(button);
        new AutoRepeatButtonUtils().installListeners(button, DEFAULT_INITIAL_DELAY, DEFAULT_DELAY);
    }

    /**
     * Enable auto-repeat feature on the button.
     *
     * @param button       the button.
     * @param initialDelay the initial delay. It is from the time mouse is pressed to the first action event.
     * @param interval     the interval between action events.
     */
    public static void install(ButtonBase button, Duration initialDelay, Duration interval) {
        uninstall(button);
        new AutoRepeatButtonUtils().installListeners(button, initialDelay, interval);
    }

    /**
     * Disabled the auto-repeat feature on the button which called install before.
     *
     * @param button the button that has auto-repeat feature.
     */
    public static void uninstall(ButtonBase button) {
        Object clientProperty = button.getProperties().get(CLIENT_PROPERTY_AUTO_REPEAT);
        if (clientProperty instanceof AutoRepeatButtonUtils) {
            ((AutoRepeatButtonUtils) clientProperty).uninstallListeners();
        }
    }

    protected void installListeners(ButtonBase button, Duration initialDelay, Duration interval) {
        _button = button;
        button.getProperties().put(CLIENT_PROPERTY_AUTO_REPEAT, this);
        if (_mouseEventEventHandler == null) {
            _mouseEventEventHandler = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                        _timer.play();
                    }
                    else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
                        _timer.stop();
                    }
                    else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                        _timer.stop();
                    }
                }
            };
        }
        button.addEventHandler(MouseEvent.ANY, _mouseEventEventHandler);

        _timer = new Timeline(new KeyFrame(interval, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                EventHandler<ActionEvent> action = _button.getOnAction();
                if (action != null) action.handle(new ActionEvent());
            }
        }));
        _timer.setDelay(initialDelay);
        _timer.setCycleCount(Animation.INDEFINITE);
    }

    protected void uninstallListeners() {
        if (_button != null) {
            _button.getProperties().put(CLIENT_PROPERTY_AUTO_REPEAT, null);
            if (_mouseEventEventHandler != null) _button.removeEventHandler(MouseEvent.ANY, _mouseEventEventHandler);
            _button = null;
        }
        if (_timer != null) {
            _timer.stop();
            _timer = null;
        }
    }
}
