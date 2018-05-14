package by.troyan.strategy.jdbc.impl;

import by.troyan.entity.User;
import by.troyan.specification.Specification;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InJdbcStrategyUserImpl implements InJdbcStrategy<User> {

    private static final Logger LOG = LogManager.getLogger(InJdbcStrategyUserImpl.class);

    @Autowired
    @Qualifier(value = "namedParameterJdbcTemplate")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static String SQL_FOR_ADD_USER =
            "INSERT INTO public.users(id, login, password)" +
            "VALUES (:id, :login, :password);";
    private final static String SQL_FOR_DELETE_USER =
            "DELETE FROM public.users " +
            "WHERE public.users.id = (:id);";
    private final static String SQL_FOR_UPDATE_USER =
            "UPDATE public.users " +
            "SET id=:id, login=:login, password=:password " +
            "WHERE public.users.id = :id;";
    private final static String SQL_FOR_GET_LIST_USER =
            "SELECT id, login, password " +
            "FROM public.users;";
    private final static String SQL_FOR_GET_USER =
            "SELECT id, login, password " +
            "FROM public.users " +
            "WHERE public.users.id = :id ";
    private final static Integer NORMAL_ROW_AFFECTED = 1;

    private RowMapper standardRowMapperCountry = (resultSet, i) -> {
        User user = new User();
        user.setId(resultSet.getString("id"));
        user.setLogin(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));

        return user;
    };


    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public boolean add(User entity) {
        try{
            String id = UUID.randomUUID().toString();
            Map<String, String> namedParameters = new HashMap<>();
            namedParameters.put("id", id);
            namedParameters.put("login", entity.getLogin());
            namedParameters.put("password", entity.getPassword() != null
                    ? DigestUtils.md5Hex(entity.getPassword())
                    : null);
            return namedParameterJdbcTemplate
                    .update(SQL_FOR_ADD_USER, namedParameters)
                    == NORMAL_ROW_AFFECTED;
        } catch (Exception e) {
            LOG.warn("Program warn: " + e);
            return false;
        }
    }

    @Override
    public Optional<List> list() {
        try{
            return Optional.ofNullable(namedParameterJdbcTemplate
                    .query(SQL_FOR_GET_LIST_USER, standardRowMapperCountry));
        } catch (DataAccessException e) {
            LOG.error("Data access exception: " + e);
            throw new RuntimeException();
        }
    }

    @Override
    public Optional<User> get(String id) {
        try{
            Map<String, String> namedParameters = new HashMap<>();
            namedParameters.put("id", id);
            User result = (User) namedParameterJdbcTemplate
                    .query(SQL_FOR_GET_USER , namedParameters, standardRowMapperCountry)
                    .get(0);
            return Optional.ofNullable(result);
        } catch (Exception e){
            LOG.warn("Program warn: " + e);
            return Optional.empty();
        }
    }

    @Override
    public boolean update(User entity) {
        try {
            Map<String, String> namedParameters = new HashMap<>();
            namedParameters.put("id", entity.getId());
            namedParameters.put("login", entity.getLogin());
            namedParameters.put("password", entity.getPassword() != null
                    ? DigestUtils.md5Hex(entity.getPassword())
                    : null);
            return namedParameterJdbcTemplate
                    .update(SQL_FOR_UPDATE_USER, namedParameters)
                    == NORMAL_ROW_AFFECTED;
        } catch (Exception e) {
            LOG.warn("Program warn: " + e);
            return false;
        }
    }

    @Override
    public boolean remove(String id) {
        return removeFromJdbc(id, namedParameterJdbcTemplate,
                SQL_FOR_DELETE_USER, NORMAL_ROW_AFFECTED, LOG);
    }


    @Override
    public Optional<List<User>> executeQuery(Specification specification) {
        try{
            return Optional.ofNullable(specification.execute());
        } catch (Exception e) {
            LOG.warn("Program warn: " + e);
            return Optional.empty();
        }
    }
}
