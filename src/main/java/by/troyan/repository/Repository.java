package by.troyan.repository;

import by.troyan.entity.Entity;
import by.troyan.specification.Specification;
import by.troyan.strategy.RepositoryStrategy;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Main abstract class for all repositories. Contains main methods to work
 * with repository as collection.
 * Using pattern Strategy it has a slot "private RepositoryStrategy<T> strategy"
 * that allows to work with all possible strategies that implemented special interface
 * RepositoryStrategy.
 * Depending of object filling "strategy" we receive different behavior (realization of repository)
 *
 * @param <T> the type parameter extends of Entity
 */

public abstract class Repository<T extends Entity> {


    private RepositoryStrategy<T> strategy;

    /**
     * Used to set special strategy. Need to be called first after
     * receiving instance
     *
     * @param strategy the strategy
     */

    public void setStrategy(RepositoryStrategy<T> strategy) {

        this.strategy = strategy;
    }

    public RepositoryStrategy<T> getStrategy() {
        return strategy;
    }

    /**
     * Used to add entity to repository. Depending on strategy it could be operational memory,
     * data base or file
     *
     * @param entity the entity
     * @return boolean. True if add was successful, false if not
     */

    @Transactional(isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = Throwable.class)
    public boolean add(T entity) {

        return strategy.add(entity);
    }

    /**
     * Used to get all list of objects from repository
     *
     * @return the list
     */
    @Transactional(readOnly = true,
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED)
    public Optional<List> list() {

        return strategy.list();
    }

    /**
     * Used to get object from repository by special id
     *
     * @param id the id
     * @return the t
     */
    @Transactional(readOnly = true,
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED)
    public Optional<T> get(String id) {

        return strategy.get(id);
    }

    /**
     * Used to update object from repository. Use id to find out what to update
     *
     * @param entity the entity
     * @return the t
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = Throwable.class)
    public boolean update(T entity) {

        return strategy.update(entity);
    }

    /**
     * Used to remove object from repository. Use id to find out what to remove
     *
     * @param id the id
     * @return the boolean
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = Throwable.class)
    public boolean remove(String id) {

        return strategy.remove(id);
    }

    /**
     * Used to execute special queries. Need specification object
     *
     * @param specification the specification
     * @return the list
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = Throwable.class)
    public Optional<List<T>> executeQuery(Specification specification) {

        return strategy.executeQuery(specification);
    }
}
