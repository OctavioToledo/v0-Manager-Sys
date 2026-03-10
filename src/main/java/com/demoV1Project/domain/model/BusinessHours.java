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

        private Integer dayOfWeek;
        private Boolean isWorkingDay;

        @Column(name = "morning_start")
        private LocalTime morningStart;

        @Column(name = "morning_end")
        private LocalTime morningEnd;

        @Column(name = "afternoon_start")
        private LocalTime afternoonStart;

        @Column(name = "afternoon_end")
        private LocalTime afternoonEnd;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "business_id")
        private Business business;
}
