package com.idragon.adastra.lang;

/**
 * Severity enumeration.
 *
 * @author  iDragon
 */
public enum Severity {

    /** Information severity */
    INFORMATION,

    /** Warning severity */
    WARNING,

    /** Error severity */
    ERROR;

    /**
     * @param   severities  Severities. The method handles {@code null} value and {@code null}
     *                      contents gracefully.
     *
     * @return  the least severe instance or {@code null}, if no valid instance was found.
     */
    public static Severity leastSevere(Severity... severities) {

        Integer ordinal = null;

        if (severities != null) {

            for (Severity severity : severities) {

                if ((severity != null) && ((ordinal == null) || (severity.ordinal() < ordinal))) {
                    ordinal = severity.ordinal();
                }
            }
        }

        return ((ordinal == null) ? null : Severity.values()[ordinal]);
    }

    /**
     * @param   severities  Severities. The method handles {@code null} value and {@code null}
     *                      contents gracefully.
     *
     * @return  the most severe instance or {@code null}, if no valid instance was found.
     */
    public static Severity mostSevere(Severity... severities) {

        Integer ordinal = null;

        if (severities != null) {

            for (Severity severity : severities) {

                if ((severity != null) && ((ordinal == null) || (ordinal < severity.ordinal()))) {
                    ordinal = severity.ordinal();
                }
            }
        }

        return ((ordinal == null) ? null : Severity.values()[ordinal]);
    }

    /**
     * @return  {@code true}, if this instance is less severe, than the given severity or {@code
     *          false}, if you pass a {@code null} value
     */
    public boolean isLessSevereThan(Severity severity) {
        return ((severity != null) && (ordinal() < severity.ordinal()));
    }

    /**
     * @return  {@code true}, if this instance is more severe, than the given severity or {@code
     *          false}, if you pass a {@code null} value
     */
    public boolean isMoreSevereThan(Severity severity) {
        return ((severity != null) && (severity.ordinal() < ordinal()));
    }
}
