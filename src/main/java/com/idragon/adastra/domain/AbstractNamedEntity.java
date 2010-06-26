package com.idragon.adastra.domain;

/**
 * Abstract class for named entities.
 *
 * @author  iDragon
 */
public class AbstractNamedEntity extends AbstractIdentifiedEntity implements NamedEntity {

    private String name;

    /**
     * Abstract named entity.
     */
    public AbstractNamedEntity() {
    }

    @Override public String getName() {
        return name;
    }

    @Override public void setName(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return getClass().getSimpleName() + "[id=" + getId() + ",name=" + name + "]";
    }
}
