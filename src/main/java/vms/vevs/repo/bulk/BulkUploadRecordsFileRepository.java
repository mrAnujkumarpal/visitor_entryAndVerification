package vms.vevs.repo.bulk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vms.vevs.entity.bulk.BulkUploadRecordsFile;

@Repository
public interface BulkUploadRecordsFileRepository extends JpaRepository<BulkUploadRecordsFile,Long> {

}
