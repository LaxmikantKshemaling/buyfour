package com.buyfour.buyfour.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupResponseDto {
    private String userName;
    private String email;
}
