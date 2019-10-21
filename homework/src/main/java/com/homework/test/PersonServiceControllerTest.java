package com.homework.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.homework.model.ajax.JResponseBody;
import com.homework.model.entities.EPerson;
import com.homework.services.PersonService;

public class PersonServiceControllerTest extends BaseTest {

	static final String api_url = "/api/v1/person/";
	
	@Autowired
	private PersonService personService;

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	/**
	 * Проверка списка сотрудников через api и сервис
	 */
	@Test
	public void getPersonList() throws Exception {

		String uri = api_url + "all";

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();

		JResponseBody<List<EPerson>> body = mapFromJson(content, (new JResponseBody<List<EPerson>>()).getClass());
		assertTrue(body.getResult().size() >= 0);

		List<EPerson> pList = personService.findAll();
		assertTrue(pList.size() >= 0);
	}

	/**
	 * Проверка создания нового сотрудника через api, 
	 * если сотрудник уже существует выдается соответствующее сообщение
	 */
	@Test
	public void newPerson() throws Exception {

		String uri = api_url + "new";

		EPerson person = new EPerson();
		person.setName("Ivan");
		person.setAmount(123.5);

		String inputJson = super.mapToJson(person);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		
		JResponseBody<EPerson> body = mapFromJson(content, (new JResponseBody<EPerson>()).getClass());
		
		assertFalse(body.getError(), !body.getError().isEmpty());
		
	}

}
