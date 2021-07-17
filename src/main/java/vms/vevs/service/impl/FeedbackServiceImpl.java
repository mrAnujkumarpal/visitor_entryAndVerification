package vms.vevs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vms.vevs.entity.visitor.VisitorFeedback;
import vms.vevs.repo.VisitorFeedbackRepository;
import vms.vevs.service.FeedbackService;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    VisitorFeedbackRepository feedbackRepository;

    @Override
    public List<VisitorFeedback> getVisitorAllFeedback(Long visitorId) {
        return feedbackRepository.findAllByVisitorId(visitorId);
    }

    @Override
    public VisitorFeedback createVisitorFeedback(VisitorFeedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public VisitorFeedback getVisitorFeedback(Long feedbackId) {
        return feedbackRepository.getById(feedbackId);
    }
}
