package com.idragon.adastra.lang;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;


/**
 * Utils for the Apache Commons Dyna classes.
 *
 * @author  hp
 */
public abstract class DynaUtils {

    /**
     * Utils for the Apache Commons Dyna classes.
     */
    private DynaUtils() {
    }

    /**
     * @param   dynaClass  Class. The value {@code null} is handled gracefully.
     *
     * @return  String representation of the class.
     */
    public static String toString(DynaClass dynaClass) {

        if (dynaClass == null) {
            return String.valueOf(null);
        }

        String dynaClassName = dynaClass.getName();
        DynaProperty[] dynaProperties = dynaClass.getDynaProperties();

        StringBuilder s = new StringBuilder(dynaClassName.length() + (dynaProperties.length * 8));
        s.append("dynaclass ").append(dynaClassName).append('[');

        for (int i = 0; i < dynaProperties.length; i++) {

            if (0 < i) {
                s.append(',');
            }

            s.append(dynaProperties[i].getName());
        }

        s.append("]");

        return s.toString();
    }

    /**
     * @param   dynaBean  Bean. The value {@code null} is handled gracefully.
     *
     * @return  String representation of the bean.
     */
    public static String toString(DynaBean dynaBean) {

        if (dynaBean == null) {
            return String.valueOf(null);
        }

        DynaClass dynaClass = dynaBean.getDynaClass();
        String dynaClassName = dynaClass.getName();
        DynaProperty[] dynaProperties = dynaClass.getDynaProperties();

        StringBuilder s = new StringBuilder(dynaClassName.length() + (dynaProperties.length * 16));
        s.append(dynaClassName).append('[');

        for (int i = 0; i < dynaProperties.length; i++) {

            DynaProperty dynaProperty = dynaProperties[i];
            String dynaPropertyName = dynaProperty.getName();

            if (0 < i) {
                s.append(',');
            }

            s.append(dynaPropertyName);
            s.append('=').append(dynaBean.get(dynaPropertyName));
        }

        s.append(']');

        return s.toString();
    }
}
