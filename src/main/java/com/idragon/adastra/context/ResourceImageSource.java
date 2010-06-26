package com.idragon.adastra.context;

import org.springframework.context.ResourceLoaderAware;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import java.io.IOException;


/**
 * Resource based image source.
 *
 * @author  iDragon
 */
public class ResourceImageSource implements ImageSource, ResourceLoaderAware {

    private String[] locations = new String[0];
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    /**
     * Resource based source.
     */
    public ResourceImageSource() {
    }

    @Override public Image getImage(String imageCode) {
        return getImage(imageCode, null);
    }

    @Override public Image getImage(String imageCode, Dimension dimension) {
        Assert.hasText(imageCode, "invalid image code: " + imageCode);

        return getImageInternal(imageCode, dimension);
    }

    @Override public Image getImage(String imageCode, int width, int height) {
        return getImage(imageCode, new Dimension(width, height));
    }

    /**
     * Resolves the image code and resizes the image, if needed.
     *
     * @param   imageCode  Image code to resolve.
     * @param   dimension  The required image dimensions, or {@code null}, if the default image size
     *                     is appropiate.
     *
     * @return  the resolved image or {@code null}, if the image doesn't exist.
     */
    protected Image getImageInternal(String imageCode, Dimension dimension) {

        Image image = resolveCode(imageCode);

        if (image == null) {
            return image;
        }

        Dimension imageDimension = new Dimension(image.getWidth(null), image.getHeight(null));

        if ((dimension == null) || imageDimension.equals(dimension)) {
            return image;
        }

        // Image needs resize
        return image.getScaledInstance(dimension.width, dimension.height,
                BufferedImage.SCALE_SMOOTH);
    }

    /**
     * @return  the array of locations, where the image resources can be found.
     */
    public String[] getLocations() {
        return locations;
    }

    /**
     * Resolves an image code, but doesn't alter the image.
     *
     * @param   imageCode  Image code to resolve.
     *
     * @return  the resolved image or {@code null}, if the image resource doesn't exist.
     */
    protected Image resolveCode(String imageCode) {

        for (int i = 0; i < locations.length; i++) {

            String path = StringUtils.applyRelativePath(locations[i], imageCode);
            Resource resource = resourceLoader.getResource(path);

            if (resource.exists()) {

                try {
                    return Toolkit.getDefaultToolkit().createImage(resource.getURL());
                } catch (IOException e) {
                    throw new IllegalArgumentException("invalid resource: " + resource);
                }
            }
        }

        return null;
    }

    /**
     * @param  location  the location, where the image resources can be found.
     */
    public void setLocation(String location) {
        setLocations(new String[] { location });
    }

    /**
     * @param  locations  the array of locations, where the image resources can be found.
     */
    public void setLocations(String[] locations) {

        if (locations != null) {
            this.locations = new String[locations.length];

            for (int i = 0; i < locations.length; i++) {
                String location = locations[i];
                Assert.hasText(location, "location is empty");
                this.locations[i] = location.trim();
            }
        } else {
            locations = new String[0];
        }

        this.locations = locations;
    }

    @Override public void setResourceLoader(ResourceLoader resourceLoader) {

        this.resourceLoader = ((resourceLoader != null) ? resourceLoader
                                                        : new DefaultResourceLoader());
    }
}
