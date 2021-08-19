package vms.vevs.entity.common;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Location  extends BaseEntity {



    private String locationContactNo;
    private String locationAddress;
    private String country;
}
