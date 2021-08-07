package vms.vevs.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vms.vevs.common.util.VmsUtils;
import vms.vevs.controller.validator.BulkValidator;
import vms.vevs.entity.bulk.BulkLocationRecords;
import vms.vevs.entity.bulk.BulkUploadRecordsFile;
import vms.vevs.entity.bulk.BulkUserRecords;
import vms.vevs.entity.common.VMSEnum;
import vms.vevs.repo.bulk.BulkUploadRecordsFileRepository;
import vms.vevs.service.BulkUploadService;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BulkUploadServiceImpl implements BulkUploadService {

    @Autowired
    BulkUploadRecordsFileRepository fileUploadRepository;

    @Autowired
    BulkValidator validator;

    @Override
    public BulkUploadRecordsFile uploadNewFileData(Long userId, MultipartFile file,String fileName,String module) throws Exception {

        Timestamp currentTime= VmsUtils.currentTime();


        List<String[]> fileDataList = validator.fetchFileDataRowWise(file);
        String[] headerColumns = fileDataList.remove(0);

        List<?> bulkResponseList = new ArrayList<>();
        if (StringUtils.equalsIgnoreCase(module, VMSEnum.MODULE_NAME.LOCATION.name())) {
            bulkResponseList = recordListFromFile(fileDataList, headerColumns, currentTime,module);
        } else {
            bulkResponseList = recordListFromFile(fileDataList, headerColumns, currentTime,module);
        }
        BulkUploadRecordsFile uploadRecordsFile=new  BulkUploadRecordsFile();
        uploadRecordsFile.setStatus(VMSEnum.BULK_UPLOAD_STATUS.PENDING.name());
        uploadRecordsFile.setFileName(fileName);
        uploadRecordsFile.setCreatedBy(userId);
        uploadRecordsFile.setModule(module);
        uploadRecordsFile.setCreatedOn(currentTime);
        uploadRecordsFile.setNoOfRecords(bulkResponseList.size());
        fileUploadRepository.save(uploadRecordsFile);


        return uploadRecordsFile;
    }

    private List<?> recordListFromFile(List<String[]> fileDataList, String[] headerColumns, Timestamp currentTime,String module) throws Exception {


        if (StringUtils.equalsIgnoreCase(module, VMSEnum.MODULE_NAME.LOCATION.name())) {
            List<BulkLocationRecords> bulkResponseList = new ArrayList<>();
            for (String[] fileData : fileDataList) {
                bulkResponseList.add(locationResponse(fileData, headerColumns));
            }
            return bulkResponseList;
        }else {
            List<BulkUserRecords> bulkResponseList = new ArrayList<>();
            for (String[] fileData : fileDataList) {
                bulkResponseList.add(userResponse(fileData, headerColumns));
            }
        }
    return new ArrayList<>();
    }

    private BulkUserRecords userResponse(String[] fileData, String[] headerColumns) {
        return null;
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
}
