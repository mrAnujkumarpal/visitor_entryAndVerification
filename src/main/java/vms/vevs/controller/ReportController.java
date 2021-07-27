package vms.vevs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vms.vevs.controller.validator.Validator;
import vms.vevs.entity.employee.Users;
import vms.vevs.entity.virtualObject.HttpResponse;
import vms.vevs.entity.virtualObject.ReportRequestVO;
import vms.vevs.entity.visitor.Visitor;
import vms.vevs.service.ReportService;
import vms.vevs.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/report/")
public class ReportController {

    @Autowired
    ReportService reportService;

    @Autowired
    Validator validator;

    @PostMapping(value = "view/searchVisitorList")
    public HttpResponse<?> searchVisitorList(@RequestBody ReportRequestVO request,
            @RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse<List<Visitor>> response = new HttpResponse<>();


        List<String> validateMsgList = validator.validateReportRequest(request);
        if (!validateMsgList.isEmpty()) {
            return new HttpResponse().errorResponse(validateMsgList);
        }
        List<Visitor> visitorsList = reportService.searchVisitorList(request,loggedInUserId);
        response.setResponseObject(visitorsList);
        return response;
    }

    @GetMapping(value = "view/todayVisitorList")
    public HttpResponse<?> todayVisitorList(@RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse<List<Visitor>> response = new HttpResponse<>();
        List<Visitor> visitorsList = reportService.todayVisitorList(loggedInUserId);
        response.setResponseObject(visitorsList);
        return response;
    }

}
