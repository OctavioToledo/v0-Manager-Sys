package com.demoV1Project.util.enums.Auxiliar;

import com.demoV1Project.domain.dto.AppointmentGridDto.TimeSlotDto;
import com.demoV1Project.domain.model.Appointment;
import com.demoV1Project.domain.model.BusinessHours;
import com.demoV1Project.domain.model.EmployeeWorkSchedule;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotUtils {

    public static List<TimeSlotDto> generateTimeSlots(BusinessHours hours, int durationInMinutes) {
        List<TimeSlotDto> slots = new ArrayList<>();

        if (!hours.getIsWorkingDay())
            return slots;

        if (hours.getMorningStart() != null && hours.getMorningEnd() != null) {
            slots.addAll(generateRange(hours.getMorningStart(), hours.getMorningEnd(), durationInMinutes));
        }

        if (hours.getAfternoonStart() != null && hours.getAfternoonEnd() != null) {
            slots.addAll(generateRange(hours.getAfternoonStart(), hours.getAfternoonEnd(), durationInMinutes));
        }

        return slots;
    }

    public static List<TimeSlotDto> generateRange(LocalTime start, LocalTime end, int durationInMinutes) {
        List<TimeSlotDto> slots = new ArrayList<>();
        LocalTime current = start;

        while (!current.plusMinutes(durationInMinutes).isAfter(end)) {
            slots.add(
                    TimeSlotDto.builder()
                            .startTime(current)
                            .endTime(current.plusMinutes(durationInMinutes))
                            .available(true) // el valor real de disponibilidad se ajusta más adelante
                            .appointmentId(null)
                            .build());
            current = current.plusMinutes(durationInMinutes);
        }

        return slots;
    }

    public static List<Boolean> calculateAvailability(
            List<TimeSlotDto> slots,
            EmployeeWorkSchedule schedule,
            List<Appointment> takenAppointments,
            int serviceDuration) {
        List<Boolean> result = new ArrayList<>();

        for (TimeSlotDto slot : slots) {
            boolean isWithinSchedule = isSlotWithinSchedule(slot, schedule);
            boolean isTaken = isSlotTaken(slot, takenAppointments);
            result.add(isWithinSchedule && !isTaken);
        }

        return result;
    }

    private static boolean isSlotWithinSchedule(TimeSlotDto slot, EmployeeWorkSchedule schedule) {
        if (!schedule.getIsWorkingDay())
            return false;

        // Verificar ambos bloques, mañana y tarde
        return ((schedule.getMorningStart() != null && schedule.getMorningEnd() != null &&
                !slot.getStartTime().isBefore(schedule.getMorningStart()) &&
                !slot.getEndTime().isAfter(schedule.getMorningEnd()))
                ||
                (schedule.getAfternoonStart() != null && schedule.getAfternoonEnd() != null &&
                        !slot.getStartTime().isBefore(schedule.getAfternoonStart()) &&
                        !slot.getEndTime().isAfter(schedule.getAfternoonEnd())));
    }

    private static boolean isSlotTaken(TimeSlotDto slot, List<Appointment> appointments) {
        return appointments.stream().anyMatch(app -> {
            // Convertir Date a LocalTime
            LocalTime appStartTime = app.getDate().toLocalTime();
            LocalTime appEndTime = appStartTime.plusMinutes(app.getService().getDuration());

            // Verificar si el slot se cruza con una cita
            return !slot.getEndTime().isBefore(appStartTime) && !slot.getStartTime().isAfter(appEndTime);
        });
    }
}
