package com.vevs.service.impl.bulk;

import com.vevs.entity.bulk.BulkLocationRecords;
import com.vevs.entity.bulk.BulkUploadRecordsFile;
import com.vevs.entity.bulk.BulkUserRecords;
import com.vevs.entity.common.VMSEnum;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulkHelper {


    protected List<BulkLocationRecords> locationRecordFromFile(List<String[]> fileDataList, String[] headerColumns) throws Exception {
        List<BulkLocationRecords> bulkResponseList = new ArrayList<>();
        for (String[] fileData : fileDataList) {
            bulkResponseList.add(locationResponse(fileData, headerColumns));
        }
        return bulkResponseList;
    }

    protected List<BulkUserRecords> userRecordFromFile(List<String[]> fileDataList, String[] headerColumns) throws Exception {
        List<BulkUserRecords> dataList = new ArrayList<>();
        for (String[] fileData : fileDataList) {
            dataList.add(userResponse(fileData, headerColumns));
        }
        return dataList;
    }

    private BulkUserRecords userResponse(String[] values, String[] headerColumns) throws Exception {
        BulkUserRecords userResponse = new BulkUserRecords();

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("NAME", "name");
        headerMap.put("USERNAME", "username");
        headerMap.put("DESIGNATION", "designation");
        headerMap.put("MOBILENUMBER", "mobilenumber");
        headerMap.put("EMPLOYEECODE", "employeecode");
        headerMap.put("EMAIL", "email");
        headerMap.put("BASELOCATIONID", "baselocationid");


        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            String header = headerColumns[i];

            Field field = userResponse.getClass().getDeclaredField(header);
            if (field != null) {
                field.setAccessible(true);
                String type = field.getType().getSimpleName();
                try {
                    if (type.equalsIgnoreCase("String")) {
                        field.set(userResponse, value);
                    } else if (type.equalsIgnoreCase("Timestamp")) {
                        Timestamp tsValue = Timestamp.valueOf(value);
                        field.set(userResponse, tsValue);
                    } else if (type.equalsIgnoreCase("BigDecimal")) {
                        BigDecimal decimalVal = new BigDecimal(value);
                        field.set(userResponse, decimalVal);
                    }else if (type.equalsIgnoreCase("Long")) {
                        Long longVal = new Long(value);
                        field.set(userResponse, longVal);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return userResponse;

    }

    private BulkLocationRecords locationResponse(String[] values, String[] headers) throws Exception {

        BulkLocationRecords locationResponse = new BulkLocationRecords();

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("LOCATIONNAME", "locationName");
        headerMap.put("CONTACTNUMBER", "contactNumber");
        headerMap.put("ADDRESS", "address");
        headerMap.put("COUNTRY", "country");

        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            String header = headers[i];
            String variableName = headerMap.get(header);

            Field field = locationResponse.getClass().getDeclaredField(header);
            if (field != null) {
                field.setAccessible(true);
                String type = field.getType().getSimpleName();
                try {
                    if (type.equalsIgnoreCase("String")) {
                        field.set(locationResponse, value);
                    } else if (type.equalsIgnoreCase("Timestamp")) {
                        Timestamp tsValue = Timestamp.valueOf(value);
                        field.set(locationResponse, tsValue);
                    } else if (type.equalsIgnoreCase("BigDecimal")) {
                        BigDecimal decimalVal = new BigDecimal(value);
                        field.set(locationResponse, decimalVal);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return locationResponse;
    }

    public static <K, V> K getKey(Map<String, String> map, int value, String headerColumn) {

        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(headerColumn)) {
                System.out.println(value + " " + entry.getValue());
                return (K) entry.getKey();
            }
        }
        return null;
    }


    protected BulkUploadRecordsFile markAsModified(BulkUploadRecordsFile record, Long loggedInUserId, Timestamp currentTime) {
        record.setModifiedOn(currentTime);
        record.setModifiedBy(loggedInUserId);
        return record;
    }

    protected BulkUploadRecordsFile markAsSubmittedFileRecord(BulkUploadRecordsFile record, Long loggedInUserId, Timestamp currentTime) {
        record.setSubmitted(true);
        record.setSubmittedOn(currentTime);
        record.setSubmittedBy(loggedInUserId);
        record.setStatus(VMSEnum.BULK_UPLOAD_STATUS.SUBMITTED.name());
        return record;
    }

    protected BulkUploadRecordsFile markAsValidateFileRecordSuccess(BulkUploadRecordsFile record, Long loggedInUserId, Timestamp currentTime) {
        record.setValidate(true);
        record.setValidateOn(currentTime);
        record.setValidateBy(loggedInUserId);
        record.setStatus(VMSEnum.BULK_UPLOAD_STATUS.VALIDATE.name());
        return record;
    }
    protected BulkUploadRecordsFile markAsValidateFileRecordFailed( List<String> issuesList,BulkUploadRecordsFile record, Long userId, Timestamp now) {

        String issues = issuesList.stream().map(e -> e).reduce(" ", String::concat);

        record.setValidate(false);
        record.setValidationIssues(issues);
        record.setValidateOn(now);
        record.setValidateBy(userId);
        record.setValidationIssueFound(true);
        record.setStatus(VMSEnum.BULK_UPLOAD_STATUS.VALIDATE.name());
        return record;

    }
    protected BulkUploadRecordsFile markAsRejectFileRecord(BulkUploadRecordsFile record, Long loggedInUserId, Timestamp currentTime) {
        record.setReject(true);
        record.setRejectedOn(currentTime);
        record.setRejectBy(loggedInUserId);
        record.setStatus(VMSEnum.BULK_UPLOAD_STATUS.REJECT.name());
        return record;
    }
}
