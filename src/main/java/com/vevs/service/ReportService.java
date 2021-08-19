package com.vevs.service;

import org.springframework.stereotype.Service;
import com.vevs.entity.vo.ReportRequestVO;
import com.vevs.entity.visitor.Visitor;

import java.util.List;

@Service
public interface ReportService {


    List<Visitor> todayVisitorList(Long loggedInUserId);

    List<Visitor> searchVisitorList(ReportRequestVO request, Long userId);

}
