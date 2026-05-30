package com.buyfour.buyfour.dto;


import com.buyfour.buyfour.entity.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class UserDto {



    private Long userId;
    private String userName;
    private String email;
    private String password;
    private boolean verified;
    private  String phoneNumber;

    private  String profileImage;
    private String address;
    private  Boolean active;

    private LocalDateTime createdAt;

    private Role role;
}

