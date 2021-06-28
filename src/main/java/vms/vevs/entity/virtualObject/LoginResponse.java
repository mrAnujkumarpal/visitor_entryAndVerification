package vms.vevs.entity.virtualObject;

import lombok.Data;

/**
 * Created by Anuj kumar pal
 */
@Data
public class LoginResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private String developer = "Anuj kumar pal";
    private Long loggedInUserId;

    public LoginResponse(String accessToken, Long loggedInUserId) {
        this.accessToken = accessToken;
        this.loggedInUserId = loggedInUserId;
    }

}
