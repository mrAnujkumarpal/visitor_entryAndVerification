package vms.vevs.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vms.vevs.entity.bulk.BulkUploadRecordsFile;
import vms.vevs.entity.virtualObject.BulkRejectVO;

@Service
public interface BulkUploadService {


    BulkUploadRecordsFile uploadNewFileData(Long userId, MultipartFile file,String fileName,String module) throws Exception;

    BulkUploadRecordsFile validateFileRecord(Long uploadedFileId,String moduleName, Long loggedInUserId);

    BulkUploadRecordsFile rejectFileRecord(BulkRejectVO rejectVO, Long loggedInUserId);

    BulkUploadRecordsFile submitFileRecord(Long uploadedFileId, String moduleName, Long loggedInUserId);
}
