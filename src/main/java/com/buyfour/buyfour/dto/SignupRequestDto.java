package com.buyfour.buyfour.dto;

import com.buyfour.buyfour.entity.Role;
import lombok.Data;

@Data
public class SignupRequestDto {
    private String name;
    private String email;
    private String password;
    private Role role;
}
