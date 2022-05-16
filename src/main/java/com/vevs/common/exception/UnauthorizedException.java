package com.vevs.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.vevs.entity.virtualObject.HttpResponse;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private HttpResponse apiResponse;

	private String message;

	public UnauthorizedException(HttpResponse apiResponse) {
		super();
		this.apiResponse = apiResponse;
	}

	public UnauthorizedException(String message) {
		super(message);
		this.message = message;
	}

	public UnauthorizedException(String message, Throwable cause) {
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
