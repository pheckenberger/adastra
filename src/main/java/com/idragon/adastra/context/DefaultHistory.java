package com.idragon.adastra.context;

import org.springframework.util.Assert;


import java.util.ArrayList;


/**
 * Default history implementation.
 *
 * @author  iDragon
 */
public class DefaultHistory<T> implements History<T> {

    /** capacity */
    private int capacity = 25;

    /** current history position */
    private int position = 0;

    /** object store */
    private ArrayList<T> objects = new ArrayList<T>(capacity);

    /**
     * Default history implementation.
     */
    public DefaultHistory() {
    }

    @Override public void add(T object) {

        dropRedoables();

        if (objects.size() == capacity) {

            objects.remove(0);
            position--;
        }

        objects.add(object);
        position++;
    }

    /**
     * Drop all current redo targets.
     */
    private void dropRedoables() {

        while (position < objects.size()) {
            objects.remove(position);
        }
    }

    @Override public int getCapacity() {
        return capacity;
    }

    /**
     * @return  the index of the current redo target.
     */
    private int getRedoIndex() {
        return position;
    }

    @Override public int getRedoSize() {
        return objects.size() - position;
    }

    @Override public int getSize() {
        return objects.size();
    }

    /**
     * @return  the index of the current undo target.
     */
    private int getUndoIndex() {
        return position - 1;
    }

    @Override public int getUndoSize() {
        return position;
    }

    @Override public boolean isRedoPossible() {
        return (position < objects.size());
    }

    @Override public boolean isUndoPossible() {
        return (0 < position);
    }

    @Override public T peekRedo() {
        return isRedoPossible() ? objects.get(getRedoIndex()) : null;
    }

    @Override public T peekUndo() {
        return isUndoPossible() ? objects.get(getUndoIndex()) : null;
    }

    @Override public void redo() {

        if (isRedoPossible()) {
            position++;
        }
    }

    /**
     * Capacity can be set, until one or more objects were added to the history.
     *
     * @param   capacity  the capacity to set.
     *
     * @throws  IllegalArgumentException  When capacity is invalid. Capacity must be greater than 0.
     * @throws  IllegalStateException     When one or more objects were added to the history.
     */
    public void setCapacity(int capacity) throws IllegalArgumentException, IllegalStateException {

        Assert.isTrue(0 < capacity, "capacity must be greater than 0");

        if (0 < objects.size()) {
            throw new IllegalStateException("capacity is immutable");
        }

        this.capacity = capacity;
    }

    @Override public void undo() {

        if (isUndoPossible()) {
            position--;
        }
    }
}
