package de.telran.todojdbc.repository;


import de.telran.todojdbc.model.ToDo;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Repository
public class ToDoRepository implements CommonRepository<ToDo> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ToDoRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<ToDo> toDoRowMapper = (ResultSet rs, int rowNum) -> {
        ToDo toDo = new ToDo();
        toDo.setId(rs.getString("id"));
        toDo.setDescription(rs.getString("description"));
        toDo.setModified(rs.getTimestamp("modified").toLocalDateTime());    //.toLocalDateTime -> transformed to DateTime
        toDo.setCreated(rs.getTimestamp("created").toLocalDateTime());
        toDo.setCompleted(rs.getBoolean("completed"));
        return toDo;
    };

    //add or update
    @Override
    public ToDo save(ToDo entity) {
        ToDo result = findById(entity.getId());
        if (result != null) {
            result.setModified(LocalDateTime.now());
            result.setDescription(entity.getDescription());
            result.setCompleted(entity.isCompleted());
            String sqlUpdate = "UPDATE todo SET description = :description, modified = :modified, " +
                    "created = :created, completed = :completed WHERE id = :id";
            return upsert(result, sqlUpdate);
        }
        String sqlInsert = "INSERT INTO todo (id, description, modified, created, completed)" +
                "VALUES (:id, :description, :modified, :created, :completed)";
        return upsert(entity, sqlInsert);
    }

    private ToDo upsert(ToDo toDo, String sql) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", toDo.getId());
        namedParameters.put("description", toDo.getDescription());
        namedParameters.put("created", Timestamp.valueOf(toDo.getCreated()));
        namedParameters.put("modified", Timestamp.valueOf(toDo.getModified()));
        namedParameters.put("completed", toDo.isCompleted());
        this.jdbcTemplate.update(sql, namedParameters);
        return findById(toDo.getId());
    }

    @Override
    public Iterable<ToDo> saveAll(Collection<ToDo> entities) {
        entities.forEach(this::save);
        return findAll();
    }

    @Override
    public void delete(ToDo entity) {
        Map<String, String> namedParameters = Collections.singletonMap("id", entity.getId());
        this.jdbcTemplate.update("DELETE FROM todo WHERE id = :id", namedParameters);
    }

    @Override
    public ToDo findById(String id) {
        try {
            Map<String, String> namedParameters = Collections.singletonMap("id", id);   //select* from DB where id= ...
            return this.jdbcTemplate.queryForObject("SELECT * FROM todo WHERE id = :id", namedParameters, toDoRowMapper);//:id - parameter
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public Iterable<ToDo> findAll() {
        return this.jdbcTemplate.query("SELECT * FROM todo", toDoRowMapper);
    }

}
