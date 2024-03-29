package com.vevs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.vevs.entity.visitor.Visitor;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor,Long> {

    List<Visitor> findAllByOrderByIdDesc();


    List<Visitor> findAllByCreatedOn(Timestamp createdOn);

   List<Visitor> findAllByCreatedOnBetween(Timestamp createdOnStart,Timestamp createdOnEnd);
}
