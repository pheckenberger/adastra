package com.idragon.adastra.context;

import java.util.List;


/**
 * The history manages a given number of objects, up to a specified capacity. The history has a
 * cursor, which can be moved forward (redo) or backward (undo). When the history at full capacity
 * receives another object to manage, the oldest one will be removed from the history.
 *
 * @author  iDragon
 *
 * @param   <T>  History content type.
 */
public interface History<T> {

    /**
     * Removes all objects after the cursor, adds the given object to the history, and moves the
     * cursor forward. If the object is {@code null}, nothing happens, and the event is omitted too.
     *
     * @param  object  the object to add.
     */
    void add(T object);

    /**
     * @param  listener  the history listener to add.
     */
    void addHistoryListener(HistoryListener listener);

    /**
     * Finds a given number of redo targets. The first element of the list will be the object
     * nearest to the cursor position. If not enough targets are present, the available targets are
     * returned.
     *
     * @param   count  Element count at most.
     *
     * @return  The list of redo targets.
     */
    List<T> findRedoTargets(int count);

    /**
     * Finds a given number of undo targets. The first element of the list will be the object
     * nearest to the cursor position. If not enough targets are present, the available targets are
     * returned.
     *
     * @param   count  Element count at most.
     *
     * @return  The list of undo targets.
     */
    List<T> findUndoTargets(int count);

    /**
     * @return  the capacity.
     */
    int getCapacity();

    /**
     * @return  the number of possible redo commands.
     */
    int getRedoSize();

    /**
     * @return  the current history size, which means the number of managed objects.
     */
    int getSize();

    /**
     * @return  the number of possible undo commands.
     */
    int getUndoSize();

    /**
     * @return  whether redo is possible.
     */
    boolean isRedoPossible();

    /**
     * @return  whether undo is possible.
     */
    boolean isUndoPossible();

    /**
     * @return  the target of the redo command, or {@code null}, when redo is currently not
     *          possible.
     */
    T peekRedo();

    /**
     * @return  the target of the undo command, or {@code null}, when undo is currently not
     *          possible.
     */
    T peekUndo();

    /**
     * Moves the cursor to the next object and fires the appropriate action. If redo is not
     * possible, nothing happens, and the event is omitted too.
     */
    void redo();

    /**
     * Moves the cursor to a following object, and fires the appropriate event, if necessary. This
     * method performs at most the given number of redo commands.
     *
     * @throws  IllegalArgumentException  If steps is less, than {@code 0}.
     */
    void redo(int steps) throws IllegalArgumentException;

    /**
     * @param  listener  the history listener to remove.
     */
    void removeHistoryListener(HistoryListener listener);

    /**
     * Moves the cursor to the previous object and fires the appropriate action. If undo is not
     * possible, nothing happens, and the event is omitted too.
     */
    void undo();

    /**
     * Moves the cursor to a preceeding object, and fires the appropriate event, if necessary. This
     * method performs at most the given number of undo commands.
     *
     * @throws  IllegalArgumentException  If steps is less, than {@code 0}.
     */
    void undo(int steps) throws IllegalArgumentException;
}
