package com.vevs.service;

import org.springframework.stereotype.Service;
import com.vevs.entity.visitor.VisitorFeedback;

import java.util.List;

@Service
public interface FeedbackService {
    VisitorFeedback createVisitorFeedback(VisitorFeedback feedback);


    VisitorFeedback getVisitorFeedback(Long feedbackId);

    List<VisitorFeedback> getVisitorAllFeedback(Long visitorId);

}
