package vms.vevs.entity.virtualObject;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ReportRequestVO {

    Timestamp fromDate;
    Timestamp toDate;
}
