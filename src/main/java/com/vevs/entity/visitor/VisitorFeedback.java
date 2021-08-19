package com.vevs.entity.visitor;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class VisitorFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long  visitorId;
    private String remarkPoints;
    private String feedback;
}
