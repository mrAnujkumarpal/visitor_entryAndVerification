package com.vevs.entity.virtualObject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ReportRequestVO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    Timestamp fromDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    Timestamp toDate;

  // @ApiModelProperty(required = true,example = "2016-01-01")
  // @JsonFormat(pattern = DATE_FORMAT)
  Long hostEmployeeId;
}
