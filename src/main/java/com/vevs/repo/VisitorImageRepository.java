package com.vevs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.vevs.entity.visitor.VisitorImage;

@Repository
public interface VisitorImageRepository extends JpaRepository<VisitorImage,Long> {

    VisitorImage  findByVisitorCode(String visitorCode);
}
