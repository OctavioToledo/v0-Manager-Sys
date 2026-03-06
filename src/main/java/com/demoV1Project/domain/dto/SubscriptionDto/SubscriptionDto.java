package com.demoV1Project.domain.dto.SubscriptionDto;

import com.demoV1Project.util.enums.SubscriptionStatus;
import com.demoV1Project.util.enums.SubscriptionTier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDto {
    private Long id;
    private SubscriptionTier tier;
    private SubscriptionStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long userId;
}
