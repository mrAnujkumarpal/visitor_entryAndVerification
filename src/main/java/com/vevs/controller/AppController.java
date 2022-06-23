package com.vevs.controller;


import com.vevs.controller.validator.Validator;
import com.vevs.entity.common.Location;
import com.vevs.entity.virtualObject.HttpResponse;
import com.vevs.entity.virtualObject.OtpVO;
import com.vevs.entity.virtualObject.VisitorVO;
import com.vevs.entity.visitor.Visitor;
import com.vevs.entity.visitor.VisitorImage;
import com.vevs.service.AppOTPService;
import com.vevs.service.LocationService;
import com.vevs.service.VisitorService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/public/")
public class AppController {


    public static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    VisitorService visitorService;

    @Autowired
    AppOTPService otpService;

    @Autowired
    Validator validator;

    @Autowired
    LocationService locationService;

    @GetMapping("allLocations")
    @ApiImplicitParams({ @ApiImplicitParam(name = "loggedInUserId") })
    public HttpResponse<?> allLocations() {
        HttpResponse<List<Location>> response = new HttpResponse<>();
        response.setResponseObject(locationService.allLocation());
        return response;
    }


    @PostMapping(value = "newVisitor")
    @ApiImplicitParams({@ApiImplicitParam(name = "loggedInUserId")})
    public HttpResponse<?> newVisitor(
            @RequestBody VisitorVO request, UriComponentsBuilder ucBuilder) {
        HttpResponse<Visitor> response = new HttpResponse<>();
        logger.info("Creating visitor : {}", request);

        List<String> validateMsgList = validator.validateVisitor(request);
        if (!validateMsgList.isEmpty()) {
            return new HttpResponse().errorResponse(validateMsgList);
        }
        response.setResponseObject(visitorService.newVisitor(request));
        return response;
    }

    @PostMapping(value = "newVisitorImage")
    @ApiImplicitParams({@ApiImplicitParam(name = "loggedInUserId")})
    public HttpResponse<?> newVisitorImage(
            @RequestParam String visitorCode, @RequestParam MultipartFile visitorImage)
            throws IOException {
        HttpResponse<VisitorImage> response = new HttpResponse<>();
        response.setResponseObject(visitorService.saveVisitorImage(visitorCode, visitorImage));
        return response;
    }

    @RequestMapping(value = "sendOTP", method = RequestMethod.POST)
    @ApiImplicitParams({@ApiImplicitParam(name = "loggedInUserId")})
    public String createAndSendOTP(@RequestBody OtpVO otpRequest) {
        return otpService.createAndSendOTP(otpRequest);
    }

    @GetMapping("purposeOfVisit")
    @ApiImplicitParams({@ApiImplicitParam(name = "loggedInUserId")})
    public HttpResponse<?> purposeOfVisit() {
        HttpResponse<List<String>> response = new HttpResponse<>();
        response.setResponseObject(visitorService.purposeOfVisit());
        return response;
    }
}
