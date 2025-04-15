package com.demoV1Project.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "businesses")
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String logo;

    @Column(name = "opening_hours")
    private String openingHours;

    @Column(name = "work_days")
    private String workDays;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne()
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<BusinessHours> businessHours;

    @OneToMany(
            mappedBy = "business",
            cascade = CascadeType.ALL
    )
    private List<Employee> employees;

    @OneToMany(
            mappedBy = "business", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Service> services;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private List<Transaction> transactions;


}
