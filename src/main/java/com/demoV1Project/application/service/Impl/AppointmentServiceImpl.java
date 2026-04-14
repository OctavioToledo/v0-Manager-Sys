package com.demoV1Project.application.service.Impl;

import com.demoV1Project.application.exceptions.InvalidAppointmentGridException;
import com.demoV1Project.application.service.*;
import com.demoV1Project.domain.dto.AppointmentDto.AppointmentCreateDto;
import com.demoV1Project.domain.dto.AppointmentGridDto.AppointmentGridDto;
import com.demoV1Project.domain.dto.AppointmentGridDto.EmployeeScheduleGridDto;
import com.demoV1Project.domain.dto.AppointmentGridDto.TimeSlotDto;
import com.demoV1Project.domain.model.*;
import com.demoV1Project.domain.repository.*;
import com.demoV1Project.util.enums.AppointmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

	private final AppointmentRepository appointmentRepository;
	private final EmployeeService employeeService;
	private final EmployeeRepository employeeRepository;
	private final ServiceService serviceService;
	private final UserService userService;
	private final BusinessService businessService;
	private final BusinessHoursRepository businessHoursRepository;
	private final EmployeeWorkScheduleRepository employeeWorkScheduleRepository;
	private final BusinessHolidayRepository businessHolidayRepository;
	private final AppointmentSlotGenerator appointmentSlotGenerator;

	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	@Override
	public List<Appointment> findAll() {
		return appointmentRepository.findAll();
	}

	@Override
	public org.springframework.data.domain.Page<Appointment> findAll(
			org.springframework.data.domain.Pageable pageable) {
		return appointmentRepository.findAll(pageable);
	}

	@Override
	public Optional<Appointment> findById(Long id) {
		return appointmentRepository.findById(id);
	}

	@Override
	public void save(Appointment appointment) {
		// Normalización de tiempos (HH:mm)
		LocalTime start = LocalTime.parse(appointment.getStartTime(), TIME_FORMATTER);
		LocalTime end = LocalTime.parse(appointment.getEndTime(), TIME_FORMATTER);

		validateAppointmentWithinSchedules(appointment.getAppointmentDate(), start, end,
				appointment.getBusiness().getId(), appointment.getEmployee().getId());

		appointmentRepository.save(appointment);
	}

	@Override
	public void deleteById(Long id) {
		appointmentRepository.deleteById(id);
	}

	@Override
	public boolean checkCollision(Long employeeId, LocalDate date, String startTime, String endTime) {
		return !appointmentRepository.findOverlappingAppointments(employeeId, date, startTime, endTime)
				.isEmpty();
	}

	@Override
	public boolean checkCollisionWithApproved(Long employeeId, LocalDate date, String startTime, String endTime) {
		return appointmentRepository.countOverlappingApprovedAppointments(employeeId, date, startTime,
				endTime) > 0;
	}

	@Override
	public Appointment createPendingAppointment(AppointmentCreateDto dto) {
		Employee employee = employeeService.findById(dto.getEmployeeId())
				.orElseThrow(() -> new IllegalArgumentException("Employee not found"));

		Business business = businessService.findById(dto.getBusinessId())
				.orElseThrow(() -> new IllegalArgumentException("Business not found"));

		com.demoV1Project.domain.model.Service service = serviceService
				.findById(dto.getServiceId())
				.orElseThrow(() -> new IllegalArgumentException("Service not found"));

		User user = null;
		if (dto.getUserId() != null) {
			user = userService.findById(dto.getUserId())
					.orElseThrow(() -> new IllegalArgumentException("User not found"));
		}

		if (!employee.getBusiness().getId().equals(business.getId())) {
			throw new IllegalArgumentException("El empleado no pertenece al negocio");
		}
		if (!service.getBusiness().getId().equals(business.getId())) {
			throw new IllegalArgumentException("El servicio no pertenece al negocio");
		}

		// Normalización a minutos para la comparación segura
		LocalTime start = LocalTime.parse(dto.getStartTime(), TIME_FORMATTER);
		LocalTime end = start.plusMinutes(service.getDuration());
		String calculatedEndTime = end.format(TIME_FORMATTER);

		AppointmentStatus targetStatus = dto.getStatus() != null ? AppointmentStatus.valueOf(dto.getStatus())
				: AppointmentStatus.PENDING_CONFIRMATION;

		// VALIDACIÓN DE COLISIÓN (Anti-sobre-turnos)
		if (targetStatus != AppointmentStatus.APPROVED) {
			boolean collision = checkCollisionWithApproved(employee.getId(), dto.getAppointmentDate(), dto.getStartTime(),
					calculatedEndTime);
			if (collision) {
			    log.warn("Cita rechazada (Error 409 probable): Colisión con turno aprobado existente. Profesor: {}, Fecha: {}, {}-{}", employee.getId(), dto.getAppointmentDate(), dto.getStartTime(), calculatedEndTime);
				throw new IllegalArgumentException("El horario seleccionado ya está ocupado por otra cita confirmada.");
			}
		}

		// VALIDACIÓN DE NEGOCIO Y FERIADOS
		validateAppointmentWithinSchedules(dto.getAppointmentDate(), start, end, business.getId(), employee.getId());

		Appointment appointment = Appointment.builder()
				.appointmentDate(dto.getAppointmentDate())
				.startTime(dto.getStartTime())
				.endTime(calculatedEndTime)
				.status(targetStatus)
				.business(business)
				.employee(employee)
				.service(service)
				.user(user)
				.clientName(dto.getClientName())
				.clientPhone(dto.getClientPhone())
				.clientEmail(dto.getClientEmail())
				.notes(dto.getNotes())
				.build();

		return appointmentRepository.save(appointment);
	}

	private void validateAppointmentWithinSchedules(LocalDate date, LocalTime start, LocalTime end, Long businessId,
			Long employeeId) {
		// ISO-8601: 1=Lunes, 7=Domingo
		int dayOfWeek = date.getDayOfWeek().getValue();

		// 1. CHEQUEO DE FERIADOS / EXCEPCIONES
		List<BusinessHoliday> holidays = businessHolidayRepository.findByBusinessIdAndHolidayDate(businessId, date);
		for (BusinessHoliday holiday : holidays) {
			if (Boolean.TRUE.equals(holiday.getIsFullDay())) {
				log.warn("Cita rechazada: El negocio se encuentra cerrado por feriado (Fecha: {})", date);
				throw new IllegalArgumentException("El negocio se encuentra cerrado por feriado/excepción.");
			}
			// Si es feriado parcial, validamos horas (el feriado significa que el negocio está CERRADO en esas horas)
			if (holiday.getStartTime() != null && holiday.getEndTime() != null) {
				if (start.isBefore(holiday.getEndTime()) && end.isAfter(holiday.getStartTime())) {
					log.warn("Cita rechazada: Horario {} - {} interseca con cierre parcial por feriado de {} a {}", 
							start, end, holiday.getStartTime(), holiday.getEndTime());
					throw new IllegalArgumentException("El horario solicitado choca con un cierre parcial del negocio (" +
							holiday.getStartTime() + " - " + holiday.getEndTime() + ")");
				}
			}
		}

		// 2. HORARIOS BASE DEL NEGOCIO (Los limites los establece el negocio)
		BusinessHours bHours = businessHoursRepository.findByBusinessIdAndDayOfWeek(businessId, dayOfWeek)
				.orElseThrow(() -> new IllegalArgumentException(
						"El negocio no tiene horarios configurados para este día (" + dayOfWeek + ")"));

		if (!Boolean.TRUE.equals(bHours.getIsWorkingDay())) {
			log.warn("Cita rechazada: Día no laborable para el negocio (Fecha {})", date);
			throw new IllegalArgumentException("El negocio no opera el día seleccionado.");
		}

		boolean fitsInBusiness = isWithinShift(start, end, bHours.getMorningStart(), bHours.getMorningEnd())
				|| isWithinShift(start, end, bHours.getAfternoonStart(), bHours.getAfternoonEnd());

		if (!fitsInBusiness) {
			log.warn("Cita rechazada: Cita fuera de horarios del negocio ({} a {}) para fecha {}", start, end, date);
			throw new IllegalArgumentException("Cita fuera del horario de apertura del negocio.");
		}

		// 3. HORARIO DEL PROFESIONAL (Debe estar dentro del negocio)
		EmployeeWorkSchedule eHours = employeeWorkScheduleRepository.findByEmployeeIdAndDayOfWeek(employeeId, dayOfWeek);
		if (eHours != null) {
		    if (!Boolean.TRUE.equals(eHours.getIsWorkingDay())) {
			    log.warn("Cita rechazada: El profesional no trabaja el día {}", date);
		        throw new IllegalArgumentException("El profesional no tiene horarios configurados o no trabaja el día seleccionado.");
		    }
			boolean fitsInEmployee = isWithinShift(start, end, eHours.getMorningStart(), eHours.getMorningEnd())
					|| isWithinShift(start, end, eHours.getAfternoonStart(), eHours.getAfternoonEnd());

			if (!fitsInEmployee) {
			    log.warn("Cita rechazada: Profesional ocupado u horario fuera de su jornada ({} a {})", start, end);
			    throw new IllegalArgumentException("Cita fuera de la disponibilidad del profesional para este día.");
			}
		}

	}

	private boolean isWithinShift(LocalTime start, LocalTime end, LocalTime shiftStart, LocalTime shiftEnd) {
		if (shiftStart == null || shiftEnd == null)
			return false;
		// Normalización: ignoramos segundos/nanos para la comparación
		start = start.withSecond(0).withNano(0);
		end = end.withSecond(0).withNano(0);
		shiftStart = shiftStart.withSecond(0).withNano(0);
		shiftEnd = shiftEnd.withSecond(0).withNano(0);

		return !start.isBefore(shiftStart) && !end.isAfter(shiftEnd);
	}

	@Override
	public AppointmentGridDto getAppointmentGrid(Long serviceId, LocalDate date) {
		com.demoV1Project.domain.model.Service service = serviceService.findById(serviceId)
				.orElseThrow(() -> new InvalidAppointmentGridException("Service not found"));
		int duration = service.getDuration();

		List<Employee> employees = employeeRepository.findByServicesId(serviceId);

		int dayOfWeek = date.getDayOfWeek().getValue();
		List<BusinessHoliday> holidays = businessHolidayRepository.findByBusinessIdAndHolidayDate(service.getBusiness().getId(),
				date);
		for (BusinessHoliday holiday : holidays) {
			if (Boolean.TRUE.equals(holiday.getIsFullDay())) {
				throw new InvalidAppointmentGridException("El negocio se encuentra cerrado por feriado el día seleccionado.");
			}
		}

		BusinessHours businessHours = businessHoursRepository
				.findByBusinessIdAndDayOfWeek(service.getBusiness().getId(), dayOfWeek)
				.orElseThrow(() -> new InvalidAppointmentGridException("No se encontraron horarios disponibles"));

		if (!businessHours.getIsWorkingDay()) {
			throw new InvalidAppointmentGridException("El negocio no opera el día seleccionado");
		}

		// Usamos la duración del slot configurada en el negocio para la grilla (ej: incrementos de 30m)
		// aunque el servicio dure 45m.
		int slotDuration = service.getBusiness().getSlotDuration();
		if (slotDuration <= 0)
			slotDuration = 30;

		List<TimeSlotDto> masterTimeSlots = new ArrayList<>();
		generateSlotsForRange(masterTimeSlots, businessHours.getMorningStart(), businessHours.getMorningEnd(), slotDuration,
				duration);
		generateSlotsForRange(masterTimeSlots, businessHours.getAfternoonStart(), businessHours.getAfternoonEnd(),
				slotDuration, duration);

		// Aplicar excepciones de feriado parcial a la master list
		for (BusinessHoliday holiday : holidays) {
			if (holiday.getStartTime() != null && holiday.getEndTime() != null) {
				masterTimeSlots.removeIf(slot -> !isWithinShift(slot.getStartTime(), slot.getEndTime(), holiday.getStartTime(),
						holiday.getEndTime()));
			}
		}

		List<EmployeeScheduleGridDto> employeeScheduleGridDtos = new ArrayList<>();

		for (Employee employee : employees) {
			EmployeeWorkSchedule schedule = employeeWorkScheduleRepository
					.findByEmployeeIdAndDayOfWeek(employee.getId(), dayOfWeek);
			List<Appointment> takenAppointments = appointmentRepository
					.findByEmployeeIdAndAppointmentDate(employee.getId(), date);

			List<String> validSlots = appointmentSlotGenerator.generateAvailableSlots(date, duration, slotDuration,
					businessHours, schedule, takenAppointments, holidays);

			List<TimeSlotDto> employeeSpecificSlots = new ArrayList<>();
			for (TimeSlotDto genericSlot : masterTimeSlots) {
				boolean isAvailableForEmployee = validSlots
						.contains(genericSlot.getStartTime().format(TIME_FORMATTER));
				employeeSpecificSlots.add(TimeSlotDto.builder()
						.startTime(genericSlot.getStartTime())
						.endTime(genericSlot.getEndTime())
						.available(isAvailableForEmployee)
						.build());
			}

			employeeScheduleGridDtos.add(new EmployeeScheduleGridDto(employee.getId(), employee.getName(),
					employeeSpecificSlots));
		}

		return AppointmentGridDto.builder()
				.serviceName(service.getName())
				.serviceDuration(duration)
				.timeSlots(masterTimeSlots)
				.employeeAvailabilities(employeeScheduleGridDtos)
				.build();
	}

	@Override
	public List<Appointment> findByBusinessIdAndDate(Long businessId, LocalDate date) {
		return appointmentRepository.findByBusinessIdAndAppointmentDate(businessId, date);
	}

	@Override
	public List<Appointment> findByBusinessIdAndDateRange(Long businessId, LocalDate startDate, LocalDate endDate) {
		return appointmentRepository.findByBusinessIdAndAppointmentDateBetween(businessId, startDate, endDate);
	}

	@Override
	public List<Appointment> findByEmployeeIdAndDate(Long employeeId, LocalDate date) {
		return appointmentRepository.findByEmployeeIdAndAppointmentDate(employeeId, date);
	}

	private void generateSlotsForRange(List<TimeSlotDto> slots, LocalTime start, LocalTime end, int interval, int duration) {
		if (start == null || end == null)
			return;
		LocalTime current = start;
		while (!current.plusMinutes(duration).isAfter(end)) {
			slots.add(TimeSlotDto.builder()
					.startTime(current)
					.endTime(current.plusMinutes(duration))
					.available(true)
					.build());
			current = current.plusMinutes(interval);
		}
	}
}
