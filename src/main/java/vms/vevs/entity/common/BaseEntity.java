package vms.vevs.entity.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;


    private String name;

    private Long createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Timestamp createdOn;

    private Long modifiedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Timestamp modifiedOn;

    private boolean enable;
}
