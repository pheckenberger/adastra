package com.idragon.adastra.context;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;


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

    /** event listeners */
    private EventListenerList eventListenerList = new EventListenerList();

    /**
     * Default history implementation with a capacity of 25.
     */
    public DefaultHistory() {
    }

    @Override public void add(T object) {

        if (object == null) {
            return;
        }

        dropRedoables();

        if (objects.size() == capacity) {

            objects.remove(0);
            position--;
        }

        objects.add(object);
        position++;

        // fire event
        fireObjectAddedEvent(new Object[] { object });
    }

    @Override public void addHistoryListener(HistoryListener listener) {
        eventListenerList.add(HistoryListener.class, listener);
    }

    /**
     * Drop all current redo targets.
     */
    protected void dropRedoables() {

        while (position < objects.size()) {
            objects.remove(position);
        }
    }

    @Override public List<T> findRedoTargets(int count) {

        ArrayList<T> targets = new ArrayList<T>();
        int p = position;

        for (int i = 0; i < count; i++) {

            if (!isRedoPossible(p)) {
                break;
            }

            targets.add(objects.get(getRedoIndex(p++)));
        }

        return targets;
    }

    @Override public List<T> findUndoTargets(int count) {

        ArrayList<T> targets = new ArrayList<T>();
        int p = position;

        for (int i = 0; i < count; i++) {

            if (!isUndoPossible(p)) {
                break;
            }

            targets.add(objects.get(getUndoIndex(p--)));
        }

        return targets;
    }

    /**
     * @param  targets  the target objects, or {@code null}, if no target objects are available.
     */
    protected void fireObjectAddedEvent(Object[] targets) {

        HistoryEvent event = new HistoryEvent(this, targets);

        // guaranteed to return a non-null array
        Object[] listeners = eventListenerList.getListenerList();

        // process the listeners last to first, notifying those that are interested in this event
        for (int i = listeners.length - 2; 0 <= i; i -= 2) {

            if (listeners[i] == HistoryListener.class) {
                ((HistoryListener) listeners[i + 1]).objectAdded(event);
            }
        }
    }

    /**
     * @param  targets  the target objects, or {@code null}, if no target objects are available.
     */
    protected void fireRedoPerformedEvent(Object[] targets) {

        HistoryEvent event = new HistoryEvent(this, targets);

        // guaranteed to return a non-null array
        Object[] listeners = eventListenerList.getListenerList();

        // process the listeners last to first, notifying those that are interested in this event
        for (int i = listeners.length - 2; 0 <= i; i -= 2) {

            if (listeners[i] == HistoryListener.class) {
                ((HistoryListener) listeners[i + 1]).redoPerformed(event);
            }
        }
    }

    /**
     * @param  targets  the target objects, or {@code null}, if no target objects are available.
     */
    protected void fireUndoPerformedEvent(Object[] targets) {

        HistoryEvent event = new HistoryEvent(this, targets);

        // guaranteed to return a non-null array
        Object[] listeners = eventListenerList.getListenerList();

        // process the listeners last to first, notifying those that are interested in this event
        for (int i = listeners.length - 2; 0 <= i; i -= 2) {

            if (listeners[i] == HistoryListener.class) {
                ((HistoryListener) listeners[i + 1]).undoPerformed(event);
            }
        }
    }

    @Override public int getCapacity() {
        return capacity;
    }

    /**
     * @return  the index of the current redo target.
     */
    protected int getRedoIndex() {
        return position;
    }

    /**
     * @param   position  the position.
     *
     * @return  the index of the redo target.
     */
    protected int getRedoIndex(int position) {
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
    protected int getUndoIndex() {
        return position - 1;
    }

    /**
     * @param   position  the position.
     *
     * @return  the index of the undo target.
     */
    protected int getUndoIndex(int position) {
        return position - 1;
    }

    @Override public int getUndoSize() {
        return position;
    }

    @Override public boolean isRedoPossible() {
        return isRedoPossible(position);
    }

    /**
     * @param   position  the position to check.
     *
     * @return  whether redo is possible at the given position.
     */
    protected boolean isRedoPossible(int position) {
        return (position < objects.size());
    }

    @Override public boolean isUndoPossible() {
        return isUndoPossible(position);
    }

    /**
     * @param   position  the position to check.
     *
     * @return  whether undo is possible at the given position.
     */
    protected boolean isUndoPossible(int position) {
        return (0 < position);
    }

    @Override public T peekRedo() {
        return isRedoPossible() ? objects.get(getRedoIndex()) : null;
    }

    @Override public T peekUndo() {
        return isUndoPossible() ? objects.get(getUndoIndex()) : null;
    }

    @Override public void redo() {
        redoUnchecked(1);
    }

    @Override public void redo(int steps) throws IllegalArgumentException {

        Assert.isTrue(0 <= steps, "invalid steps < 0");
        redoUnchecked(steps);
    }

    /**
     * Perform redo unchecked.
     */
    protected void redoUnchecked() {
        position++;
    }

    /**
     * Perform redo commands.
     *
     * @param  steps  Number of redo commands to perform.
     */
    protected void redoUnchecked(int steps) {

        ArrayList<Object> targets = new ArrayList<Object>();

        for (int i = 0; i < steps; i++) {

            if (!isRedoPossible()) {
                break;
            }

            T target = objects.get(getRedoIndex());
            redoUnchecked();

            targets.add(target);
        }

        if (0 < targets.size()) {

            // fire event
            fireRedoPerformedEvent(targets.toArray(new Object[targets.size()]));
        }
    }

    @Override public void removeHistoryListener(HistoryListener listener) {
        eventListenerList.remove(HistoryListener.class, listener);
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

        Assert.isTrue(0 < capacity, "invalid capacity <= 0");

        if (0 < objects.size()) {
            throw new IllegalStateException("capacity is immutable");
        }

        this.capacity = capacity;
    }

    @Override public void undo() {
        undoUnchecked(1);
    }

    @Override public void undo(int steps) throws IllegalArgumentException {

        Assert.isTrue(0 <= steps, "invalid steps < 0");
        undoUnchecked(steps);
    }

    /**
     * Perform undo unchecked.
     */
    protected void undoUnchecked() {
        position--;
    }

    /**
     * Perform undo commands.
     *
     * @param  steps  Number of undo commands to perform.
     */
    protected void undoUnchecked(int steps) {

        ArrayList<Object> targets = new ArrayList<Object>();

        for (int i = 0; i < steps; i++) {

            if (!isUndoPossible()) {
                break;
            }

            T target = objects.get(getUndoIndex());
            undoUnchecked();

            targets.add(target);

        }

        if (0 < targets.size()) {

            // fire event
            fireUndoPerformedEvent(targets.toArray(new Object[targets.size()]));
        }
    }
}
