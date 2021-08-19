package com.vevs.controller.bulk;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.vevs.controller.validator.BulkValidator;
import com.vevs.entity.bulk.BulkUploadRecordsFile;
import com.vevs.entity.vo.BulkRejectVO;
import com.vevs.entity.vo.HttpResponse;
import com.vevs.service.BulkUploadService;

import java.util.List;

@RestController
@RequestMapping("/bulkUpload/")
public class BulkUploadController {

    @Autowired
    private BulkValidator validator;


    @Autowired
    private BulkUploadService bulkUploadService;


    @PostMapping(value = "file/{fileName}/{moduleName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResponse<?> createLocation(@RequestParam("file") MultipartFile file,
                                          @PathVariable("fileName") String fileName,
                                          @PathVariable("moduleName") String moduleName,
                                          @RequestHeader("loggedInUserId") Long loggedInUserId) throws Exception {
        HttpResponse<BulkUploadRecordsFile> response = new HttpResponse<>();
        List<String> validateNewFile = validator.validateNewFile(file, moduleName);
        if (!validateNewFile.isEmpty()) {
            return new HttpResponse().errorResponse(validateNewFile);
        }

        BulkUploadRecordsFile uploadedData = bulkUploadService.uploadNewFileData(loggedInUserId, file, fileName, moduleName);
        response.setResponseObject(uploadedData);
        return response;
    }

    @GetMapping("/validate/{uploadedFileId}")
    public HttpResponse validateFileRecord(@PathVariable("uploadedFileId") Long uploadedFileId,
                                           @RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse<BulkUploadRecordsFile> response = new HttpResponse<>();
        List<String> validateNewFileData = validator.validateNewFileData(uploadedFileId);
        if (!validateNewFileData.isEmpty()) {
            return new HttpResponse().errorResponse(validateNewFileData);
        }
        BulkUploadRecordsFile validateData = bulkUploadService.validateFileRecord(uploadedFileId, loggedInUserId);
        response.setResponseObject(validateData);
        return response;
    }

    @PostMapping("/rejectFileRecord")
    public HttpResponse rejectFileRecord(@RequestBody BulkRejectVO rejectVO,
                                         @RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse<BulkUploadRecordsFile> response = new HttpResponse<>();
        List<String> rejectNewFileData = validator.rejectNewFileData(rejectVO, loggedInUserId);
        if (!rejectNewFileData.isEmpty()) {
            return new HttpResponse().errorResponse(rejectNewFileData);
        }
        BulkUploadRecordsFile validateData = bulkUploadService.rejectFileRecord(rejectVO, loggedInUserId);
        response.setResponseObject(validateData);
        return response;
    }


    @GetMapping("/submit/{uploadedFileId}")
    public HttpResponse submitFileRecord(@PathVariable("uploadedFileId") Long uploadedFileId,
                                         @RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse<BulkUploadRecordsFile> response = new HttpResponse<>();
        List<String> submitNewFileData = validator.submitNewFileData(uploadedFileId);
        if (!submitNewFileData.isEmpty()) {
            return new HttpResponse().errorResponse(submitNewFileData);
        }
        BulkUploadRecordsFile validateData = bulkUploadService.submitFileRecord(uploadedFileId, loggedInUserId);
        response.setResponseObject(validateData);
        return response;
    }
}
