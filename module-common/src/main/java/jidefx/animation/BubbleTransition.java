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
package jidefx.animation;

import com.fxexperience.javafx.animation.CachedTimelineTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a bubble effect on a node
 * <pre>
 * {@literal @}keyframes bubble {
 *  0%, { transform: scaleX(0) scaleY(0); }
 *  60%, { transform: scaleX(0.5) scaleY(0.5); }
 *  75%, { transform: scaleX(1.2) scaleY(1.2); }
 *  85%, { transform: scaleX(1.3) scaleY(1.3); }
 *  100%, { transform: scaleX(1) scaleY(1); }
 * }
 * </pre>
 */
public class BubbleTransition extends CachedTimelineTransition {
    public static final double DURATION = 400.0;

    /**
     * Create new BubbleTransition
     *
     * @param node The node to affect
     */
    public BubbleTransition(final Node node) {
        super(
                node,
                new Timeline(
                        new KeyFrame(Duration.millis(DURATION * .0), new KeyValue(node.scaleXProperty(), 0, WEB_EASE), new KeyValue(node.scaleYProperty(), 0, WEB_EASE)),
                        new KeyFrame(Duration.millis(DURATION * .60), new KeyValue(node.scaleXProperty(), 0.5, WEB_EASE), new KeyValue(node.scaleYProperty(), 0.5, WEB_EASE)),
                        new KeyFrame(Duration.millis(DURATION * .75), new KeyValue(node.scaleXProperty(), 1.2, WEB_EASE), new KeyValue(node.scaleYProperty(), 1.2, WEB_EASE)),
                        new KeyFrame(Duration.millis(DURATION * .85), new KeyValue(node.scaleXProperty(), 1.3, WEB_EASE), new KeyValue(node.scaleYProperty(), 1.3, WEB_EASE)),
                        new KeyFrame(Duration.millis(DURATION), new KeyValue(node.scaleXProperty(), 1.0, WEB_EASE), new KeyValue(node.scaleYProperty(), 1.0, WEB_EASE))
                ),
                false
        );
        setCycleDuration(Duration.millis(DURATION));
        setInterpolator(Interpolator.EASE_BOTH);
        setDelay(Duration.millis(200));
    }
}
