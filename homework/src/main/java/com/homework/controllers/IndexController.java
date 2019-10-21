package com.homework.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.homework.controllers.BaseController;

@Controller
public class IndexController extends BaseController {	

	@RequestMapping(value = {"/", "/react/**"}, method = RequestMethod.GET)
	public String index() {
		return "index";
	}
	
}