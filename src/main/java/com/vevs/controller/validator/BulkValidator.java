package com.vevs.controller.validator;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.vevs.entity.bulk.BulkUploadRecordsFile;
import com.vevs.entity.common.VMSEnum;
import com.vevs.entity.virtualObject.BulkRejectVO;
import com.vevs.i18N.MessageByLocaleService;
import com.vevs.repo.bulk.BulkUploadRecordsFileRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Configurable
public class BulkValidator extends ValidatorHelper {

    @Autowired
    private Validator validator;

    @Autowired
    private MessageByLocaleService messageSource;

    @Autowired
    private BulkUploadRecordsFileRepository fileUploadRepository;


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

    public List<String> validateNewFileData(Long uploadedFileId) {
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
        String oldStatus = VMSEnum.BULK_UPLOAD_STATUS.PENDING.name();
        if (!StringUtils.equalsIgnoreCase(fileRecord.getStatus(), oldStatus)) {
            validateMessage.add(messageSource.getMessage("bulk.validate.error.status.mismatch", new Object[]{oldStatus}));
        }
        String status = VMSEnum.BULK_UPLOAD_STATUS.VALIDATE.name();
        if (StringUtils.equalsIgnoreCase(fileRecord.getStatus(), status)) {
            validateMessage.add(messageSource.getMessage("bulk.validate.error.status.already", new Object[]{status}));
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
        if (StringUtils.equalsIgnoreCase(fileRecord.getStatus(), status)) {
            validateMessage.add(messageSource.getMessage("bulk.validate.error.status.already", new Object[]{status}));
        }
        return validateMessage;
    }

    public List<String> submitNewFileData(Long uploadedFileId) {
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
        if (fileRecord.isValidationIssueFound()) {
            validateMessage.add(messageSource.getMessage("bulk.validate.error.found.issues"));
            return validateMessage;
        }
        String oldStatus = VMSEnum.BULK_UPLOAD_STATUS.VALIDATE.name();
        if (!StringUtils.equalsIgnoreCase(fileRecord.getStatus(), oldStatus)) {
            validateMessage.add(messageSource.getMessage("bulk.validate.error.status.mismatch", new Object[]{oldStatus}));
        }
        String status = VMSEnum.BULK_UPLOAD_STATUS.SUBMITTED.name();
        if (StringUtils.equalsIgnoreCase(fileRecord.getStatus(), status)) {
            validateMessage.add(messageSource.getMessage("bulk.validate.error.status.already", new Object[]{status}));
        }
        return validateMessage;
    }
}
