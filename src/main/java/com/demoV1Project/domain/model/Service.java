package com.demoV1Project.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Table(name = "services")
public class Service extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer duration; // En minutos

    private String description;

    private Double price;

    private String category;

    @Builder.Default
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    @JsonIgnore
    private Business business;

    @ManyToMany(mappedBy = "services")
    @JsonIgnore
    @Builder.Default
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Appointment> appointments;

}
