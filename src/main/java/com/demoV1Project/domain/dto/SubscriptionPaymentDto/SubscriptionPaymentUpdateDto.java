package com.demoV1Project.domain.dto.SubscriptionPaymentDto;

import com.demoV1Project.util.enums.SubscriptionPaymentUtils.SubscriptionPaymentMethod;
import com.demoV1Project.util.enums.SubscriptionPaymentUtils.SubscriptionPaymentStatus;
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
public class SubscriptionPaymentUpdateDto {
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private LocalDateTime endDate;
    private SubscriptionPaymentMethod paymentMethod;
    private SubscriptionPaymentStatus status;
    private String description;
}
