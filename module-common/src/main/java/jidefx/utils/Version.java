/*
 * @(#)Version.java 6/3/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils;

/**
 * A class contains the version number of the JideFX products.
 */
public class Version {
    public static final String PRODUCT_VERSION = "0.9.1";

    /**
     * Gets the product version. We used the same version number for all JideFX products. So you will get a version
     * string that applies to all JideFX products. It's in the format of x.x.x.xx. or x.x.x. Those numbers are milestone
     * version, major version, minor version and patch version respectively.
     *
     * @return the product version.
     */
    public static String getProductVersion() {
        return PRODUCT_VERSION;
    }

    private static void showVersion() {
        StringBuilder buffer = new StringBuilder();

        buffer.append("JideFX Version: ").append(getProductVersion());
        buffer.append("\n");
        buffer.append("JDK Version: ").append(SystemInfo.getJavaVersion());
        buffer.append("\n");
        buffer.append("JDK Vendor: ").append(SystemInfo.getJavaVendor());
        buffer.append("\n");
        buffer.append("Java Class Version: ").append(SystemInfo.getJavaClassVersion());
        buffer.append("\n");
        buffer.append("Platform: ").append(SystemInfo.getOS());
        buffer.append("\n");
        buffer.append("Platform Version: ").append(SystemInfo.getOSVersion());
        buffer.append("\n");
        buffer.append("Platform Architecture: ").append(SystemInfo.getOSArchitecture());
        buffer.append("\n");

        String text = buffer.toString();

        System.out.println(text);
    }

    /**
     * Prints out the version information.
     *
     * @param args the command line
     */
    public static void main(String[] args) {
        showVersion();
    }
}
