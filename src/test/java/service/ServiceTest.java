package service;

import by.troyan.entity.Entity;
import by.troyan.repository.Repository;
import org.junit.Assert;

import java.util.List;

public class ServiceTest {

    public static void universalGetTest(Repository<Entity> repository, Entity entity) {
        repository.add(entity);
        if(repository.list().isPresent()){
            List allEntities = repository.list().get();
            Entity extractedCountry = (Entity) allEntities.get(0);
            String id = extractedCountry.getId();

            Assert.assertNotNull(repository.get(id));
        }
    }


    public static void universalRemoveTest(Repository<Entity> repository, Entity entity) {
        repository.add(entity);
        if(repository.list().isPresent()){
            List list =  repository.list().get();
            Assert.assertEquals(1, list.size());

            Entity entityExtracted = (Entity) list.get(0);
            Assert.assertTrue(repository.remove(entityExtracted.getId()));

            list =  repository.list().get();
            Assert.assertEquals(0, list.size());
        }
    }

    public static void universalListTest(Repository<Entity> repository, Entity entity) {
        repository.add(entity);
        if(repository.list().isPresent()){
            List list =  repository.list().get();

            Assert.assertTrue(list.size() != 0);
        }
    }

    public static void universalAddTest(Repository<Entity> repository, Entity entity) {
        Assert.assertTrue(repository.add(entity));
        if(repository.list().isPresent()) {
            List list = repository.list().get();
            Entity extractedEntity = (Entity) list.get(0);

            Assert.assertNotNull(extractedEntity.getId());
        }
    }
}
