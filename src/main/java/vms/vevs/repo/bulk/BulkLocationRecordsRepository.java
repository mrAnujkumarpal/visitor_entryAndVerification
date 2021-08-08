package vms.vevs.repo.bulk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vms.vevs.entity.bulk.BulkLocationRecords;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface BulkLocationRecordsRepository extends JpaRepository<BulkLocationRecords,Long> {

    List<BulkLocationRecords> findAllByUploadedFileId(Long uploadFileId);
}
