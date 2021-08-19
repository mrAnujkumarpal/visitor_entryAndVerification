package com.vevs.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import com.vevs.controller.validator.Validator;
import com.vevs.entity.common.AppOTP;
import com.vevs.entity.vo.HttpResponse;
import com.vevs.entity.vo.VisitorVO;
import com.vevs.entity.visitor.Visitor;
import com.vevs.entity.visitor.VisitorImage;
import com.vevs.service.AppOTPService;
import com.vevs.service.VisitorService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/visitor/")
public class VisitorController {
    public static final Logger logger = LoggerFactory.getLogger(VisitorController.class);

    @Autowired
    VisitorService visitorService;

    @Autowired
    AppOTPService otpService;

    @Autowired
    Validator validator;


    @RequestMapping(value = "allVisitor", method = RequestMethod.GET)
    public HttpResponse<?> listAllVisitor() {
        HttpResponse<List<Visitor>> response = new HttpResponse<>();
        List<Visitor> visitors = visitorService.allVisitors();
        if (visitors.isEmpty()) {

            // You many decide to return HttpStatus.NOT_FOUND
        }
        response.setResponseObject(visitors);
        return response;
    }

    @PostMapping(value = "public/newVisitor")
    @ApiImplicitParams({@ApiImplicitParam(name = "loggedInUserId")})
    public HttpResponse<?> newVisitor(@RequestBody VisitorVO request, UriComponentsBuilder ucBuilder) {
        HttpResponse<Visitor> response = new HttpResponse<>();
        logger.info("Creating visitor : {}", request);

        List<String> validateMsgList = validator.validateVisitor(request);
        if (!validateMsgList.isEmpty()) {
            return new HttpResponse().errorResponse(validateMsgList);
        }

        response.setResponseObject(visitorService.newVisitor(request));
        return response;
    }

    @PostMapping(value = "public/newVisitorImage")
    @ApiImplicitParams({@ApiImplicitParam(name = "loggedInUserId")})
    public HttpResponse<?> newVisitorImage(@RequestParam String visitorCode,
                                           @RequestParam MultipartFile visitorImage)
            throws IOException {
        HttpResponse<VisitorImage> response = new HttpResponse<>();
        response.setResponseObject(visitorService.saveVisitorImage(visitorCode,visitorImage));
        return response;
    }

    @GetMapping(value = "view/{visitorId}")
    public HttpResponse<?> visitorById(@PathVariable("visitorId") long id,
                                       UriComponentsBuilder ucBuilder, @RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse<Visitor> response = new HttpResponse<>();
        response.setResponseObject(visitorService.getVisitorById(id));
        return response;
    }

    @PutMapping(value = "update")
    public HttpResponse<?> updateVisitor(@RequestBody Visitor visitor, UriComponentsBuilder ucBuilder, @RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse<Visitor> response = new HttpResponse<>();
        List<String> validationMsgList = validator.updateVisitor(visitor);
        if (!validationMsgList.isEmpty()) {
            return new HttpResponse().errorResponse(validationMsgList);
        }

        response.setResponseObject(visitorService.updateVisitor(visitor, loggedInUserId));
        return response;
    }


    @RequestMapping(value = "public/sendOTP", method = RequestMethod.POST)
    @ApiImplicitParams({@ApiImplicitParam(name = "loggedInUserId")})
    public String createAndSendOTP(@RequestBody AppOTP otpRequest) {
        return otpService.createAndSendOTP(otpRequest);
    }

    @RequestMapping(value = "pendingOTP", method = RequestMethod.GET)
    public List<AppOTP> availPendingOTP() {

        return otpService.allPendingOTP();

    }

    @GetMapping("public/purposeOfVisit")
    @ApiImplicitParams({@ApiImplicitParam(name = "loggedInUserId")})
    public HttpResponse<?> purposeOfVisit() {
        HttpResponse<List<String>> response = new HttpResponse<>();
        response.setResponseObject(visitorService.purposeOfVisit());
        return response;
    }


}
