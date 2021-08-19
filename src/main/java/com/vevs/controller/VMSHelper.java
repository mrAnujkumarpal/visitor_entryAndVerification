package com.vevs.controller;

import org.apache.commons.lang3.StringUtils;
import com.vevs.entity.common.Role;
import com.vevs.entity.employee.Users;

import java.util.Set;

public class VMSHelper {


    public String roleOfUser(Users user) {
        Set<Role> roles = user.getRoles();
        String roleName = StringUtils.EMPTY;
        for (Role r : roles) {
            roleName = r.getName().name();
        }
        return roleName;
    }






}
