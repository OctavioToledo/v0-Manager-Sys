package com.demoV1Project.domain.model;

import com.demoV1Project.util.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointments")
public class Appointment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppointmentStatus status;

    @Column(nullable = false, name = "appointment_date")
    private LocalDate appointmentDate;

    @Column(nullable = false, name = "start_time", length = 5)
    private String startTime;

    @Column(nullable = false, name = "end_time", length = 5)
    private String endTime;

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = AppointmentStatus.PENDING;
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    @JsonIgnore
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
}
