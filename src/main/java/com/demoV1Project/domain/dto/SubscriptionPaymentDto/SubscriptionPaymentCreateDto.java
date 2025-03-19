package com.demoV1Project.domain.dto.SubscriptionPaymentDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPaymentCreateDto {
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private LocalDateTime endDate;
    private String paymentMethod;
    private String status;
    private String description;
    private Long userId; // ID del usuario
}
