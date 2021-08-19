package vms.vevs.entity.virtualObject;

import lombok.Data;

@Data
public class BulkRejectVO {


    String moduleName;
    Long uploadedFileId;
    String rejectReason;
}
