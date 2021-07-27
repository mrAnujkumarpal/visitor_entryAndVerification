package vms.vevs.service;

import vms.vevs.entity.employee.Users;
import vms.vevs.entity.virtualObject.ReportRequestVO;
import vms.vevs.entity.visitor.Visitor;

import java.util.List;

public interface ReportService {


    List<Visitor> todayVisitorList(Long loggedInUserId);

    List<Visitor> searchVisitorList(ReportRequestVO request, Long userId);

}
