package by.troyan.repository;

import by.troyan.entity.Review;
import by.troyan.strategy.RepositoryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * ReviewRepository class. Extended from entity
 */

@Component
public class ReviewRepository extends Repository {

    @Autowired
    public ReviewRepository(RepositoryStrategy<Review> strategyReview) {
        super.setStrategy(strategyReview);
    }
}
