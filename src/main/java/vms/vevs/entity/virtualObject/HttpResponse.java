package vms.vevs.entity.virtualObject;

import lombok.Data;
import vms.vevs.entity.common.VMSEnum;

@Data
public class HttpResponse<T>{

    private String httpCode = "200";

    private String httpStatus = VMSEnum.HTTP_RESPONSE.SUCCESS.name();

    private String responseMessage; //success message or failure message

    private T responseObject;

    private String error;


    public static HttpResponse errorResponse(Object validationMsg){
        HttpResponse errorResponse = new HttpResponse<>();
        errorResponse.setHttpStatus(VMSEnum.HTTP_RESPONSE.FAILURE.name());
        errorResponse.setResponseObject(validationMsg);
        errorResponse.setHttpCode("3000");

        return  errorResponse;
    }

}
