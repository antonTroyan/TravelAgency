import by.troyan.entity.Country;
import by.troyan.repository.CountryRepository;
import by.troyan.spring.config.HibernateConfigTest;
import by.troyan.strategy.hiber.impl.HibernateStrategy;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import java.util.List;
import java.util.Optional;

import static service.ServiceTest.*;

public class HibernateStrategyTest{

    private CountryRepository countryRepository;
    private EmbeddedDatabase db;
    private AnnotationConfigApplicationContext applicationContext;
    private Country testCountry;

    @Before
    public void init() {

        applicationContext = new AnnotationConfigApplicationContext(HibernateConfigTest.class);
        db = (EmbeddedDatabase) applicationContext.getBean("dataSource");
        countryRepository = (CountryRepository) applicationContext.getBean("countryRepository");
        countryRepository.setStrategy(applicationContext.getBean(HibernateStrategy.class));

        testCountry = new Country();
        testCountry.setName("Belarus");
    }

    @Test
    public void testAdd() {
        universalAddTest(countryRepository, testCountry);
    }

    @Test
    public void testList() {
        universalListTest(countryRepository, testCountry);
    }

    @Test
    public void testGet() {
        universalGetTest(countryRepository, testCountry);
    }

    @Test
    public void testUpdate() {
        Optional optional = countryRepository.list();
        if(optional.isPresent()) {
            countryRepository.add(testCountry);

            testCountry.setName("Nigeria");
            Assert.assertTrue(countryRepository.update(testCountry));

            List <Country> list = (List) countryRepository.list().get();
            Assert.assertEquals("Nigeria", list.get(0).getName());
        }
    }

    @Test
    public void testRemove() {
        universalRemoveTest(countryRepository, testCountry);
    }

    @Test
    public void testExecuteQuery() {
    }

    @After
    public void tearDown() {
        db.shutdown();
        applicationContext.close();
    }
}