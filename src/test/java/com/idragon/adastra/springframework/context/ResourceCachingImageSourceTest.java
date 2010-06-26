package com.idragon.adastra.springframework.context;

import org.testng.Assert;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.idragon.adastra.context.ResourceCachingImageSource;

import java.awt.Dimension;


/**
 * Unit test of the resource based image source with caching functionality.
 *
 * @author  hp
 */
@Test public class ResourceCachingImageSourceTest {

    private ResourceCachingImageSource imageSource;

    /**
     * Unit test of the resource based image source with caching functionality.
     */
    public ResourceCachingImageSourceTest() {
    }

    @BeforeMethod protected void setUp() {

        imageSource = new ResourceCachingImageSource();
        imageSource.setLocation("classpath:");
    }

    @AfterMethod protected void tearDown() {

        imageSource = null;
    }

    /**
     * Test of clearing the cache.
     */
    @Test public void testClear() {

        Assert.assertNotNull(imageSource.getImage("test-images/a.png"));
        Assert.assertNotNull(imageSource.getImage("test-images/a.png", new Dimension(16, 16)));
        Assert.assertNotNull(imageSource.getImage("test-images/b.png"));
        Assert.assertNotNull(imageSource.getImage("test-images/b.png", new Dimension(64, 64)));
        Assert.assertNotNull(imageSource.getImage("test-images/b.png", new Dimension(32, 32)));
        Assert.assertNotNull(imageSource.getImage("test-images/b.png", new Dimension(64, 64)));
        Assert.assertNotNull(imageSource.getImage("test-images/c.png"));
        Assert.assertNotNull(imageSource.getImage("test-images/c.png"));

        imageSource.clear();

        Assert.assertEquals(imageSource.size(), 0);
    }

    /**
     * Test with various image requests.
     */
    @Test public void testComplex() {

        Assert.assertNotNull(imageSource.getImage("test-images/a.png"));
        Assert.assertNotNull(imageSource.getImage("test-images/a.png", new Dimension(16, 16)));
        Assert.assertNotNull(imageSource.getImage("test-images/b.png"));
        Assert.assertNotNull(imageSource.getImage("test-images/b.png", new Dimension(64, 64)));
        Assert.assertNotNull(imageSource.getImage("test-images/b.png", new Dimension(32, 32)));
        Assert.assertNotNull(imageSource.getImage("test-images/b.png", new Dimension(64, 64)));
        Assert.assertNotNull(imageSource.getImage("test-images/c.png"));
        Assert.assertNotNull(imageSource.getImage("test-images/c.png"));

        Assert.assertEquals(imageSource.size(), 6);
    }

    /**
     * Test with a default size image.
     */
    @Test public void testDefault() {

        Assert.assertNotNull(imageSource.getImage("test-images/a.png"));
        Assert.assertEquals(imageSource.size(), 1);
    }

    /**
     * Test with multiple default size images.
     */
    @Test public void testDefaultMulti() {

        Assert.assertNotNull(imageSource.getImage("test-images/a.png"));
        Assert.assertNotNull(imageSource.getImage("test-images/a.png"));
        Assert.assertNotNull(imageSource.getImage("test-images/b.png"));
        Assert.assertNotNull(imageSource.getImage("test-images/c.png"));

        Assert.assertEquals(imageSource.size(), 3);
    }

    /**
     * Testing invalid path.
     */
    @Test public void testMissing() {

        Assert.assertNull(imageSource.getImage("test-images/z.png"));
        Assert.assertEquals(imageSource.size(), 1);
    }

    /**
     * Test of removing cached images.
     */
    @Test public void testRemove() {

        Assert.assertNotNull(imageSource.getImage("test-images/a.png"));
        Assert.assertNotNull(imageSource.getImage("test-images/a.png", new Dimension(16, 16)));
        Assert.assertNotNull(imageSource.getImage("test-images/b.png"));
        Assert.assertNotNull(imageSource.getImage("test-images/b.png", new Dimension(64, 64)));
        Assert.assertNotNull(imageSource.getImage("test-images/b.png", new Dimension(32, 32)));
        Assert.assertNotNull(imageSource.getImage("test-images/b.png", new Dimension(64, 64)));
        Assert.assertNotNull(imageSource.getImage("test-images/c.png"));
        Assert.assertNotNull(imageSource.getImage("test-images/c.png"));

        imageSource.remove("test-images/b.png");

        Assert.assertEquals(imageSource.size(), 3);
    }

    /**
     * Test with a resized size image.
     */
    @Test public void testResized() {

        Assert.assertNotNull(imageSource.getImage("test-images/a.png", new Dimension(64, 64)));
        Assert.assertEquals(imageSource.size(), 1);
    }

    /**
     * Test with multiple resized size images.
     */
    @Test public void testResizedMulti() {

        Assert.assertNotNull(imageSource.getImage("test-images/a.png", new Dimension(64, 64)));
        Assert.assertNotNull(imageSource.getImage("test-images/a.png", new Dimension(64, 64)));
        Assert.assertNotNull(imageSource.getImage("test-images/b.png", new Dimension(64, 64)));
        Assert.assertNotNull(imageSource.getImage("test-images/b.png", new Dimension(32, 32)));
        Assert.assertNotNull(imageSource.getImage("test-images/c.png", new Dimension(64, 64)));

        Assert.assertEquals(imageSource.size(), 4);
    }
}
