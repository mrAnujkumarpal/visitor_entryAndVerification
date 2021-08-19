package com.vevs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.vevs.entity.common.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location,Long> {
}
