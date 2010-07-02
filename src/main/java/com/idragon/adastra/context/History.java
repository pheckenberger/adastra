package com.idragon.adastra.context;

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
     * cursor forward.
     *
     * @param  object  the object to add.
     */
    void add(T object);

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
     * Moves the cursor to the next object. If redo is not possible, nothing happens.
     */
    void redo();

    /**
     * Moves the cursor to the previous object. If undo is not possible, nothing happens.
     */
    void undo();
}
