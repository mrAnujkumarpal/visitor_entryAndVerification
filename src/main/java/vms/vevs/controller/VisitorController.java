package vms.vevs.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import vms.vevs.common.util.VmsUtils;
import vms.vevs.controller.validator.Validator;
import vms.vevs.entity.common.AppOTP;
import vms.vevs.entity.common.Role;
import vms.vevs.entity.common.VMSEnum;
import vms.vevs.entity.employee.Users;
import vms.vevs.entity.virtualObject.HttpResponse;
import vms.vevs.entity.virtualObject.VisitorVO;
import vms.vevs.entity.visitor.Visitor;
import vms.vevs.entity.visitor.VisitorFeedback;
import vms.vevs.i18.MessageByLocaleService;
import vms.vevs.service.AppOTPService;
import vms.vevs.service.UserService;
import vms.vevs.service.VisitorService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    UserService userService;

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

    @PostMapping(value = "public/create")
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

    @GetMapping(value = "view/homePageVisitorList")
    public HttpResponse<?> homePageVisitorList(@RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse<List<Visitor> > response = new HttpResponse<>();

        Users user = userService.findById(loggedInUserId);
        Long currentLocId = user.getCurrentLocation().getId();
        Set<Role> roles =user.getRoles();

        List<Visitor> todayVisitors = visitorService.todayVisitorList(currentLocId);

        //if loggeedIn user role is User only
        List<Visitor>  result = todayVisitors.stream()
                .filter(me -> me.getHostEmployee().getId() ==loggedInUserId)
                .collect(Collectors.toList());

        response.setResponseObject(todayVisitors);
        return response;
    }

    @GetMapping(value = "view/feedback/{visitorId}")
    public HttpResponse<?> viewVisitorFeedback(@PathVariable("visitorId") long visitorId,
                                       UriComponentsBuilder ucBuilder, @RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse<List<VisitorFeedback> > response = new HttpResponse<>();

        response.setResponseObject(visitorService.getVisitorAllFeedback(visitorId));
        return response;
    }

    @PostMapping(value = "feedback")
    public HttpResponse<?> visitorFeedback(@RequestBody VisitorFeedback feedback,
                                           UriComponentsBuilder ucBuilder,
                                           @RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse<VisitorFeedback> response = new HttpResponse<>();
        response.setResponseObject(visitorService.createVisitorFeedback(feedback));
        return response;
    }

}
