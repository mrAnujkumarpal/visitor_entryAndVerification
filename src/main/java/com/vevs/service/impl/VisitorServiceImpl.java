package com.vevs.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.vevs.common.notification.EmailService;
import com.vevs.common.util.VMSUtils;
import com.vevs.entity.common.VMSEnum;
import com.vevs.entity.vo.VisitorVO;
import com.vevs.entity.visitor.Visitor;
import com.vevs.entity.visitor.VisitorImage;
import com.vevs.repo.*;
import com.vevs.service.VisitorService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class VisitorServiceImpl implements VisitorService {

    private static final Logger logger = LoggerFactory.getLogger(VisitorServiceImpl.class);

    @Autowired
    VisitorRepository visitorRepository;

    @Autowired
    VisitorImageRepository imageRepository;

    @Autowired
    LocationRepository locRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    VisitorFeedbackRepository feedbackRepository;


    @Override
    public Visitor newVisitor(VisitorVO newVisitor) {
        Visitor visitor = new Visitor();
        visitor.setEnable(true);
        visitor.setCreatedOn(VMSUtils.currentTime());
        visitor.setVisitorCode(VMSUtils.visitorCode());
        visitor.setName(newVisitor.getVisitorName());
        visitor.setVisitorEmail(newVisitor.getVisitorEmail());
        visitor.setMobileNumber(newVisitor.getMobileNumber());
        visitor.setVisitorAddress(newVisitor.getVisitorAddress());
        visitor.setPurposeOfVisit(newVisitor.getPurposeOfVisit());
        visitor.setVisitorStatus(VMSEnum.VISITOR_STATUS.CHECK_IN.name());
        visitor.setLocation(locRepository.getById(newVisitor.getLocationId()));
        visitor.setHostEmployee(userRepository.getById(newVisitor.getHostEmployeeId()));
       /* if(StringUtils.isNotEmpty(newVisitor.getVisitorImage())) {
            VisitorImage visitorImage = new VisitorImage();
            visitorImage.setVisitorCode(visitor.getVisitorCode());
            visitorImage.setVisitorImage(newVisitor.getVisitorImage());
            imageRepository.save(visitorImage);
        }*/
        Visitor newlyAddedVisitor = visitorRepository.save(visitor);
        notifyToHostEmployee(newlyAddedVisitor);
        return newlyAddedVisitor;
    }

    private void notifyToHostEmployee(Visitor newlyAddedVisitor) {
        emailService.sendEmailToHostEmployee(newlyAddedVisitor);
    }


    @Override
    public List<Visitor> allVisitors() {
        List<Visitor> visitors = visitorRepository.findAllByOrderByIdDesc();
        for (Visitor visitor : visitors) {
            VisitorImage image = imageRepository.findByVisitorCode(visitor.getVisitorCode());
            visitor.setVisitorImage(image.getPhoto());
        }
        return visitors;
    }

    @Override
    public Visitor getVisitorById(long id) {
        Visitor visitor = visitorRepository.getById(id);
        VisitorImage image = imageRepository.findByVisitorCode(visitor.getVisitorCode());
        visitor.setVisitorImage(image.getPhoto());
        return visitor;
    }

    @Override
    public Visitor updateVisitor(Visitor visitor, Long loggedInUserId) {

        visitor.setVisitorStatus(VMSEnum.VISITOR_STATUS.CHECK_OUT.name());
        visitor.setCardNoGivenToVisitor("Token-001");
        visitor.setModifiedOn(VMSUtils.currentTime());
        visitor.setModifiedBy(loggedInUserId);
        Visitor updateVisitor = visitorRepository.save(visitor);
        VisitorImage image = imageRepository.findByVisitorCode(updateVisitor.getVisitorCode());
        visitor.setVisitorImage(image.getPhoto());
        return visitor;
    }

    @Override
    public List<String> purposeOfVisit() {
        return Stream.of(VMSEnum.PURPOSE_OF_VISIT.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public VisitorImage saveVisitorImage(String visitorCode, MultipartFile visitorImage) throws IOException {

        VisitorImage image = new VisitorImage();
        image.setPhoto(visitorImage.getBytes());
        image.setVisitorCode(visitorCode);
        return imageRepository.save(image);
    }


}
