package com.demoV1Project.domain.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "business_hours")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class BusinessHours extends Auditable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Enumerated(EnumType.STRING)
        private DayOfWeek dayOfWeek;

        @Column(name = "opening_morning_time")
        private LocalTime openingMorningTime;

        @Column(name = "closing_morning_time")
        private LocalTime closingMorningTime;

        @Column(name = "opening_evening_time")
        private LocalTime openingEveningTime;

        @Column(name = "closing_evening_time")
        private LocalTime closingEveningTime;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "business_id")
        private Business business;
}
