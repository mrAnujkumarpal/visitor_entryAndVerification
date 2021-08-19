package com.vevs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.vevs.entity.visitor.VisitorFeedback;

import java.util.List;

@Repository
public interface VisitorFeedbackRepository extends JpaRepository<VisitorFeedback,Long> {

    VisitorFeedback findByVisitorId(Long visitorId);

    List<VisitorFeedback> findAllByVisitorId(Long visitorId);
}
