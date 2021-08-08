package vms.vevs.entity.employee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import vms.vevs.entity.common.BaseEntity;
import vms.vevs.entity.common.Location;
import vms.vevs.entity.common.Role;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Users extends BaseEntity {

    private String username;
    private String mobileNo;

    private String email;

    @JsonIgnore
    private String password;

    private String employeeCode;

    private String designation;


    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private Location baseLocation;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private Location currentLocation;





    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();



}
