package com.idragon.adastra.domain;


/**
 * Abstract class for identified entities.
 *
 * @author  iDragon
 */
public class AbstractIdentifiedEntity implements IdentifiedEntity {

    private Long id;

    /**
     * Abstract identified entity.
     */
    public AbstractIdentifiedEntity() {
    }

    @Override public boolean equals(Object o) {

        // Returns true in the following cases
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // - trivial match
        // - technical key match, if keys are present
        // - reference match, if keys are not present

        if ((this == o) ||
                ((o != null) && getClass().equals(o.getClass()) && (id != null) &&
                    id.equals(((AbstractIdentifiedEntity) o).id))) {

            return true;
        }

        return false;
    }

    @Override public Long getId() {
        return id;
    }

    @Override public int hashCode() {
        return (id == null) ? 0 : id.hashCode();
    }

    @Override public String toString() {
        return getClass().getSimpleName() + "[id=" + id + "]";
    }
}
