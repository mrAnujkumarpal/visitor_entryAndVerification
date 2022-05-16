package com.vevs.entity.virtualObject;

import lombok.Data;

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
