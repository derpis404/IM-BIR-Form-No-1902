package com.tin.backend.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tin.backend.domain.dto.SpouseDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="taxpayer_information")
public class TaxpayerEntity {

    @Id
    @Column(name="tinid")
    private String tinID;
    @Column(name="payerName",length = 50)
    private String payerName;
    @Column(name="payerType")
    private Integer payerType;
    @Column(name="gender")
    private Integer gender;
    @Column(name="civilStatus")
    private Integer civilStatus;
    @Column(name = "birthday", columnDefinition = "DATE")
    private Date birthday;
    @Column(name="birthPlace",length = 100)
    private String birthPlace;
    @Column(name="motherName",length = 50)
    private String motherName;
    @Column(name="fatherName",length = 50)
    private String fatherName;
    @Column(name="citizenship",length = 50)
    private String citizenship;
    @Column(name="address",length = 100)
    private String address;
    @Column(name="idType",length = 50)
    private String idType;
    @Column(name="idNumber",length = 20)
    private String idNumber;
    @Column(name = "idEffectiveDate", columnDefinition = "DATE")
    private Date idEffectiveDate;
    @Column(name = "idExpiryDate", columnDefinition = "DATE")
    private Date idExpiryDate;
    @Column(name="contactNumber",length = 12)
    private String contactNumber;
    @Column(name="multiEmpType")
    private Integer multiEmpType;
    @Column(name = "hiredDate", columnDefinition = "DATE")
    private Date hiredDate;
    @OneToOne(mappedBy = "taxpayer")
    private SpouseEntity spouse;
    @ManyToMany(mappedBy = "taxpayers", fetch = FetchType.LAZY)
    private Set<PrevEmpEntity> prevEmp;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CurrentEmpTIN")
    private CurEmpEntity currentEmployer;
    //@JoinColumn(name="CurrentEmpTIN", insertable = true, updatable = true, referencedColumnName = "CurrentEmpTIN")
    //private CurEmpEntity currentEmployer;
    
}
