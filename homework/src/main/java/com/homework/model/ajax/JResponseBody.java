package com.homework.model.ajax;


import com.fasterxml.jackson.annotation.JsonView;
import com.homework.model.enums.T_REQ_STATUS;
import com.homework.model.view.Views;


/**
 * Обертка для ответа api
 * */
public class JResponseBody<T> {
	
	@JsonView(Views.Public.class)
	T_REQ_STATUS status = T_REQ_STATUS.SUCCESS;
	
	@JsonView(Views.Public.class)
	String error = "";
	
	@JsonView(Views.Public.class)
	String message = "";	
	
	@JsonView(Views.Public.class)
	T result;	
	
	public JResponseBody() {		
	}

	public JResponseBody(Class<T> clazz) {
		try {
			result = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {			
			e.printStackTrace();
		}
	}

	public T_REQ_STATUS getStatus() {
		return status;
	}

	public void setStatus(T_REQ_STATUS status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setStateError() {
		this.status = T_REQ_STATUS.ERROR;
	}
	
	public void setStateError(String error) {
		setStateError();
		setError(error);
	}
	
	public void setStateError(String error, String msg) {
		setStateError();
		setError(error);
		setMessage(msg);
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}	
	
}
