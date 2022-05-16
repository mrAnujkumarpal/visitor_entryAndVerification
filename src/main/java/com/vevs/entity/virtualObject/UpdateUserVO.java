package com.vevs.entity.virtualObject;

import lombok.Data;

@Data
public class UpdateUserVO {
    private Long id ;
    private String name;
    private String mobileNo;
    private boolean enable;

    private String employeeCode;

    private String designation;

    private Long baseLocationId;

    private Long currentLocationId;


}

