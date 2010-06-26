package com.idragon.adastra.context;

import java.io.File;
import java.io.IOException;


/**
 * Workspace interface. Its purpose is to store all application data in a central place on the file
 * system.
 *
 * @author  hp
 */
public interface Workspace {

    /**
     * Find a directory within the workspace walking along the path given in the sequence. The
     * sequence parts should not contain any separators, since this implies platform dependency.
     * Missing directories are created along the way.
     *
     * @param   pathSequence  Path sequence.
     *
     * @return  the requested workspace directory. Returns the root directory, if the path sequence
     *          is {@code null} or empty. Never returns a {@code null} value.
     *
     * @throws  IOException  when the result denotes a file, or a missing directory couldn't be
     *                       created.
     */
    File getDirectory(String... pathSequence) throws IOException;
}
