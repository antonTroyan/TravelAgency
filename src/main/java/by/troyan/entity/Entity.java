package by.troyan.entity;

import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Main entity class. All others entity classes extend from it.
 * Contains id to avoid duplication
 */

@javax.persistence.Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Entity {

    @Id
    private String id;

    public abstract String getId();

    public abstract void setId(String id);
}