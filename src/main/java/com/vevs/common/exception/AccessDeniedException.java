package com.vevs.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.vevs.entity.vo.HttpResponse;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class AccessDeniedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private HttpResponse apiResponse;

	private String message;

	public AccessDeniedException(HttpResponse apiResponse) {
		super();
		this.apiResponse = apiResponse;
	}

	public AccessDeniedException(String message) {
		super(message);
		this.message = message;
	}

	public AccessDeniedException(String message, Throwable cause) {
		super(message, cause);
	}

	public HttpResponse getApiResponse() {
		return apiResponse;
	}

	public void setApiResponse(HttpResponse apiResponse) {
		this.apiResponse = apiResponse;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
