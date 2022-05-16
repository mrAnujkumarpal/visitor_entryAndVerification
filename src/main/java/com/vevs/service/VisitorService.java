package com.vevs.service;

import com.vevs.entity.virtualObject.UpdateVisitorVO;
import org.springframework.web.multipart.MultipartFile;
import com.vevs.entity.virtualObject.VisitorVO;
import com.vevs.entity.visitor.Visitor;
import com.vevs.entity.visitor.VisitorImage;

import java.io.IOException;
import java.util.List;

public interface VisitorService {


    Visitor newVisitor(VisitorVO newVisitor);

    List<Visitor> allVisitors();

    Visitor getVisitorById(Long id);

    Visitor updateVisitor(UpdateVisitorVO visitor, Long userId);

    List<String> purposeOfVisit();

    VisitorImage saveVisitorImage(String visitorCode,MultipartFile image) throws IOException;
}
