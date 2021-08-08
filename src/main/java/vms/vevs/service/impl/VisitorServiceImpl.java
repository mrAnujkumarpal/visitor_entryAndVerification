package vms.vevs.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vms.vevs.common.notification.EmailService;
import vms.vevs.common.util.VmsUtils;
import vms.vevs.entity.common.VMSEnum;
import vms.vevs.entity.virtualObject.HttpResponse;
import vms.vevs.entity.virtualObject.VisitorVO;
import vms.vevs.entity.visitor.Visitor;
import vms.vevs.entity.visitor.VisitorFeedback;
import vms.vevs.entity.visitor.VisitorImage;
import vms.vevs.repo.*;
import vms.vevs.service.VisitorService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
        visitor.setCreatedOn(VmsUtils.currentTime());
        visitor.setVisitorCode(VmsUtils.visitorCode());
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
        Visitor newlyAddedVisitor= visitorRepository.save(visitor);
        notifyToHostEmployee(newlyAddedVisitor);
        return newlyAddedVisitor;
    }

    private void notifyToHostEmployee(Visitor newlyAddedVisitor) {
        emailService.sendEmailToHostEmployee(newlyAddedVisitor);
    }




    @Override
    public List<Visitor> allVisitors() {
        return visitorRepository.findAllByOrderByIdDesc();
    }

    @Override
    public Visitor getVisitorById(long id) {
        return visitorRepository.getById(id);
    }

    @Override
    public Visitor updateVisitor(Visitor visitor,Long loggedInUserId) {

        visitor.setVisitorStatus(VMSEnum.VISITOR_STATUS.CHECK_OUT.name());
        visitor.setCardNoGivenToVisitor("Token-001");
        visitor.setModifiedOn(VmsUtils.currentTime());
        visitor.setModifiedBy(loggedInUserId);
        return visitorRepository.save(visitor);
    }

    @Override
    public List<String> purposeOfVisit() {
        return  Stream.of(VMSEnum.PURPOSE_OF_VISIT.values())
                        .map(Enum::name)
                        .collect(Collectors.toList());
    }

    @Override
    public VisitorImage saveVisitorImage(String visitorCode, MultipartFile visitorImage) throws IOException {

        VisitorImage image = new VisitorImage();
        image.setVisitorCode(visitorCode);
        image.setPhoto(visitorImage.getBytes());

        return imageRepository.save(image);
    }


}
