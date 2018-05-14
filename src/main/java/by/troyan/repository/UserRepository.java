package by.troyan.repository;

import by.troyan.entity.User;
import by.troyan.strategy.RepositoryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * UserRepository class. Extended from entity
 */

@Component
public class UserRepository extends Repository {

    @Autowired
    public UserRepository(RepositoryStrategy<User> strategyUser) {
        super.setStrategy(strategyUser);
    }
}
