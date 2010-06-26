package com.idragon.adastra.lang;

import java.io.Serializable;


/**
 * Value holder.
 *
 * @author  hp
 *
 * @param   <T>  Type of the value to hold.
 */
public class ValueHolder<T> implements Serializable {

    // Sorozatszám
    private static final long serialVersionUID = 5189235091070107373L;

    private T value;

    /**
     * Value holder.
     */
    public ValueHolder() {
        this(null);
    }

    /**
     * Value holder.
     *
     * @param  value  The value to hold.
     */
    public ValueHolder(T value) {
        this.value = value;
    }

    /**
     * @return  the value.
     */
    public T getValue() {
        return value;
    }

    /**
     * @param  value  the value to set.
     */
    public void setValue(T value) {
        this.value = value;
    }
}
