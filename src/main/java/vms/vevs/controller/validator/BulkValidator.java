package vms.vevs.controller.validator;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import vms.vevs.entity.bulk.BulkLocationRecords;
import vms.vevs.entity.bulk.BulkUploadRecordsFile;
import vms.vevs.entity.bulk.BulkUserRecords;
import vms.vevs.entity.common.Location;
import vms.vevs.entity.common.VMSEnum;
import vms.vevs.entity.employee.Users;
import vms.vevs.entity.virtualObject.BulkRejectVO;
import vms.vevs.i18.MessageByLocaleService;
import vms.vevs.repo.bulk.BulkLocationRecordsRepository;
import vms.vevs.repo.bulk.BulkUploadRecordsFileRepository;
import vms.vevs.repo.bulk.BulkUserRecordsRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Configurable
public class BulkValidator extends ValidatorHelper {


    @Autowired
    MessageByLocaleService messageSource;

    @Autowired
    BulkUserRecordsRepository userRecordsRepository;

    @Autowired
    private BulkUploadRecordsFileRepository fileUploadRepository;

    @Autowired
    private BulkLocationRecordsRepository bulkLocationRecordsRepository;

    @Autowired
    Validator validator;


    public List<String> validateNewFile(MultipartFile file, String moduleName) throws IOException {

        List<String> validateMessage = new ArrayList<>();

        checkFileExtension(file, validateMessage);
        if (!validateMessage.isEmpty()) {
            return validateMessage;
        }
        if (StringUtils.isEmpty(moduleName)) {
            validateMessage.add(messageSource.getMessage("bulk.upload.error.na.module"));
        }
        moduleName = moduleName.toUpperCase();
        if (!EnumUtils.isValidEnum(VMSEnum.MODULE_NAME.class, moduleName)) {
            validateMessage.add(messageSource.getMessage("bulk.upload.error.invalid.module"));
        }
        checkModuleWiseCSVColumn(moduleName, file, validateMessage);
        if (!validateMessage.isEmpty()) {

            return validateMessage;
        }
        return validateMessage;
    }

    private void checkModuleWiseCSVColumn(String moduleName, MultipartFile file, List<String> validateMessage) throws IOException {

        List<String[]> fileDataList = fetchFileDataRowWise(file);

        List<String> columnMismatched = new ArrayList<>();
        String[] headers = fileDataList.remove(0);

        List<String> keySet = getAllClassMembers(moduleName);

        List<String> emptyFieldName = new ArrayList<>();
        for (String header : headers) {

            if (!keySet.contains(header)) {
                columnMismatched.add(header);

            }
        }

        if (fileDataList.isEmpty()) {
            validateMessage.add(messageSource.getMessage("bulk.upload.error.no.record"));
        }
        if (!columnMismatched.isEmpty()) {
            validateMessage.add(messageSource.getMessage("bulk.upload.error.header.mismatch"));
        }

        for (int col = 0; col < fileDataList.size(); col++) {
            String[] values = fileDataList.get(col);
            String emptyColumn = StringUtils.EMPTY;
            for (int i = 0; i < values.length; i++) {
                String colValue = values[i];
                if (StringUtils.isBlank(colValue)) {

                    emptyColumn = emptyColumn + headers[i] + ",";
                }
            }
            if (StringUtils.isNotBlank(emptyColumn)) {
                emptyColumn = emptyColumn.substring(0, emptyColumn.length() - 1);
                String msg = "In row " + String.valueOf(col + 1) + " empty column are : " + emptyColumn;
                emptyFieldName.add(msg);
                emptyColumn = StringUtils.EMPTY;
            }
        }
        if (emptyFieldName.size() > 0) {
            validateMessage.add(messageSource.getMessage("bulk.upload.error.empty.field", new Object[]{emptyFieldName}));
        }


    }

