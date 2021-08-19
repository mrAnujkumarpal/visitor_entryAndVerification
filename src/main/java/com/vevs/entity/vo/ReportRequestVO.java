package com.vevs.entity.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ReportRequestVO {

    Timestamp fromDate;
    Timestamp toDate;
    Long hostEmployeeId;
}
