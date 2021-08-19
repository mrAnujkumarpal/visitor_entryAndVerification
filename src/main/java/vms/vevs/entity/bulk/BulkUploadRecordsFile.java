package vms.vevs.entity.bulk;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
public class BulkUploadRecordsFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;
    String module;
    String fileName;
    int noOfRecords=0;
    String status;

    boolean validate=false;
    Long validateBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    Timestamp validateOn;
    boolean validationIssueFound=false;
    String validationIssues;


    boolean submitted=false;
    @JsonIgnore
    private Long submittedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Timestamp submittedOn;


    boolean reject=false;
    String  rejectReason;
    @JsonIgnore
    private Long rejectBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Timestamp rejectedOn;

    @JsonIgnore
    private Long createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Timestamp createdOn;

    @JsonIgnore
    private Long modifiedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Timestamp modifiedOn;
}
