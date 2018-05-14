package by.troyan.strategy.jdbc.impl;

import by.troyan.aspect.TimeCounted;
import by.troyan.entity.Country;
import by.troyan.specification.Specification;
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
public class InJdbcStrategyCountryImpl implements InJdbcStrategy<Country> {

    private static final Logger LOG = LogManager.getLogger(InJdbcStrategyCountryImpl.class);

    @Autowired
    @Qualifier(value = "namedParameterJdbcTemplate")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static String SQL_FOR_ADD_COUNTRY =
            "INSERT INTO public.countries(id, name) " +
            "VALUES (:id, :name);";
    private final static String SQL_FOR_DELETE_COUNTRY =
            "DELETE FROM public.countries " +
            "WHERE public.countries.id = (:id);";
    private final static String SQL_FOR_UPDATE_COUNTRY =
            "UPDATE public.countries " +
            "SET id = :id, name = :name " +
            "WHERE public.countries.id = :id;";
    private final static String SQL_FOR_GET_LIST_COUNTRIES =
            "SELECT id, name " +
            "FROM public.countries";
    private final static String SQL_FOR_GET_COUNTRY =
            "SELECT id, name " +
            "FROM public.countries " +
            "WHERE public.countries.id = :id ";
    private final static Integer NORMAL_ROW_AFFECTED = 1;

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {

        return namedParameterJdbcTemplate;
    }

    private RowMapper standardRowMapperCountry = (resultSet, i) -> {
        Country country = new Country();
        country.setId(resultSet.getString("id"));
        country.setName(resultSet.getString("name"));
        return country;
    };

    @Override
    @TimeCounted
    public boolean add(Country entity) {
        try{
            String id = UUID.randomUUID().toString();
            Map<String, String> namedParameters = new HashMap<>();
            namedParameters.put("id", id);
            namedParameters.put("name", entity.getName());
            return namedParameterJdbcTemplate
                    .update(SQL_FOR_ADD_COUNTRY, namedParameters)
                    == NORMAL_ROW_AFFECTED;
        } catch (Exception e){
            LOG.warn("Program warn: " + e);
            return false;
        }
    }

    @Override
    public Optional<List> list() {
        try{
            return Optional.ofNullable(namedParameterJdbcTemplate
                    .query(SQL_FOR_GET_LIST_COUNTRIES, standardRowMapperCountry));
        } catch (DataAccessException e){
            LOG.error("Data access exception: " + e);
            throw new RuntimeException();
        }
    }

    @Override
    public Optional<Country> get(String id) {
        try{
            Map<String, String> namedParameters = new HashMap<>();
            namedParameters.put("id", id);
            Country result = (Country) namedParameterJdbcTemplate
                    .query(SQL_FOR_GET_COUNTRY , namedParameters, standardRowMapperCountry)
                    .get(0);
            return Optional.ofNullable(result);
        } catch (Exception e){
            LOG.warn("Program warn: " + e);
            return Optional.empty();
        }
    }

    @Override
    public boolean update(Country entity) {
        try{
            Map<String, String> namedParameters = new HashMap<>();
            namedParameters.put("id", entity.getId());
            namedParameters.put("name", entity.getName());
            return namedParameterJdbcTemplate
                    .update(SQL_FOR_UPDATE_COUNTRY, namedParameters)
                    == NORMAL_ROW_AFFECTED;
        } catch (Exception e){
            LOG.warn("Program warn: " + e);
            return false;
        }
    }

    @Override
    public boolean remove(String id) {
        return removeFromJdbc(id, namedParameterJdbcTemplate,
                SQL_FOR_DELETE_COUNTRY, NORMAL_ROW_AFFECTED, LOG);
    }

    @Override
    public Optional<List<Country>> executeQuery(Specification specification) {
        try {
            return Optional.ofNullable(specification.execute());
        } catch (Exception e){
            LOG.warn("Program warn: " + e);
            return Optional.empty();
        }
    }
}
