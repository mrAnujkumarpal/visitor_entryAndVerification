package vms.vevs.repo.bulk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vms.vevs.entity.bulk.BulkLocationRecords;
import vms.vevs.entity.bulk.BulkUserRecords;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface BulkUserRecordsRepository extends JpaRepository<BulkUserRecords,Long> {
    List<BulkUserRecords> findAllByUploadedFileId(Long createdOn);
}
