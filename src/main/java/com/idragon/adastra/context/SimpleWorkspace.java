package com.idragon.adastra.context;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;


/**
 * The simple workspace doesn't manage available paths, only returns the file handles relative to
 * the workspace root.
 *
 * @author  hp
 */
public class SimpleWorkspace extends AbstractWorkspace {

    /**
     * The simple workspace doesn't manage available paths, only returns the file handles relative
     * to the workspace root.
     */
    public SimpleWorkspace() {
    }

    @Override protected File getDirectoryInternal(String[] pathSequence) throws IOException {

        File delegate = joinPathSequence(getRoot(), pathSequence);
        FileUtils.forceMkdir(delegate);

        return delegate;
    }
}
