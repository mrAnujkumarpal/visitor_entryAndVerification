package com.vevs.repo.bulk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.vevs.entity.bulk.BulkLocationRecords;

import java.util.List;

@Repository
public interface BulkLocationRecordsRepository extends JpaRepository<BulkLocationRecords,Long> {

    List<BulkLocationRecords> findAllByUploadedFileId(Long uploadFileId);
}
