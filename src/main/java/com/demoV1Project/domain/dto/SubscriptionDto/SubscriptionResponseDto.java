package com.demoV1Project.domain.dto.SubscriptionDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponseDto {
    private SubscriptionDto subscription;
    private String refreshedToken;
}
