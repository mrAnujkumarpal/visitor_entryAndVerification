package vms.vevs.service;

import org.springframework.web.multipart.MultipartFile;
import vms.vevs.entity.virtualObject.VisitorVO;
import vms.vevs.entity.visitor.Visitor;
import vms.vevs.entity.visitor.VisitorFeedback;
import vms.vevs.entity.visitor.VisitorImage;

import java.io.IOException;
import java.util.List;

public interface VisitorService {


    Visitor newVisitor(VisitorVO newVisitor);

    List<Visitor> allVisitors();

    Visitor getVisitorById(long id);

    Visitor updateVisitor(Visitor visitor,Long userId);

    List<String> purposeOfVisit();

    VisitorImage saveVisitorImage(String visitorCode,MultipartFile image) throws IOException;
}
