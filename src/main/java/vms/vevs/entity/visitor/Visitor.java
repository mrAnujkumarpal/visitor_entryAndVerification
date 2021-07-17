package vms.vevs.entity.visitor;

import lombok.Getter;
import lombok.Setter;
import vms.vevs.entity.common.BaseEntity;
import vms.vevs.entity.common.Location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import vms.vevs.entity.employee.Users;

import javax.persistence.*;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Visitor extends BaseEntity   {

    private String visitorCode;
    private String mobileNumber;
    private String visitorEmail;
    private String purposeOfVisit; //PURPOSE_OF_VISIT
    private String visitorAddress;
    private String visitorStatus; //VISITOR_STATUS
    private String cardNoGivenToVisitor;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Users hostEmployee;


    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Location location;

}
