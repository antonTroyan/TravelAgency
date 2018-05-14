package by.troyan.strategy.jdbc.impl;

import by.troyan.entity.Entity;
import by.troyan.strategy.RepositoryStrategy;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.Map;

public interface InJdbcStrategy <T extends Entity> extends RepositoryStrategy<T> {

    default boolean removeFromJdbc(String id, NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                   String sqlForDeleteUser, Integer normalRowAffected, Logger log) {
        try{
            Map<String, String> namedParameters = new HashMap<>();
            namedParameters.put("id", id);
            return namedParameterJdbcTemplate
                    .update(sqlForDeleteUser, namedParameters)
                    == normalRowAffected;
        } catch (Exception e) {
            log.warn("Program warn: " + e);
            return false;
        }
    }
}
