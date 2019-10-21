package com.homework.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.homework.model.ajax.JResponseBody;
import com.homework.model.entities.EPerson;
import com.homework.model.view.PersonSalary;
import com.homework.services.PersonService;

@RestController
@RequestMapping("/api/v1/person/")
public class ApiPersonController extends BaseController  {

	@Autowired
	PersonService personService;
	
	/**
	 * Список всех сотрудников
	 * */
	@RequestMapping(value = "all", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
	public  @ResponseBody JResponseBody<List<EPerson>> getAll() {	
		
		JResponseBody<List<EPerson>> b = new JResponseBody<List<EPerson>>();
		
		b.setResult(personService.findAll());
		
		return b;		
	}
	
	/**
	 * Создание нового сотрудника
	 * */
	@RequestMapping(value = "new", method = RequestMethod.POST, produces={"application/json; charset=UTF-8"})
	public @ResponseBody JResponseBody<EPerson> setNew(@RequestBody EPerson newPerson) {
		
		JResponseBody<EPerson> b = new JResponseBody<EPerson>();
		
		if (personService.findByName(newPerson.getName()) != null) {
			b.setError("Пользователь с таким именем уже существует");
			return b;
		}
		
		b.setResult(personService.insert(newPerson));
		
		return b;
	}
	
	/**
	 * Обновление оклада
	 * */
	@RequestMapping(value = "update", method = RequestMethod.PUT, produces={"application/json; charset=UTF-8"})
	public @ResponseBody JResponseBody<EPerson> update(@RequestBody PersonSalary person) {
		
		JResponseBody<EPerson> b = new JResponseBody<EPerson>();
		
		b.setResult(personService.changePersonSalary(person));
		
		return b;
	}
	
	/**
	 * Удаление все сотрудников
	 * необходима авторизация
	 * */
	@RequestMapping(value = "deleteall", method = RequestMethod.DELETE, produces={"application/json; charset=UTF-8"})
	public @ResponseBody JResponseBody<?> deleteAll() {
		
		personService.deleteAll();
		
		return new JResponseBody<Object>();
	}
	
}
