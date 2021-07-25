package vms.vevs.entity.virtualObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import vms.vevs.entity.common.VMSEnum;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpResponse<T> implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = 7702134516418120340L;

    private String httpStatus = VMSEnum.HTTP_RESPONSE.SUCCESS.name();

    private String httpCode = HttpStatus.OK.name();

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
        errorResponse.setHttpCode(HttpStatus.FAILED_DEPENDENCY.name());
        errorResponse.setResponseObject(validationMsg);
        return  errorResponse;
    }

}
