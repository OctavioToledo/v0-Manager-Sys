package com.demoV1Project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")

    private Category category;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")

    private Address address;
}
