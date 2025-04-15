package com.demoV1Project.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "business_hours")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BusinessHours {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Enumerated(EnumType.STRING) // Guarda el nombre del día (ej: "MONDAY")
        private DayOfWeek dayOfWeek;

        private LocalTime openingTime; // Ej: 09:00
        private LocalTime closingTime; // Ej: 18:00

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "business_id")
        private Business business;



}
