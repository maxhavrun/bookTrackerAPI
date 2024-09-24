package com.havrun.bookTrackerAPI.Services;

import com.havrun.bookTrackerAPI.DTO.auth.VerificationDTO;
import com.havrun.bookTrackerAPI.Repository.UserRepository;
import com.havrun.bookTrackerAPI.exception.auth.InvalidVerificationCodeException;
import com.havrun.bookTrackerAPI.exception.auth.UserNotFoundExcepption;
import com.havrun.bookTrackerAPI.exception.auth.VerificationCodeExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Transactional
    public void verifyAccount(VerificationDTO request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundExcepption("User not found"));
        if(user.getVerificationCodeExpiresAt().isBefore(java.time.LocalDateTime.now())){
            throw new VerificationCodeExpiredException("Verification code expired");
        }
        if(!user.getVerificationCode().equals(request.getVerificationCode())){
            throw new InvalidVerificationCodeException("Invalid verification code");
        }
        user.setAccountEnabled(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiresAt(null);
        userRepository.save(user);
    }

    @Transactional
    public void resendVerificationCode(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundExcepption("User not found"));

        if (user.isAccountEnabled()) {
            throw new UserNotFoundExcepption("User already verified");
        }

        user.setVerificationCode(emailService.generateVerificationCode());
        user.setVerificationCodeExpiresAt(emailService.generateVerificationCodeExpiration());
        emailService.sendVerificationEmail(user);
        userRepository.save(user);
    }

}
