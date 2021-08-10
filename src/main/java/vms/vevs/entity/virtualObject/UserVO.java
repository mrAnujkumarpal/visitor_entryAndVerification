package vms.vevs.entity.virtualObject;

import lombok.Data;
import vms.vevs.entity.common.Location;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Data
public class UserVO {

    private String name;
    private String username;
    private String mobileNo;

    private String role;
    private String email;
    private String password;

    private String employeeCode;

    private String designation;

    private Long baseLocationId;

    private Long currentLocationId;
}
