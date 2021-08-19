package com.vevs.entity.vo;

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
