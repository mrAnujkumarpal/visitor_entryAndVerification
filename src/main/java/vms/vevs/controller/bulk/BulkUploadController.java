package vms.vevs.controller.bulk;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vms.vevs.controller.validator.BulkValidator;
import vms.vevs.entity.bulk.BulkUploadRecordsFile;
import vms.vevs.entity.common.Location;
import vms.vevs.entity.virtualObject.BulkRejectVO;
import vms.vevs.entity.virtualObject.HttpResponse;
import vms.vevs.service.BulkUploadService;

import java.io.IOException;
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
        List<String> validateLocation = validator.validateNewFile(file, moduleName);
        if (!validateLocation.isEmpty()) {
            return new HttpResponse().errorResponse(validateLocation);
        }

        BulkUploadRecordsFile uploadedData = bulkUploadService.uploadNewFileData(loggedInUserId, file, fileName, moduleName);
        response.setResponseObject(uploadedData);
        return response;
    }

    @GetMapping("/validate/{uploadedFileId}/{moduleName}")
    public HttpResponse validateFileRecord(@PathVariable("moduleName") String moduleName,
                                           @PathVariable("uploadedFileId") Long uploadedFileId,
                                           @RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse<BulkUploadRecordsFile> response = new HttpResponse<>();

        BulkUploadRecordsFile validateData = bulkUploadService.validateFileRecord(uploadedFileId, moduleName, loggedInUserId);
        response.setResponseObject(validateData);
        return response;
    }

    @GetMapping("/rejectFileRecord")
    public HttpResponse rejectFileRecord(@RequestBody BulkRejectVO rejectVO,
                                         @RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse<BulkUploadRecordsFile> response = new HttpResponse<>();

        BulkUploadRecordsFile validateData = bulkUploadService.rejectFileRecord(rejectVO, loggedInUserId);
        response.setResponseObject(validateData);
        return response;
    }


    @GetMapping("/submit/{uploadedFileId}/{moduleName}")
    public HttpResponse submitFileRecord(@PathVariable("moduleName") String moduleName,
                                         @PathVariable("uploadedFileId") Long uploadedFileId,
                                         @RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse<BulkUploadRecordsFile> response = new HttpResponse<>();

        BulkUploadRecordsFile validateData = bulkUploadService.submitFileRecord(uploadedFileId, moduleName, loggedInUserId);
        response.setResponseObject(validateData);
        return response;
    }
}
