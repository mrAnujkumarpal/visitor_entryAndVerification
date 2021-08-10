package vms.vevs.entity.bulk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class BulkUserRecords {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String userName;
    private String designation;
    private String mobileNumber;
    private String employeeCode;
    private String email;
    private String role;


    private Long baseLocationId;


    private Long uploadedFileId;
}
