package vms.vevs.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vms.vevs.controller.validator.BulkValidator;
import vms.vevs.entity.bulk.BulkUploadRecordsFile;
import vms.vevs.entity.common.Location;
import vms.vevs.entity.virtualObject.HttpResponse;
import vms.vevs.service.BulkUploadService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/bulkUpload/")
public class BulkUploadController {

    @Autowired private  BulkValidator validator;


    @Autowired private BulkUploadService bulkUploadService;

    @PostMapping(value = "file/{fileName}/{moduleName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResponse<?> createLocation(@RequestParam("file") MultipartFile file,
                                          @PathVariable("fileName") String fileName,
                                          @PathVariable("moduleName") String moduleName,
                                          @RequestHeader("loggedInUserId") Long loggedInUserId) throws Exception {
        HttpResponse<BulkUploadRecordsFile> response = new HttpResponse<>();
        List<String> validateLocation = validator.validateNewFile(file,moduleName);
        if (!validateLocation.isEmpty()) {
            return new HttpResponse().errorResponse(validateLocation);
        }

        BulkUploadRecordsFile uploadedData=bulkUploadService.uploadNewFileData(loggedInUserId,file,fileName,moduleName);
        response.setResponseObject(uploadedData);
        return response;
    }

}
