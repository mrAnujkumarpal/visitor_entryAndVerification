package vms.vevs.entity.visitor;

import lombok.Getter;
import lombok.Setter;
import vms.vevs.entity.employee.Employee;
import vms.vevs.entity.common.BaseEntity;
import vms.vevs.entity.common.Location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;
import java.io.Serializable;

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
    private Employee hostEmployee;


    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Location location;

}
