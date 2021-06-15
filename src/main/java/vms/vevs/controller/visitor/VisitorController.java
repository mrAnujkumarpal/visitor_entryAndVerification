package vms.vevs.controller.visitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import vms.vevs.controller.validator.Validator;
import vms.vevs.entity.common.AppOTP;
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
    public List<Visitor> listAllVisitor() {
        List<Visitor> visitors = visitorService.allVisitors();
        if (visitors.isEmpty()) {

            // You many decide to return HttpStatus.NOT_FOUND
        }
        return visitors;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Visitor createVisitor(@RequestBody VisitorVO request, UriComponentsBuilder ucBuilder) {
        logger.info("Creating User : {}", request);

        List<String> validateVisitor = new Validator().validateVisitor(request);
        if (validateVisitor.size() > 0) {
            logger.info(validateVisitor.toString());
        }

        return visitorService.newVisitor(request);
    }

    @RequestMapping(value = "view/{visitorId}", method = RequestMethod.POST)
    public Visitor visitorById(@PathVariable("visitorId") long id, UriComponentsBuilder ucBuilder) {

        return visitorService.getVisitorById(id);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Visitor updateVisitor(@RequestBody Visitor visitor, UriComponentsBuilder ucBuilder) {

        List<String> validateVisitor = new Validator().updateVisitor(visitor);
        if (validateVisitor.size() > 0) {
            logger.info(validateVisitor.toString());
        }

        return visitorService.updateVisitor(visitor);

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
