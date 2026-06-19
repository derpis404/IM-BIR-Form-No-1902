package com.tin.backend.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "current_primary_employer")
public class CurEmpEntity {

    @Id
@Column(length = 12)
private String currentEmpTIN;

    private String currentEmpName;
    private String currentEmpAddress;
    private String empContactDetails;

    @OneToOne(mappedBy = "currentEmployer")
@JsonIgnore
private TaxpayerEntity taxpayer;
}