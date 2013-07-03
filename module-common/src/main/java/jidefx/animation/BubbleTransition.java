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
