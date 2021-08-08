package vms.vevs.entity.virtualObject;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VisitorVO {


    private String visitorName;
    private String visitorEmail;
    private String mobileNumber;
    private String visitorAddress;
    private String purposeOfVisit; //PURPOSE_OF_VISIT
    private Long hostEmployeeId;

    private Long locationId;
    private String visitorOTP;





}
