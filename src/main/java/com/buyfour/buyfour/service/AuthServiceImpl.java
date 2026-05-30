package com.buyfour.buyfour.service;

import com.buyfour.buyfour.dto.*;
import com.buyfour.buyfour.entity.*;
import com.buyfour.buyfour.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final OtpTokenRepository otpTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ModelMapper modelMapper;

    // ✅ ENTITY → DTO  back end to front end
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();

        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setVerified(user.isVerified());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setProfileImage(user.getProfileImage());
        dto.setAddress(user.getAddress());
        dto.setActive(user.getActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setRole(user.getRole());

        return dto;
    }

    // ✅ DTO → ENTITY   front end  to  back end
    private User convertToEntity(UserDto dto) {
        User user = new User();

        user.setUserId(dto.getUserId());
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setVerified(dto.isVerified());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setProfileImage(dto.getProfileImage());
        user.setAddress(dto.getAddress());
        user.setActive(dto.getActive());
        user.setCreatedAt(dto.getCreatedAt());
        user.setRole(dto.getRole());

        return user;
    }

    // ✅ SIGNUP
    @Override
    public SignupResponseDto signup(SignupRequestDto dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = User.builder()
                .userName(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole() != null ? dto.getRole() : Role.USER)
                .verified(true)
                .active(true)

                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        emailService.sendEmail(
                user.getEmail(),
                "Welcome to Buy4",
                "Hi " + user.getUserName() + ",\n\nWelcome to the Buy4 App!"
        );

        return SignupResponseDto.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .build();
    }

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String redirectUrl = switch (user.getRole()) {
            case USER -> "/user";
            case ADMIN -> "/admin";
        };

        return new LoginResponseDto(
                "Login successful",
                user.getRole().name(),
                redirectUrl,
                user.getUserId(),          // ✅ CRITICAL FIX
                user.getUserName(),
                user.getEmail()
        );
    }

    // ✅ LOGOUT
    @Override
    public String logout(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return "Logout successful";
    }


    // ✅ FORGOT PASSWORD
    @Override
    public void forgotPassword(String email) {

        userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not registered"));

        otpTokenRepository.deleteByEmail(email);

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        OtpToken token = OtpToken.builder()
                .email(email)
                .otp(otp)
                .expiryTime(LocalDateTime.now().plusMinutes(2))
                .build();

        otpTokenRepository.save(token);

        emailService.sendEmail(
                email,
                "Buy4 Password Reset OTP",
                "Your OTP is: " + otp
        );
    }

    // ✅ VERIFY OTP
    @Override
    public void verifyOtp(String email, String otp) {

        OtpToken token = otpTokenRepository
                .findTopByEmailOrderByExpiryTimeDesc(email)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (token.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        if (!token.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        otpTokenRepository.delete(token);
    }

    // ✅ RESET PASSWORD
    @Override
    public void resetPassword(String email, String newPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        otpTokenRepository.deleteByEmail(email);
    }

    // ✅ PROFILE IMAGE METHODS
    @Override
    public void addProfileImage(Long userId, String imageUrl) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getProfileImage() != null) {
            throw new RuntimeException("Profile image already exists. Use update.");
        }

        user.setProfileImage(imageUrl);
        userRepository.save(user);
    }

    @Override
    public void updateProfileImage(Long userId, String imageUrl) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getProfileImage() == null) {
            throw new RuntimeException("No profile image found. Use add first.");
        }

        user.setProfileImage(imageUrl);
        userRepository.save(user);
    }

    @Override
    public void deleteProfileImage(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getProfileImage() == null) {
            throw new RuntimeException("Profile image not found");
        }

        user.setProfileImage(null);
        userRepository.save(user);
    }

    @Override
    public String uploadProfileImage(Long userId, MultipartFile file) {

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        try {
            String uploadDir = "uploads/";

            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // ✅ Clean filename
            String originalName = file.getOriginalFilename();
            String cleanName = originalName != null
                    ? originalName.replaceAll("\\s+", "_")
                    : "image";

            String fileName = System.currentTimeMillis() + "_" + cleanName;

            Path filePath = Paths.get(uploadDir, fileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            String imageUrl = "http://localhost:9090/uploads/" + fileName;

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setProfileImage(imageUrl);
            userRepository.save(user);

            return imageUrl;

        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }



    @Override
    public String getProfileImage(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getProfileImage() == null) {
            throw new RuntimeException("Profile image not available");
        }

        return user.getProfileImage();
    }


}
