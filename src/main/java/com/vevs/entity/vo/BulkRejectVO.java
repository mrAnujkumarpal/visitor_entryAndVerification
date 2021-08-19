package com.vevs.entity.vo;

import lombok.Data;

@Data
public class BulkRejectVO {


    String moduleName;
    Long uploadedFileId;
    String rejectReason;
}
