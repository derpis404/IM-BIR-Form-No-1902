package com.tin.backend.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class TaxpayerDTO {

    @NotBlank(message = "TinID is required")
    @Size(max = 12)
    private String tinID;
    @NotBlank(message = "PayerName is required")
    private String payerName;
    @NotNull(message = "PayerType is required")
    @Min(value = 1, message = "Value of PayerType must at least be 1")
    @Max(value = 3, message = "Value of PayerType is at most 3")
    private Integer payerType;
    @NotNull(message = "gender is mandatory")
    @Min(value = 1, message = "Value of gender must be at least 1")
    @Max(value = 2, message = "Value of gender must be at most 2")
    private Integer gender;
    @NotNull(message = "civilStatus is mandatory")
    @Min(value = 1, message = "Value of civilStatus must be at least 1")
    @Max(value = 4, message = "Value of civilStatus must be at most 4")
    private Integer civilStatus;
    @NotNull(message = "Birthday is required")
    private Date birthday;
    @NotBlank(message = "Birthplace is required")
    @Size(max = 100)
    private String birthPlace;
    @Size(max = 50)
    private String motherName;
    @Size(max = 50)
    private String fatherName;
    @NotBlank(message = "Citizenship is required")
    @Size(max = 50)
    private String citizenship;
    @NotBlank(message = "Address is required")
    @Size(max = 100)
    private String address;
    @NotBlank(message = "IdType is required")
    @Size(max = 50)
    private String idType;
    @NotBlank(message = "IdNumber is required")
    @Size(max = 20)
    private String idNumber;
    @NotNull(message = "Id Effective Date is required")
    private Date idEffectiveDate;
    /*@NotNull(message = "Id Expiry Date is required")*/
    private Date idExpiryDate;
    @Size(max = 12)
    @NotBlank(message = "Contact Number is required")
    private String contactNumber;
    @Min(value = 1, message = "Value of MultiEmpType must be at least 1")
    @Max(value = 3, message = "Value of MultiEmpType must be at least 3")
    private Integer multiEmpType;
    @NotNull(message = "Hired Date is required")
    private Date hiredDate;
    private CurEmpDTO currentEmployer;

}
