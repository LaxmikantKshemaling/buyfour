package com.buyfour.buyfour.controller;

import com.buyfour.buyfour.dto.*;
import com.buyfour.buyfour.repository.UserRepository;
import com.buyfour.buyfour.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private   final UserRepository userRepository;


    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto dto) {

        SignupResponseDto response = authService.signup(dto);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {

        LoginResponseDto response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestParam String email) {
        return ResponseEntity.ok(userRepository.findByEmail(email).isPresent());
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String email) {

        String response = authService.logout(email);

        return ResponseEntity.ok(response);
    }



    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgot(@RequestParam String email) {

        authService.forgotPassword(email);
        return ResponseEntity.ok("OTP sent");
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<String> verify(@RequestParam String email,
                                         @RequestParam String otp) {

        authService.verifyOtp(email, otp);
        return ResponseEntity.ok("OTP verified");
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> reset(@RequestParam String email,
                                        @RequestParam String newPassword) {

        authService.resetPassword(email, newPassword);
        return ResponseEntity.ok("Password updated");
    }

    @PostMapping("/profile-image")
    public ResponseEntity<String> addProfileImage(
            @RequestParam Long userId,
            @RequestParam String imageUrl) {

        authService.addProfileImage(userId, imageUrl);
        return ResponseEntity.ok("Profile image added");
    }


    @PutMapping("/profile-image")
    public ResponseEntity<String> updateProfileImage(
            @RequestParam Long userId,
            @RequestParam String imageUrl) {

        authService.updateProfileImage(userId, imageUrl);
        return ResponseEntity.ok("Profile image updated");
    }


    @DeleteMapping("/profile-image")
    public ResponseEntity<String> deleteProfileImage(
            @RequestParam Long userId) {

        authService.deleteProfileImage(userId);
        return ResponseEntity.ok("Profile image deleted");
    }


    @GetMapping("/profile-image/{userId}")
    public ResponseEntity<String> getProfileImage(
            @PathVariable Long userId) {

        String imageUrl = authService.getProfileImage(userId);
        return ResponseEntity.ok(imageUrl);
    }

    @PostMapping("/profile-image/upload")
    public ResponseEntity<String> uploadProfileImage(
            @RequestParam Long userId,
            @RequestParam("file") MultipartFile file) {

        String imageUrl = authService.uploadProfileImage(userId, file);
        return ResponseEntity.ok(imageUrl);
    }

}
