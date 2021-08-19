package com.vevs.repo.bulk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.vevs.entity.bulk.BulkUserRecords;

import java.util.List;

@Repository
public interface BulkUserRecordsRepository extends JpaRepository<BulkUserRecords,Long> {
    List<BulkUserRecords> findAllByUploadedFileId(Long createdOn);
}
