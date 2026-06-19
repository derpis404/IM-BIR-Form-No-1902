package com.tin.backend.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrevEmpDTO {

    @NotBlank
    @Size(max = 12)
    @JsonProperty("MultiEmpTIN")
    private String multiEmpTIN;

    @NotBlank
    @Size(max = 50)
    @JsonProperty("multiEmpName")
    private String multiEmpName;

    @JsonProperty("taxpayerIds")
    private Set<String> taxpayerIds;
}