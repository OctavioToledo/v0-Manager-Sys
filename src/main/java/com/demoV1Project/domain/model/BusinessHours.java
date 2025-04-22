package com.demoV1Project.domain.model;

import com.demoV1Project.util.enums.DayOfWeek;
import jakarta.persistence.*;
import lombok.*;

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

        @Column(name = "opening_morning_time")
        private LocalTime openingMorningTime; // Ej: 09:00

        @Column(name = "closing_morning_time")
        private LocalTime closingMorningTime; // Ej: 12:30

        @Column(name = "opening_evening_time")
        private LocalTime openingEveningTime; // Ej: 16:00

        @Column(name = "closing_evening_time")
        private LocalTime closingEveningTime; // Ej: 20:00


        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "business_id")
        private Business business;



}
