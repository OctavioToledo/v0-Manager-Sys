package com.demoV1Project.application.service;

import com.demoV1Project.domain.model.Appointment;
import com.demoV1Project.domain.model.BusinessHours;
import com.demoV1Project.domain.model.EmployeeWorkSchedule;
import com.demoV1Project.util.enums.AppointmentStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentSlotGenerator {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public List<String> generateAvailableSlots(
            LocalDate date,
            int serviceDurationMinutes,
            BusinessHours business,
            EmployeeWorkSchedule employee,
            List<Appointment> existingAppointments) {
        List<String> availableSlots = new ArrayList<>();

        if (business == null || !Boolean.TRUE.equals(business.getIsWorkingDay()) ||
                employee == null || !Boolean.TRUE.equals(employee.getIsWorkingDay())) {
            return availableSlots;
        }

        LocalTime morningStart = getLatest(business.getMorningStart(), employee.getMorningStart());
        LocalTime morningEnd = getEarliest(business.getMorningEnd(), employee.getMorningEnd());

        if (morningStart != null && morningEnd != null && morningStart.isBefore(morningEnd)) {
            availableSlots.addAll(
                    calculateSlotsForShift(morningStart, morningEnd, serviceDurationMinutes, existingAppointments));
        }

        LocalTime afternoonStart = getLatest(business.getAfternoonStart(), employee.getAfternoonStart());
        LocalTime afternoonEnd = getEarliest(business.getAfternoonEnd(), employee.getAfternoonEnd());

        if (afternoonStart != null && afternoonEnd != null && afternoonStart.isBefore(afternoonEnd)) {
            availableSlots.addAll(
                    calculateSlotsForShift(afternoonStart, afternoonEnd, serviceDurationMinutes, existingAppointments));
        }

        return availableSlots;
    }

    private List<String> calculateSlotsForShift(
            LocalTime shiftStart,
            LocalTime shiftEnd,
            int durationMinutes,
            List<Appointment> existingAppointments) {
        List<String> shiftSlots = new ArrayList<>();
        LocalTime currentSlotStart = shiftStart;

        while (!currentSlotStart.plusMinutes(durationMinutes).isAfter(shiftEnd)) {
            LocalTime currentSlotEnd = currentSlotStart.plusMinutes(durationMinutes);

            boolean hasOverlap = false;

            for (Appointment appt : existingAppointments) {
                if (appt.getStatus() == AppointmentStatus.REJECTED || appt.getStatus() == AppointmentStatus.CANCELLED) {
                    continue;
                }

                LocalTime apptStart = LocalTime.parse(appt.getStartTime(), TIME_FORMATTER);
                LocalTime apptEnd = LocalTime.parse(appt.getEndTime(), TIME_FORMATTER);

                if (currentSlotStart.isBefore(apptEnd) && currentSlotEnd.isAfter(apptStart)) {
                    hasOverlap = true;
                    break;
                }
            }

            if (!hasOverlap) {
                shiftSlots.add(currentSlotStart.format(TIME_FORMATTER));
            }

            currentSlotStart = currentSlotStart.plusMinutes(durationMinutes);
        }

        return shiftSlots;
    }

    private LocalTime getLatest(LocalTime t1, LocalTime t2) {
        if (t1 == null)
            return t2;
        if (t2 == null)
            return t1;
        return t1.isAfter(t2) ? t1 : t2;
    }

    private LocalTime getEarliest(LocalTime t1, LocalTime t2) {
        if (t1 == null)
            return t2;
        if (t2 == null)
            return t1;
        return t1.isBefore(t2) ? t1 : t2;
    }
}
