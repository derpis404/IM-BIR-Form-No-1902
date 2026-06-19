package com.tin.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurEmpDTO {

    @NotBlank(message = "currentEmpTIN is required")
    @Size(max = 12)
    private String currentEmpTIN;

    @NotBlank(message = "currentEmpName is required")
    @Size(max = 50)
    private String currentEmpName;

    @NotBlank(message = "currentEmpAddress is required")
    @Size(max = 50)
    private String currentEmpAddress;

    @NotBlank(message = "empContactDetails is required")
    @Size(max = 12)
    private String empContactDetails;
}