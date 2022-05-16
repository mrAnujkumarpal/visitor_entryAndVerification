package com.vevs.entity.common;


public class VMSEnum {

    public enum VISITOR_STATUS {
        CHECK_IN, CHECK_OUT
    }

    public enum PURPOSE_OF_VISIT {
        MEETING, INTERVIEW, VENDOR, AUDIT
    }

    public enum HTTP_RESPONSE {
        SUCCESS, FAILURE
    }

    public enum NOTIFICATION_TYPE {
        EMAIL, SMS
    }

    public  enum MODULE_NAME{
        LOCATION,USER
    }
    public  enum BULK_UPLOAD_STATUS{
        PENDING,VALIDATE,REJECT,SUBMITTED
    }
}

