package com.vevs.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.vevs.entity.bulk.BulkUploadRecordsFile;
import com.vevs.entity.vo.BulkRejectVO;

@Service
public interface BulkUploadService {


    BulkUploadRecordsFile uploadNewFileData(Long userId, MultipartFile file,String fileName,String module) throws Exception;

    BulkUploadRecordsFile validateFileRecord(Long uploadedFileId, Long loggedInUserId);

    BulkUploadRecordsFile rejectFileRecord(BulkRejectVO rejectVO, Long loggedInUserId);

    BulkUploadRecordsFile submitFileRecord(Long uploadedFileId, Long loggedInUserId);
}
