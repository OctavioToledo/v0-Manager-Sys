package com.demoV1Project.domain.dto.TransactionDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionCreateDto {
    private Long id;
    private BigDecimal amount;
    private String paymentMethod;
    private Date date;
    private String description;
    private Long appointmentId;
    private Long businessId;
}
