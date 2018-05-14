package by.troyan.strategy;

import by.troyan.entity.Entity;
import by.troyan.specification.Specification;

import java.util.List;
import java.util.Optional;

/**
 * The interface Repository strategy.
 *
 * @param <T> the type parameter
 */
public interface RepositoryStrategy<T extends Entity> {


    /**
     * Add entity to repository.
     *
     * @param entity the entity
     */
    boolean add(T entity);

    /**
     * Get list of all entities.
     *
     * @return the list
     */
    Optional<List> list();

    /**
     * Get special entity by id.
     *
     * @param id the id
     * @return the optional wrapper of object
     */
    Optional<T> get(String id);

    /**
     * Update.
     *
     * @param entity the entity
     */
    boolean update(T entity);

    /**
     * Remove.
     *
     * @param id the id
     */
    boolean remove(String id);

    /**
     * Execute query optional.
     *
     * @param specification the specification
     * @return list of objects that were asked by specification
     */
    Optional<List<T>> executeQuery (Specification specification);
}