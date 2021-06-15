package vms.vevs.entity.employee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import vms.vevs.entity.common.BaseEntity;
import vms.vevs.entity.common.Location;

import javax.persistence.*;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Employee extends BaseEntity {

    private String employeeCode;
    private String mobileNumber;
    private String designation;
    private String emailId;
    private String employeeImage;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private Location baseLocation;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private Location currentLocation;


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeCode='" + employeeCode + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", designation='" + designation + '\'' +
                ", emailId='" + emailId + '\'' +
                ", employeeImage='" + employeeImage + '\'' +
                ", baseLocation=" + baseLocation +
                ", currentLocation=" + currentLocation +
                '}';
    }
}
