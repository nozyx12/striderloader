package dev.nozyx.strider.loader.impl;

public class Utils {
    public static String getJavaMajorVersion() {
        String version = System.getProperty("java.version");
        String major;

        if (version.startsWith("1.")) major = version.split("\\.")[1];
        else major = version.split("\\.")[0];

        return major;
    }
}
