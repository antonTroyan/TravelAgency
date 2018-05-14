package by.troyan.repository;

import by.troyan.entity.Hotel;
import by.troyan.strategy.RepositoryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * HotelRepository class. Extended from entity
 */

@Component
public class HotelRepository extends Repository {

    @Autowired
    public HotelRepository(RepositoryStrategy<Hotel> strategyHotel) {
        super.setStrategy(strategyHotel);
    }
}
