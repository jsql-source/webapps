package com.homework.config;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

/** 
 * Класс обертка для работы с параметризованными нативными запросами и процедурами
 * Включает: SimpleJdbcCall, NamedParameterJdbcTemplate, HibernateNativeQuery
 * 
 * */
public class DBWrapper {
			
	protected EntityManager entityManager;
	
	private DataSource dataSource;
	
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	public class SimpleProcCall extends SimpleJdbcCall {

		public SimpleProcCall(DataSource dataSource) {
			super(dataSource);			
		}
		
		@Override
		public Map<String, Object> execute(Object... args) {
			return super.execute(args);
		}

		@Override
		public Map<String, Object> execute(Map<String, ?> args) {
			return super.execute(args);
		}

		@Override
		public Map<String, Object> execute(SqlParameterSource parameterSource) {
			return super.execute(parameterSource);
		}
	}
	
	public DBWrapper() {
	}
	
	public DBWrapper(DataSource ds) {
		dataSource = ds;		
		namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);		
	}
	
	public MapSqlParameterSource newSqlParameters() {		
		return new MapSqlParameterSource();
	}
	
	public MapSqlParameterSource newSqlParameters(String paramName, Object value) {
		return new MapSqlParameterSource(paramName, value);
	}
	
	/**
	 * Возвращает новый обернутый SimpleJdbcCall
	 * */	
	public SimpleProcCall newSimpleProcCall() {		
		return new SimpleProcCall(dataSource);
	}
	
	/**
	 * Ожидается, что запрос будет запросом одной строки; строка результата будет сопоставлена с Map 
	 * (одна запись для каждого столбца, используя имя столбца в качестве ключа).
	 * */
	public Map<String, Object> queryMap(String sql) {
		
		return queryMap(sql, newSqlParameters());
	}
	
	/**
	 * Ожидается, что запрос будет запросом одной строки; строка результата будет сопоставлена с Map 
	 * (одна запись для каждого столбца, используя имя столбца в качестве ключа).
	 * */
	public Map<String, Object> queryMap(String sql, MapSqlParameterSource param) {
		
		return namedJdbcTemplate.queryForMap(sql, param);
	}
	
	/**
	 * Ожидается, что запрос будет возвращать одну или несколько строк; 
	 * колонки результата будет сопоставлена с параметрами ключ и значение	  
	 * */
	public Map<Object, Object> queryKeyValue(String sql, String keyColumn, String valueColumn) {
		
		List<Map<String, Object>> res = queryList(sql);
		
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		for (Map<String, Object> row : res)
			result.put(getInt(row.get(keyColumn)), getString(row.get(valueColumn)));
		
		return result;
		
	}
	
	/**
	 * Результаты будут сопоставлены с List (одна запись для каждой строки) Map 
	 * (одна запись для каждого столбца, используя имя столбца в качестве ключа). 	 
	 * */
	public List<Map<String, Object>> queryList(String sql) {
		
		return queryList(sql, newSqlParameters());
	}
	
	/**
	 * Результаты будут сопоставлены с List (одна запись для каждой строки) Map 
	 * (одна запись для каждого столбца, используя имя столбца в качестве ключа). 	 
	 * */
	public List<Map<String, Object>> queryList(String sql, MapSqlParameterSource param) {
		
		return namedJdbcTemplate.queryForList(sql, param);
	}
	
	/**
	 * Cопоставление каждой строки с объектом Java через RowMapper. 
	 * Название колонки в запросе соответствует названию поля в классе
	 */
	public <T> List<T> queryList(String sql, MapSqlParameterSource param, Class<T> requiredType) {
			
		return namedJdbcTemplate.query(sql, param, new BeanPropertyRowMapper<>(requiredType));
	}
	
	/**
	 * Сопоставление одной строки результата с объектом Java через RowMapper.
	 * Integer к = DBWrapper.queryForObject(sql, param, Integer.class);
	 * */
	public <T> T queryObject(String sql, MapSqlParameterSource param, Class<T> requiredType) {		
		
		return namedJdbcTemplate.queryForObject(sql, param, new SingleColumnRowMapper<T>(requiredType));
	}	
	
	/**
	 * Нативный запрос через Hibernate
	 * */
	private <T> Query getHibernateNativeQuery(String sql, MapSqlParameterSource param, Class<T> requiredType) {
		
		Query q = entityManager.createNativeQuery(sql, requiredType);		
		
		for(Map.Entry<String, Object> entry : param.getValues().entrySet()) {
		    q.setParameter(entry.getKey(), entry.getValue());
		}
		
		return q;
	}
	
	/**
	 * Запрос через Hibernate c JPA
	 * */
	private <T> TypedQuery<T> getHibernateJPAQuery(String sql, MapSqlParameterSource param, Class<T> requiredType) {
		
		TypedQuery<T> q = entityManager.createQuery(sql, requiredType);		
		
		for(Map.Entry<String, Object> entry : param.getValues().entrySet()) {
		    q.setParameter(entry.getKey(), entry.getValue());
		}

		return q;
	}
	
	/**
	 * Cопоставление каждой строки с объектом Entity через RowMapper. Название колонки в запросе соответствует 
	 * названию колонки в аннотации @Сolumn .
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> queryEntityList(String sql, MapSqlParameterSource param, Class<T> requiredType) {
		
		Query q = getHibernateNativeQuery(sql, param, requiredType);		
		return (List<T>)q.getResultList();		
	}

	/**
	 * Сопоставление одной строки результата с объектом Entity через RowMapper.
	 * Название колонки в запросе соответствует названию колонки в аннотации @Сolumn.
	 * */
	@SuppressWarnings("unchecked")
	public <T> T queryEntityObject(String sql, MapSqlParameterSource param, Class<T> requiredType) {		
		
		Query q = getHibernateNativeQuery(sql, param, requiredType);		  
		return (T) q.getSingleResult();
	}	
	
	/**
	 * JPA запрос для массива
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> queryJPAEntityList(String sql, MapSqlParameterSource param, Class<T> requiredType) {
		
		Query q = getHibernateJPAQuery(sql, param, requiredType);		
		return (List<T>)q.getResultList();		
	}

	/**
	 * JPA запрос для одной строки
	 * */
	@SuppressWarnings("unchecked")
	public <T> T queryJPAEntityObject(String sql, MapSqlParameterSource param, Class<T> requiredType) {		
		
		Query q = getHibernateJPAQuery(sql, param, requiredType);		  
		return (T) q.getSingleResult();
	}
	
	
	/**
	 * Выполняет sql, insert/update/delete
	 * */
	public int execute(String sql) {		
		
		return execute(sql, newSqlParameters());
	}
	
	/**
	 * Выполняет sql, insert/update/delete
	 * */
	public int execute(String sql, MapSqlParameterSource param) {		
		
		return namedJdbcTemplate.update(sql, param);
	}
		
	
	private int rowCount(String sql, MapSqlParameterSource param){
		
		String countQuery = String.format("select count(*) from (%s) cnt", sql);
		
		return queryObject(countQuery, param, Integer.class);
	}
	
	private String paginateQuery(Pageable pg, String sql) {		
		
		String query = String.format("select * from (%s) subq", sql);
		
		if (pg.getSort() != null) {
			for(Order o : pg.getSort()) {
				query += " ORDER BY " + o.getProperty() + " " + o.getDirection().toString() + " ";
			}
		}
		
		query += " OFFSET " + pg.getPageNumber() * pg.getPageSize() + " LIMIT " + pg.getPageSize() + " ";
		
		return query;
		
	}
	
	
	/**
	 * Преобразует объект в Integer
	 * */
	public Integer getInt(Object obj) {
		
		Class<? extends Object> cls = obj.getClass();
		
		if (cls == BigDecimal.class) {
			
			return ((BigDecimal)obj).intValue();
		
		} else if (cls == Long.class) {
			
			return ((Long)obj).intValue();
			
		} else if (cls == Integer.class) {
			
			return (Integer)obj;
			
		} else if (cls == String.class) {
			
			return Integer.parseInt((String)obj);			
		}		
		
		return (Integer)obj;
	}
	
	/**
	 * Преобразует объект в String
	 * */
	public String getString(Object obj) {
		
		return (String)obj;
	}
	
	/**
	 * Преобразует объект в java.util.Date
	 * */
	public java.util.Date getDate(Object obj) {
		
		return (java.util.Date)obj;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

}
