package com.idragon.adastra.springframework.context;

import com.idragon.adastra.context.SimpleWorkspace;

import org.apache.commons.io.FileUtils;

import org.testng.Assert;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;


/**
 * Simple workspace unit test.
 *
 * @author  hp
 */
@Test public class SimpleWorkspaceTest {

    private SimpleWorkspace workspace;

    /**
     * Simple workspace unit test.
     */
    public SimpleWorkspaceTest() {
    }

    @BeforeMethod protected void setUp() {

        workspace = new SimpleWorkspace();
        workspace.setRoot(new File(System.getProperty("user.home"), "simple-workspace-test"));
        workspace.init();
    }

    @AfterMethod protected void tearDown() throws IOException {

        FileUtils.forceDelete(workspace.getRoot());
        workspace = null;
    }

    @Test public void testWorkspace() throws IOException {

        File directory = workspace.getDirectory("aaa", "bbb", "ccc");
        Assert.assertTrue(directory.exists(), "directory does not exist");
    }
}
