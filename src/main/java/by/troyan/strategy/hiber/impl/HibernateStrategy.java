package by.troyan.strategy.hiber.impl;

import by.troyan.entity.Entity;
import by.troyan.specification.Specification;
import by.troyan.strategy.RepositoryStrategy;
import by.troyan.strategy.jdbc.impl.InJdbcStrategyCountryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class HibernateStrategy implements RepositoryStrategy {
    private static final Logger LOG = LogManager.getLogger(InJdbcStrategyCountryImpl.class);
    private SessionFactory sessionFactory;

    @Autowired
    public HibernateStrategy(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public boolean add(Entity entity) {
        try {
            String id = UUID.randomUUID().toString();
            entity.setId(id);
            currentSession().save(entity);
            return true;
        } catch (Exception e) {
            LOG.error(e);
            return false;
        }
    }

    @Override
    public Optional<List> list() {
        try{
            Criteria criteria = currentSession().createCriteria(Entity.class);
            return Optional.ofNullable(criteria.list());
        } catch (Exception e){
            LOG.error(e);
            return Optional.empty();
        }
    }

    @Override
    public Optional get(String id) {
        try{
            return Optional.ofNullable(currentSession().get(Entity.class, id));
        } catch (Exception e){
            LOG.error(e);
            return Optional.empty();
        }
    }

    @Override
    public boolean update(Entity entity) {
        try {
            currentSession().update(entity);
            return true;
        } catch (Exception e){
            LOG.error(e);
            return false;
        }
    }

    @Override
    public boolean remove(String id) {
        try{
            Optional optional = get(id);
            Entity entity = (Entity) optional.get();
            currentSession().delete(entity);
            return true;
        } catch (Exception e){
            LOG.error(e);
            return false;
        }
    }

    @Override
    public Optional<List> executeQuery(Specification specification) {
        try{
            return Optional.ofNullable(specification.execute());
        } catch (Exception e){
            LOG.error(e);
            return Optional.empty();
        }
    }
}
