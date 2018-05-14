package by.troyan.strategy.jdbc.impl;

import by.troyan.entity.Country;
import by.troyan.entity.Entity;
import by.troyan.repository.CountryRepository;
import by.troyan.spring.config.InJDBCConfigTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import java.util.List;

import static service.ServiceTest.*;

public class InJdbcStrategyCountryImplTest {

    private CountryRepository countryRepository;
    private EmbeddedDatabase db;
    private Country testCountry;

    @Before
    public void init(){
        AnnotationConfigApplicationContext applicationContext
                = new AnnotationConfigApplicationContext(InJDBCConfigTest.class);
        db = (EmbeddedDatabase) applicationContext.getBean("driverManagerDataSource");
        countryRepository = (CountryRepository) applicationContext.getBean("countryRepository");
        testCountry = new Country();
        testCountry.setName("Belarus");
    }

    @Test
    public void testAdd() throws Exception {
        universalAddTest(countryRepository, testCountry);
    }

    @Test
    public void testList() throws Exception {
        universalListTest(countryRepository, testCountry);
    }

    @Test
    public void testGet() throws Exception {
        universalGetTest(countryRepository, testCountry);
    }

    @Test
    public void testUpdate() throws Exception {
        countryRepository.add(testCountry);

        List<Country> countryList = (List) countryRepository.list().get();
        Country extractedCountry = countryList.get(0);
        extractedCountry.setName("Russia");
        countryRepository.update(extractedCountry);

        countryList = (List) countryRepository.list().get();
        Country extractedUpdatedCountry =  countryList.get(0);

        Assert.assertTrue(countryRepository.update(extractedCountry));
        Assert.assertEquals("Russia", extractedUpdatedCountry.getName());

    }

    @Test
    public void testRemove() throws Exception {
        universalRemoveTest(countryRepository, testCountry);
    }

    @Test
    public void testExecuteQuery() throws Exception {
    }

    @After
    public void tearDown() {
        db.shutdown();
    }
}