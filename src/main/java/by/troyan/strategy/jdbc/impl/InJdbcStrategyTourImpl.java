package by.troyan.strategy.jdbc.impl;

import by.troyan.entity.Country;
import by.troyan.entity.Hotel;
import by.troyan.entity.Tour;
import by.troyan.entity.TourType;
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
public class InJdbcStrategyTourImpl implements InJdbcStrategy<Tour> {

    private static final Logger LOG = LogManager.getLogger(InJdbcStrategyTourImpl.class);

    @Autowired
    @Qualifier(value = "namedParameterJdbcTemplate")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static String SQL_FOR_ADD_TOUR =
            "INSERT INTO public.tours(id, photo, date, duration, country_id, " +
                    "hotel_id, tour_type_id, description, cost)" +
            "VALUES (:id, :photo, :date, :duration, :country_id, :hotel_id, " +
                    ":tour_type_id, :description, :cost);";
    private final static String SQL_FOR_DELETE_TOUR =
            "DELETE FROM public.tours " +
            "WHERE public.tours.id = (:id);";
    private final static String SQL_FOR_UPDATE_TOUR =
            "UPDATE public.tours " +
            "SET id=:id, photo=:photo, date=:date, duration=:duration, country_id=:country_id," +
                    " hotel_id=:hotel_id, tour_type_id=:tour_type_id, description =:description, cost=:cost " +
            "WHERE public.tours.id = :id;";
    private final static String SQL_FOR_GET_LIST_TOUR =
            "SELECT id, photo, date, duration, country_id, hotel_id, tour_type_id, description, cost " +
            "FROM public.tours;";
    private final static String SQL_FOR_GET_TOUR =
            "SELECT id, photo, date, duration, country_id, hotel_id, tour_type_id, description, cost " +
            "FROM public.tours " +
            "WHERE public.tours.id = :id ";
    private final static Integer NORMAL_ROW_AFFECTED = 1;

    private RowMapper standardRowMapperCountry = (resultSet, i) -> {
        Tour tour = new Tour();
        tour.setId(resultSet.getString("id"));
        tour.setPhoto((Byte[]) resultSet.getObject("photo"));
        tour.setDate((Date) resultSet.getObject("date"));
        tour.setDuration(resultSet.getInt("duration"));

        Country emptyCountry = new Country();
        emptyCountry.setId(resultSet.getString("country_id"));
        tour.setCountry(emptyCountry);

        Hotel emptyHotel = new Hotel();
        emptyHotel.setId(resultSet.getString("hotel_id"));
        tour.setHotel(emptyHotel);

        TourType emptyTourType;
        emptyTourType = (TourType) resultSet.getObject("tour_type_id");
        tour.setTourType(emptyTourType);

        tour.setDescription(resultSet.getString("description"));
        tour.setCost(resultSet.getDouble("cost"));
        return tour;
    };

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public boolean add(Tour entity) {
        try {
            String id = UUID.randomUUID().toString();
            Map<String, java.io.Serializable> namedParameters = new HashMap<String, java.io.Serializable>();
            namedParameters.put("id", id);
            namedParameters.put("photo", entity.getPhoto());
            namedParameters.put("date", entity.getDate());
            namedParameters.put("duration", entity.getDuration());
            namedParameters.put("country_id", entity.getCountry() != null
                    ? entity.getCountry().getId()
                    : null);
            namedParameters.put("hotel_id", entity.getHotel() != null
                    ? entity.getHotel().getId()
                    : null);
            namedParameters.put("tour_type_id", entity.getTourType());
            namedParameters.put("description", entity.getDescription());
            namedParameters.put("cost", entity.getCost());

            return namedParameterJdbcTemplate
                    .update(SQL_FOR_ADD_TOUR, namedParameters)
                    == NORMAL_ROW_AFFECTED;
        } catch (Exception e) {
            LOG.warn("Program warn: " + e);
            return false;
        }
    }

    @Override
    public Optional<List> list() {
        try {
            return Optional.ofNullable(namedParameterJdbcTemplate
                    .query(SQL_FOR_GET_LIST_TOUR, standardRowMapperCountry));
        } catch (DataAccessException e) {
            LOG.error("Data access exception: " + e);
            throw new RuntimeException();
        }
    }

    @Override
    public Optional<Tour> get(String id) {
        try{
            Map<String, String> namedParameters = new HashMap<>();
            namedParameters.put("id", id);
            Tour result = (Tour) namedParameterJdbcTemplate
                    .query(SQL_FOR_GET_TOUR , namedParameters, standardRowMapperCountry)
                    .get(0);
            return Optional.ofNullable(result);
        } catch (Exception e){
            LOG.warn("Program warn: " + e);
            return Optional.empty();
        }
    }

    @Override
    public boolean update(Tour entity) {
        try {
            Map<String, java.io.Serializable> namedParameters = new HashMap<>();
            namedParameters.put("id", entity.getId());
            namedParameters.put("photo", entity.getPhoto());
            namedParameters.put("date", entity.getDate());
            namedParameters.put("duration", entity.getDuration());
            namedParameters.put("country_id", entity.getCountry().getId());
            namedParameters.put("hotel_id", entity.getHotel().getId());
            namedParameters.put("tour_type_id", entity.getTourType());
            namedParameters.put("description", entity.getDescription());
            namedParameters.put("cost", entity.getCost());

            return namedParameterJdbcTemplate
                    .update(SQL_FOR_UPDATE_TOUR, namedParameters)
                    == NORMAL_ROW_AFFECTED;
        } catch (Exception e) {
            LOG.warn("Program warn: " + e);
            return false;
        }
    }

    @Override
    public boolean remove(String id) {
        return removeFromJdbc(id, namedParameterJdbcTemplate,
                SQL_FOR_DELETE_TOUR, NORMAL_ROW_AFFECTED, LOG);
    }

    @Override
    public Optional<List<Tour>> executeQuery(Specification specification) {
        try {
            return Optional.ofNullable(specification.execute());
        } catch (Exception e) {
            LOG.warn("Program warn: " + e);
            return Optional.empty();
        }
    }
}
