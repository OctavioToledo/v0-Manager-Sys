package com.demoV1Project.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.demoV1Project.util.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Table(name = "users")
public class User extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "auth0_sub", unique = true)
    private String auth0Sub;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<Business> businessList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Appointment> appointments;
}
