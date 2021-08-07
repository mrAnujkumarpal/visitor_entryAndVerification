package vms.vevs.entity.bulk;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class BulkLocationRecords {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;


    private String locationName;
    private String contactNumber;
    private String address;
    private String country;
    private Long uploadedFileId;

}
