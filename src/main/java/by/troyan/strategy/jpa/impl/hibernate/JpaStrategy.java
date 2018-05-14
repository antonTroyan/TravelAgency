package by.troyan.strategy.jpa.impl.hibernate;

import by.troyan.entity.Entity;
import by.troyan.specification.Specification;
import by.troyan.strategy.RepositoryStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaStrategy implements RepositoryStrategy {
    private static final Logger LOG = LogManager.getLogger(JpaStrategy.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean add(Entity entity) {
        try {
            String id = UUID.randomUUID().toString();
            entity.setId(id);
            entityManager.merge(entity);
            return true;
        } catch (Exception e) {
            LOG.error(e);
            return false;
        }
    }



    @SuppressWarnings("JpaQlInspection")
    @Override
    public Optional<List> list() {
        try {
            return Optional.ofNullable(entityManager
                    .createQuery("SELECT e FROM Entity e", Entity.class)
                    .getResultList());
        } catch (Exception e) {
            LOG.error(e);
            return Optional.empty();
        }
    }

    @Override
    public Optional get(String id) {
        try {
            return Optional.ofNullable(entityManager.find(Entity.class, id));
        } catch (Exception e) {
            LOG.error(e);
            return Optional.empty();
        }
    }

    @Override
    public boolean update(Entity entity) {
        try {
            entityManager.merge(entity);
            return true;
        } catch (Exception e) {
            LOG.error(e);
            return false;
        }
    }

    @Override
    public boolean remove(String id) {
        try {
            Entity deleted;
            if(get(id).isPresent()){
                deleted = (Entity) get(id).get();
                entityManager.remove(deleted);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            LOG.error(e);
            return false;
        }
    }

    @Override
    public Optional<List> executeQuery(Specification specification) {
        try {
            return Optional.ofNullable(specification.execute());
        } catch (Exception e) {
            LOG.error(e);
            return Optional.empty();
        }
    }
}
