package by.troyan.repository;

import by.troyan.entity.Country;
import by.troyan.strategy.RepositoryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * CountryRepository class. Extended from entity
 */

@Component
public class CountryRepository extends Repository  {

    @Autowired
    public CountryRepository(RepositoryStrategy<Country> strategyCountry) {
        super.setStrategy(strategyCountry);
    }
}
