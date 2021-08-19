package com.vevs.entity.visitor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vevs.entity.common.BaseEntity;
import com.vevs.entity.common.Location;
import com.vevs.entity.employee.Users;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Visitor extends BaseEntity {

    private String visitorCode;
    private String mobileNumber;
    private String visitorEmail;
    private String purposeOfVisit; //PURPOSE_OF_VISIT
    private String visitorAddress;
    private String visitorStatus; //VISITOR_STATUS
    private String cardNoGivenToVisitor;

    @Lob
    private byte[] visitorImage;


    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Users hostEmployee;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Location location;

}
