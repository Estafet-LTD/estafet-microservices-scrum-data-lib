package com.estafet.microservices.scrum.lib.utils;

import java.util.Locale;

/**
 * Class to check the operating system type.
 *
 * <p>This is based on the answer to this
 * <a href="http://stackoverflow.com/questions/228477/how-do-i-programmatically-determine-operating-system-in-java">stackoverflow question</a>.</p>
 */
public final class OsUtils {

    /**
     * types of Operating Systems
     */
    public enum OsType {

        /**
         * Operating system is Linux.
         */
        LINUX,

        /**
         * Operating system is Windows.
         */
        WINDOWS,

        /**
         * Operating system is Mac OS.
         */
        MACOS,

        /**
         * Operating system is some other operating system.
         */
        OTHER;
    }

    /**
     * The detected operating system type.
     */
    private static final OsType DETECTED_OS_TYPE;

    static {
        final OsType detectedOSType = detectOperatingSystemType();
        DETECTED_OS_TYPE = detectedOSType;
    }

    /**
     * Get the Operating system type.
     * @return
     *      The detected operating system type.
     */
    public static OsType getDetectedOsType() {
        return DETECTED_OS_TYPE;
    }

    /**
     * @return
     *          {@code true} if the operating system is Windows.
     */
    public static boolean isWindows() {
       return DETECTED_OS_TYPE == OsType.WINDOWS;
    }

    /**
     * @return
     *          {@code true} if the operating system is Linux.
     */
    public static boolean isLinux() {
       return DETECTED_OS_TYPE == OsType.LINUX;
    }

    /**
     * @return
     *          {@code true} if the operating system is MacOS.
     */
    public static boolean isMacOS() {
       return DETECTED_OS_TYPE == OsType.MACOS;
    }

    /**
     * @return
     *          {@code true} if the operating system is Some other operating system.
     */
    public static boolean isOther() {
       return DETECTED_OS_TYPE == OsType.OTHER;
    }

    /**
     * Detect the operating system from the {@code os.name} System property.
     *
     * @return
     *          The detected operating system.
     */
    private static OsType detectOperatingSystemType() {

        // Get the Java operating system name.
        final String osName = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);

        if (osName.indexOf("win") >= 0) {
            return OsType.WINDOWS;
        }

        if (osName.indexOf("nux") >= 0) {
            return OsType.LINUX;
        }

        if (osName.indexOf("mac") >= 0 || osName.indexOf("darwin") >= 0) {
            return OsType.MACOS;
        }

        return OsType.OTHER;
    }
}