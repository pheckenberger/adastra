package com.idragon.adastra.context;

import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;


/**
 * Abstract workspace. This class includes some common workspace logic.
 *
 * @author  hp
 */
public abstract class AbstractWorkspace implements Workspace {

    /** Workspace root directory */
    private File root;

    /** Allow root creation? */
    private boolean allowRootCreation = true;

    /** Initialized? */
    private boolean initialized = false;

    /**
     * Abstract workspace. This class includes some common workspace logic.
     */
    public AbstractWorkspace() {
    }

    @Override public File getDirectory(String... pathSequence) throws IOException {
        Assert.isTrue(initialized, "workspace is not initialized");

        return getDirectoryInternal(pathSequence);
    }

    /**
     * Find a directory within the workspace walking along the path given in the sequence. The
     * sequence parts should not contain any separators, since this implies platform dependency.
     * Missing directories are created along the way.
     *
     * @param   pathSequence  Path sequence.
     *
     * @return  the requested workspace directory. Returns the root directory, if the path sequence
     *          is {@code null} or empty.
     *
     * @throws  IOException  when the result denotes a file, or a missing directory couldn't be
     *                       created.
     *
     * @see     com.idragon.adastra.context.Workspace#getDirectory
     */
    protected abstract File getDirectoryInternal(String[] pathSequence) throws IOException;

    /**
     * @return  the workspace root directory. After successful initialization of the workspace, this
     *          method surely returns an existing directory.
     */
    public File getRoot() {
        return root;
    }

    /**
     * Initialize workspace after performing dependency injection.
     *
     * @throws  IllegalArgumentException  if the workspace is already initialized, or the given root
     *                                    is invalid.
     */
    public void init() {

        Assert.isTrue(!initialized, "workspace already initialized");
        Assert.notNull(root, "root is null");

        if (root.exists()) {
            Assert.isTrue(root.isDirectory(), "root path doesn't denote a directory");
        } else {
            Assert.isTrue(allowRootCreation, "root creation forbidden");
            Assert.isTrue(root.mkdirs(), "root can't be created");
        }

        initialized = true;
    }

    /**
     * @param  allowRootCreation  whether to allow root creation. Root creation is allowed ({@code
     *                            true}) by default.
     */
    public void setAllowRootCreation(boolean allowRootCreation) {
        this.allowRootCreation = allowRootCreation;
    }

    /**
     * @param  root  the root directory to set.
     */
    public void setRoot(File root) {
        this.root = root;
    }

    /**
     * @param   parent        Parent file or directory.
     * @param   pathSequence  Path sequence.
     *
     * @return  the file or directory after joining the path sequence. The method does not
     *          guarantee, that the result really exists.
     *
     * @throws  IllegalArgumentException  if any of the path elements is empty.
     */
    protected File joinPathSequence(File parent, String[] pathSequence) {

        Assert.notNull(parent, "parent is null");

        File result = parent;

        if ((pathSequence != null) && (0 < pathSequence.length)) {

            for (String pathElement : pathSequence) {

                Assert.hasText(pathElement, "path element is empty");
                result = new File(result, pathElement);
            }
        }

        return result;
    }
}
