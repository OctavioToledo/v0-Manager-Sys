package com.demoV1Project.domain.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "employee_work_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmployeeWorkSchedule extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer dayOfWeek;
    private Boolean isWorkingDay;

    private LocalTime morningStart;
    private LocalTime morningEnd;

    private LocalTime afternoonStart;
    private LocalTime afternoonEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Employee employee;
}
