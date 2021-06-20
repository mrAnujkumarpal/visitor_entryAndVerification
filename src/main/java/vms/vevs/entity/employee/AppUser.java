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
public class AppUser extends BaseEntity {

    private String userName;
    private String mobileNo;
    private String password;
    private String userImage;


    private Long locationId;


}
