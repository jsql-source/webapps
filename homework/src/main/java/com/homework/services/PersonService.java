package com.homework.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.homework.config.DBWrapper;
import com.homework.model.entities.EPerson;
import com.homework.model.repositories.PersonRepository;
import com.homework.model.view.PersonSalary;

@Service
public class PersonService extends BaseEntityService<EPerson, PersonRepository> {

	@Autowired
	@Qualifier("DB")
	private DBWrapper db;
	
	public EPerson findByName(String name) {
		return getRepository().findByName(name);		
	}
	
	/**
	 * Обновление оклада сотрудника
	 * пример работы с нативным запросом и mapping в объект через имена столбцов,
	 * так же это можно сделать и на основе алиасов
	 * */
	public EPerson changePersonSalary(PersonSalary pSalary) {
		
		db.newSqlParameters("p_id", pSalary.getId() );
		
		String sql = "select * from persons p join salary s on p.id = s.person_id where person_id = :p_id";		
		EPerson person = db.queryEntityObject(sql, db.newSqlParameters("p_id", pSalary.getId()), EPerson.class);
		
		person.setAmount(pSalary.getAmount());
		
		return update(person);
		
	}

}
