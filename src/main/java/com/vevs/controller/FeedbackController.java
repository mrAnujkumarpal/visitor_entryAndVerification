package com.vevs.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import com.vevs.controller.validator.Validator;
import com.vevs.entity.virtualObject.HttpResponse;
import com.vevs.entity.visitor.VisitorFeedback;
import com.vevs.service.FeedbackService;

import java.util.List;

@RestController
@RequestMapping("/feedback/")
public class FeedbackController {

    public static final Logger logger = LoggerFactory.getLogger(VisitorController.class);


    @Autowired
    FeedbackService feedbackService;

    @Autowired
    Validator validator;

    @GetMapping(value = "view/feedback/{visitorId}")
    public HttpResponse<?> viewVisitorFeedback(@PathVariable("visitorId") long visitorId,
                                               UriComponentsBuilder ucBuilder, @RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse<List<VisitorFeedback>> response = new HttpResponse<>();

        response.setResponseObject(feedbackService.getVisitorAllFeedback(visitorId));
        return response;
    }

    @PostMapping(value = "create/feedback")
    public HttpResponse<?> visitorFeedback(@RequestBody VisitorFeedback feedback,
                                           UriComponentsBuilder ucBuilder,
                                           @RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse<VisitorFeedback> response = new HttpResponse<>();
        List<String> validateLocation = validator.createFeedback(feedback);
        if (!validateLocation.isEmpty()) {
            return new HttpResponse().errorResponse(validateLocation);
        }
        response.setResponseObject(feedbackService.createVisitorFeedback(feedback));
        return response;
    }
}
