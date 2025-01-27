package com.demoV1Project.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "Addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String streetNumber;
    private String city;
    private String province;
    private String country;

    @OneToOne(mappedBy = "address",  cascade = CascadeType.ALL)
    @JsonIgnore
    private Business business;


}

