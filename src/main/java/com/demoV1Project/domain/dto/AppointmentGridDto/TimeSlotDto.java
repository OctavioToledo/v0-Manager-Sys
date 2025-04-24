package com.demoV1Project.domain.dto.AppointmentGridDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotDto {

    private LocalTime startTime;
    private LocalTime endTime;
    private boolean available;
    private Long appointmentId;

    public TimeSlotDto(TimeSlotDto timeSlotDto, Boolean aBoolean) {
        this.startTime = timeSlotDto.startTime;
        this.endTime = timeSlotDto.endTime;
        this.available = aBoolean;
        this.appointmentId = timeSlotDto.appointmentId;
    }
}
