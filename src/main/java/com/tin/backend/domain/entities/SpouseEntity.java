package com.tin.backend.domain.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="spousetable")
public class SpouseEntity {
    @Id
    @Column(name = "spouseTIN")
    private String spouseTIN;
    @Column(name = "spouseName",length = 50)
    private String spouseName;
    @Column(name = "spouseEmpStatus",length = 50)
    private Integer spouseEmpStatus;
    @Column(name = "spouseEmpName",length = 50)
    private String spouseEmpName;
    @Column(name = "spouseEmpTIN", length = 12)
    private String spouseEmpTIN;
    @OneToOne
    @JoinColumn(name = "tinid")
    private TaxpayerEntity taxpayer;
}
