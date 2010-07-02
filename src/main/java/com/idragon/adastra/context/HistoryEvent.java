package com.idragon.adastra.context;

import java.util.EventObject;


/**
 * History event.
 *
 * @author  iDragon
 */
public class HistoryEvent extends EventObject {

    // serial version
    private static final long serialVersionUID = -2749777544353148478L;

    /** target objects */
    private final Object[] targets;

    /**
     * History event.
     *
     * @param   source  History instance.
     * @param   target  Target object.
     *
     * @throws  IllegalArgumentException  If any of source or target is {@code null}.
     */
    public HistoryEvent(Object source, Object[] targets) {

        super(source);

        this.targets = targets;
    }

    /**
     * @return  the target objects, or {@code null}, if no target objects are available.
     */
    public Object[] getTargets() {
        return targets;
    }

    /**
     * @return  the targets length.
     */
    public int getTargetsLength() {
        return (targets == null) ? 0 : targets.length;
    }
}
