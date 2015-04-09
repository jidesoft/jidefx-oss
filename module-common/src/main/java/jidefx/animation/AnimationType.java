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

/**
 * {@code AnimationType} defines many built-in Transitions. Most of the animations are from Jasper Potts porting of
 * Animate.css http://daneden.me/animate by Dan Eden, while we added some animations and most likely adding more in the
 * future.
 */
public enum AnimationType {
    NONE,

    // Attention seekers
    FLASH,
    BOUNCE,
    SHAKE,
    TADA,
    SWING,
    WOBBLE,
    PULSE,
    PANIC_SHAKE, // Invalid password animation as on Mac OS X
    BUBBLE,

    // Flippers
    FLIP,
    FLIP_IN_X,
    FLIP_OUT_X,
    FLIP_IN_Y,
    FLIP_OUT_Y,

    // Fading entrances
    FADE_IN,
    FADE_IN_UP,
    FADE_IN_DOWN,
    FADE_IN_LEFT,
    FADE_IN_RIGHT,
    FADE_IN_UP_BIG,
    FADE_IN_DOWN_BIG,
    FADE_IN_LEFT_BIG,
    FADE_IN_RIGHT_BIG,

    // Fading exits
    FADE_OUT,
    FADE_OUT_UP,
    FADE_OUT_DOWN,
    FADE_OUT_LEFT,
    FADE_OUT_RIGHT,
    FADE_OUT_UP_BIG,
    FADE_OUT_DOWN_BIG,
    FADE_OUT_LEFT_BIG,
    FADE_OUT_RIGHT_BIG,

    // Bouncing entrances
    BOUNCE_IN,
    BOUNCE_IN_UP,
    BOUNCE_IN_DOWN,
    BOUNCE_IN_LEFT,
    BOUNCE_IN_RIGHT,

    // Bouncing exits
    BOUNCE_OUT,
    BOUNCE_OUT_UP,
    BOUNCE_OUT_DOWN,
    BOUNCE_OUT_LEFT,
    BOUNCE_OUT_RIGHT,

    // Rotating entrances
    ROTATE_IN,
    ROTATE_IN_DOWN_LEFT,
    ROTATE_IN_DOWN_RIGHT,
    ROTATE_IN_UP_LEFT,
    ROTATE_IN_UP_RIGHT,

    // Rotating exits
    ROTATE_OUT,
    ROTATE_OUT_DOWN_LEFT,
    ROTATE_OUT_DOWN_RIGHT,
    ROTATE_OUT_UP_LEFT,
    ROTATE_OUT_UP_RIGHT,

    // Specials
    HINGE_OUT,
    ROLL_IN,
    ROLL_OUT,
}
