package vms.vevs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vms.vevs.common.util.VmsUtils;
import vms.vevs.entity.common.VMSEnum;
import vms.vevs.entity.virtualObject.VisitorVO;
import vms.vevs.entity.visitor.Visitor;
import vms.vevs.entity.visitor.VisitorImage;
import vms.vevs.repo.EmployeeRepository;
import vms.vevs.repo.LocationRepository;
import vms.vevs.repo.VisitorImageRepository;
import vms.vevs.repo.VisitorRepository;
import vms.vevs.service.VisitorService;

import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class VisitorServiceImpl implements VisitorService {

    @Autowired
    VisitorRepository visitorRepository;

    @Autowired
    EmployeeRepository empRepository;

    @Autowired
    VisitorImageRepository imageRepository;

    @Autowired
    LocationRepository locRepository;

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
        visitor.setHostEmployee(empRepository.getById(newVisitor.getHostEmployeeId()));
        VisitorImage visitorImage = new VisitorImage();
        visitorImage.setVisitorCode(visitor.getVisitorCode());
        visitorImage.setVisitorImage(newVisitor.getVisitorImage());
        imageRepository.save(visitorImage);

        return visitorRepository.save(visitor);

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
    public Visitor updateVisitor(Visitor visitor) {

        visitor.setVisitorStatus(VMSEnum.VISITOR_STATUS.CHECK_OUT.name());
        visitor.setCardNoGivenToVisitor("Token-001");
        visitor.setModifiedOn(VmsUtils.currentTime());
        visitor.setModifiedBy("ADMIN USER");
        return visitorRepository.save(visitor);
    }

    @Override
    public List<String> purposeOfVisit() {
        return enumNames;
    }
    List<String> enumNames = Stream.of(VMSEnum.PURPOSE_OF_VISIT.values())
            .map(Enum::name)
            .collect(Collectors.toList());


}
