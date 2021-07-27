package vms.vevs.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vms.vevs.common.util.VmsUtils;
import vms.vevs.controller.VMSHelper;
import vms.vevs.entity.common.RoleName;
import vms.vevs.entity.employee.Users;
import vms.vevs.entity.virtualObject.ReportRequestVO;
import vms.vevs.entity.visitor.Visitor;
import vms.vevs.repo.UserRepository;
import vms.vevs.repo.VisitorRepository;
import vms.vevs.service.ReportService;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
public class RepostServiceImpl implements ReportService {

    @Autowired
    VisitorRepository visitorRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<Visitor> searchVisitorList(ReportRequestVO request, Long userId) {
        Users user = userRepository.getById(userId);
        Long locId = user.getCurrentLocation().getId();
        String roleName = new VMSHelper().roleOfUser(user);

        List<Visitor> visitorsList = getVisitorList(request);

        if (StringUtils.equals(roleName, RoleName.FRONT_DESK.name())) {
            visitorsList = filterByLocation(visitorsList, locId);
        } else if (StringUtils.equals(roleName, RoleName.USER.name())) {
            visitorsList = filterByLocationAndUserId(visitorsList, locId, userId);
            request.setHostEmployeeId(null);
        }
        Long hostEmployeeId = request.getHostEmployeeId();
        if (null != hostEmployeeId) {
            visitorsList = filterByHostEmployee(visitorsList, hostEmployeeId);
        }
        return visitorsList;
    }


    @Override
    public List<Visitor> todayVisitorList(Long loggedInUserId) {
        Users user = userRepository.getById(loggedInUserId);
        Long locId = user.getCurrentLocation().getId();
        String roleName = new VMSHelper().roleOfUser(user);
        List<Visitor> visitorsList = todayVisitorList();
        if (StringUtils.equals(roleName, RoleName.FRONT_DESK.name())) {
            visitorsList = filterByLocation(visitorsList, locId);
        } else if (StringUtils.equals(roleName, RoleName.USER.name())) {
            visitorsList = filterByLocationAndUserId(visitorsList, locId, loggedInUserId);
        }
        return visitorsList;
    }

    private List<Visitor> getVisitorList(ReportRequestVO request) {
        Timestamp to = VmsUtils.endOfTheDay(request.getToDate());
        Timestamp from = VmsUtils.startOfTheDay(request.getFromDate());
        return visitorRepository.findAllByCreatedOnBetween(from, to);
    }

    private List<Visitor> todayVisitorList() {
        Timestamp today = VmsUtils.currentTime();
        Timestamp to = VmsUtils.endOfTheDay(today);
        Timestamp from = VmsUtils.startOfTheDay(today);
        return visitorRepository.findAllByCreatedOnBetween(from, to);
    }


    private List<Visitor> filterByLocation(List<Visitor> visitorsList, Long currentLocId) {

        List<Visitor> result = new ArrayList<>();
        if (!visitorsList.isEmpty()) {
            result = visitorsList.stream()
                    .filter(loc -> loc.getLocation().getId() == currentLocId)
                    .collect(Collectors.toList());

        }
        return result;
    }


    private List<Visitor> filterByLocationAndUserId(List<Visitor> visitorsList, Long currentLocId, Long userId) {

        List<Visitor> result = new ArrayList<>();
        if (!visitorsList.isEmpty()) {
            result = visitorsList.stream()
                    .filter(me -> me.getHostEmployee().getId() == userId
                            && me.getLocation().getId() == currentLocId)
                    .collect(Collectors.toList());
        }
        return result;
    }


    private List<Visitor> filterByHostEmployee(List<Visitor> visitorsList, Long hostEmployeeId) {

        List<Visitor> result = new ArrayList<>();
        if (!visitorsList.isEmpty()) {
            result = visitorsList.stream()
                    .filter(me -> me.getHostEmployee().getId() == hostEmployeeId)
                    .collect(Collectors.toList());

        }
        return result;
    }

}