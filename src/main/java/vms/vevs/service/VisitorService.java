package vms.vevs.service;

import vms.vevs.entity.virtualObject.VisitorVO;
import vms.vevs.entity.visitor.Visitor;

import java.util.List;

public interface VisitorService {


    Visitor newVisitor(VisitorVO newVisitor);

    List<Visitor> allVisitors();

    Visitor getVisitorById(long id);

    Visitor updateVisitor(Visitor visitor);

    List<String> purposeOfVisit();
}
