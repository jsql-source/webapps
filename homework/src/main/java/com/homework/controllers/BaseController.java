package com.homework.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;


public abstract class BaseController {
	
	@Autowired
  	public ApplicationContext appContext;
	
	
	/**
	 * Поиск Bean'a по его типу
	 * @param requiredType тип Bean'a
	 * @param args параметры конструктора
	 */
	protected <T> T getBean(Class<T> requiredType, Object... args) {		
		return appContext.getBean(requiredType, args);
	}
}
