package com.idragon.adastra.context;

import java.util.EventListener;


/**
 * History listener interface.
 *
 * @author  iDragon
 */
public interface HistoryListener extends EventListener {

    /**
     * Invoked after object has been added to the history.
     *
     * @param  event  History event.
     */
    void objectAdded(HistoryEvent event);

    /**
     * Invoked after redo has been performed.
     *
     * @param  event  History event.
     */
    void redoPerformed(HistoryEvent event);

    /**
     * Invoked after undo has been performed.
     *
     * @param  event  History event.
     */
    void undoPerformed(HistoryEvent event);
}
