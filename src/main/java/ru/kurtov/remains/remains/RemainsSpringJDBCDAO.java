package ru.kurtov.remains.remains;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class RemainsSpringJDBCDAO implements RemainsDAO{
  
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public RemainsSpringJDBCDAO(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("remains")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public void insert(final Remains remains) {
        if (remains.getId() != null) {
            throw new IllegalArgumentException("can not insert " + remains + " with already assigned id");
        }

        final Map<String, Object> params = new HashMap<>();
        params.put("goods_name", remains.getGoodsName());
        params.put("value", remains.getValue());

        final int userId = simpleJdbcInsert.executeAndReturnKey(params).intValue();

        remains.setId(userId);
    }


    @Override
    public Optional<Remains> get(final int remainsId) {

        final String query = "SELECT id, goods_name, value FROM remains WHERE id = :id";

        final Map<String, Object> params = new HashMap<>();
        params.put("id", remainsId);

        final Remains remains;
        try {
            remains = namedParameterJdbcTemplate.queryForObject(query, params, rowToRemains);
        } catch (final EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
        return Optional.of(remains);
    }

    @Override
    public Set<Remains> getAll() {

        final String query = "SELECT id, goods_name, value FROM remains";

        return new HashSet<>(jdbcTemplate.query(query, rowToRemains));
    }

    @Override
    public void update(final Remains remains) {

        if (remains.getId() == null) {
            throw new IllegalArgumentException("can not update " + remains + " without id");
        }

        final String query = "UPDATE remains SET goods_name = :goods_name, value = :value WHERE id = :id";

        final Map<String, Object> params = new HashMap<>();
        params.put("goods_name", remains.getGoodsName());
        params.put("value", remains.getValue());
        params.put("id", remains.getId());

        namedParameterJdbcTemplate.update(query, params);
    }

    @Override
    public void delete(final int remainsId) {

        final String query = "DELETE FROM remains WHERE id = :id";

        final Map<String, Object> params = new HashMap<>();
        params.put("id", remainsId);

        namedParameterJdbcTemplate.update(query, params);
    }

    private static final RowMapper<Remains> rowToRemains = (resultSet, rowNum) ->
        Remains.existing(
            resultSet.getInt("id"),
            resultSet.getString("goods_name"),
            resultSet.getInt("value")
    );
}