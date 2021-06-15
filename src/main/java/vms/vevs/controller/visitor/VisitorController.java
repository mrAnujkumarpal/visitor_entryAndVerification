package vms.vevs.controller.visitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import vms.vevs.controller.validator.Validator;
import vms.vevs.entity.common.AppOTP;
import vms.vevs.entity.common.Location;
import vms.vevs.entity.virtualObject.HttpResponse;
import vms.vevs.entity.virtualObject.VisitorVO;
import vms.vevs.entity.visitor.Visitor;
import vms.vevs.service.AppOTPService;
import vms.vevs.service.VisitorService;

import java.util.List;

@RestController
@RequestMapping("/visitor/")
public class VisitorController {
    public static final Logger logger = LoggerFactory.getLogger(VisitorController.class);

    @Autowired
    VisitorService visitorService;

    @Autowired
    AppOTPService otpService;

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public HttpResponse<?> listAllVisitor() {
        HttpResponse<List<Visitor>> response = new HttpResponse<>();
        List<Visitor> visitors = visitorService.allVisitors();
        if (visitors.isEmpty()) {

            // You many decide to return HttpStatus.NOT_FOUND
        }
        response.setResponseObject(visitors);
        return response;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public HttpResponse<?> createVisitor(@RequestBody VisitorVO request, UriComponentsBuilder ucBuilder) {
        HttpResponse<Visitor> response = new HttpResponse<>();
        logger.info("Creating User : {}", request);

        List<String> validateMsgList = new Validator().validateVisitor(request);
        if (!validateMsgList.isEmpty()) {
            return new HttpResponse().errorResponse(validateMsgList);
        }

        response.setResponseObject(visitorService.newVisitor(request));
        return response;
    }

    @RequestMapping(value = "view/{visitorId}", method = RequestMethod.POST)
    public HttpResponse<?> visitorById(@PathVariable("visitorId") long id, UriComponentsBuilder ucBuilder) {
        HttpResponse<Visitor> response = new HttpResponse<>();
        response.setResponseObject(visitorService.getVisitorById(id));
        return response;
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public HttpResponse<?> updateVisitor(@RequestBody Visitor visitor, UriComponentsBuilder ucBuilder) {
        HttpResponse<Visitor> response = new HttpResponse<>();
        List<String> validationMsgList = new Validator().updateVisitor(visitor);
        if (!validationMsgList.isEmpty()) {
            return new HttpResponse().errorResponse(validationMsgList);
        }

        response.setResponseObject(visitorService.updateVisitor(visitor));
        return response;
    }


    @RequestMapping(value = "sendOTP", method = RequestMethod.POST)
    public String createAndSendOTP(@RequestBody AppOTP otpRequest) {
        return otpService.createAndSendOTP(otpRequest);
    }

    @RequestMapping(value = "pendingOTP", method = RequestMethod.GET)
    public List<AppOTP> availPendingOTP() {

        return otpService.allPendingOTP();

    }
}