    private void checkFileExtension(MultipartFile file, List<String> validateMessage) {

        String fileName = file.getOriginalFilename();
        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            String extension = fileName.substring(index + 1);
            if (!extension.equalsIgnoreCase("csv")) {
                validateMessage.add(messageSource.getMessage("bulk.upload.error.not.csv"));
            }
        } else {
            validateMessage.add(messageSource.getMessage("bulk.upload.error.without.extension"));
        }

    }

    public List<String> validateNewFileData(Long uploadedFileId, String moduleName) {
        List<String> validateMessage = new ArrayList<>();
        if (uploadedFileId == null) {
            validateMessage.add(messageSource.getMessage("bulk.validate.error.na.uploadedFileId"));
            return validateMessage;
        }
        BulkUploadRecordsFile fileRecord = fileUploadRepository.getById(uploadedFileId);
        if (fileRecord == null) {
            validateMessage.add(messageSource.getMessage("bulk.validate.error.na.records"));
            return validateMessage;
        }

        if (!StringUtils.equalsIgnoreCase(fileRecord.getStatus(), VMSEnum.BULK_UPLOAD_STATUS.PENDING.name())) {
            validateMessage.add(messageSource.getMessage("bulk.validate.error.status.mismatch"));
        }
        String status = VMSEnum.BULK_UPLOAD_STATUS.VALIDATE.name();
        if (StringUtils.equalsIgnoreCase(fileRecord.getStatus(),status )) {
            validateMessage.add(messageSource.getMessage("bulk.validate.error.status.already", new Object[]{status}));
        }

        if (StringUtils.equalsIgnoreCase(moduleName, VMSEnum.MODULE_NAME.LOCATION.name())) {
            validateMessage.addAll(validateLocationRecordOfFile(validateMessage, uploadedFileId));
        } else {
            validateMessage.addAll(validateUserRecordOfFile(validateMessage, uploadedFileId));
        }
        return validateMessage;
    }

    private Collection<String> validateUserRecordOfFile(List<String> validateMessage, Long uploadedFileId) {
        List<BulkUserRecords> usersRecords = userRecordsRepository.findAllByUploadedFileId(uploadedFileId);
        for (BulkUserRecords user : usersRecords) {
            Users newUser = new Users();

            validateMessage.addAll(validator.createUser(newUser));
        }

        return validateMessage;
    }

    private List<String> validateLocationRecordOfFile(List<String> validateMessage, Long uploadedFileId) {
        List<BulkLocationRecords> locationRecords = bulkLocationRecordsRepository.findAllByUploadedFileId(uploadedFileId);
        for (BulkLocationRecords location : locationRecords) {
            Location newLocation = new Location();
            newLocation.setName(location.getLocationName());
            newLocation.setLocationAddress(location.getAddress());
            newLocation.setLocationContactNo(location.getContactNumber());
            newLocation.setCountry(location.getCountry());
            validateMessage.addAll(validator.createLocation(newLocation));
        }
        return validateMessage;
    }

    public List<String> rejectNewFileData(BulkRejectVO rejectVO, Long loggedInUserId) {
        List<String> validateMessage = new ArrayList<>();

        Long uploadedFileId = rejectVO.getUploadedFileId();
        if (uploadedFileId == null) {
            validateMessage.add(messageSource.getMessage("bulk.validate.error.na.uploadedFileId"));
            return validateMessage;
        }
        BulkUploadRecordsFile fileRecord = fileUploadRepository.getById(uploadedFileId);
        if (fileRecord == null) {
            validateMessage.add(messageSource.getMessage("bulk.validate.error.na.records"));
            return validateMessage;
        }
        String status = VMSEnum.BULK_UPLOAD_STATUS.REJECT.name();
        if (StringUtils.equalsIgnoreCase(fileRecord.getStatus(),status )) {
            validateMessage.add(messageSource.getMessage("bulk.validate.error.status.already", new Object[]{status}));
        }
        return validateMessage;
    }

    public List<String> submitNewFileData(Long uploadedFileId, String moduleName) {
        List<String> validateMessage = new ArrayList<>();
        if (uploadedFileId == null) {
            validateMessage.add(messageSource.getMessage("bulk.validate.error.na.uploadedFileId"));
            return validateMessage;
        }
        BulkUploadRecordsFile fileRecord = fileUploadRepository.getById(uploadedFileId);
        if (fileRecord == null) {
            validateMessage.add(messageSource.getMessage("bulk.validate.error.na.records"));
            return validateMessage;
        }

        if (!StringUtils.equalsIgnoreCase(fileRecord.getStatus(), VMSEnum.BULK_UPLOAD_STATUS.VALIDATE.name())) {
            validateMessage.add(messageSource.getMessage("bulk.validate.error.status.mismatch"));
        }
        String status = VMSEnum.BULK_UPLOAD_STATUS.SUBMITTED.name();
        if (StringUtils.equalsIgnoreCase(fileRecord.getStatus(),status )) {
            validateMessage.add(messageSource.getMessage("bulk.validate.error.status.already", new Object[]{status}));
        }

        if (StringUtils.equalsIgnoreCase(moduleName, VMSEnum.MODULE_NAME.LOCATION.name())) {
            validateMessage.addAll(validateLocationRecordOfFile(validateMessage, uploadedFileId));
        } else {
            validateMessage.addAll(validateUserRecordOfFile(validateMessage, uploadedFileId));
        }
        return validateMessage;
    }
}
