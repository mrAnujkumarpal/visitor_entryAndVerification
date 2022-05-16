package com.vevs.entity.virtualObject;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by Anuj kumar pal
 */
@Data
public class LoginRequest {


    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
