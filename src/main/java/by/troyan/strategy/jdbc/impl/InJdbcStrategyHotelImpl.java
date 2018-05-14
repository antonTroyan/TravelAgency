package by.troyan.strategy.jdbc.impl;

import by.troyan.entity.Country;
import by.troyan.entity.Hotel;
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
public class InJdbcStrategyHotelImpl implements InJdbcStrategy<Hotel> {

    private static final Logger LOG = LogManager.getLogger(InJdbcStrategyHotelImpl.class);

    @Autowired
    @Qualifier(value = "namedParameterJdbcTemplate")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static String SQL_FOR_ADD_HOTEL =
            "INSERT INTO public.hotels(id, name, phone, country_id, stars)" +
            "VALUES (:id, :name, :phone, :country_id, :stars);";
    private final static String SQL_FOR_DELETE_HOTEL =
            "DELETE FROM public.hotels " +
            "WHERE public.hotels.id = (:id);";
    private final static String SQL_FOR_UPDATE_HOTEL =
            "UPDATE public.hotels " +
            "SET id=:id, name=:name, phone=:phone, country_id=:country_id, stars=:stars " +
            "WHERE public.hotels.id = :id;";
    private final static String SQL_FOR_GET_LIST_HOTEL =
            "SELECT id, name, phone, country_id, stars " +
            "FROM public.hotels;";
    private final static String SQL_FOR_GET_HOTEL =
            "SELECT id, name, phone, country_id, stars " +
            "FROM public.hotels " +
            "WHERE public.hotels.id = :id ";
    private final static Integer NORMAL_ROW_AFFECTED = 1;

    private RowMapper standardRowMapperCountry = (resultSet, i) -> {
        Hotel hotel = new Hotel();
        hotel.setId(resultSet.getString("id"));
        hotel.setName(resultSet.getString("name"));
        hotel.setPhone(resultSet.getString("phone"));
        Country emptyCountry = new Country();
        emptyCountry.setId(resultSet.getString("country_id"));
        hotel.setCountry(emptyCountry);
        hotel.setStars(resultSet.getInt("stars"));
        return hotel;
    };

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public boolean add(Hotel entity) {
        try{
            String id = UUID.randomUUID().toString();
            Map<String, java.io.Serializable> namedParameters
                    = new HashMap<>();
            namedParameters.put("id", id);
            namedParameters.put("name", entity.getName());
            namedParameters.put("phone", entity.getPhone());
            namedParameters.put("country_id", entity.getCountry() != null
                    ? entity.getCountry().getId()
                    : null);
            namedParameters.put("stars", entity.getStars());
            return namedParameterJdbcTemplate
                    .update(SQL_FOR_ADD_HOTEL, namedParameters) == NORMAL_ROW_AFFECTED;
        } catch (Exception e){
            LOG.warn("Program warn: " + e);
            return false;
        }
    }

    @Override
    public Optional<List> list() {
        try{
            return Optional.ofNullable(namedParameterJdbcTemplate
                    .query(SQL_FOR_GET_LIST_HOTEL, standardRowMapperCountry));
        } catch (DataAccessException e){
            LOG.error("Data access exception: " + e);
            throw new RuntimeException();
        }
    }

    @Override
    public Optional<Hotel> get(String id) {
        try{
            Map<String, String> namedParameters = new HashMap<>();
            namedParameters.put("id", id);
            Hotel result = (Hotel) namedParameterJdbcTemplate
                    .query(SQL_FOR_GET_HOTEL, namedParameters, standardRowMapperCountry)
                    .get(0);
            return Optional.ofNullable(result);
        } catch (Exception e){
            LOG.warn("Program warn: " + e);
            return Optional.empty();
        }
    }

    @Override
    public boolean update(Hotel entity) {
        try {
            Map<String, java.io.Serializable> namedParameters
                    = new HashMap<>();
            namedParameters.put("id", entity.getId());
            namedParameters.put("name", entity.getName());
            namedParameters.put("phone", entity.getPhone());
            namedParameters.put("country_id", entity.getCountry().getId());
            namedParameters.put("stars", entity.getStars());
            return namedParameterJdbcTemplate
                    .update(SQL_FOR_UPDATE_HOTEL, namedParameters)
                    == NORMAL_ROW_AFFECTED;
        } catch (Exception e) {
            LOG.warn("Program warn: " + e);
            return false;
        }
    }

    @Override
    public boolean remove(String id) {
        return removeFromJdbc(id, namedParameterJdbcTemplate,
                SQL_FOR_DELETE_HOTEL, NORMAL_ROW_AFFECTED, LOG);
    }

    @Override
    public Optional<List<Hotel>> executeQuery(Specification specification) {
        try{
            return Optional.ofNullable(specification.execute());
        } catch (Exception e){
            LOG.warn("Program warn: " + e);
            return Optional.empty();
        }
    }
}
