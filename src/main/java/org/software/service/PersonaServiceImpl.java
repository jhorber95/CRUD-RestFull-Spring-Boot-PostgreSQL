package org.software.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.software.model.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;


@Service("personaService")
public class PersonaServiceImpl implements PersonaService{
	
	@Autowired
    JdbcTemplate jdbcTemplate; 
	
	
	private final String INSERT_SQL = "INSERT INTO persona(per_nombre, per_apellido) values(?,?)";
	private final String UPDATE_SQL = "UPDATE persona set per_nombre=?, per_apellido=? where per_id=?";
	private final String DELETE_SQL = "DELETE FROM persona where per_id=?";
	private final String FETCH_SQL = "select per_id, per_nombre, per_apellido from persona";
	private final String FETCH_SQL_BY_ID = "select * from persona where per_id = ?";
	
	
	public Persona findById(long id) {
		return (Persona) jdbcTemplate.queryForObject(FETCH_SQL_BY_ID, new Object[] { id }, new PersonaMapper());
	}
	
	
	@Override
	public Persona findByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public Persona createPersona(final Persona persona) {
		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, persona.getNombre());
				ps.setString(2, persona.getApellido());
				return ps;
			}
		}, holder);

		int newPersonaId = holder.getKey().intValue();
		//System.out.println("newPersonaId: " + newPersonaId);
		System.out.println("{\"mensaje\" : \"Ok\"}");
		persona.setId(newPersonaId);
		return persona;
	}
	
	
	@Override
	public Persona updatePersona(Persona persona) {
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(UPDATE_SQL);
				ps.setString(1, persona.getNombre());
				ps.setString(2, persona.getApellido());
				ps.setLong(3, persona.getId());
				return ps;
			}
		});
		
		return persona;
	}
		
	
	@Override
	public void deletePersonaById(long id) {
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(DELETE_SQL);
				ps.setLong(1, id);
				return ps;
			}
		});
		
	}

	
	@Override
	public List<Persona> findAllPersonas() {
		return jdbcTemplate.query(FETCH_SQL, new PersonaMapper());
	}

	
	@Override
	public boolean isPersonaExist(Persona persona) {
		// TODO Auto-generated method stub
		return false;
	}

}

class PersonaMapper implements RowMapper {

	public Persona mapRow(ResultSet rs, int rowNum) throws SQLException {
		Persona persona = new Persona();
		persona.setId(rs.getInt("per_id"));
		persona.setNombre(rs.getString("per_nombre"));
		persona.setApellido(rs.getString("per_apellido"));
		return persona;
	}

}
