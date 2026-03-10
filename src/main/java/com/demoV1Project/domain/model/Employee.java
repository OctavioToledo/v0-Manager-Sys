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
@Table(name = "employees")
public class Employee extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String role;

    @Column(name = "profile_picture")
    private String profilePicture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    @JsonIgnore
    private Business business;

    @ManyToMany
    @JoinTable(name = "employee_service", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "service_id"))
    @Builder.Default
    private Set<Service> services = new HashSet<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<EmployeeWorkSchedule> workSchedules;

}
