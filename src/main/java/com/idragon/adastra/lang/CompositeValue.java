package com.idragon.adastra.lang;

import java.util.Arrays;


/**
 * Composite value, which contains an arbitrary count of elements. Very useful in situations, where
 * maps use ad-hoc keys.
 *
 * @author  iDragon
 */
public class CompositeValue {

    /** Composite value elements */
    private final Object[] elements;

    /**
     * Composite value.
     *
     * @param  elements  the composite value elements
     */
    public CompositeValue(Object... elements) {

        this.elements = new Object[(elements == null) ? 0 : elements.length];

        if (elements != null) {
            System.arraycopy(elements, 0, this.elements, 0, elements.length);
        }
    }

    /**
     * @return  the composite value length
     */
    public int getLength() {
        return elements.length;
    }

    /**
     * This method is considering element array hash code.
     */
    @Override public int hashCode() {
        return Arrays.hashCode(elements);
    }

    /**
     * This method is considering element array equality.
     */
    @Override public boolean equals(Object object) {

        if (this == object)
            return true;

        if ((object == null) || !(object instanceof CompositeValue)) {
            return false;
        }

        return Arrays.equals(elements, ((CompositeValue) object).elements);
    }
}
