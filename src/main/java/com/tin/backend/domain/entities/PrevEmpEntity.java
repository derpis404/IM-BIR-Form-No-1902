package com.tin.backend.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "previous_concurrent_employer")
public class PrevEmpEntity {

    @Id
    @Column(name = "MultiEmpTIN")
    private String MultiEmpTIN;

    @Column(name = "multiEmpName", length = 50)
    private String multiEmpName;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST}
    )
    @JoinTable(
            name = "prevCon_employers_and_taxpayers",
            joinColumns = @JoinColumn(name = "MultiEmpTIN"),
            inverseJoinColumns = @JoinColumn(name = "TinID")
    )
    private Set<TaxpayerEntity> taxpayers = new HashSet<>();
}