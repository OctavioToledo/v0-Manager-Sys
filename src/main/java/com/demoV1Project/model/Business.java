package com.demoV1Project.model;

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



    public Long getUserId() {
        return user != null ? user.getId() : null; // Exponer solo el ID del usuario
    }



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

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "service_id")
    private Service service;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private List<Transaction> transactions;



}
