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

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Utils to allow enable/disable a Java Logger to print certain level of message to console. This is mainly used for
 * debugging purpose.
 */
public class LoggerUtils {
    public static Handler enableLogger(String loggerName, Level level) {
        Logger log = Logger.getLogger(loggerName);
        log.setLevel(level);
        Handler handler = new Handler() {
            @SuppressWarnings({"UseOfSystemOutOrSystemErr"})
            @Override
            public void publish(LogRecord record) {
                System.err.print(record.getMessage());
                Object[] params = record.getParameters();
                if (params != null) {
                    if (params.length > 0)
                        System.err.print("= ");
                    for (int i = 0; i < params.length; i++) {
                        System.err.print(params[i]);
                        if (i < params.length - 1)
                            System.err.print(", ");
                    }
                }
                System.err.println();
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }
        };
        log.addHandler(handler);
        return handler;
    }

    public static void disableLogger(String loggerName, Handler handler) {
        Logger log = Logger.getLogger(loggerName);
        log.setLevel(null);
        log.removeHandler(handler);
    }
}
