package vms.vevs.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vms.vevs.entity.bulk.BulkUploadRecordsFile;

import java.io.IOException;

@Service
public interface BulkUploadService {


    BulkUploadRecordsFile uploadNewFileData(Long userId, MultipartFile file,String fileName,String module) throws Exception;
}
