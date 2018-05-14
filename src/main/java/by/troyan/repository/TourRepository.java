package by.troyan.repository;

import by.troyan.entity.Tour;
import by.troyan.strategy.RepositoryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TourRepository class. Extended from entity
 */

@Component
public class TourRepository extends Repository<Tour> {

    @Autowired
    public TourRepository(RepositoryStrategy<Tour> strategyTour) {
        super.setStrategy(strategyTour);
    }
}
