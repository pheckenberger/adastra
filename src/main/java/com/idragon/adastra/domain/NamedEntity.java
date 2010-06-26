package com.idragon.adastra.domain;

/**
 * Interface of named entities.
 *
 * @author  iDragon
 */
public interface NamedEntity extends IdentifiedEntity {

    /**
     * @return  Entity name.
     */
    String getName();

    /**
     * @param  name  Entity name to set.
     */
    void setName(String name);
}
