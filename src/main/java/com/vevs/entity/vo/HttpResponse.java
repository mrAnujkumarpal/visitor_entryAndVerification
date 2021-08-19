package com.vevs.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vevs.entity.common.VMSEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpResponse<T> implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = 7702134516418120340L;

    private String httpStatus = VMSEnum.HTTP_RESPONSE.SUCCESS.name();

    private Integer httpCode = HttpStatus.OK.value();

    private String responseMessage; //success message or failure message

    private T responseObject;

    private String error;

    public HttpResponse(String success, String message) {
        this.httpStatus = success;
        this.responseMessage = message;
    }

    public static HttpResponse errorResponse(Object validationMsg){
        HttpResponse errorResponse = new HttpResponse<>();
        errorResponse.setHttpStatus(VMSEnum.HTTP_RESPONSE.FAILURE.name());
        errorResponse.setHttpCode(HttpStatus.EXPECTATION_FAILED.value());
        errorResponse.setResponseObject(validationMsg);
        return  errorResponse;
    }

    public static HttpResponse loginErrorResponse(Object validationMsg){
        HttpResponse errorResponse = new HttpResponse<>();
        errorResponse.setHttpStatus(VMSEnum.HTTP_RESPONSE.FAILURE.name());
        errorResponse.setHttpCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setResponseObject(validationMsg);
        return  errorResponse;
    }

}
