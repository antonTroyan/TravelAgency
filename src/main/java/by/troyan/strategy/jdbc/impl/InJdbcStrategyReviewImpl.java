package by.troyan.strategy.jdbc.impl;

import by.troyan.entity.Review;
import by.troyan.entity.Tour;
import by.troyan.entity.User;
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
public class InJdbcStrategyReviewImpl implements InJdbcStrategy<Review> {

    private static final Logger LOG = LogManager.getLogger(InJdbcStrategyReviewImpl.class);

    @Autowired
    @Qualifier(value = "namedParameterJdbcTemplate")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static String SQL_FOR_ADD_REVIEW =
            "INSERT INTO public.reviews(id, tour_id, user_id, content)" +
            "VALUES (:id, :tour_id, :user_id, :content);";
    private final static String SQL_FOR_DELETE_REVIEW =
            "DELETE FROM public.reviews " +
            "WHERE public.reviews.id = (:id);";
    private final static String SQL_FOR_UPDATE_REVIEW =
            "UPDATE public.reviews " +
            "SET id=:id, tour_id=:tour_id, user_id=:user_id, content=:content " +
            "WHERE public.reviews.id = :id;";
    private final static String SQL_FOR_GET_LIST_REVIEW =
            "SELECT id, tour_id, user_id, content " +
            "FROM public.reviews;";
    private final static String SQL_FOR_GET_REVIEW =
            "SELECT id, tour_id, user_id, content " +
            "FROM public.reviews " +
            "WHERE id = :id ";
    private final static Integer NORMAL_ROW_AFFECTED = 1;

    private RowMapper standardRowMapperCountry = (resultSet, i) -> {
        Review review = new Review();
        review.setId(resultSet.getString("id"));

        Tour tour = new Tour();
        tour.setId(resultSet.getString("tour_id"));
        review.setTour(tour);

        User user = new User();
        user.setId(resultSet.getString("user_id"));
        review.setUser(user);

        review.setContent(resultSet.getString("content"));
        return review;
    };

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    @Override
    public boolean add(Review entity) {
        try{
            String id = UUID.randomUUID().toString();
            Map<String, String> namedParameters = new HashMap<String, String>();
            namedParameters.put("id", id);
            namedParameters.put("tour_id", entity.getTour() != null
                    ? entity.getTour().getId()
                    : null);
            namedParameters.put("user_id", entity.getUser() != null
                    ? entity.getUser().getId()
                    : null);
            namedParameters.put("content", entity.getContent());
            return namedParameterJdbcTemplate
                    .update(SQL_FOR_ADD_REVIEW, namedParameters)
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
                    .query(SQL_FOR_GET_LIST_REVIEW, standardRowMapperCountry));
        } catch (DataAccessException e){
            LOG.error("Data access exception: " + e);
            throw new RuntimeException();
        }
    }

    @Override
    public Optional<Review> get(String id) {
        try{
            Map<String, String> namedParameters = new HashMap<>();
            namedParameters.put("id", id);
            Review result = (Review) namedParameterJdbcTemplate
                    .query(SQL_FOR_GET_REVIEW, namedParameters, standardRowMapperCountry)
                    .get(0);
            return Optional.ofNullable(result);
        } catch (Exception e){
            LOG.warn("Program warn: " + e);
            return Optional.empty();
        }
    }

    @Override
    public boolean update(Review entity) {
        try{
            Map<String, String> namedParameters = new HashMap<>();
            namedParameters.put("id", entity.getId());
            namedParameters.put("tour_id", entity.getTour() != null
                    ? entity.getTour().getId()
                    : null);
            namedParameters.put("user_id", entity.getUser() != null
                    ? entity.getUser().getId()
                    : null);
            namedParameters.put("content", entity.getContent());
            return namedParameterJdbcTemplate
                    .update(SQL_FOR_UPDATE_REVIEW, namedParameters)
                    == NORMAL_ROW_AFFECTED;
        } catch (Exception e) {
            LOG.warn("Program warn: " + e);
            return false;
        }
    }

    @Override
    public boolean remove(String id) {
        return removeFromJdbc(id, namedParameterJdbcTemplate,
                SQL_FOR_DELETE_REVIEW, NORMAL_ROW_AFFECTED, LOG);
    }

    @Override
    public Optional<List<Review>> executeQuery(Specification specification) {
        try{
            return Optional.ofNullable(specification.execute());
        } catch (Exception e){
            LOG.warn("Program warn: " + e);
            return Optional.empty();
        }
    }
}
