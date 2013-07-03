/*
 * @(#)PanicShakeTransition.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
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
 * Animate a quick shake effect on the given node as the wrong password animation on the Mac OS X.
 * <pre>
 * {@literal @}keyframes shake {
 *  8%, 41% { transform: translateX(-10px); }
 *  25%, 58% { transform: translateX(10px); }
 *  75% { transform: translateX(-5px); }
 *  92% { transform: translateX(5px); }
 *  0%, 100% { transform: translateX(0); }
 * }
 * </pre>
 */
public class PanicShakeTransition extends CachedTimelineTransition {
    public static final double DURATION = 500.0;

    /**
     * Create new QuickShakeTransition
     *
     * @param node The node to affect
     */
    public PanicShakeTransition(final Node node) {
        super(
                node,
                new Timeline(
                        new KeyFrame(Duration.millis(0), new KeyValue(node.translateXProperty(), 0, WEB_EASE)),
                        new KeyFrame(Duration.millis(DURATION * .08), new KeyValue(node.translateXProperty(), -10, WEB_EASE)),
                        new KeyFrame(Duration.millis(DURATION * .25), new KeyValue(node.translateXProperty(), 10, WEB_EASE)),
                        new KeyFrame(Duration.millis(DURATION * .41), new KeyValue(node.translateXProperty(), -10, WEB_EASE)),
                        new KeyFrame(Duration.millis(DURATION * .58), new KeyValue(node.translateXProperty(), 10, WEB_EASE)),
                        new KeyFrame(Duration.millis(DURATION * .75), new KeyValue(node.translateXProperty(), -5, WEB_EASE)),
                        new KeyFrame(Duration.millis(DURATION * .92), new KeyValue(node.translateXProperty(), 5, WEB_EASE)),
                        new KeyFrame(Duration.millis(DURATION * 1.00), new KeyValue(node.translateXProperty(), 0, WEB_EASE))
                )
        );
        setCycleDuration(Duration.millis(DURATION));
        setInterpolator(Interpolator.EASE_BOTH);
        setDelay(Duration.seconds(0.2));
    }
}
