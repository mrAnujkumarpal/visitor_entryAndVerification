package com.vevs.entity.virtualObject;

import com.vevs.common.util.VmsConstants;
import lombok.Data;

/**
 * Created by Anuj kumar pal
 */
@Data
public class LoginResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private String developer = VmsConstants.DEVELOPERS;
    private Long loggedInUserId;
    private String role;

    public LoginResponse(String accessToken, Long loggedInUserId,String role) {
        this.accessToken = accessToken;
        this.loggedInUserId = loggedInUserId;
        this.role=role;
    }

}
