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

import com.fxexperience.javafx.animation.*;
import javafx.animation.Transition;
import javafx.scene.Node;

/**
 * {@code AnimationUtils} creates built-in Transition using enums defined in the {@link AnimationType}. Most of the
 * animations are from Jasper Potts porting of Animate.css http://daneden.me/animate by Dan Eden, while we added some
 * animations and most likely adding more in the future.
 */
public class AnimationUtils {

    /**
     * Create default transition according to the animation type.
     *
     * @param node the target node
     * @param type the AnimationType
     * @return the animation for the node.
     */
    public static Transition createTransition(Node node, AnimationType type) {
        if (node == null || type == null) {
            return null;
        }
        switch (type) {
            // From FxExperience
            case FLASH:
                return new FlashTransition(node);
            case BOUNCE:
                return new BounceTransition(node);
            case BUBBLE:
                return new BubbleTransition(node);
            case SHAKE:
                return new ShakeTransition(node);
            case TADA:
                return new TadaTransition(node);
            case SWING:
                return new SwingTransition(node);
            case WOBBLE:
                return new WobbleTransition(node);
            case PULSE:
                return new PulseTransition(node);
            case FLIP:
                return new FlipTransition(node);
            case FLIP_IN_X:
                return new FlipInXTransition(node);
            case FLIP_OUT_X:
                return new FlipOutXTransition(node);
            case FLIP_IN_Y:
                return new FlipInYTransition(node);
            case FLIP_OUT_Y:
                return new FlipOutYTransition(node);
            case FADE_IN:
                return new FadeInTransition(node);
            case FADE_IN_UP:
                return new FadeInUpTransition(node);
            case FADE_IN_DOWN:
                return new FadeInDownTransition(node);
            case FADE_IN_LEFT:
                return new FadeInLeftTransition(node);
            case FADE_IN_RIGHT:
                return new FadeInRightTransition(node);
            case FADE_IN_UP_BIG:
                return new FadeInUpBigTransition(node);
            case FADE_IN_DOWN_BIG:
                return new FadeInDownBigTransition(node);
            case FADE_IN_LEFT_BIG:
                return new FadeInLeftBigTransition(node);
            case FADE_IN_RIGHT_BIG:
                return new FadeInRightBigTransition(node);
            case FADE_OUT:
                return new FadeOutTransition(node);
            case FADE_OUT_UP:
                return new FadeOutUpTransition(node);
            case FADE_OUT_DOWN:
                return new FadeOutDownTransition(node);
            case FADE_OUT_LEFT:
                return new FadeOutLeftTransition(node);
            case FADE_OUT_RIGHT:
                return new FadeOutRightTransition(node);
            case FADE_OUT_UP_BIG:
                return new FadeOutUpBigTransition(node);
            case FADE_OUT_DOWN_BIG:
                return new FadeOutDownBigTransition(node);
            case FADE_OUT_LEFT_BIG:
                return new FadeOutLeftBigTransition(node);
            case FADE_OUT_RIGHT_BIG:
                return new FadeOutRightBigTransition(node);
            case BOUNCE_IN:
                return new BounceInTransition(node);
            case BOUNCE_IN_UP:
                return new BounceInUpTransition(node);
            case BOUNCE_IN_DOWN:
                return new BounceInDownTransition(node);
            case BOUNCE_IN_LEFT:
                return new BounceInLeftTransition(node);
            case BOUNCE_IN_RIGHT:
                return new BounceInRightTransition(node);
            case BOUNCE_OUT:
                return new BounceOutTransition(node);
            case BOUNCE_OUT_UP:
                return new BounceOutUpTransition(node);
            case BOUNCE_OUT_DOWN:
                return new BounceOutDownTransition(node);
            case BOUNCE_OUT_LEFT:
                return new BounceOutLeftTransition(node);
            case BOUNCE_OUT_RIGHT:
                return new BounceOutRightTransition(node);
            case ROTATE_IN:
                return new RollInTransition(node);
            case ROTATE_IN_DOWN_LEFT:
                return new RotateInDownLeftTransition(node);
            case ROTATE_IN_DOWN_RIGHT:
                return new RotateInDownRightTransition(node);
            case ROTATE_IN_UP_LEFT:
                return new RotateInUpLeftTransition(node);
            case ROTATE_IN_UP_RIGHT:
                return new RotateInUpRightTransition(node);
            case ROTATE_OUT:
                return new RotateOutTransition(node);
            case ROTATE_OUT_DOWN_LEFT:
                return new RotateOutDownLeftTransition(node);
            case ROTATE_OUT_DOWN_RIGHT:
                return new RotateOutDownRightTransition(node);
            case ROTATE_OUT_UP_LEFT:
                return new RotateOutUpLeftTransition(node);
            case ROTATE_OUT_UP_RIGHT:
                return new RotateOutUpRightTransition(node);
            case HINGE_OUT:
                return new HingeTransition(node);
            case ROLL_IN:
                return new RollInTransition(node);
            case ROLL_OUT:
                return new RollOutTransition(node);

            // From JideFX
            case PANIC_SHAKE:
                return new PanicShakeTransition(node);
            default:
                return null;
        }
    }
}
