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
public class TransactionUpdateDto {
    private BigDecimal amount;
    private Date date;
    private String description;
}
