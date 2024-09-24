package com.havrun.bookTrackerAPI.DTO.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponseDTO {
    private String email;
    private String username;
    private String role;
    private String message;
}
