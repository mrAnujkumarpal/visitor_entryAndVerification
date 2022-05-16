package com.vevs.service.impl;

import com.vevs.common.notification.EmailService;
import com.vevs.common.util.VMSUtils;
import com.vevs.entity.common.VMSEnum;
import com.vevs.entity.virtualObject.UpdateVisitorVO;
import com.vevs.entity.visitor.Visitor;
import com.vevs.entity.visitor.VisitorImage;
import com.vevs.entity.virtualObject.VisitorVO;
import com.vevs.repo.LocationRepository;
import com.vevs.repo.UserRepository;
import com.vevs.repo.VisitorImageRepository;
import com.vevs.repo.VisitorRepository;
import com.vevs.service.VisitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
            if (image != null) {
                visitor.setVisitorImage(image.getPhoto());
            }
        }
        return visitors;
    }

    @Override
    public Visitor getVisitorById(Long id) {
        Optional<Visitor> optionalVisitor = visitorRepository.findById(id);
        Visitor visitor =null;
        if (optionalVisitor.isPresent()) {
            visitor = optionalVisitor.get();
            VisitorImage image = imageRepository.findByVisitorCode(visitor.getVisitorCode());
            if (image != null) {
                visitor.setVisitorImage(image.getPhoto());
            }
        }
        return visitor;
    }

    @Override
    public Visitor updateVisitor(UpdateVisitorVO visitor, Long loggedInUserId) {
        Visitor visitorFromDB = visitorRepository.getById(visitor.getId());

        visitorFromDB.setVisitorCode(visitor.getVisitorCode());
        visitorFromDB.setVisitorStatus(VMSEnum.VISITOR_STATUS.CHECK_OUT.name());
        visitorFromDB.setCardNoGivenToVisitor("Token-001");
        visitorFromDB.setModifiedOn(VMSUtils.currentTime());
        visitorFromDB.setModifiedBy(loggedInUserId);
        Visitor updateVisitor = visitorRepository.save(visitorFromDB);
        VisitorImage image = imageRepository.findByVisitorCode(updateVisitor.getVisitorCode());
        if(image!=null){updateVisitor.setVisitorImage(image.getPhoto());}
        return updateVisitor;
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
