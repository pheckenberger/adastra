package com.idragon.adastra.context;

import org.testng.Assert;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;


/**
 * Test case of the default history implementation.
 *
 * @author  iDragon
 */
@Test public class DefaultHistoryTest {

    private DefaultHistory<Integer> history;

    /**
     * Test case of the default history implementation.
     */
    public DefaultHistoryTest() {
    }

    @BeforeMethod protected void setUp() {
        history = new DefaultHistory<Integer>();
    }

    @AfterMethod protected void tearDown() {
        history = null;
    }

    /**
     * Add test.
     */
    public void testAdd() {

        history.setCapacity(5);

        history.add(1);
        history.add(2);
        history.add(3);

        Assert.assertEquals(history.getSize(), 3);

        history.add(4);
        history.add(5);
        history.add(6);

        Assert.assertEquals(history.getSize(), 5);
        Assert.assertEquals(history.peekUndo(), new Integer(6));
    }

    /**
     * Test redo content.
     */
    public void testContent() {

        CustomHistoryListener listener = new CustomHistoryListener();
        history.addHistoryListener(listener);

        history.add(1);

        Assert.assertEquals(listener.lastTargets.length, 1);
        Assert.assertEquals(listener.lastTargets[0], 1);

        history.add(2);

        Assert.assertEquals(listener.lastTargets.length, 1);
        Assert.assertEquals(listener.lastTargets[0], 2);

        history.add(3);

        Assert.assertEquals(listener.lastTargets.length, 1);
        Assert.assertEquals(listener.lastTargets[0], 3);

        history.add(4);

        Assert.assertEquals(listener.lastTargets.length, 1);
        Assert.assertEquals(listener.lastTargets[0], 4);

        history.undo();

        Assert.assertEquals(listener.lastTargets.length, 1);
        Assert.assertEquals(listener.lastTargets[0], 4);

        history.undo(2);

        Assert.assertEquals(listener.lastTargets.length, 2);
        Assert.assertEquals(listener.lastTargets[0], 3);
        Assert.assertEquals(listener.lastTargets[1], 2);

        history.redo();

        Assert.assertEquals(listener.lastTargets.length, 1);
        Assert.assertEquals(listener.lastTargets[0], 2);

        history.redo(2);

        Assert.assertEquals(listener.lastTargets.length, 2);
        Assert.assertEquals(listener.lastTargets[0], 3);
        Assert.assertEquals(listener.lastTargets[1], 4);
    }

    /**
     * Tests invalid capacity.
     */
    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testInvalidCapacity() {
        history.setCapacity(0);
    }

    /**
     * Redo test.
     */
    public void testRedo() {

        history.add(1);
        history.add(2);
        history.add(3);
        history.add(4);

        history.undo(4);
        history.redo();

        Assert.assertEquals(history.getRedoSize(), 3);
        Assert.assertEquals(history.getUndoSize(), 1);

        history.redo(2);

        Assert.assertEquals(history.getRedoSize(), 1);
        Assert.assertEquals(history.getUndoSize(), 3);
    }

    /**
     * Tests setting capacity, when commands were added previously.
     */
    @Test(expectedExceptions = { IllegalStateException.class })
    public void testIllegalState() {

        history.add(1);
        history.setCapacity(10);
    }

    /**
     * Tests find redo targets.
     */
    public void testFindRedoTargets() {

        history.add(1);
        history.add(2);
        history.add(3);
        history.undo(3);

        List<Integer> redoTargets;

        redoTargets = history.findRedoTargets(2);

        Assert.assertEquals(redoTargets.size(), 2);
        Assert.assertEquals(redoTargets.get(0).intValue(), 1);
        Assert.assertEquals(redoTargets.get(1).intValue(), 2);

        redoTargets = history.findRedoTargets(5);

        Assert.assertEquals(redoTargets.size(), 3);
        Assert.assertEquals(redoTargets.get(0).intValue(), 1);
        Assert.assertEquals(redoTargets.get(1).intValue(), 2);
        Assert.assertEquals(redoTargets.get(2).intValue(), 3);
    }

    /**
     * Tests find undo targets.
     */
    public void testFindUndoTargets() {

        history.add(1);
        history.add(2);
        history.add(3);

        List<Integer> undoTargets;

        undoTargets = history.findUndoTargets(2);

        Assert.assertEquals(undoTargets.size(), 2);
        Assert.assertEquals(undoTargets.get(0).intValue(), 3);
        Assert.assertEquals(undoTargets.get(1).intValue(), 2);

        undoTargets = history.findUndoTargets(5);

        Assert.assertEquals(undoTargets.size(), 3);
        Assert.assertEquals(undoTargets.get(0).intValue(), 3);
        Assert.assertEquals(undoTargets.get(1).intValue(), 2);
        Assert.assertEquals(undoTargets.get(2).intValue(), 1);
    }

    /**
     * Undo test.
     */
    public void testUndo() {

        history.add(1);
        history.add(2);
        history.add(3);
        history.add(4);

        history.undo();

        Assert.assertEquals(history.getRedoSize(), 1);
        Assert.assertEquals(history.getUndoSize(), 3);

        history.undo(2);

        Assert.assertEquals(history.getRedoSize(), 3);
        Assert.assertEquals(history.getUndoSize(), 1);
    }

    /**
     * Custom history listener.
     *
     * @author  iDragon
     */
    private static class CustomHistoryListener implements HistoryListener {

        private Object[] lastTargets;

        @Override public void objectAdded(HistoryEvent event) {
            lastTargets = event.getTargets();
        }

        @Override public void redoPerformed(HistoryEvent event) {
            lastTargets = event.getTargets();
        }

        @Override public void undoPerformed(HistoryEvent event) {
            lastTargets = event.getTargets();
        }
    }
}
