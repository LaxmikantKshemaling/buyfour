package com.buyfour.buyfour.dto;

import lombok.*;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {

    private String message;
    private String role;
    private String redirectUrl;

    private Long userId;          // ✅ ADD THIS
    private String userName;      // ✅ OPTIONAL (nice UX)
    private String email;
}


