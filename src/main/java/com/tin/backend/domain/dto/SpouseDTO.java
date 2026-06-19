package com.tin.backend.domain.dto;

import com.tin.backend.domain.entities.TaxpayerEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpouseDTO {
    /*@NotBlank(message = "SpouseTIN is required")*/
    @Size(max = 12)
    private String spouseTIN;
    /*@NotBlank(message = "spouseName is required")*/
    @Size(max = 50)
    private String spouseName;
    /*@NotNull(message = "spouseEmpStatus is required")*/
    @Min(value = 1, message = "Value of spouseEmpStatus must be at least 1")
    @Max(value = 4, message = "Value of spouseEmpStatus is at most 4")
    private Integer spouseEmpStatus;
    /*@NotBlank(message = "spouseEmpName is required")*/
    @Size(max = 50)
    private String spouseEmpName;
    private TaxpayerDTO taxpayer;
    private String spouseEmpTIN;
}
