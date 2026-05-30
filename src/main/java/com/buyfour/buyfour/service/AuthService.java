package com.buyfour.buyfour.service;

import com.buyfour.buyfour.dto.*;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {

    SignupResponseDto signup(SignupRequestDto dto);

    LoginResponseDto login(LoginRequestDto dto);

    void forgotPassword(String email);

    void verifyOtp(String email, String otp);

    void resetPassword(String email, String newPassword);

    void  addProfileImage(Long userId, String imageUrl);

    void  updateProfileImage(Long userId, String imageUrl);

    void  deleteProfileImage(Long userId);

    String getProfileImage(Long userId);

    String logout(String email);

    String uploadProfileImage(Long userId, MultipartFile file);
}
