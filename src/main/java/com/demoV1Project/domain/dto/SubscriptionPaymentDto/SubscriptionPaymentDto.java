package com.demoV1Project.domain.dto.SubscriptionPaymentDto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class SubscriptionPaymentDto {

    private Long id;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private String status;
    private String transactionId;
    private String description;

    private Long userId; // ID del usuario
}

