package com.havrun.bookTrackerAPI.DTO.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class VerificationDTO {

    @NotBlank(message = "Verification code is required")
    private String verificationCode;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
}
