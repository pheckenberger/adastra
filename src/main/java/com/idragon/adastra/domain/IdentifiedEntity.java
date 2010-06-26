package com.idragon.adastra.domain;


/**
 * Interface of identified entities.
 *
 * @author  iDragon
 */
public interface IdentifiedEntity {

    /**
     * @return  Technical identifier of the entity, which is not a business key. Transient entities
     *          return {@code null}.
     */
    Long getId();
}
