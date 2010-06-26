package com.idragon.adastra.context;

/**
 * Image source interface with caching functionality.
 *
 * @author  iDragon
 */
public interface CachingImageSource extends ImageSource {

    /**
     * Clears the cache.
     */
    void clear();

    /**
     * Removes an image from the cache.
     *
     * @param  imageCode  Code of the image to remove.
     */
    void remove(String imageCode);

    /**
     * @return  the cache size.
     */
    int size();
}
