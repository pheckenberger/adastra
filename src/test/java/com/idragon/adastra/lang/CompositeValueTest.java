package com.idragon.adastra.lang;

import org.testng.Assert;

import org.testng.annotations.Test;


/**
 * Composite value test.
 *
 * @author hp
 */
@Test public class CompositeValueTest {

    /**
     * Composite value equals() test.
     */
    public void testEquals() {

        CompositeValue value1 = new CompositeValue(1, 2, 3);
        CompositeValue value2 = new CompositeValue(1, 2);
        CompositeValue value3 = new CompositeValue(1);
        CompositeValue value4 = new CompositeValue();
        CompositeValue value5 = new CompositeValue(new Object[] { 1, 2, 3 });
        CompositeValue value6 = new CompositeValue("1", "2", "3");

        Assert.assertFalse(value1.equals(value2));
        Assert.assertFalse(value1.equals(value3));
        Assert.assertFalse(value1.equals(value4));
        Assert.assertEquals(value1, value5);
        Assert.assertFalse(value1.equals(value6));
    }

    /**
     * Composite value hashCode() test.
     */
    public void testHashCode() {

        CompositeValue value1 = new CompositeValue(1, 2, 3);
        CompositeValue value2 = new CompositeValue(new Object[] { 1, 2, 3 });

        Assert.assertEquals(value1, value2);
    }

    /**
     * Composite value getLength() test.
     */
    public void testLength() {

        Assert.assertEquals(new CompositeValue().getLength(), 0);
        Assert.assertEquals(new CompositeValue((Object[]) null).getLength(), 0);
        Assert.assertEquals(new CompositeValue(1, 2, 3).getLength(), 3);
    }
}
