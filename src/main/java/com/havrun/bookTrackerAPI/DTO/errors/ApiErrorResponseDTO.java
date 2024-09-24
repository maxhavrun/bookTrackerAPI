package com.havrun.bookTrackerAPI.DTO.errors;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorResponseDTO {
    private String errorCode;
    private String message;
}
